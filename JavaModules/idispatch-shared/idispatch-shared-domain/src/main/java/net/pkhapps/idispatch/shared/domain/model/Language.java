package net.pkhapps.idispatch.shared.domain.model;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.stream.Stream;

/**
 * Enumeration of languages.
 */
public enum Language {
    FINNISH("fin", "fi"),
    SWEDISH("swe", "sv"),
    ENGLISH("eng", "en");

    private final String iso639_2;
    private final String iso639_1;

    Language(@NotNull String iso639_2, @NotNull String iso639_1) {
        this.iso639_2 = iso639_2;
        this.iso639_1 = iso639_1;
    }

    /**
     * Returns the language of the given ISO-639 Alpha-3 code.
     *
     * @throws IllegalArgumentException if the given code is not supported.
     */
    @NotNull
    public static Language fromISO639_2(@NotNull String iso639_2) {
        return Stream.of(values())
                .filter(l -> l.iso639_2.equals(iso639_2))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported language: " + iso639_2));
    }

    /**
     * Returns the language of the given ISO-639 Alpha-2 code.
     *
     * @throws IllegalArgumentException if the given code is not supported.
     */
    @NotNull
    public static Language fromISO639_1(@NotNull String iso639_1) {
        return Stream.of(values())
                .filter(l -> l.iso639_1.equals(iso639_1))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported language: " + iso639_1));
    }

    /**
     * Returns the language as an ISO-639 Alpha-3 code.
     */
    @NotNull
    public String toISO639_2() {
        return iso639_2;
    }

    /**
     * Returns the language as an ISO-639 Alpha-2 code.
     */
    @NotNull
    public String toISO639_1() {
        return iso639_1;
    }

    /**
     * Returns the language as a locale.
     */
    @NotNull
    public Locale toLocale() {
        return new Locale(iso639_1);
    }
}
