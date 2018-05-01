package net.pkhapps.idispatch.web.ui.common;

import org.springframework.lang.NonNull;

import java.util.Locale;

/**
 * TODO Document me!
 */
public interface I18N {

    @NonNull
    Locale getLocale();

    @NonNull
    default String get(@NonNull String key, @NonNull Object... args) {
        return getOrDefault(key, key, args);
    }

    @NonNull
    String getOrDefault(@NonNull String key, @NonNull String defaultValue, @NonNull Object... args);
}
