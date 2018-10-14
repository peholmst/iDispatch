package net.pkhapps.idispatch.domain.support;

import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link AggregateRoot}.
 */
public class AggregateRootTest {

    @Test
    public void initialStateAfterDefaultConstructor() {
        var aggregate = new TestAggregate();
        assertThat(aggregate.domainEvents()).isEmpty();
        assertThat(aggregate.isNew()).isFalse();
    }

    @Test
    public void initialStateAfterInitializingConstructor() {
        var id = new TestAggregateId(1);
        var aggregate = new TestAggregate(id);
        assertThat(aggregate.domainEvents()).isEmpty();
        assertThat(aggregate.isNew()).isTrue();
        assertThat(aggregate.id()).isEqualTo(id);
    }

    @Test
    public void registerDomainEvent_eventShowsUpInStream() {
        var aggregate = new TestAggregate(new TestAggregateId(1));
        var event = new TestEvent();
        aggregate.registerDomainEvent(event);
        assertThat(aggregate.domainEvents()).containsExactly(event);
    }

    @Test
    public void clearDomainEvents_streamIsEmpty() {
        var aggregate = new TestAggregate(new TestAggregateId(1));
        var event = new TestEvent();
        aggregate.registerDomainEvent(event);
        aggregate.clearDomainEvents();
        assertThat(aggregate.domainEvents()).isEmpty();
    }

    private static class TestAggregateId extends DomainObjectId {
        TestAggregateId(@Nonnull Serializable id) {
            super(id);
        }
    }

    private static class TestAggregate extends AggregateRoot<TestAggregateId> {
        TestAggregate() {
        }

        TestAggregate(@Nonnull TestAggregateId id) {
            super(id);
        }
    }

    private static class TestEvent implements DomainEvent {
        @Nonnull
        @Override
        public Instant occurredOn() {
            return Instant.now();
        }
    }
}
