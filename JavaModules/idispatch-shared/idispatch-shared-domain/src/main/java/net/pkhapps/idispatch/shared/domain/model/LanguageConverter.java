package net.pkhapps.idispatch.shared.domain.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA attribute converter for {@link Language}.
 */
@Converter(autoApply = true)
public class LanguageConverter implements AttributeConverter<Language, String> {

    @Override
    public String convertToDatabaseColumn(Language attribute) {
        return attribute == null ? null : attribute.toISO639_1();
    }

    @Override
    public Language convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Language.fromISO639_1(dbData);
    }
}
