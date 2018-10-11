package net.pkhapps.idispatch.domain.base;

/**
 * Interface to be implemented by objects (mainly aggregates) that support soft delete, i.e. instead of removing the
 * object from the storage, it is only flagged as deleted (and can thus be excluded from queries while remaining in
 * the storage).
 */
public interface SupportsSoftDelete {

    /**
     * Returns whether this object is active (i.e. not {@link #isDeleted() deleted}).
     */
    boolean isActive();

    /**
     * Returns whether this object is deleted (i.e. not {@link #isActive() active}).
     */
    default boolean isDeleted() {
        return !isActive();
    }

    /**
     * Marks this object as {@link #isActive() active}.
     */
    void undelete();

    /**
     * Marks this object as {@link #isDeleted() deleted}.
     */
    void delete();
}
