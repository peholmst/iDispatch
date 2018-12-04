package net.pkhapps.idispatch.gis.importer;

import net.pkhapps.idispatch.gis.domain.model.MaterialImportRepository;
import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xml.AppSchemaConfiguration;
import org.geotools.xml.PullParser;
import org.geotools.xml.resolver.SchemaCache;
import org.geotools.xml.resolver.SchemaResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.PlatformTransactionManager;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Base class for importers that read GML-files from a directory.
 */
public abstract class GmlFileImporter extends DirectoryScanningImporter {

    private final AppSchemaConfiguration configuration;
    private final String namespace;

    public GmlFileImporter(@NotNull Clock clock,
                           @NotNull PlatformTransactionManager platformTransactionManager,
                           @NotNull MaterialImportRepository materialImportRepository,
                           @NotNull String namespace,
                           @NotNull String schemaLocation) {
        super(clock, platformTransactionManager, materialImportRepository, "*.xml");
        this.namespace = namespace;
        var cacheDirectory = Paths.get("idispatch-gis-importer-schema-cache").toAbsolutePath().toFile();
        if (!cacheDirectory.exists()) {
            logger().info("Creating directory {}", cacheDirectory);
            if (!cacheDirectory.mkdirs()) {
                throw new RuntimeException("Could not create directory " + cacheDirectory);
            }
        } else if (!cacheDirectory.isDirectory()) {
            throw new RuntimeException("The path " + cacheDirectory + " does not point to a directory");
        }
        logger().info("Storing cached XML schemas in {}", cacheDirectory);

        var schemaCache = new SchemaCache(cacheDirectory, true);
        SchemaResolver resolver = new SchemaResolver(schemaCache);
        configuration = new AppSchemaConfiguration(namespace, schemaLocation, resolver) {
            {
                addDependency(new GMLConfiguration());
            }

            @Override
            protected void configureBindings(Map bindings) {
                // We don't want any Features, only Maps (otherwise we might lose some data). Other GML types are fine
                // so we still want the GMLConfiguration.
                bindings.remove(new QName("http://www.opengis.net/gml", "AbstractFeatureType"));
            }
        };
    }

    @Override
    protected void importDataFromFile(@NotNull File file) {
        var materialImport = createMaterialImport(file.getName());
        logger().info("Importing data from {}", file);
        importers().forEach(importer -> {
            try (var is = new FileInputStream(file)) {
                PullParser parser = new PullParser(configuration, is, importer.featureType());
                importer.importFeatures(createIterator(parser), materialImport);
            } catch (Exception ex) {
                logger().error("Error importing " + importer.featureType() + " from " + file, ex);
            }
        });
    }

    @NotNull
    private Iterator<Map<String, Object>> createIterator(@NotNull PullParser parser) {
        return new Iterator<>() {

            private Map<String, Object> next = parse();

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public Map<String, Object> next() {
                try {
                    return next;
                } finally {
                    next = parse();
                }
            }

            @SuppressWarnings("unchecked")
            private @Nullable Map<String, Object> parse() {
                try {
                    return (Map<String, Object>) parser.parse();
                } catch (Exception ex) {
                    throw new RuntimeException("Error parsing source file", ex);
                }
            }
        };
    }

    /**
     * Returns a stream of importers to delegate the actual importing to once the GML file has been parsed.
     */
    @NotNull
    protected abstract Stream<Importer> importers();

    /**
     * Creates a new {@link QName} with the namespace that was passed in as a
     * {@link #GmlFileImporter(Clock, PlatformTransactionManager, MaterialImportRepository, String, String) constructor argument}.
     * Mainly intended to be used by implementations of {@link Importer}.
     *
     * @param localPart the local part of the name.
     * @return the new {@link QName}.
     */
    @NotNull
    protected QName createFeatureTypeName(@NotNull String localPart) {
        return new QName(namespace, localPart);
    }

    /**
     * Interface defining an importer that will import features of a specific type from a parsed GML file.
     */
    public interface Importer {

        /**
         * The fully qualified name of the type of features that this importer knows how to handle.
         */
        @NotNull
        QName featureType();

        /**
         * Imports the features in the feature iterator.
         *
         * @param featureIterator  an iterator of features where each feature is represented by a map.
         * @param materialImportId the ID of the {@link net.pkhapps.idispatch.gis.domain.model.MaterialImport} to associate with the imported material.
         */
        void importFeatures(@NotNull Iterator<Map<String, Object>> featureIterator,
                            @NotNull MaterialImportId materialImportId);
    }
}
