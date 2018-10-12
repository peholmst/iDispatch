package net.pkhapps.idispatch.cad.infrastructure.jpa;

import net.pkhapps.idispatch.cad.infrastructure.tx.UnitOfWork;
import net.pkhapps.idispatch.cad.infrastructure.tx.UnitOfWorkManager;
import net.pkhapps.idispatch.domain.support.*;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * TODO Document me!
 *
 * @param <ID>
 * @param <T>
 */
@ThreadSafe
public abstract class JpaRepository<ID extends DomainObjectId, T extends AggregateRoot<ID>>
        extends AbstractRepository<ID, T> {

    private final Class<ID> idClass;
    private final Class<T> aggregateRootClass;
    private final UnitOfWorkManager unitOfWorkManager;

    protected JpaRepository(@Nonnull DomainEventStore domainEventStore,
                            @Nonnull DomainEventPublisher domainEventPublisher,
                            @Nonnull Class<ID> idClass,
                            @Nonnull Class<T> aggregateRootClass,
                            @Nonnull UnitOfWorkManager unitOfWorkManager) {
        super(domainEventStore, domainEventPublisher);
        this.idClass = Objects.requireNonNull(idClass, "idClass must not be null");
        this.aggregateRootClass = Objects.requireNonNull(aggregateRootClass, "aggregateRootClass must not be null");
        this.unitOfWorkManager = Objects.requireNonNull(unitOfWorkManager, "unitOfWorkManager must not be null");
    }

    @Nonnull
    private EntityManager entityManager() {
        return unitOfWorkManager.requireExisting().unwrap(EntityManager.class);
    }

    @Nonnull
    protected String entityName() {
        return aggregateRootClass.getName();
    }

    @Nonnull
    @Override
    public ID nextFreeId() {
        try {
            return idClass.getConstructor(Serializable.class).newInstance(UUID.randomUUID().toString());
        } catch (Exception ex) {
            throw new UnsupportedOperationException("Could not create new ID, please override nextFreeId");
        }
    }

    @Nonnull
    @Override
    public Optional<T> get(@Nonnull ID id) {
        return Optional.ofNullable(entityManager().find(aggregateRootClass, id));
    }

    @Override
    public boolean contains(@Nonnull ID id) {
        var query = entityManager().createQuery("select count(ag) from " + entityName() + " ag where ag.id = :id",
                Long.class);
        query.setParameter("id", id);
        return query.getSingleResult() == 1L;
    }

    @Override
    public void add(@Nonnull T aggregate) {
        if (isNew(aggregate)) {
            entityManager().persist(aggregate);
        } else if (!entityManager().contains(aggregate)) {
            entityManager().merge(aggregate);
        }
        storeDomainEvents(aggregate);
        unitOfWorkManager.requireExisting().registerCallback(
                UnitOfWork.CallbackEvent.AFTER_COMMIT,
                () -> publishDomainEvents(aggregate));
    }

    @Override
    public long size() {
        var query = entityManager().createQuery("select count(ag) from " + entityName() + " ag", Long.class);
        return query.getSingleResult();
    }
}
