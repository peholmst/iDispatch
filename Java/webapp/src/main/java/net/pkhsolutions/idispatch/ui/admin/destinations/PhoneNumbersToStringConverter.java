package net.pkhsolutions.idispatch.ui.admin.destinations;

import com.vaadin.data.util.converter.Converter;

import java.util.Locale;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

class PhoneNumbersToStringConverter implements Converter<String, Set> {
    @Override
    public Set convertToModel(String value, Class<? extends Set> targetType, Locale locale) throws ConversionException {
        if (value == null || value.isEmpty()) {
            return newHashSet();
        } else {
            final Set<String> destination = newHashSet();
            StringBuilder sb = new StringBuilder();
            for (char c : value.toCharArray()) {
                if (c == '+' || Character.isDigit(c)) {
                    sb.append(c);
                } else if (c == '\n' || c == ',') {
                    destination.add(sb.toString());
                    sb = new StringBuilder();
                }
            }
            destination.add(sb.toString());
            return destination;
        }
    }

    @Override
    public String convertToPresentation(Set value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        if (value == null) {
            return "";
        } else {
            return String.join("\n", value);
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
