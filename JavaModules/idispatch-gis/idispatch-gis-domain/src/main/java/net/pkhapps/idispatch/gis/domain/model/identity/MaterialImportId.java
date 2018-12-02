package net.pkhapps.idispatch.gis.domain.model.identity;

import net.pkhapps.idispatch.shared.domain.base.DomainObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * ID type for {@link net.pkhapps.idispatch.gis.domain.model.MaterialImport}.
 */
public class MaterialImportId extends DomainObjectId<Long> {

    public MaterialImportId(@NotNull Long id) {
        super(id);
    }
}
