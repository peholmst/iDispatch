package net.pkhapps.idispatch.gis.api;

import java.util.List;
import java.util.Locale;

/**
 * Collection of locales supported by the GIS.
 */
@SuppressWarnings("SpellCheckingInspection")
public final class Locales {

    public static final Locale FINNISH = new Locale("fi", "FI");
    public static final Locale SWEDISH = new Locale("sv", "FI");
    public static final Locale NORTHERN_SAMI = new Locale("se", "FI");
    public static final Locale INARI_SAMI = new Locale("smn", "FI");
    public static final Locale SKOLT_SAMI = new Locale("sms", "FI");

    public static final List<Locale> ALL_LOCALES = List.of(FINNISH, SWEDISH, NORTHERN_SAMI, INARI_SAMI, SKOLT_SAMI);

    private Locales() {
    }
}
