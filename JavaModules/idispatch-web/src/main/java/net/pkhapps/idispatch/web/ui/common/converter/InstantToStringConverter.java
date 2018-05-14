package net.pkhapps.idispatch.web.ui.common.converter;

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
 * One-way converter for converting a {@link Instant} to a string.
 */
@SpringComponent
public class InstantToStringConverter extends OneWayToStringConverter<Instant> {

    private final ZoneId timeZone;

    /**
     * Creates a new converter.
     *
     * @param timeZone the timezone to use when converting the instant to a string.
     */
    public InstantToStringConverter(@Value("${dispatch.ui.timeZone}") @NonNull ZoneId timeZone) {
        this.timeZone = Objects.requireNonNull(timeZone);
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
