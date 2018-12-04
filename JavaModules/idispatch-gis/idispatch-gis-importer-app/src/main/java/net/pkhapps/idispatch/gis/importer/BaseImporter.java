package net.pkhapps.idispatch.gis.importer;

import net.pkhapps.idispatch.gis.domain.model.MaterialImport;
import net.pkhapps.idispatch.gis.domain.model.MaterialImportRepository;
import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;

/**
 * Base class for importers of geographic material.
 *
 * @param <Argument> the type of argument passed to the importer, can be {@link Void} if the importer does not take any
 *                   arguments.
 */
public abstract class BaseImporter<Argument> {

    private final Clock clock;
    private final MaterialImportRepository materialImportRepository;
    private final TransactionTemplate transactionTemplate;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BaseImporter(@NotNull Clock clock,
                        @NotNull PlatformTransactionManager platformTransactionManager,
                        @NotNull MaterialImportRepository materialImportRepository) {
        this.clock = clock;
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
        this.materialImportRepository = materialImportRepository;
    }

    /**
     * Imports data without passing in any arguments.
     */
    public void importData() {
        importData(null);
    }

    /**
     * Imports data, passing in the given argument.
     *
     * @param argument any argument to pass to the importer.
     */
    public abstract void importData(@Nullable Argument argument);

    /**
     * Returns a transaction template for controlling transactions programmatically.
     */
    @NotNull
    protected TransactionTemplate transactionTemplate() {
        return transactionTemplate;
    }

    /**
     * Returns a logger than can be used to log progress.
     */
    @NotNull
    protected Logger logger() {
        return logger;
    }

    /**
     * Creates and saves a new {@link MaterialImport} to associate with the imported material.
     *
     * @param source the source string to store in the {@link MaterialImport}.
     * @return the ID of the created {@link MaterialImport}.
     */
    @NotNull
    protected MaterialImportId createMaterialImport(@NotNull String source) {
        var materialImport = new MaterialImport(clock.instant(), source);
        materialImportRepository.saveAndFlush(materialImport);
        return materialImport.id();
    }
}
