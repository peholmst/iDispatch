package net.pkhapps.idispatch.core.domain.common;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public interface Deactivatable {

    ActivationState activationState();

    void activate();

    void deactivate(String reason);

    interface ActivationState {
        static ActivationState active() {
            return new ActivationState() {
                @Override
                public boolean isActive() {
                    return true;
                }
            };
        }

        static ActivationState inactive(String reason) {
            requireNonNull(reason);
            return new ActivationState() {
                @Override
                public boolean isActive() {
                    return false;
                }

                @Override
                public Optional<String> getDeactivationReason() {
                    return Optional.of(reason);
                }
            };
        }

        boolean isActive();

        default boolean isInactive() {
            return !isActive();
        }

        default Optional<String> getDeactivationReason() {
            return Optional.empty();
        }
    }
}
