package net.pkhapps.idispatch.core.domain.common;

import org.jetbrains.annotations.NotNull;

import java.time.Clock;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me!
 */
public abstract class DomainContext {

    private static Strategy STRATEGY = new Strategy() {

        private final DomainContext context = new Default();

        @Override
        public @NotNull DomainContext get() {
            return context;
        }
    };

    /**
     * @return
     */
    public static @NotNull DomainContext instance() {
        return requireNonNull(STRATEGY).get();
    }

    /**
     * @param strategy
     */
    public static void setStrategy(@NotNull Strategy strategy) {
        STRATEGY = requireNonNull(strategy);
    }

    /**
     * @return
     */
    public abstract Clock clock();

    /**
     *
     */
    @FunctionalInterface
    public interface Strategy {

        /**
         *
         */
        @NotNull DomainContext get();
    }

    public static final class Default extends DomainContext {

        @Override
        public Clock clock() {
            return Clock.systemUTC();
        }
    }
}
