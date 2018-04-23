package net.pkhapps.idispatch.domain.repository;

import net.pkhapps.idispatch.domain.Destination;
import net.pkhapps.idispatch.domain.resource.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository of {@link Destination}s.
 */
public interface DestinationRepository extends JpaRepository<Destination, Long> {

    @Query("select d from Destination d join d.resources r where d.active = TRUE and r = ?1")
    List<Destination> findDestinationsForResource(Resource resource);

}
