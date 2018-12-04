package net.pkhapps.idispatch.gis.domain.model.identity;

import net.pkhapps.idispatch.shared.domain.base.DomainObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * ID type for {@link net.pkhapps.idispatch.gis.domain.model.AddressPoint}.
 */
public class AddressPointId extends DomainObjectId<Long> {

    public AddressPointId(@NotNull Long id) {
        super(id);
    }
}
