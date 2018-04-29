package net.pkhsolutions.idispatch.ui.common.resources;

import com.vaadin.data.util.converter.Converter;
import net.pkhsolutions.idispatch.entity.ResourceState;
import org.vaadin.spring.VaadinComponent;

import java.util.Locale;

/**
 * A one-way hibernate for converting a {@link net.pkhsolutions.idispatch.entity.ResourceState} to a string. This
 * is a singleton bean since it is completely stateless.
 */
@VaadinComponent
public class ResourceStateToStringConverter implements Converter<String, ResourceState> {
    @Override
    public ResourceState convertToModel(String value, Class<? extends ResourceState> targetType, Locale locale) throws ConversionException {
        throw new UnsupportedOperationException("Cannot convert to ResourceState");
    }

    @Override
    public String convertToPresentation(ResourceState value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        if (value != null) {
            // TODO Internationalize
            switch (value) {
                case AT_STATION:
                    return "At station";
                case RESERVED:
                    return "Reserved (not dispatched yet)";
                case DISPATCHED:
                    return "Dispatched";
                case EN_ROUTE:
                    return "En route";
                case ON_SCENE:
                    return "On scene";
                case AVAILABLE:
                    return "Available";
                case OUT_OF_SERVICE:
                    return "Unavailable";
            }
        }
        return null;
    }

    @Override
    public Class<ResourceState> getModelType() {
        return ResourceState.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
