package net.pkhapps.idispatch.client.v3;

import net.pkhapps.idispatch.client.v3.base.DomainObjectId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * ID type for assignments.
 */
@Immutable
public class AssignmentId extends DomainObjectId {
    public AssignmentId(@Nonnull Long id) {
        super(id);
    }
}
