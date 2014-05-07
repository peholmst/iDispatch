package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.Destination;
import net.pkhsolutions.idispatch.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination, Long> {

    @Query("select d from Destination d where d.active = TRUE and ?0 in d.resources")
    List<Destination> findDestinationsForResource(Resource resource);

}
