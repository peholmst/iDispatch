package net.pkhapps.idispatch.gis.domain.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.Clock;

/**
 * TODO Document me!
 */
@Service
public class RoadSegmentImportService extends ImportedGeographicalMaterialImportService<RoadSegment> {

    RoadSegmentImportService(@NotNull RoadSegmentRepository roadSegmentRepository,
                             @NotNull MaterialImportRepository materialImportRepository,
                             @NotNull Clock clock) {
        super(roadSegmentRepository, materialImportRepository, clock, RoadSegment.class);
    }
}
