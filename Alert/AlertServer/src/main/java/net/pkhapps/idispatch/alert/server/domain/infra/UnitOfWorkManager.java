package net.pkhapps.idispatch.alert.server.domain.infra;

import java.util.function.Supplier;

/**
 * Interface defining a unit-of-work manager that is used by the application
 * layer to implement units-of-work. A unit-of-work is like a transaction;
 * either all the work in the unit gets done (the transaction is committed) or
 * none of it gets done (the transaction is rolled back).
 */
public interface UnitOfWorkManager {

    /**
     * Performs the given unit-of-work and returns its result.
     * 
     * @param <T>        the type of the result.
     * @param unitOfWork the unit-of-work to perform, must not be {@code null}.
     * @return the result returned by the unit-of-work, may be {@code null}.
     */
    <T> T performUnitOfWork(Supplier<T> unitOfWork);

    /**
     * Performs the given unit-of-work, discarding its result.
     * 
     * @param unitOfWork the unit-of-work to perform, must not be {@code null}.
     */
    default void performUnitOfWorkWithoutResult(Runnable unitOfWork) {
        performUnitOfWork(() -> {
            unitOfWork.run();
            return null;
        });
    }
}
