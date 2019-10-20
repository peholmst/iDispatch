package net.pkhapps.idispatch.gis.postgis.importer.base;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * TODO Document me
 */
public final class FeatureImporterRunner {

    private FeatureImporterRunner() {
    }

    public static void run(@NotNull Class<? extends AbstractImporter> importerClass, @NotNull String[] args)
            throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("Insufficient arguments");
        }
        var inputFile = new File(args[0]);
        if (!inputFile.exists()) {
            throw new IllegalArgumentException(inputFile + " does not exist");
        }
        var jdbcConnectionUrl = args[1];
        var user = args.length <= 2 ? null : args[2];
        var password = args.length <= 3 ? null : args[3];
        try (var connection = DriverManager.getConnection(jdbcConnectionUrl, user, password)) {
            var importer = importerClass.getConstructor(Connection.class).newInstance(connection);
            if (inputFile.isDirectory()) {
                try (var fileStream = Files.newDirectoryStream(inputFile.toPath(), "*.xml")) {
                    for (var file : fileStream) {
                        importFile(file.toFile(), importer);
                    }
                }
            } else {
                importFile(inputFile, importer);
            }
        }
    }

    private static void importFile(@NotNull File file, @NotNull AbstractImporter importer) throws Exception {
        try (var inputStream = new FileInputStream(file)) {
            System.out.println("Importing data from " + file.getAbsolutePath());
            importer.importData(inputStream, file.getName());
        }
    }
}
