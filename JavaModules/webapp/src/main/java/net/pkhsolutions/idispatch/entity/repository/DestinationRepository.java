package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.Destination;
import net.pkhsolutions.idispatch.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.Destination}s.
 */
public interface DestinationRepository extends JpaRepository<Destination, Long> {

    @Query("select d from Destination d join d.resources r where d.active = TRUE and r = ?1")
    List<Destination> findDestinationsForResource(Resource resource);

}
