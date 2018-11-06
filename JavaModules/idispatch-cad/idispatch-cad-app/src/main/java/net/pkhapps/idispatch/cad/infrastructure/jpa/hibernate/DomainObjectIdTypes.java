package net.pkhapps.idispatch.cad.infrastructure.jpa.hibernate;

import net.pkhapps.idispatch.application.support.infrastructure.jpa.hibernate.AbstractDomainObjectIdCustomType;
import net.pkhapps.idispatch.application.support.infrastructure.jpa.hibernate.DomainObjectIdTypeDescriptor;
import net.pkhapps.idispatch.domain.support.DomainEventRecordId;
import org.hibernate.type.descriptor.sql.BigIntTypeDescriptor;

/**
 * TODO Document me!
 */
class DomainObjectIdTypes {

    public static class DomainEventRecordIdType extends AbstractDomainObjectIdCustomType<DomainEventRecordId> {
        public DomainEventRecordIdType() {
            super(BigIntTypeDescriptor.INSTANCE, new DomainObjectIdTypeDescriptor<>(DomainEventRecordId.class));
        }
    }
}
