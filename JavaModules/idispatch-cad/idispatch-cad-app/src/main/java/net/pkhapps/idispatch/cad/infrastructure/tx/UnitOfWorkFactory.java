package net.pkhapps.idispatch.cad.infrastructure.tx;

import javax.annotation.Nonnull;

/**
 * TODO Document me
 */
public interface UnitOfWorkFactory {

    @Nonnull
    UnitOfWork createUnitOfWork();
}
