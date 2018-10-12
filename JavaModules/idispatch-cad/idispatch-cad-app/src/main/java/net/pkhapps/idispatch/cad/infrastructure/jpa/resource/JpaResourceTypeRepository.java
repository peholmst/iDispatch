package net.pkhapps.idispatch.cad.infrastructure.jpa.resource;

import net.pkhapps.idispatch.cad.domain.model.resource.ResourceType;
import net.pkhapps.idispatch.cad.domain.model.resource.ResourceTypeId;
import net.pkhapps.idispatch.cad.domain.model.resource.ResourceTypeRepository;
import net.pkhapps.idispatch.cad.infrastructure.jpa.JpaRepository;
import net.pkhapps.idispatch.cad.infrastructure.tx.UnitOfWorkManager;
import net.pkhapps.idispatch.domain.support.DomainEventPublisher;
import net.pkhapps.idispatch.domain.support.DomainEventStore;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * JPA implementation of {@link ResourceTypeRepository}.
 */
@ThreadSafe
public class JpaResourceTypeRepository extends JpaRepository<ResourceTypeId, ResourceType>
        implements ResourceTypeRepository {

    public JpaResourceTypeRepository(@Nonnull DomainEventStore domainEventStore,
                                     @Nonnull DomainEventPublisher domainEventPublisher,
                                     @Nonnull UnitOfWorkManager unitOfWorkManager) {
        super(domainEventStore, domainEventPublisher, ResourceTypeId.class, ResourceType.class, unitOfWorkManager);
    }
}
