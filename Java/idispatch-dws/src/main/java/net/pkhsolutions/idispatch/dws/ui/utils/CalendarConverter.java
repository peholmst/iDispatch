package net.pkhsolutions.idispatch.dws.ui.utils;

import com.vaadin.data.util.converter.Converter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarConverter implements Converter<String, Calendar> {

    private final String formatString;

    private CalendarConverter(String formatString) {
        this.formatString = formatString;
    }

    public static CalendarConverter time() {
        return new CalendarConverter("HH:mm:ss");
    }

    public static CalendarConverter dateTime() {
        return new CalendarConverter("d.M.yyyy HH:mm:ss");
    }

    public static CalendarConverter date() {
        return new CalendarConverter("d.M.yyyy");
    }

    @Override
    public Calendar convertToModel(String value, Locale locale) throws ConversionException {
        if (value == null) {
            return null;
        } else {
            try {
                Date date = new SimpleDateFormat(formatString, locale).parse(value);
                Calendar cal = Calendar.getInstance(locale);
                cal.setTime(date);
                return cal;
            } catch (ParseException e) {
                throw new ConversionException(e);
            }
        }
    }

    @Override
    public String convertToPresentation(Calendar value, Locale locale) throws ConversionException {
        return value == null ? null : new SimpleDateFormat(formatString, locale).format(value.getTime());
    }

    @Override
    public Class<Calendar> getModelType() {
        return Calendar.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
