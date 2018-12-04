package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.shared.domain.model.Language;
import net.pkhapps.idispatch.shared.domain.model.MultilingualString;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me
 */
public enum Elevation implements Code<String> {
    IN_TUNNEL("-11", "Tunnelissa", "I tunnel"),
    BELOW_SURFACE_LEVEL_3("-3", "Pinnan alla taso 3", "Under ytan nivå 3"),
    BELOW_SURFACE_LEVEL_2("-2", "Pinnan alla taso 2", "Under ytan nivå 2"),
    BELOW_SURFACE_LEVEL_1("-1", "Pinnan alla taso 1", "Under ytan nivå 1"),
    ON_SURFACE("0", "Pinnalla", "På ytan"),
    ABOVE_SURFACE_LEVEL_1("1", "Pinnan yllä taso 1", "Ovanför ytan nivå 1"),
    ABOVE_SURFACE_LEVEL_2("2", "Pinnan yllä taso 2", "Ovanför ytan nivå 2"),
    ABOVE_SURFACE_LEVEL_3("3", "Pinnan yllä taso 3", "Ovanför ytan nivå 3"),
    ABOVE_SURFACE_LEVEL_4("4", "Pinnan yllä taso 4", "Ovanför ytan nivå 4"),
    ABOVE_SURFACE_LEVEL_5("5", "Pinnan yllä taso 5", "Ovanför ytan nivå 5"),
    UNDEFINED("10", "Määrittelemätön", "Odefinierad");

    private final String code;
    private final MultilingualString description;

    Elevation(String code, String descriptionFin, String descriptionSwe) {
        this.code = code;
        this.description = new MultilingualString.Builder()
                .with(Language.FINNISH, descriptionFin)
                .with(Language.SWEDISH, descriptionSwe)
                .build();
    }

    @Override
    public @NotNull String code() {
        return code;
    }

    public @NotNull MultilingualString description() {
        return description;
    }
}
