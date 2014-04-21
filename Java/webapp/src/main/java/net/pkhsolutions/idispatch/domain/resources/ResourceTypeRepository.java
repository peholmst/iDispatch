package net.pkhsolutions.idispatch.domain.resources;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of {@link net.pkhsolutions.idispatch.domain.resources.ResourceType}s.
 */
public interface ResourceTypeRepository extends JpaRepository<ResourceType, Long> {
}
