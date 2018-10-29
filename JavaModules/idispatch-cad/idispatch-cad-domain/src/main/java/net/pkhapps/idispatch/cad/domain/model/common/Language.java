package net.pkhapps.idispatch.cad.domain.model.common;

import javax.annotation.Nonnull;

public enum Language {
    FINNISH("fin"),
    SWEDISH("swe"),
    ENGLISH("eng");

    final String iso639_2;

    Language(@Nonnull String iso639_2) {
        this.iso639_2 = iso639_2;
    }

    @Nonnull
    public String iso639_2() {
        return iso639_2;
    }
}
