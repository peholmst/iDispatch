package net.pkhapps.idispatch.gis.domain.model.identity;

import net.pkhapps.idispatch.shared.domain.base.DomainObjectIdConverter;

import javax.persistence.Converter;

/**
 * JPA attribute converter for {@link MaterialImportId}.
 */
@Converter(autoApply = true)
public class MaterialImportIdConverter extends DomainObjectIdConverter<Long, MaterialImportId> {
}
