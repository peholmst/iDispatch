package net.pkhsolutions.idispatch.domain.resources;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of {@link net.pkhsolutions.idispatch.domain.resources.Resource}s.
 */
public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
