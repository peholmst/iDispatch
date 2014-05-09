package net.pkhsolutions.idispatch.ui.admin.destinations;


import com.vaadin.data.util.converter.Converter;
import net.pkhsolutions.idispatch.entity.Resource;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class ResourcesToStringConverter implements Converter<String, Set> {

    @Override
    public Set convertToModel(String value, Class<? extends Set> targetType, Locale locale) throws ConversionException {
        throw new ConversionException("This is a one-way conversion");
    }

    @Override
    @SuppressWarnings("unchecked")
    public String convertToPresentation(Set value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        if (value == null || value.isEmpty()) {
            return "";
        } else {
            List<String> resources = ((Set<Resource>) value).stream().map(Resource::getCallSign).collect(Collectors.toList());
            Collections.sort(resources);
            return String.join(", ", resources);
        }
    }

    @Override
    public Class<Set> getModelType() {
        return Set.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
