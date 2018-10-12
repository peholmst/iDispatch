package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface Repository<ID extends DomainObjectId, T extends AggregateRoot<ID>> {

    @Nonnull
    ID nextFreeId();

    @Nonnull
    Optional<T> get(@Nonnull ID id);

    boolean contains(@Nonnull ID id);

    void add(@Nonnull T aggregate);

    long size();
}
