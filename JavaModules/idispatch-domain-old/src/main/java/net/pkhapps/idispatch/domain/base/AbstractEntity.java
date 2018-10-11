package net.pkhapps.idispatch.domain.base;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Base class for entities that are not aggregate roots.
 */
public abstract class AbstractEntity extends AbstractPersistable<Long> {
}
