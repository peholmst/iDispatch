package net.pkhapps.idispatch.application.overview;

import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Set;

public interface AssignmentOverviewService {

    /**
     * Returns an overview of all open assignments.
     */
    @NonNull
    Set<AssignmentOverviewDTO> getAll();

    /**
     * Returns the assignments that have changed at or after the given timestamp.
     */
    @NonNull
    Set<AssignmentOverviewDTO> getChangesSince(@NonNull Instant timestamp);
}
