package net.pkhapps.idispatch.client.v3;

import net.pkhapps.idispatch.client.v3.base.DomainObjectId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * ID type for {@link Municipality}.
 */
@Immutable
public class MunicipalityId extends DomainObjectId {
    public MunicipalityId(@Nonnull Long id) {
        super(id);
    }
}
