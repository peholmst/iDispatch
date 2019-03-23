package net.pkhapps.idispatch.dispatcher.console.i18n;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * TODO Document me!
 */
public class NullTranslator implements Translator {

    private static final NullTranslator INSTANCE = new NullTranslator();

    /**
     * @param translator
     * @return
     */
    public static @NotNull Translator providedOrNull(@Nullable Translator translator) {
        return translator == null ? INSTANCE : translator;
    }

    @Override
    public @NotNull Locale getLocale() {
        return Locale.getDefault();
    }

    @Override
    public @NotNull String get(@NotNull String key) {
        return "!" + key;
    }

    @Override
    public @NotNull String get(@NotNull String key, @NotNull Object... params) {
        return "!" + key;
    }
}
