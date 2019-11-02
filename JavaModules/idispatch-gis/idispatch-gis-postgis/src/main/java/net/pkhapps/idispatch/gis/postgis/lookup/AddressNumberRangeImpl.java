package net.pkhapps.idispatch.gis.postgis.lookup;

import net.pkhapps.idispatch.gis.api.lookup.AddressNumberRange;

/**
 * TODO Document me
 */
class AddressNumberRangeImpl implements AddressNumberRange {

    private final int min;
    private final int max;

    AddressNumberRangeImpl(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public int getMinimum() {
        return min;
    }

    @Override
    public int getMaximum() {
        return max;
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", min, max);
    }
}
