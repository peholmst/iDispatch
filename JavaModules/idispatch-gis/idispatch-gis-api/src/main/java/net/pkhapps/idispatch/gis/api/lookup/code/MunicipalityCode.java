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

    @NotNull
    public static MunicipalityCode of(int code) {
        return of(String.valueOf(code));
    }

    public @NotNull String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("%s{%s}", getClass().getSimpleName(), code);
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
