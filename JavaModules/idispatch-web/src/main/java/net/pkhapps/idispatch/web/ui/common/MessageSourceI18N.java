package net.pkhapps.idispatch.web.ui.common;

import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.lang.NonNull;

import java.util.Locale;
import java.util.Objects;

/**
 * TODO Implement me!
 */
public class MessageSourceI18N implements I18N {

    private final ResourceBundleMessageSource messageSource;
    private final Locale locale;

    public MessageSourceI18N(@NonNull String basename, @NonNull Locale locale) {
        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(Objects.requireNonNull(basename));
        this.locale = Objects.requireNonNull(locale);
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public String getOrDefault(String key, String defaultValue, Object... args) {
        try {
            return messageSource.getMessage(key, args, locale);
        } catch (NoSuchMessageException ex) {
            return defaultValue;
        }
    }
}
