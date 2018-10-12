package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.time.Instant;

public interface DomainEvent extends Serializable {

    @Nonnull
    Instant occurredOn();
}
