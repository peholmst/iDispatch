package net.pkhsolutions.idispatch.domain.dispatch;

import net.pkhsolutions.idispatch.domain.resources.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination, Long> {

    @Query("select d from Destination d where d.active = TRUE and ?0 in d.resources")
    List<Destination> findDestinationsForResource(Resource resource);

}
