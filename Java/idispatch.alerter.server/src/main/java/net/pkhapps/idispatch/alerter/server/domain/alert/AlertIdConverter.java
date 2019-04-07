package net.pkhapps.idispatch.alerter.server.domain.alert;

import net.pkhapps.idispatch.base.domain.DomainObjectIdConverter;

import javax.persistence.Converter;

/**
 * Attribute converter for {@link AlertId}.
 */
@Converter(autoApply = true)
public class AlertIdConverter extends DomainObjectIdConverter<AlertId> {
    public static final AlertIdConverter INSTANCE = new AlertIdConverter();
}
