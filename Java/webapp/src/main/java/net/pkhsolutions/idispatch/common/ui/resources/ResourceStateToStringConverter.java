package net.pkhsolutions.idispatch.common.ui.resources;

import com.vaadin.data.util.converter.Converter;
import net.pkhsolutions.idispatch.domain.resources.ResourceState;

import java.util.Locale;

/**
 * A one-way converter for converting a {@link net.pkhsolutions.idispatch.domain.resources.ResourceState} to a string.
 */
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
                case ASSIGNED:
                    return "Assigned";
                case DISPATCHED:
                    return "Dispatched";
                case EN_ROUTE:
                    return "En route";
                case ON_SCENE:
                    return "On scene";
                case AVAILABLE:
                    return "Available";
                case UNAVAILABLE:
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
