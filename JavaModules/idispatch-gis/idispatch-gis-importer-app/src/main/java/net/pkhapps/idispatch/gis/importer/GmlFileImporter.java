package net.pkhapps.idispatch.gis.importer;

import net.pkhapps.idispatch.gis.domain.model.ImportedGeographicalMaterial;
import net.pkhapps.idispatch.gis.domain.model.ImportedGeographicalMaterialImportService;
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
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Base class for importers that read GML-files from a directory.
 */
public abstract class GmlFileImporter extends DirectoryScanningImporter {

    private final AppSchemaConfiguration configuration;
    private final String namespace;

    public GmlFileImporter(@NotNull PlatformTransactionManager platformTransactionManager,
                           @NotNull String namespace,
                           @NotNull String schemaLocation) {
        super(platformTransactionManager, "*.xml");
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
    @SuppressWarnings("unchecked")
    protected void importDataFromFile(@NotNull File file) {
        logger().info("Importing data from {}", file);
        var sourceFileName = file.getName();
        var sourceTimestamp = extractMaterialTimestamp(file);
        importers().forEach(importer -> {
            try (var is = new FileInputStream(file)) {
                var parser = new PullParser(configuration, is, importer.featureType());
                importer.importService().importData(sourceFileName, sourceTimestamp, createIterator(parser, importer));
            } catch (Exception ex) {
                logger().error("Error importing " + importer.featureType() + " from " + file, ex);
            }
        });
    }

    protected @NotNull String namespace() {
        return namespace;
    }

    protected abstract @NotNull Instant extractMaterialTimestamp(@NotNull File file);

    private <T extends ImportedGeographicalMaterial> @NotNull Iterator<T> createIterator(@NotNull PullParser parser,
                                                                                         @NotNull Importer<T> importer) {
        return new Iterator<>() {

            private Map<String, Object> next = parse();

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public T next() {
                try {
                    return importer.mapFeature(next);
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
    protected abstract @NotNull Stream<Importer> importers();

    /**
     * Creates a new {@link QName} with the namespace that was passed in as a
     * {@link #GmlFileImporter(PlatformTransactionManager, String, String)} constructor argument}.
     * Mainly intended to be used by implementations of {@link Importer}.
     *
     * @param localPart the local part of the name.
     * @return the new {@link QName}.
     */
    protected @NotNull QName createFeatureTypeName(@NotNull String localPart) {
        return new QName(namespace, localPart);
    }

    /**
     * Interface defining an importer that will import features of a specific type from a parsed GML file.
     */
    public interface Importer<T extends ImportedGeographicalMaterial> {

        /**
         * The fully qualified name of the type of features that this importer knows how to handle.
         */
        @NotNull
        QName featureType();

        /**
         * TODO Document me
         *
         * @param feature
         * @return
         */
        @NotNull T mapFeature(@NotNull Map<String, Object> feature);

        /**
         * TODO Document me
         *
         * @return
         */
        @NotNull ImportedGeographicalMaterialImportService<T> importService();
    }
}
