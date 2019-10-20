package net.pkhapps.idispatch.gis.postgis.lookup;

import net.pkhapps.idispatch.gis.api.lookup.AddressNumberRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * TODO Document me
 */
class AddressNumberRangeImpl implements AddressNumberRange {

    private final Integer min;
    private final Integer max;

    AddressNumberRangeImpl(@Nullable Integer min, @Nullable Integer max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public @NotNull Optional<Integer> getMinimum() {
        return Optional.ofNullable(min);
    }

    @Override
    public @NotNull Optional<Integer> getMaximum() {
        return Optional.ofNullable(max);
    }
}
