package net.pkhapps.idispatch.application.overview;

import net.pkhapps.idispatch.domain.assignment.AssignmentId;
import net.pkhapps.idispatch.domain.assignment.AssignmentState;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Service
class AssignmentOverviewServiceImpl implements AssignmentOverviewService {

    private final HashSet<AssignmentOverviewDTO> dummyData;

    AssignmentOverviewServiceImpl() {
        dummyData = new HashSet<>();
        dummyData.add(createDummy("103", AssignmentState.DISPATCHED, "Pargas", "Skolgatan 1"));
        dummyData.add(createDummy("202", AssignmentState.DISPATCHED, "Pargas", "Lielaxvägen"));
        dummyData.add(createDummy("581", AssignmentState.ON_HOLD, "Pargas", "Mustfinnvägen"));
        dummyData.add(createDummy("700", AssignmentState.NEW, "Pargas", "Malmnäs strandväg"));
    }

    @Override
    public Set<AssignmentOverviewDTO> getAll() {
        return dummyData; // TODO Replace with real data
    }

    @Override
    public Set<AssignmentOverviewDTO> getChangesSince(Instant timestamp) {
        return Collections.emptySet();
    }

    private static AtomicLong nextId = new AtomicLong();

    private static AssignmentOverviewDTO createDummy(String type, AssignmentState state, String municipality, String address) {
        AssignmentOverviewDTO dto = new AssignmentOverviewDTO();
        dto.setAssignmentId(new AssignmentId(nextId.incrementAndGet()));
        dto.setAssignmentTypeCode(type);
        dto.setState(state);
        dto.setMunicipalityName(municipality);
        dto.setAddress(address);
        return dto;
    }
}
