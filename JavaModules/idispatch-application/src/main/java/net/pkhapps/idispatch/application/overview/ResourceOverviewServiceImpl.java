package net.pkhapps.idispatch.application.overview;

import net.pkhapps.idispatch.domain.resource.ResourceId;
import net.pkhapps.idispatch.domain.status.ResourceState;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Service
class ResourceOverviewServiceImpl implements ResourceOverviewService {

    private final HashSet<ResourceOverviewDTO> dummyData;

    ResourceOverviewServiceImpl() {
        dummyData = new HashSet<>();
        dummyData.add(createDummy("RVSPG31", ResourceState.RESERVED, "103 Pargas, Skolgatan 1"));
        dummyData.add(createDummy("RVSPG371", ResourceState.AVAILABLE, null));
        dummyData.add(createDummy("RVSPG11", ResourceState.EN_ROUTE, "103 Pargas, Skolgatan 1"));
        dummyData.add(createDummy("RVSPG21", ResourceState.ON_SCENE, "202 Pargas, Lielaxvägen"));
        dummyData.add(createDummy("RVSPG27", ResourceState.DISPATCHED, "202 Pargas, Lielaxvägen"));
        dummyData.add(createDummy("RVSPG13", ResourceState.AT_STATION, null));
        dummyData.add(createDummy("RVSPG23", ResourceState.AT_STATION, null));
        dummyData.add(createDummy("RVSPG16", ResourceState.AT_STATION, null));
        dummyData.add(createDummy("RVSPG29", ResourceState.AT_STATION, null));
        dummyData.add(createDummy("RVSPG17", ResourceState.AVAILABLE, null));
    }

    @Override
    public Set<ResourceOverviewDTO> getAll() {
        return dummyData; // TODO Replace with real data
    }

    @Override
    public Set<ResourceOverviewDTO> getChangesSince(Instant timestamp) {
        return Collections.emptySet();
    }

    private static AtomicLong nextId = new AtomicLong();

    private static ResourceOverviewDTO createDummy(String callSign, ResourceState state, String assignment) {
        ResourceOverviewDTO dto = new ResourceOverviewDTO();
        dto.setResourceId(new ResourceId(nextId.incrementAndGet()));
        dto.setCallSign(callSign);
        dto.setState(state);
        dto.setAssignmentSummary(assignment);
        return dto;
    }
}
