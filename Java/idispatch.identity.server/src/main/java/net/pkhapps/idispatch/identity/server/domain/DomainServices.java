package net.pkhapps.idispatch.identity.server.domain;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Helper class for statically getting access to different domain services without having access to the
 * Spring application context.
 */
public abstract class DomainServices {

    private static InstanceHolderStrategy instanceHolderStrategy = new GlobalInstanceHolderStrategy();

    /**
     * Returns the current instance of {@code DomainServices}.
     *
     * @throws IllegalStateException if no instance has been set.
     */
    @NonNull
    public static DomainServices getInstance() {
        return instanceHolderStrategy.get().orElseThrow(() -> new IllegalStateException("No DomainServices available"));
    }

    /**
     * Sets the current instance of {@code DomainServices}.
     */
    public static void setInstance(@Nullable DomainServices instance) {
        instanceHolderStrategy.set(instance);
    }

    /**
     * Sets the strategy for storing and retrieving the {@code DomainServices} instance. This is intended to be used
     * for testing only. By default, a {@link GlobalInstanceHolderStrategy} is used.
     * <p>
     * This method is not thread-safe.
     */
    static void setInstanceHolderStrategy(@NonNull Class<? extends InstanceHolderStrategy> instanceHolderStrategyClass) {
        Objects.requireNonNull(instanceHolderStrategyClass, "instanceHolderStrategyClass must not be null");
        if (!instanceHolderStrategyClass.isInstance(instanceHolderStrategy)) {
            try {
                var constructor = instanceHolderStrategyClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                instanceHolderStrategy = constructor.newInstance();
            } catch (Exception ex) {
                throw new IllegalArgumentException("The given InstanceHolderStrategy class does not have a default constructor");
            }
        }
    }

    /**
     * Returns the system clock.
     */
    public abstract Clock clock();

    /**
     * Returns the domain properties.
     */
    public abstract DomainProperties properties();

    /**
     * Returns the password encoder.
     */
    public abstract PasswordEncoder passwordEncoder();

    interface InstanceHolderStrategy {

        @NonNull
        Optional<DomainServices> get();

        void set(@Nullable DomainServices instance);
    }

    /**
     * Implementation of {@link InstanceHolderStrategy} that uses a {@link ThreadLocal} to store the instance. Mainly
     * useful for testing.
     */
    static class ThreadLocalInstanceHolderStrategy implements InstanceHolderStrategy {
        private final ThreadLocal<DomainServices> threadLocal = new ThreadLocal<>();

        @Override
        public Optional<DomainServices> get() {
            return Optional.ofNullable(threadLocal.get());
        }

        @Override
        public void set(DomainServices instance) {
            if (instance == null) {
                threadLocal.remove();
            } else {
                threadLocal.set(instance);
            }
        }
    }

    /**
     * Implementation of {@link InstanceHolderStrategy} that stores a single instance.
     */
    static class GlobalInstanceHolderStrategy implements InstanceHolderStrategy {

        private AtomicReference<DomainServices> instance = new AtomicReference<>();

        @Override
        public Optional<DomainServices> get() {
            return Optional.ofNullable(instance.get());
        }

        @Override
        public void set(DomainServices instance) {
            this.instance.set(instance);
        }
    }
}
