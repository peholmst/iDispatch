package net.pkhapps.idispatch.domain.common.hibernate;

import net.pkhapps.idispatch.domain.base.hibernate.AbstractAggregateRootIdCustomType;
import net.pkhapps.idispatch.domain.base.hibernate.AbstractAggregateRootIdTypeDescriptor;
import net.pkhapps.idispatch.domain.common.MunicipalityId;

/**
 * TODO Document me!
 */
public class MunicipalityIdTypeDescriptor extends AbstractAggregateRootIdTypeDescriptor<MunicipalityId> {

    public static final MunicipalityIdTypeDescriptor INSTANCE = new MunicipalityIdTypeDescriptor();

    public MunicipalityIdTypeDescriptor() {
        super(MunicipalityId.class);
    }

    public static class Type extends AbstractAggregateRootIdCustomType<MunicipalityId> {

        public Type() {
            super(MunicipalityIdTypeDescriptor.INSTANCE);
        }
    }
}
