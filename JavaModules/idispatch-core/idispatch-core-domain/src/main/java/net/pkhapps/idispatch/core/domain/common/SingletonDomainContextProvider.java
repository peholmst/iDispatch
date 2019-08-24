package net.pkhapps.idispatch.core.domain.common;

import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me!
 */
public final class SingletonDomainContextProvider implements DomainContextProvider {

    private final DomainContext domainContext;

    /**
     * @param domainContext
     */
    public SingletonDomainContextProvider(@NotNull DomainContext domainContext) {
        this.domainContext = requireNonNull(domainContext);
    }

    @Override
    public @NotNull DomainContext get() {
        return domainContext;
    }
}
