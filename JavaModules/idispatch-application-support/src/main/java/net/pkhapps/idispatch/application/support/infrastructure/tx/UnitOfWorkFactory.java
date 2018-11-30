package net.pkhapps.idispatch.application.support.infrastructure.tx;

import javax.annotation.Nonnull;

/**
 * TODO Document me
 */
public interface UnitOfWorkFactory {

    @Nonnull
    UnitOfWork createUnitOfWork();
}
