package net.pkhapps.idispatch.gis.importer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Base class for importers of geographic material.
 *
 * @param <Argument> the type of argument passed to the importer, can be {@link Void} if the importer does not take any
 *                   arguments.
 */
public abstract class BaseImporter<Argument> {

    private final TransactionTemplate transactionTemplate;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BaseImporter(@NotNull PlatformTransactionManager platformTransactionManager) {
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
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
}
