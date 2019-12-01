package net.pkhapps.idispatch.dws;

import com.vaadin.flow.i18n.I18NProvider;
import net.pkhapps.idispatch.gis.api.Locales;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * TODO Document me
 */
public class ResourceBundleI18NProvider implements I18NProvider {

    private final List<Locale> locales = List.of(Locales.FINNISH, Locales.SWEDISH);

    @Override
    public List<Locale> getProvidedLocales() {
        return locales;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        try {
            return MessageFormat.format(getResourceBundle(locale).getString(key), params);
        } catch (MissingResourceException ex) {
            return "!" + key;
        }
    }

    private ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle("net.pkhapps.idispatch.dws.translations", locale);
    }
}
