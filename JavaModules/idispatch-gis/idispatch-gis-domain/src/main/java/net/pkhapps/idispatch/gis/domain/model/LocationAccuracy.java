package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.shared.domain.model.Language;
import net.pkhapps.idispatch.shared.domain.model.MultilingualString;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me
 */
public enum LocationAccuracy implements Code<String> {

    UNDEFINED("0", "Ei määr.", "Odef."),
    MM500("500", "0,5 m"),
    MM800("800", "0,8 m"),
    MM1000("1000", "1 m"),
    MM2000("2000", "2 m"),
    MM3000("3000", "3 m"),
    MM4000("4000", "4 m"),
    MM5000("5000", "5 m"),
    MM7500("7500", "7,5 m"),
    MM8000("8000", "8 m"),
    MM10000("10000", "10 m"),
    MM12500("12500", "12,5 m"),
    MM15000("15000", "15 m"),
    MM20000("20000", "20 m"),
    MM25000("25000", "25 m"),
    MM30000("30000", "30 m"),
    MM40000("40000", "40 m"),
    MM80000("80000", "80 m"),
    MM100000("100000", "100 m");

    private final String code;
    private final MultilingualString description;

    LocationAccuracy(String code, String descriptionFin, String descriptionSwe) {
        this.code = code;
        this.description = new MultilingualString.Builder()
                .with(Language.FINNISH, descriptionFin)
                .with(Language.SWEDISH, descriptionSwe)
                .build();
    }

    LocationAccuracy(String code, String description) {
        this(code, description, description);
    }

    @Override
    public @NotNull String code() {
        return code;
    }

    public @NotNull MultilingualString description() {
        return description;
    }
}
