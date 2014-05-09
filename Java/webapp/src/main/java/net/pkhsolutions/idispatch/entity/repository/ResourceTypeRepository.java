package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.ResourceType}s.
 */
public interface ResourceTypeRepository extends JpaRepository<ResourceType, Long> {

    List<ResourceType> findByActiveTrueOrderByCodeAsc();
}
