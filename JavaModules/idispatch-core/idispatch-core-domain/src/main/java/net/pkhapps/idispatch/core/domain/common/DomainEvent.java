package net.pkhapps.idispatch.core.domain.common;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * TODO document me
 */
public interface DomainEvent extends DomainObject {

    @NotNull Instant occurredOn();
}
