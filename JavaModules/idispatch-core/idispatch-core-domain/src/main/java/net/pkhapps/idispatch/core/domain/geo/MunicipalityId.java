package net.pkhapps.idispatch.core.domain.geo;

import net.pkhapps.idispatch.core.domain.common.ValueObject;
import org.jetbrains.annotations.NotNull;

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
}
