package net.pkhapps.idispatch.domain.support;

import org.junit.Test;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link DomainEventPublisher}.
 */
public class DomainEventPublisherTest {

    @Test
    public void publish_listenerThrowsException_exceptionIsIgnored() {
        var publisher = new DomainEventPublisher();
        var listenerCalled = new AtomicBoolean(false);
        publisher.registerListener((event) -> {
            listenerCalled.set(true);
            throw new RuntimeException("This will be ignored");
        }, DomainEventPublisher.ALL_EVENTS);
        publisher.publish(new TestEvent());
        assertThat(listenerCalled).isTrue();
    }

    @Test
    public void publish_listenerPredicateDoesNotMatch_listenerIsNotCalled() {
        var publisher = new DomainEventPublisher();
        var listenerCalled = new AtomicBoolean(false);
        publisher.registerListener((event) -> listenerCalled.set(true), TestEvent.class::isInstance);
        publisher.publish(new TestEvent2());
        assertThat(listenerCalled).isFalse();
    }

    @Test
    public void publish_listenerPredicateMatches_listenerIsCalled() {
        var publisher = new DomainEventPublisher();
        var event = new TestEvent();
        var receivedEvent = new AtomicReference<DomainEvent>();
        publisher.registerListener(receivedEvent::set, DomainEventPublisher.ALL_EVENTS);
        publisher.publish(event);
        assertThat(receivedEvent).hasValue(event);
    }

    @Test
    public void unregister_listenerIsUnregistered_eventIsNotReceived() {
        var publisher = new DomainEventPublisher();
        var eventCount = new AtomicInteger(0);
        var handle = publisher.registerListener(event -> eventCount.incrementAndGet(), DomainEventPublisher.ALL_EVENTS);
        publisher.publish(new TestEvent());
        handle.unregister();
        publisher.publish(new TestEvent2());
        assertThat(eventCount).hasValue(1);
    }

    private static class TestEvent implements DomainEvent {

        private final Instant occurredOn = Instant.now();

        @Nonnull
        @Override
        public Instant occurredOn() {
            return occurredOn;
        }
    }

    private static class TestEvent2 implements DomainEvent {

        private final Instant occurredOn = Instant.now();

        @Nonnull
        @Override
        public Instant occurredOn() {
            return occurredOn;
        }
    }
}
