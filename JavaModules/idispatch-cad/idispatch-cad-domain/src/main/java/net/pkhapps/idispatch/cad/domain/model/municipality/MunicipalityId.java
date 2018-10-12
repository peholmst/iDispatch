package net.pkhapps.idispatch.cad.domain.model.municipality;

import net.pkhapps.idispatch.domain.support.DomainObjectId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

/**
 * ID class for {@link Municipality}.
 */
@Immutable
public class MunicipalityId extends DomainObjectId {

    public MunicipalityId(@Nonnull Serializable id) {
        super(id);
    }
}
