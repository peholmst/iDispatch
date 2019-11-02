package net.pkhapps.idispatch.gis.api.lookup.code;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Every municipality in Finland has a unique, national municipality code.
 */
public class MunicipalityCode {

    private final String code;

    private MunicipalityCode(@NotNull String code) {
        this.code = requireNonNull(code);
    }

    /**
     * Creates a new {@code MunicipalityCode}.
     *
     * @param code the municipality code as a string
     * @return the new {@code MunicipalityCode} instance or {@code null} if the municipality code string was {@code null}
     */
    @Contract("null -> null")
    public static MunicipalityCode of(String code) {
        return code == null ? null : new MunicipalityCode(code);
    }

    /**
     * Creates a new {@code MunicipalityCode}.
     *
     * @param code the municipality code as an integer
     * @return the new {@code MunicipalityCode} instance
     */
    @NotNull
    public static MunicipalityCode of(int code) {
        return of(String.valueOf(code));
    }

    /**
     * The municipality code as a string
     */
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
