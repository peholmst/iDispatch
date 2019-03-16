package net.pkhapps.idispatch.base.domain;

import javax.persistence.Converter;

/**
 * Attribute converter for {@link UserId}.
 */
@Converter(autoApply = true)
public class UserIdConverter extends DomainObjectIdConverter<UserId> {
}
