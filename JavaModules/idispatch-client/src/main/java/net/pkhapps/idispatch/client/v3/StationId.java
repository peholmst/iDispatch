package net.pkhapps.idispatch.client.v3;

import net.pkhapps.idispatch.client.v3.base.DomainObjectId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * ID type for {@link Station}.
 */
@Immutable
public class StationId extends DomainObjectId {
    public StationId(@Nonnull Long id) {
        super(id);
    }
}
