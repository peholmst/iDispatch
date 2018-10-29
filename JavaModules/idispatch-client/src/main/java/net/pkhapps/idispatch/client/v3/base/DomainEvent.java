package net.pkhapps.idispatch.client.v3.base;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.time.Instant;

/**
 * Interface implemented by domain objects that represent domain events.
 */
public interface DomainEvent extends Serializable {

    @Nonnull
    Instant occurredOn();
}
