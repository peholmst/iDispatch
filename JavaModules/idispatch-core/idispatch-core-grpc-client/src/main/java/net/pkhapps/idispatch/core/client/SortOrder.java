package net.pkhapps.idispatch.core.client;

import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me!
 */
public class SortOrder {

    private final String sortBy;
    private final boolean ascending;

    private SortOrder(@NotNull String sortBy, boolean ascending) {
        this.sortBy = requireNonNull(sortBy);
        this.ascending = ascending;
    }

    public static @NotNull SortOrder asc(@NotNull String sortBy) {
        return new SortOrder(sortBy, true);
    }

    public static @NotNull SortOrder desc(@NotNull String sortBy) {
        return new SortOrder(sortBy, false);
    }

    public @NotNull String getSortBy() {
        return sortBy;
    }

    public boolean isAscending() {
        return ascending;
    }

    public boolean isDescending() {
        return !ascending;
    }
}
