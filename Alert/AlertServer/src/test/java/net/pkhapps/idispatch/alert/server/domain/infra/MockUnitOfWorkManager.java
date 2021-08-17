package net.pkhapps.idispatch.alert.server.domain.infra;

import java.util.function.Supplier;

/**
 * A mock implementation of {@link UnitOfWorkManager} designed to be used in
 * unit tests.
 */
public class MockUnitOfWorkManager implements UnitOfWorkManager {

    private Object lastUnitOfWorkResult;

    @Override
    public <T> T performUnitOfWork(Supplier<T> unitOfWork) {
        final var result = unitOfWork.get();
        this.lastUnitOfWorkResult = result;
        return result;
    }

    /**
     * Gets the result of the last performed unit of work.
     * 
     * @return the result, may be {@code null}.
     */
    @SuppressWarnings("unchecked")
    public <T> T getLastUnitOfWorkResult() {
        return (T) lastUnitOfWorkResult;
    }
}
