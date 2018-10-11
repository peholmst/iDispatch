package net.pkhsolutions.idispatch.ui.common.resources;

import com.vaadin.data.util.converter.Converter;
import net.pkhsolutions.idispatch.entity.Resource;
import org.vaadin.spring.VaadinComponent;

import java.util.Locale;

/**
 * A one-way hibernate for converting a {@link net.pkhsolutions.idispatch.entity.Resource} to a string. This
 * is a singleton bean since it is completely stateless.
 */
@VaadinComponent
public class ResourceToStringConverter implements Converter<String, Resource> {
    @Override
    public Resource convertToModel(String value, Class<? extends Resource> targetType, Locale locale) throws ConversionException {
        throw new UnsupportedOperationException("Cannot convert to Resource");
    }

    @Override
    public String convertToPresentation(Resource value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        return value == null ? null : value.getCallSign();
    }

    @Override
    public Class<Resource> getModelType() {
        return Resource.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
