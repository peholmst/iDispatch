package net.pkhapps.idispatch.gis.domain.model.identity;

import net.pkhapps.idispatch.shared.domain.base.DomainObjectIdConverter;

import javax.persistence.Converter;

/**
 * JPA attribute converter for {@link MunicipalityId}.
 */
@Converter(autoApply = true)
public class MunicipalityIdConverter extends DomainObjectIdConverter<Integer, MunicipalityId> {
}
