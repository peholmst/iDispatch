package net.pkhapps.idispatch.application.support.infrastructure.tx;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * TODO document me
 */
public class UnitOfWorkManager {

    private final ThreadLocal<UnitOfWork> currentUnitOfWork = new ThreadLocal<>();
    private final UnitOfWorkFactory unitOfWorkFactory;

    public UnitOfWorkManager(@Nonnull UnitOfWorkFactory unitOfWorkFactory) {
        this.unitOfWorkFactory = unitOfWorkFactory;
    }

    @Nonnull
    public Optional<UnitOfWork> current() {
        return Optional.ofNullable(currentUnitOfWork.get());
    }

    private UnitOfWork createCurrentUnitOfWork() {
        var unitOfWork = unitOfWorkFactory.createUnitOfWork();
        unitOfWork.registerCallback(UnitOfWork.CallbackEvent.ON_COMPLETION, currentUnitOfWork::remove);
        currentUnitOfWork.set(unitOfWork);
        return unitOfWork;
    }

    @Nonnull
    public UnitOfWork requireExisting() {
        return current().orElseThrow(() -> new IllegalStateException("No UnitOfWork bound to current thread"));
    }

    @Nonnull
    public UnitOfWork requireNew() {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
