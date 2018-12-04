package net.pkhapps.idispatch.gis.domain.model.identity;

import net.pkhapps.idispatch.shared.domain.base.DomainObjectIdConverter;

import javax.persistence.Converter;

/**
 * JPA attribute converter for {@link AddressPointId}.
 */
@Converter(autoApply = true)
public class AddressPointIdConverter extends DomainObjectIdConverter<Long, AddressPointId> {
}
