package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.gis.domain.model.identity.AddressPointId;
import net.pkhapps.idispatch.shared.domain.base.BaseRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Repository interface for {@link AddressPoint}.
 */
public interface AddressPointRepository extends BaseRepository<Long, AddressPointId, AddressPoint> {

    @NotNull List<AddressPoint> findByGid(long gid);

    boolean existsByGid(long gid);
}
