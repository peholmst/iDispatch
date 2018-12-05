package net.pkhapps.idispatch.gis.domain.model.identity;

import net.pkhapps.idispatch.shared.domain.base.DomainObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * ID type for {@link net.pkhapps.idispatch.gis.domain.model.Municipality}.
 */
public class MunicipalityId extends DomainObjectId<Integer> {

    public MunicipalityId(@NotNull Integer id) {
        super(id);
    }
}
