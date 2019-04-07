package net.pkhapps.idispatch.alerter.server.domain.acknowledgment;

import net.pkhapps.idispatch.base.domain.DomainObjectIdConverter;

import javax.persistence.Converter;

/**
 * Attribute converter for {@link AcknowledgementId}.
 */
@Converter(autoApply = true)
public class AcknowledgementIdConverter extends DomainObjectIdConverter<AcknowledgementId> {
    public static final AcknowledgementIdConverter INSTANCE = new AcknowledgementIdConverter();
}
