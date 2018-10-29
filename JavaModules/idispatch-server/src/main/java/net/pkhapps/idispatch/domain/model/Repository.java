package net.pkhapps.idispatch.domain.model;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface Repository<ID extends DomainObjectId, AR extends AggregateRoot<ID>> {

    @Nonnull
    Optional<AR> findById(@Nonnull ID id);


}
