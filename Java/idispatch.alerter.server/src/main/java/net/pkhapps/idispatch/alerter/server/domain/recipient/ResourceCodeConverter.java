package net.pkhapps.idispatch.alerter.server.domain.recipient;

import org.springframework.lang.Nullable;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Attribute converter for {@link ResourceCode}.
 */
@Converter(autoApply = true)
public class ResourceCodeConverter implements AttributeConverter<ResourceCode, String> {

    @Override
    @Nullable
    public String convertToDatabaseColumn(@Nullable ResourceCode attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    @Nullable
    public ResourceCode convertToEntityAttribute(@Nullable String dbData) {
        return dbData == null ? null : new ResourceCode(dbData);
    }
}
