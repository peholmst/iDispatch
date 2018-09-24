package net.pkhapps.idispatch.domain.assignment.hibernate;

import net.pkhapps.idispatch.domain.assignment.AssignmentTypeId;
import net.pkhapps.idispatch.domain.base.hibernate.AbstractAggregateRootIdCustomType;
import net.pkhapps.idispatch.domain.base.hibernate.AbstractAggregateRootIdTypeDescriptor;

/**
 * TODO Document me!
 */
public class AssignmentTypeIdTypeDescriptor extends AbstractAggregateRootIdTypeDescriptor<AssignmentTypeId> {

    public static final AssignmentTypeIdTypeDescriptor INSTANCE = new AssignmentTypeIdTypeDescriptor();

    public AssignmentTypeIdTypeDescriptor() {
        super(AssignmentTypeId.class);
    }

    public static class Type extends AbstractAggregateRootIdCustomType<AssignmentTypeId> {

        public Type() {
            super(AssignmentTypeIdTypeDescriptor.INSTANCE);
        }
    }
}
