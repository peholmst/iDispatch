package net.pkhapps.idispatch.domain.assignment;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRootId;
import org.springframework.lang.NonNull;

/**
 * ID type for {@link Assignment}.
 */
public class AssignmentId extends AbstractAggregateRootId {

    public AssignmentId(@NonNull Long id) {
        super(id);
    }

    public AssignmentId(@NonNull String id) {
        super(Long.valueOf(id));
    }
}
