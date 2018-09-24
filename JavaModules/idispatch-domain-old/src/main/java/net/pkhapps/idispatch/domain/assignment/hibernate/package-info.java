@TypeDefs({
        @TypeDef(defaultForType = AssignmentTypeId.class, typeClass = AssignmentTypeIdTypeDescriptor.Type.class),
        @TypeDef(defaultForType = AssignmentId.class, typeClass = AssignmentIdTypeDescriptor.Type.class)
})
package net.pkhapps.idispatch.domain.assignment.hibernate;

import net.pkhapps.idispatch.domain.assignment.AssignmentId;
import net.pkhapps.idispatch.domain.assignment.AssignmentTypeId;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;