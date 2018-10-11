package net.pkhapps.idispatch.domain.assignment.hibernate;

import net.pkhapps.idispatch.domain.assignment.AssignmentId;
import net.pkhapps.idispatch.domain.base.hibernate.AbstractAggregateRootIdCustomType;
import net.pkhapps.idispatch.domain.base.hibernate.AbstractAggregateRootIdTypeDescriptor;

/**
 * TODO Document me!
 */
public class AssignmentIdTypeDescriptor extends AbstractAggregateRootIdTypeDescriptor<AssignmentId> {

    public static final AssignmentIdTypeDescriptor INSTANCE = new AssignmentIdTypeDescriptor();

    public AssignmentIdTypeDescriptor() {
        super(AssignmentId.class);
    }

    public static class Type extends AbstractAggregateRootIdCustomType<AssignmentId> {

        public Type() {
            super(AssignmentIdTypeDescriptor.INSTANCE);
        }
    }
}
