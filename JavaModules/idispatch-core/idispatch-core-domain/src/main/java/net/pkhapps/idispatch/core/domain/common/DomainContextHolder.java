package net.pkhapps.idispatch.core.domain.common;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me!
 */
public final class DomainContextHolder {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DomainContextHolder.class);
    private static volatile DomainContextProvider PROVIDER;

    private DomainContextHolder() {
    }

    public static @NotNull DomainContext get() {
        if (PROVIDER == null) {
            LOGGER.error("Tried to get the DomainContext without plugging in a DomainContextProvider");
            throw new IllegalStateException("No DomainContextProvider has been set");
        }
        return PROVIDER.get();
    }

    public static void setProvider(@NotNull DomainContextProvider provider) {
        requireNonNull(provider);
        synchronized (DomainContextHolder.class) {
            if (PROVIDER != null) {
                LOGGER.warn("Replacing existing DomainContextProvider {} with {}", PROVIDER, provider);
            }
            PROVIDER = provider;
        }
    }
}
