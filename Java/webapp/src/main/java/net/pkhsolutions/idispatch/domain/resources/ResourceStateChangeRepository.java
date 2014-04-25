package net.pkhsolutions.idispatch.domain.resources;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of {@link net.pkhsolutions.idispatch.domain.resources.ResourceStateChange}s.
 */
public interface ResourceStateChangeRepository extends JpaRepository<ResourceStateChange, Long> {

    List<ResourceStateChange> findByResource(Resource resource, Pageable pageable);
}
