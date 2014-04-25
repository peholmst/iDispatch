package net.pkhsolutions.idispatch.domain.resources;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of {@link net.pkhsolutions.idispatch.domain.resources.Resource}s.
 */
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    /**
     * Finds and returns the resource with the specified call sign, or {@code null} if not found.
     */
    Resource findByCallSign(String callSign);

    /**
     * Finds and returns all active resources.
     */
    List<Resource> findByActiveTrue();
}
