package net.pkhapps.idispatch.gis.api.lookup;

/**
 * The data provided by NLS does not contain exact locations for address numbers. Instead, you get road segments that
 * cover certain address number ranges.
 */
public interface AddressNumberRange {

    /**
     * The lower bound (inclusive) of this address number range
     */
    int getMinimum();

    /**
     * The upper bound (inclusive) of this address number range
     */
    int getMaximum();

    /**
     * Checks if the given address number is within this address number range.
     *
     * @param addressNumber the address number to check
     * @return true if the address number is inside the range, false otherwise
     */
    default boolean contains(int addressNumber) {
        return addressNumber >= getMinimum() && addressNumber <= getMaximum();
    }
}
