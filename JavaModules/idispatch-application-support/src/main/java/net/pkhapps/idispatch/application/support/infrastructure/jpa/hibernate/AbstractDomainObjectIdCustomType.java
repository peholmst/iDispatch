package net.pkhapps.idispatch.application.support.infrastructure.jpa.hibernate;

import net.pkhapps.idispatch.domain.support.DomainObjectId;
import org.hibernate.id.ResultSetIdentifierConsumer;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TODO Document me!
 *
 * @param <ID>
 */
public abstract class AbstractDomainObjectIdCustomType<ID extends DomainObjectId>
        extends AbstractSingleColumnStandardBasicType<ID> implements ResultSetIdentifierConsumer {

    public AbstractDomainObjectIdCustomType(@Nonnull SqlTypeDescriptor sqlTypeDescriptor,
                                            @Nonnull JavaTypeDescriptor<ID> javaTypeDescriptor) {
        super(sqlTypeDescriptor, javaTypeDescriptor);
    }


    @Override
    public Serializable consumeIdentifier(ResultSet resultSet) {
        try {
            return getJavaTypeDescriptor().wrap(resultSet.getObject(1), null);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getName() {
        return getJavaTypeDescriptor().getJavaType().getSimpleName();
    }
}
