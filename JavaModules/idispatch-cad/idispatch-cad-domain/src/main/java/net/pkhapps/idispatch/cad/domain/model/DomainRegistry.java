package net.pkhapps.idispatch.cad.domain.model;

import net.pkhapps.idispatch.cad.domain.model.resource.ResourceRepository;
import net.pkhapps.idispatch.cad.domain.model.resource.ResourceTypeRepository;
import net.pkhapps.idispatch.domain.support.AbstractDomainRegistry;
import net.pkhapps.idispatch.domain.support.DomainEventPublisher;
import net.pkhapps.idispatch.domain.support.DomainEventStore;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.time.Clock;
import java.util.Objects;

/**
 * TODO Document me!
 */
@ThreadSafe
public class DomainRegistry extends AbstractDomainRegistry {

    private static DomainRegistry INSTANCE;
    private final ResourceRepository resourceRepository;
    private final ResourceTypeRepository resourceTypeRepository;

    protected DomainRegistry(@Nonnull Clock clock,
                             @Nonnull DomainEventPublisher domainEventPublisher,
                             @Nonnull DomainEventStore domainEventStore,
                             @Nonnull ResourceRepository resourceRepository,
                             @Nonnull ResourceTypeRepository resourceTypeRepository) {
        super(clock, domainEventPublisher, domainEventStore);
        this.resourceRepository = Objects.requireNonNull(resourceRepository, "resourceRepository must not be null");
        this.resourceTypeRepository = Objects.requireNonNull(resourceTypeRepository, "resourceTypeRepository must not be null");
    }

    @Nonnull
    public static DomainRegistry instance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("The DomainRegistry has not been initialized yet");
        }
        return INSTANCE;
    }

    public static void initialize(@Nonnull DomainRegistry domainRegistry) {
        INSTANCE = Objects.requireNonNull(domainRegistry, "domainRegistry must not be null");
    }

    @Nonnull
    public ResourceRepository resourceRepository() {
        return resourceRepository;
    }

    @Nonnull
    public ResourceTypeRepository resourceTypeRepository() {
        return resourceTypeRepository;
    }

}
