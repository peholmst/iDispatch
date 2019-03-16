package net.pkhapps.idispatch.base.domain.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * TODO Document me!
 *
 * @param <DS>
 */
public interface DomainServicesHolder<DS extends DomainServices> {

    /**
     * Returns a {@code DomainServicesHolder} that uses a {@link ThreadLocal} to store the instance. Mainly useful for
     * testing.
     */
    static <DS extends DomainServices> DomainServicesHolder<DS> threadLocal() {
        return new DomainServicesHolder<>() {

            private final ThreadLocal<DS> instance = new ThreadLocal<>();

            @Override
            public Optional<DS> getInstanceOptional() {
                return Optional.ofNullable(instance.get());
            }

            @Override
            public void setInstance(DS domainServices) {
                if (domainServices == null) {
                    instance.remove();
                } else {
                    instance.set(domainServices);
                }
            }
        };
    }

    /**
     * Returns a {@code DomainServicesHolder} that stores a single, global instance.
     */
    static <DS extends DomainServices> DomainServicesHolder<DS> global() {
        return new DomainServicesHolder<>() {
            private final AtomicReference<DS> instance = new AtomicReference<>();

            @Override
            public void setInstance(DS domainServices) {
                instance.set(domainServices);
            }

            @Override
            public Optional<DS> getInstanceOptional() {
                return Optional.ofNullable(instance.get());
            }
        };
    }

    /**
     * Returns the current domain services instance.
     *
     * @throws IllegalStateException if no instance has been set.
     */
    @NonNull
    default DS getInstance() {
        return getInstanceOptional().orElseThrow(() -> new IllegalStateException("No DomainServices available"));
    }

    /**
     * Sets the current domain services instance.
     */
    void setInstance(@Nullable DS domainServices);

    /**
     * Returns the current domain services instance or an empty {@code Optional} if no instance has been set.
     */
    @NonNull
    Optional<DS> getInstanceOptional();
}
