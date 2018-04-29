package net.pkhapps.idispatch.application.overview;

import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Set;

public interface ResourceOverviewService {

    /**
     * Returns an overview of all active resources.
     */
    @NonNull
    Set<ResourceOverviewDTO> getAll();

    /**
     * Returns the resources that have changed at or after the given timestamp.
     */
    @NonNull
    Set<ResourceOverviewDTO> getChangesSince(@NonNull Instant timestamp);
}
