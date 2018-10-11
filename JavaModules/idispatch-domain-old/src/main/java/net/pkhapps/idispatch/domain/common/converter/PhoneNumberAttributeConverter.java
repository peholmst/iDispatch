package net.pkhapps.idispatch.domain.common.converter;

import net.pkhapps.idispatch.domain.common.PhoneNumber;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Attribute hibernate for {@link PhoneNumber}.
 */
@Converter(autoApply = true)
public class PhoneNumberAttributeConverter implements AttributeConverter<PhoneNumber, String> {

    @Override
    public String convertToDatabaseColumn(PhoneNumber attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public PhoneNumber convertToEntityAttribute(String dbData) {
        return PhoneNumber.fromString(dbData);
    }
}
