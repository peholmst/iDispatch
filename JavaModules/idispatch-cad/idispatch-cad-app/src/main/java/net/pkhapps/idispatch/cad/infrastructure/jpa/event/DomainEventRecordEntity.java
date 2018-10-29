package net.pkhapps.idispatch.cad.infrastructure.jpa.event;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.pkhapps.idispatch.cad.infrastructure.json.JsonConverter;
import net.pkhapps.idispatch.domain.support.DomainEvent;
import net.pkhapps.idispatch.domain.support.DomainEventRecord;
import net.pkhapps.idispatch.domain.support.DomainEventRecordId;
import net.pkhapps.idispatch.domain.support.UsedByPersistenceFramework;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Objects;

/**
 * Implementation of {@link DomainEventRecord} that can be persisted using JPA and that serializes the actual domain
 * event to JSON.
 */
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Immutable
class DomainEventRecordEntity implements DomainEventRecord {

    private DomainEventRecordId id;

    private Instant occurredOn;

    private String eventType;

    private String eventContent;

    private DomainEvent event;

    @UsedByPersistenceFramework
    protected DomainEventRecordEntity() {
    }

    DomainEventRecordEntity(@Nonnull DomainEvent event) {
        Objects.requireNonNull(event, "event must not be null");
        occurredOn = event.occurredOn();
        eventType = event.getClass().getName();
        eventContent = JsonConverter.instance().toJson(event);
        this.event = event;
    }

    @Nonnull
    @Override
    public DomainEvent event() {
        if (event == null) {
            event = JsonConverter.instance().fromJson(eventType(), eventContent);
        }
        return event;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends DomainEvent> eventType() {
        try {
            return (Class<? extends DomainEvent>) Class.forName(eventType);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Found event type in database that does not exist", e);
        }
    }

    @Nonnull
    @Override
    public DomainEventRecordId id() {
        return id;
    }
}
