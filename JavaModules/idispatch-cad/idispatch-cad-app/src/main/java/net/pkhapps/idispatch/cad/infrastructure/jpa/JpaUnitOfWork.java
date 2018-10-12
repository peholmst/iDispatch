package net.pkhapps.idispatch.cad.infrastructure.jpa;

import lombok.extern.slf4j.Slf4j;
import net.pkhapps.idispatch.cad.infrastructure.tx.UnitOfWork;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;
import java.util.*;

/**
 * Implementation of {@link UnitOfWork} that binds the unit of work to a single {@link EntityManager} instance.
 */
@NotThreadSafe
@Slf4j
class JpaUnitOfWork implements UnitOfWork {

    private final EntityManager entityManager;
    private Map<CallbackEvent, Set<Runnable>> callbacks;
    private boolean invokingCallbacks = false;

    /**
     * Creates a new {@link JpaUnitOfWork}.
     *
     * @param entityManagerFactory the {@link EntityManagerFactory} to use for creating the {@link EntityManager}.
     */
    JpaUnitOfWork(@Nonnull EntityManagerFactory entityManagerFactory) {
        Objects.requireNonNull(entityManagerFactory, "entityManagerFactory must not be null");
        entityManager = entityManagerFactory.createEntityManager();
    }
    
    @Override
    public void beginTransaction() {
        log.debug("Beginning transaction in {}", this);
        entityManager.getTransaction().begin();
    }

    @Override
    public void commit() {
        try {
            doCommit();
        } catch (RollbackException ex) {
            doRollback();
            throw ex;
        } finally {
            closeEntityManager();
        }
    }

    private void doCommit() {
        var tx = entityManager.getTransaction();
        if (tx.isActive()) {
            invokeCallbacks(CallbackEvent.BEFORE_COMMIT);
            log.debug("Committing transaction in {}", this);
            tx.commit();
            invokeCallbacks(CallbackEvent.AFTER_COMMIT);
        }
    }

    @Override
    public void rollback() {
        try {
            doRollback();
        } finally {
            closeEntityManager();
        }
    }

    @Override
    public <T> T unwrap(@Nonnull Class<T> type) {
        if (type == EntityManager.class) {
            return type.cast(entityManager);
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    private void doRollback() {
        var tx = entityManager.getTransaction();
        if (tx.isActive()) {
            invokeCallbacks(CallbackEvent.BEFORE_ROLLBACK);
            log.debug("Rolling back transaction in {}", this);
            tx.rollback();
            invokeCallbacks(CallbackEvent.AFTER_ROLLBACK);
        }
    }

    @Override
    public void registerCallback(@Nonnull CallbackEvent event, @Nonnull Runnable operation) {
        if (invokingCallbacks) {
            throw new IllegalStateException("Cannot register new callback while invoking existing callbacks");
        }
        Objects.requireNonNull(event, "event must not be null");
        Objects.requireNonNull(operation, "operation must not be null");
        log.trace("Registering callback {} for event {} in {}", operation, event, this);
        if (callbacks == null) {
            callbacks = new HashMap<>();
        }
        callbacks.computeIfAbsent(event, ignoreMe -> new HashSet<>()).add(operation);
    }

    private void invokeCallbacks(@Nonnull CallbackEvent event) {
        invokingCallbacks = true;
        try {
            log.debug("Invoking callbacks for event {} in {}", event, this);
            getCallbacksForEvent(event).forEach(callback -> {
                try {
                    callback.run();
                } catch (Exception ex) {
                    log.error(String.format("Exception thrown by callback %s while invoking callbacks for event %s",
                            callback, event), ex);
                }
            });
        } finally {
            invokingCallbacks = false;
        }
    }

    @Nonnull
    private Set<Runnable> getCallbacksForEvent(@Nonnull CallbackEvent event) {
        if (callbacks == null) {
            return Collections.emptySet();
        }
        return callbacks.getOrDefault(event, Collections.emptySet());
    }

    private void closeEntityManager() {
        try {
            log.debug("Closing EntityManager in {}", this);
            entityManager.close();
        } finally {
            invokeCallbacks(CallbackEvent.ON_COMPLETION);
        }
    }
}
