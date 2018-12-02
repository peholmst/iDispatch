package net.pkhapps.idispatch.gis.importer;

import net.pkhapps.idispatch.gis.domain.model.MaterialImport;
import net.pkhapps.idispatch.gis.domain.model.MaterialImportRepository;
import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;

/**
 * TODO Document me
 */
public abstract class BaseImporter {

    private final Clock clock;
    private final MaterialImportRepository materialImportRepository;
    private final TransactionTemplate transactionTemplate;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BaseImporter(Clock clock,
                        PlatformTransactionManager platformTransactionManager,
                        MaterialImportRepository materialImportRepository) {
        this.clock = clock;
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
        this.materialImportRepository = materialImportRepository;
    }

    public abstract void importData();

    @NotNull
    protected TransactionTemplate transactionTemplate() {
        return transactionTemplate;
    }

    @NotNull
    protected Logger logger() {
        return logger;
    }

    @NotNull
    protected MaterialImportId createMaterialImport(@NotNull String source) {
        var materialImport = new MaterialImport(clock.instant(), source);
        materialImportRepository.saveAndFlush(materialImport);
        return materialImport.id();
    }
}
