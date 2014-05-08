package net.pkhsolutions.idispatch.ui.common.resources;

import com.vaadin.data.util.converter.Converter;
import net.pkhsolutions.idispatch.entity.ResourceType;
import org.vaadin.spring.VaadinComponent;

import java.util.Locale;

/**
 * A one-way converter for converting a {@link net.pkhsolutions.idispatch.entity.ResourceType} to a string. This
 * is a singleton bean since it is completely stateless.
 */
@VaadinComponent
public class ResourceTypeToStringConverter implements Converter<String, ResourceType> {
    @Override
    public ResourceType convertToModel(String value, Class<? extends ResourceType> targetType, Locale locale) throws ConversionException {
        throw new UnsupportedOperationException("Cannot convert to ResourceType");
    }

    @Override
    public String convertToPresentation(ResourceType value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        return value == null ? null : value.getDescription();
    }

    @Override
    public Class<ResourceType> getModelType() {
        return ResourceType.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
