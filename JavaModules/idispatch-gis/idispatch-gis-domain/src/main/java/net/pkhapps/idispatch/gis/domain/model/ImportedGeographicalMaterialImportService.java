package net.pkhapps.idispatch.gis.domain.model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * TODO Implement me
 *
 * @param <T>
 */
public abstract class ImportedGeographicalMaterialImportService<T extends ImportedGeographicalMaterial> {

    private static final int BATCH_SIZE = 50;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ImportedGeographicalMaterialRepository<T> repository;
    private final MaterialImportRepository materialImportRepository;
    private final Clock clock;
    private final Class<T> materialClass;

    ImportedGeographicalMaterialImportService(@NotNull ImportedGeographicalMaterialRepository<T> repository,
                                              @NotNull MaterialImportRepository materialImportRepository,
                                              @NotNull Clock clock,
                                              @NotNull Class<T> materialClass) {
        this.repository = repository;
        this.materialImportRepository = materialImportRepository;
        this.clock = clock;
        this.materialClass = materialClass;
    }

    /**
     * @param sourceFileName
     * @return
     */
    @NotNull String buildSourceName(@NotNull String sourceFileName) {
        return materialClass.getSimpleName() + "/" + sourceFileName;
    }

    /**
     * @param sourceFileName
     * @param sourceTimestamp
     * @param materialIterator
     */
    public void importData(@NotNull String sourceFileName, @NotNull Instant sourceTimestamp,
                           @NotNull Iterator<T> materialIterator) {
        if (sourceFileName.contains(File.separator)) {
            throw new IllegalArgumentException("sourceFileName must not contain a separator character");
        }
        var sourceName = buildSourceName(sourceFileName);
        var existingMaterialImport = materialImportRepository.findBySource(sourceName);
        if (existingMaterialImport.isPresent()) {
            if (existingMaterialImport.get().sourceTimestamp().isBefore(sourceTimestamp)) {
                logger.info("Database contains an older version of {}, removing it before import", sourceName);
                repository.deleteByMaterialImport(existingMaterialImport.get().id());
                materialImportRepository.delete(existingMaterialImport.get());
            } else {
                logger.info("Database already contains newest version of {}, skipping", sourceName);
                return;
            }
        }
        var newMaterialImport = materialImportRepository.saveAndFlush(
                new MaterialImport(clock.instant(), sourceName, sourceTimestamp));
        var batchContent = new ArrayList<T>(BATCH_SIZE);
        var count = 0L;
        logger.info("Importing items from {}", sourceName);
        while (materialIterator.hasNext()) {
            var material = materialIterator.next();
            material.setMaterialImport(newMaterialImport.id());
            batchContent.add(material);
            count++;
            if (batchContent.size() == BATCH_SIZE) {
                repository.saveAll(batchContent);
                batchContent.clear();
            }
        }
        if (batchContent.size() > 0) {
            repository.saveAll(batchContent);
        }
        logger.info("Imported {} items of type {}", count, materialClass.getName());
    }
}
