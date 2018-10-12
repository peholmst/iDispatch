package net.pkhapps.idispatch.cad.domain.model.station;

import net.pkhapps.idispatch.domain.support.DomainObjectId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

/**
 * ID class for {@link Station}.
 */
@Immutable
public class StationId extends DomainObjectId {

    public StationId(@Nonnull Serializable id) {
        super(id);
    }
}
