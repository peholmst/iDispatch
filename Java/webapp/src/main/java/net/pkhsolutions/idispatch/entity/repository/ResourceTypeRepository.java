package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.ResourceType}s.
 */
public interface ResourceTypeRepository extends JpaRepository<ResourceType, Long> {
}
