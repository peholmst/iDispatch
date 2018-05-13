package net.pkhapps.idispatch.web.ui.common.converter;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Objects;

/**
 * TODO Document me!
 */
@SpringComponent
public class StringToInstantConverter implements Converter<String, Instant> {

    private final ZoneId timeZone;

    public StringToInstantConverter(@Value("${dispatch.ui.timeZone}") @NonNull ZoneId timeZone) {
        this.timeZone = Objects.requireNonNull(timeZone);
    }

    @Override
    public Result<Instant> convertToModel(String value, ValueContext context) {
        return Result.error("not implemented"); // TODO Implement me!
    }

    @Override
    public String convertToPresentation(Instant value, ValueContext context) {
        if (value == null) {
            return "";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)
                    .withLocale(context.getLocale().orElse(Locale.getDefault())).withZone(timeZone);
            return formatter.format(value);
        }
    }
}
