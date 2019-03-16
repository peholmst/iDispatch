package net.pkhapps.idispatch.base.domain;

import javax.persistence.Converter;

/**
 * Attribute converter for {@link OrganizationId}.
 */
@Converter(autoApply = true)
public class OrganizationIdConverter extends DomainObjectIdConverter<OrganizationId> {
}
