package net.pkhapps.idispatch.gis.api.lookup;

/**
 * TODO Document me
 */
public interface AddressNumberRange {

    int getMinimum();

    int getMaximum();

    default boolean contains(int addressNumber) {
        return addressNumber >= getMinimum() && addressNumber <= getMaximum();
    }
}
