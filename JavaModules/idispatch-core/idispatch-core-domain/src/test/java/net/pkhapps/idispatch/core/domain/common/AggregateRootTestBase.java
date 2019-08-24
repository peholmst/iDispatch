package net.pkhapps.idispatch.core.domain.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

/**
 * TODO Document me!
 */
public abstract class AggregateRootTestBase {

    protected static <E extends DomainEvent> void assertPublishedDomainEvent(@NotNull AggregateRoot<?> aggregateRoot,
                                                                             @NotNull Class<E> domainEventType) {
        assertPublishedDomainEvent(aggregateRoot, domainEventType, null);
    }

    protected static <E extends DomainEvent> void assertPublishedDomainEvent(@NotNull AggregateRoot<?> aggregateRoot,
                                                                             @NotNull Class<E> domainEventType,
                                                                             @Nullable Predicate<E> predicate) {
        var iterator = aggregateRoot.domainEvents().iterator();
        while (iterator.hasNext()) {
            var event = iterator.next();
            if (domainEventType.isInstance(event) && (predicate == null
                    || predicate.test(domainEventType.cast(event)))) {
                return;
            }
        }
        fail("%s did not publish domain event %s", aggregateRoot, domainEventType.getName());
    }

    protected static void assertNoPublishedDomainEvents(@NotNull AggregateRoot<?> aggregateRoot) {
        assertThat(aggregateRoot.domainEvents()).isEmpty();
    }

    protected static void clearDomainEvents(@NotNull AggregateRoot<?> aggregateRoot) {
        aggregateRoot.clearDomainEvents();
    }

    public abstract void initialState_newlyCreatedAggregateIsInValidState();
}
