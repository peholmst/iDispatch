package net.pkhapps.idispatch.core.domain.geo;

import net.pkhapps.idispatch.core.domain.common.ValueObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * TODO Implement me!
 */
public class MunicipalityId implements ValueObject {

    private final String nationalCode;

    public MunicipalityId(@NotNull String nationalCode) {
        this.nationalCode = nationalCode;
    }

    @Override
    public String toString() {
        return nationalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MunicipalityId that = (MunicipalityId) o;
        return nationalCode.equals(that.nationalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nationalCode);
    }
}
