package net.pkhapps.idispatch.domain.base.hibernate;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRootId;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.BigIntTypeDescriptor;

/**
 * TODO Document me!
 *
 * @param <ID>
 */
public abstract class AbstractAggregateRootIdCustomType<ID extends AbstractAggregateRootId>
        extends AbstractSingleColumnStandardBasicType<ID> {

    public AbstractAggregateRootIdCustomType(JavaTypeDescriptor<ID> javaTypeDescriptor) {
        super(BigIntTypeDescriptor.INSTANCE, javaTypeDescriptor);
    }

    @Override
    public String getName() {
        return getJavaTypeDescriptor().getJavaTypeClass().getSimpleName();
    }
}
