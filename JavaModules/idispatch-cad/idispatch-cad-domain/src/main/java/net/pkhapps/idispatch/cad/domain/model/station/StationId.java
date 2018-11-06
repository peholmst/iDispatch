package net.pkhapps.idispatch.cad.domain.model.station;

import net.pkhapps.idispatch.domain.support.DomainObjectId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * ID class for {@link Station}.
 */
@Immutable
public class StationId extends DomainObjectId {

    public StationId(@Nonnull String id) {
        super(id);
    }
}
