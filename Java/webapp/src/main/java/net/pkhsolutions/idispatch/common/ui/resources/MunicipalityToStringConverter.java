package net.pkhsolutions.idispatch.common.ui.resources;

import com.vaadin.data.util.converter.Converter;
import net.pkhsolutions.idispatch.domain.Municipality;
import org.vaadin.spring.VaadinComponent;

import java.util.Locale;

/**
 * A one-way converter for converting a {@link net.pkhsolutions.idispatch.domain.Municipality} to a string. This
 * is a singleton bean since it is completely stateless.
 */
@VaadinComponent
public class MunicipalityToStringConverter implements Converter<String, Municipality> {
    @Override
    public Municipality convertToModel(String value, Class<? extends Municipality> targetType, Locale locale) throws ConversionException {
        throw new UnsupportedOperationException("Cannot convert to Municipality");
    }

    @Override
    public String convertToPresentation(Municipality value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        return value == null ? null : value.getName();
    }

    @Override
    public Class<Municipality> getModelType() {
        return Municipality.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
