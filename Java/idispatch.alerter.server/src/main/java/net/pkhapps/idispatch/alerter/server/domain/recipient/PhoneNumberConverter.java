package net.pkhapps.idispatch.alerter.server.domain.recipient;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Attribute converter for {@link PhoneNumber}.
 */
@Converter(autoApply = true)
public class PhoneNumberConverter implements AttributeConverter<PhoneNumber, String> {

    @Override
    public String convertToDatabaseColumn(PhoneNumber attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public PhoneNumber convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new PhoneNumber(dbData);
    }
}
