package net.pkhapps.idispatch.core.domain.common;

import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me
 */
@FunctionalInterface
public interface DomainContextProvider {

    @NotNull DomainContext get();
}
