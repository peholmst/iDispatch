package net.pkhapps.idispatch.client.v3.type;

import net.pkhapps.idispatch.client.v3.base.DomainObjectId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * ID type for {@link AssignmentType}.
 */
@Immutable
public class AssignmentTypeId extends DomainObjectId {
    public AssignmentTypeId(@Nonnull Long id) {
        super(id);
    }
}
