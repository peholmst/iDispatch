package net.pkhapps.idispatch.gis.api.lookup.code;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me!
 */
public class MunicipalityCode {

    private final String code;

    public MunicipalityCode(@NotNull String code) {
        this.code = requireNonNull(code);
    }

    @Contract("null -> null")
    public static MunicipalityCode of(String code) {
        return code == null ? null : new MunicipalityCode(code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MunicipalityCode that = (MunicipalityCode) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
