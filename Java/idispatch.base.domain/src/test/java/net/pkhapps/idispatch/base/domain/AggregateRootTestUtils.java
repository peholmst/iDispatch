package net.pkhapps.idispatch.base.domain;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Utility class for asserting that certain domain events have been published.
 */
public class AggregateRootTestUtils {

    public static <T> void assertDomainEvent(@NonNull AggregateRoot<?> aggregateRoot,
                                             @NonNull Class<T> eventType, @Nullable Consumer<T> checker) {
        var event = aggregateRoot.domainEvents().stream().filter(eventType::isInstance).findFirst().map(eventType::cast);
        assertThat(event).isPresent();
        if (checker != null) {
            event.ifPresent(checker);
        }
    }

    public static void assertDomainEvent(@NonNull AggregateRoot<?> aggregateRoot, @NonNull Class<?> eventType) {
        assertDomainEvent(aggregateRoot, eventType, null);
    }

    public static void clearDomainEvents(@NonNull AggregateRoot<?> aggregateRoot) {
        aggregateRoot.clearDomainEvents();
    }
}
