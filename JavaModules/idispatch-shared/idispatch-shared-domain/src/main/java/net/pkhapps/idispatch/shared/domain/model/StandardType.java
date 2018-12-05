package net.pkhapps.idispatch.shared.domain.model;

import net.pkhapps.idispatch.shared.domain.base.DomainObjectId;
import net.pkhapps.idispatch.shared.domain.base.IdentifiableDomainObject;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * TODO Document me!
 *
 * @param <ID>
 */
public interface StandardType<ID extends DomainObjectId<?>> extends IdentifiableDomainObject<ID> {

    @NotNull
    MultilingualString name();

    @NotNull
    default Optional<MultilingualString> description() {
        return Optional.empty();
    }
}
