package net.pkhapps.idispatch.gis.api.lookup;

/**
 * Enumeration of different strategies for matching a search term to a feature name.
 */
public enum NameMatchStrategy {
    /**
     * Look for features that have a name in any language that starts with the search term (case insensitive).
     */
    PREFIX,
    /**
     * Look for features that have a name in any language that is exactly equal to the search term (case
     * insensitive).
     */
    EXACT,
}
