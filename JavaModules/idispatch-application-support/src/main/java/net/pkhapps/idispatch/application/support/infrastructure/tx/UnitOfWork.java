package net.pkhapps.idispatch.application.support.infrastructure.tx;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * TODO Document me
 */
public interface UnitOfWork {

    /**
     *
     */
    void beginTransaction();

    /**
     *
     */
    void commit();

    /**
     *
     */
    void rollback();

    /**
     *
     * @param type
     * @param <T>
     * @return
     */
    <T> T unwrap(@Nonnull Class<T> type);

    /**
     * Any exceptions thrown by the callback will be logged and ignored.
     * @param event
     * @param operation
     */
    void registerCallback(@Nonnull CallbackEvent event, @Nonnull Runnable operation);

    /**
     * @param operation
     */
    default void execute(@Nonnull Runnable operation) {
        Objects.requireNonNull(operation, "operation must not be null");
        execute(() -> {
            operation.run();
            return null;
        });
    }

    /**
     * @param operation
     * @param <T>
     * @return
     */
    default <T> T execute(Supplier<T> operation) {
        Objects.requireNonNull(operation, "operation must not be null");
        beginTransaction();
        try {
            var result = operation.get();
            commit();
            return result;
        } catch (RuntimeException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Enumeration of callback events for {@link #registerCallback(CallbackEvent, Runnable)}.
     */
    enum CallbackEvent {
        /**
         * Callback is invoked before the transaction is committed.
         */
        BEFORE_COMMIT,
        /**
         * Callback is invoked after the transaction has been successfully committed.
         */
        AFTER_COMMIT,
        /**
         * Callback is invoked before the transaction is rolled back.
         */
        BEFORE_ROLLBACK,
        /**
         * Callback is invoked after the transaction has been successfully rolled back.
         */
        AFTER_ROLLBACK,
        /**
         * Callback is invoked after the transaction has been completed, regardless of whether it was committed or
         * rolled back.
         */
        ON_COMPLETION
    }

}
