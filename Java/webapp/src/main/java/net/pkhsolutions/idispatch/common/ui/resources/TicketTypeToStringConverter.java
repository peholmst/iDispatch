package net.pkhsolutions.idispatch.common.ui.resources;

import com.vaadin.data.util.converter.Converter;
import net.pkhsolutions.idispatch.domain.tickets.TicketType;
import org.vaadin.spring.VaadinComponent;

import java.util.Locale;

/**
 * A one-way converter for converting a {@link net.pkhsolutions.idispatch.domain.tickets.TicketType} to a string. This
 * is a singleton bean since it is completely stateless.
 */
@VaadinComponent
public class TicketTypeToStringConverter implements Converter<String, TicketType> {
    @Override
    public TicketType convertToModel(String value, Class<? extends TicketType> targetType, Locale locale) throws ConversionException {
        throw new UnsupportedOperationException("Cannot convert to TicketType");
    }

    @Override
    public String convertToPresentation(TicketType value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        return value == null ? null : value.getFormattedDescription();
    }

    @Override
    public Class<TicketType> getModelType() {
        return TicketType.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
