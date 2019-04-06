package net.pkhapps.idispatch.alerter.server.domain.recipient;

import net.pkhapps.idispatch.base.domain.DomainObjectIdConverter;

import javax.persistence.Converter;

/**
 * Attribute converter for {@link RecipientId}.
 */
@Converter(autoApply = true)
public class RecipientIdConverter extends DomainObjectIdConverter<RecipientId> {
    public static final RecipientIdConverter INSTANCE = new RecipientIdConverter();
}
