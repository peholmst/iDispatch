package net.pkhapps.idispatch.gis.importer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Base class for importers that scan a particular directory for a particular type of files and imports them one
 * by one. The directory to scan is taken as an argument to {@link #importData(File)} and the {@code glob} for
 * filtering the directory is passed as a {@link #DirectoryScanningImporter(PlatformTransactionManager, String) constructor argument}.
 */
public abstract class DirectoryScanningImporter extends BaseImporter<File> {

    private final String glob;

    public DirectoryScanningImporter(@NotNull PlatformTransactionManager platformTransactionManager,
                                     @NotNull String glob) {
        super(platformTransactionManager);
        this.glob = glob;
    }

    @Override
    public void importData(@Nullable File file) {
        Objects.requireNonNull(file, "file must not be null");
        if (!file.isDirectory()) {
            throw new IllegalArgumentException(file + " is not a directory");
        }
        try (var stream = Files.newDirectoryStream(file.toPath(), glob)) {
            stream.forEach(path -> importDataFromFile(path.toFile()));
        } catch (IOException ex) {
            logger().error("Error scanning directory {}", file);
        }
    }

    /**
     * Imports data from the given {@code file}.
     *
     * @param file the file to import from.
     */
    protected abstract void importDataFromFile(@NotNull File file);
}
