package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface DomainEventPublisher {

    void publish(@Nonnull DomainEvent domainEvent);

    @Nonnull
    RegistrationHandle registerListener(@Nonnull Consumer<DomainEvent> listener,
                                        @Nonnull Predicate<DomainEvent> filter);

    interface RegistrationHandle {
        void unregister();
    }
}
