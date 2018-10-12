package net.pkhapps.idispatch.cad.infrastructure.jpa.resource;

import net.pkhapps.idispatch.cad.domain.model.resource.Resource;
import net.pkhapps.idispatch.cad.domain.model.resource.ResourceId;
import net.pkhapps.idispatch.cad.domain.model.resource.ResourceRepository;
import net.pkhapps.idispatch.cad.infrastructure.jpa.JpaRepository;
import net.pkhapps.idispatch.cad.infrastructure.tx.UnitOfWorkManager;
import net.pkhapps.idispatch.domain.support.DomainEventPublisher;
import net.pkhapps.idispatch.domain.support.DomainEventStore;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * JPA implementation of {@link ResourceRepository}.
 */
@ThreadSafe
public class JpaResourceRepository extends JpaRepository<ResourceId, Resource> implements ResourceRepository {

    public JpaResourceRepository(@Nonnull DomainEventStore domainEventStore,
                                 @Nonnull DomainEventPublisher domainEventPublisher,
                                 @Nonnull UnitOfWorkManager unitOfWorkManager) {
        super(domainEventStore, domainEventPublisher, ResourceId.class, Resource.class, unitOfWorkManager);
    }
}
