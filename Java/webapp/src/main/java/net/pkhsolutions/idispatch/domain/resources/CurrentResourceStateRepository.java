package net.pkhsolutions.idispatch.domain.resources;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository of {@link net.pkhsolutions.idispatch.domain.resources.CurrentResourceState}s.
 */
interface CurrentResourceStateRepository extends JpaRepository<CurrentResourceState, Long> {

    @Query("select s.lastResourceStateChange from CurrentResourceState s where s.resource.active = true order by s.resource.callSign")
    List<ResourceStateChange> findCurrentStatesOfAllActiveResources();

    @Query("select s.lastResourceStateChange from CurrentResourceState s where s.resource = :resource")
    ResourceStateChange findCurrentStateByResource(Resource resource);
}