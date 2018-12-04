package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.gis.domain.model.identity.RoadSegmentId;
import net.pkhapps.idispatch.shared.domain.base.BaseRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Repository interface for {@link RoadSegment}.
 */
public interface RoadSegmentRepository extends BaseRepository<Long, RoadSegmentId, RoadSegment> {

    @NotNull
    List<RoadSegment> findByGid(long gid);

    boolean existsByGid(long gid);
}
