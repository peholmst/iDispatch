package net.pkhsolutions.idispatch.ui.common.resources;

import com.vaadin.data.util.converter.Converter;
import net.pkhsolutions.idispatch.entity.AssignmentType;
import org.vaadin.spring.VaadinComponent;

import java.util.Locale;

/**
 * A one-way converter for converting a {@link net.pkhsolutions.idispatch.entity.AssignmentType} to a string. This
 * is a singleton bean since it is completely stateless.
 */
@VaadinComponent
public class AssignmentTypeToStringConverter implements Converter<String, AssignmentType> {
    @Override
    public AssignmentType convertToModel(String value, Class<? extends AssignmentType> targetType, Locale locale) throws ConversionException {
        throw new UnsupportedOperationException("Cannot convert to AssignmentType");
    }

    @Override
    public String convertToPresentation(AssignmentType value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        return value == null ? null : value.getFormattedDescription();
    }

    @Override
    public Class<AssignmentType> getModelType() {
        return AssignmentType.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
