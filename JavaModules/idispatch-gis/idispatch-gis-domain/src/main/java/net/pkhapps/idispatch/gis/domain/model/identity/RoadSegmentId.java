package net.pkhapps.idispatch.gis.domain.model.identity;

import net.pkhapps.idispatch.shared.domain.base.DomainObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * ID type for {@link net.pkhapps.idispatch.gis.domain.model.RoadSegment}.
 */
public class RoadSegmentId extends DomainObjectId<Long> {

    public RoadSegmentId(@NotNull Long id) {
        super(id);
    }
}
