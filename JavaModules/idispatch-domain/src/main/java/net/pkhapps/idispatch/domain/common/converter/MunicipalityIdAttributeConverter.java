package net.pkhapps.idispatch.domain.common.converter;

import net.pkhapps.idispatch.domain.base.converter.AbstractAggregateRootIdAttributeConverter;
import net.pkhapps.idispatch.domain.common.MunicipalityId;

import javax.persistence.Converter;

/**
 * Attribute converter for {@link MunicipalityId}.
 */
@Converter(autoApply = true)
public class MunicipalityIdAttributeConverter extends AbstractAggregateRootIdAttributeConverter<MunicipalityId> {

    public MunicipalityIdAttributeConverter() {
        super(MunicipalityId.class);
    }
}
