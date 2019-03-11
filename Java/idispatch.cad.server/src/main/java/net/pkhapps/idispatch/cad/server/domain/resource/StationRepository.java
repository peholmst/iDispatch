package net.pkhapps.idispatch.cad.server.domain.resource;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link Station}.
 */
public interface StationRepository extends JpaRepository<Station, Long> {
    // TODO Add query method for location (find stations within a specific area)
}
