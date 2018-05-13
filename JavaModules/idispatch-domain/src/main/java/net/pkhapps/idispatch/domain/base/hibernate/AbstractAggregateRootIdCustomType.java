package net.pkhapps.idispatch.domain.base.hibernate;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRootId;
import org.hibernate.id.ResultSetIdentifierConsumer;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.BigIntTypeDescriptor;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.sql.ResultSet;

/**
 * Base class for Hibernate custom types for {@link AbstractAggregateRootId ID} types. You will need to create one
 * subclass of this for every ID type and register them, together with their corresponding ID types, using the
 * {@link org.hibernate.annotations.TypeDef} (or {@link org.hibernate.annotations.TypeDefs}) annotation.
 * <p/>
 * This custom type also implements the {@link ResultSetIdentifierConsumer} interface, making it possible to use the
 * custom type in {@link javax.persistence.Id} fields as well.
 *
 * @param <ID> the ID type.
 */
public abstract class AbstractAggregateRootIdCustomType<ID extends AbstractAggregateRootId>
        extends AbstractSingleColumnStandardBasicType<ID> implements ResultSetIdentifierConsumer {

    /**
     * Protected constructor that accepts the {@link AbstractAggregateRootIdTypeDescriptor type descriptor} for the ID
     * type as parameter. Subclasses should declare a default constructor and invoke this super constructor, passing
     * in the correct parameter.
     */
    protected AbstractAggregateRootIdCustomType(@NonNull JavaTypeDescriptor<ID> javaTypeDescriptor) {
        super(BigIntTypeDescriptor.INSTANCE, javaTypeDescriptor);
    }

    @Override
    public String getName() {
        return getJavaTypeDescriptor().getJavaTypeClass().getSimpleName();
    }

    @Override
    public Serializable consumeIdentifier(ResultSet resultSet) {
        try {
            var id = resultSet.getLong(1); // For some reason, the ID is always at index 1. I don't know why.
            return getJavaTypeDescriptor().wrap(id, null);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
