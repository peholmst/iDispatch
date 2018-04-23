package net.pkhsolutions.idispatch.ui.common;

import com.vaadin.data.util.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Converter from {@link java.util.Date} to {@code String} (and back).
 */
public class DateToStringConverter implements Converter<String, Date> {

    private final String formatString;

    private DateToStringConverter(String formatString) {
        this.formatString = formatString;
    }

    public static DateToStringConverter time() {
        return new DateToStringConverter("HH:mm:ss");
    }

    public static DateToStringConverter dateTime() {
        return new DateToStringConverter("d.M.yyyy HH:mm:ss");
    }

    public static DateToStringConverter date() {
        return new DateToStringConverter("d.M.yyyy");
    }

    @Override
    public Date convertToModel(String value, Class<? extends Date> targetType, Locale locale) throws ConversionException {
        if (value == null || value.isEmpty()) {
            return null;
        } else {
            try {
                if (locale == null) {
                    locale = Locale.getDefault();
                }
                Date date = new SimpleDateFormat(formatString, locale).parse(value);
                return date;
            } catch (ParseException e) {
                throw new ConversionException(e);
            }
        }
    }

    @Override
    public String convertToPresentation(Date value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return value == null ? null : new SimpleDateFormat(formatString, locale).format(value.getTime());
    }

    @Override
    public Class<Date> getModelType() {
        return Date.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
