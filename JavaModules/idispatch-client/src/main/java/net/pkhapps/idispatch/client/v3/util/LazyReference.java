package net.pkhapps.idispatch.client.v3.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Thread safe class that contains a reference to some object that will be lazily created when first requested.
 *
 * @see <a href="https://dzone.com/articles/be-lazy-with-java-8">Be Lazy With Java 8</a>
 */
@ThreadSafe
public final class LazyReference<T> {

    private final Logger logger = LoggerFactory.getLogger(LazyReference.class);
    private final Supplier<T> factory;
    private volatile T reference;

    public LazyReference(@Nonnull Supplier<T> factory) {
        this.factory = Objects.requireNonNull(factory, "factory must not be null");
    }

    @Nonnull
    public T get() {
        final T result = reference;
        return result == null ? computeIfNeeded() : result;
    }

    @Nonnull
    private synchronized T computeIfNeeded() {
        if (reference == null) {
            reference = Objects.requireNonNull(factory.get(), "factory must not return null");
            logger.debug("Created new instance {}", reference);
        }
        return reference;
    }
}
