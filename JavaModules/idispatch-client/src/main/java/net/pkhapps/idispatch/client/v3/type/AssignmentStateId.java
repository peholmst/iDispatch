package net.pkhapps.idispatch.client.v3.type;

import net.pkhapps.idispatch.client.v3.base.DomainObjectId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * ID type for {@link AssignmentState}.
 */
@Immutable
public class AssignmentStateId extends DomainObjectId {
    public AssignmentStateId(@Nonnull Long id) {
        super(id);
    }
}
