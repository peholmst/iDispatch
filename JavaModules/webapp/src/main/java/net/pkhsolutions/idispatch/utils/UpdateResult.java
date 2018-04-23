package net.pkhsolutions.idispatch.utils;

import java.util.Optional;

/**
 * TODO Document me!
 */
public abstract class UpdateResult<D> {

    private final Optional<D> data;

    protected UpdateResult(Optional<D> data) {
        this.data = data;
    }

    public abstract boolean isSuccessful();

    public Optional<D> getData() {
        return data;
    }

    /**
     * TODO Document me!
     *
     * @param <D>
     */
    public static class Success<D> extends UpdateResult<D> {

        public Success(D data) {
            super(Optional.of(data));
        }

        @Override
        public boolean isSuccessful() {
            return true;
        }
    }

    /**
     * TODO Document me!
     *
     * @param <D>
     */
    public static class Conflict<D> extends UpdateResult<D> {

        private final D conflictingData;

        public Conflict(D data, D conflictingData) {
            super(Optional.of(data));
            this.conflictingData = conflictingData;
        }

        @Override
        public boolean isSuccessful() {
            return false;
        }

        public D getConflictingData() {
            return conflictingData;
        }
    }

    /**
     * TODO Document me!
     *
     * @param <D>
     */
    public static class NoChange<D> extends UpdateResult<D> {

        public NoChange(D data) {
            super(Optional.of(data));
        }

        @Override
        public boolean isSuccessful() {
            return true;
        }
    }
}
