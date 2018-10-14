package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Interface defining a repository of aggregates. This repository assumes a collection-like behavior in that there is
 * no explicit save operation. Any changes made to aggregates that have already been added to the repository should
 * be automatically persisted. Implementations may limit this behavior to some specific context, such as an active
 * transaction.
 *
 * @param <ID> the ID type of the aggregate.
 * @param <T>  the aggregate type.
 */
public interface Repository<ID extends DomainObjectId, T extends AggregateRoot<ID>> {

    /**
     * Returns the next free ID. This method will never return two IDs that are equal to each other. Clients should
     * use this method when creating new aggregate roots.
     */
    @Nonnull
    ID nextFreeId();

    /**
     * Retrieves the aggregate with the given ID.
     *
     * @param id the ID of the aggregate.
     */
    @Nonnull
    Optional<T> get(@Nonnull ID id);

    /**
     * Checks if the repository contains an aggregate with the given ID.
     *
     * @param id the ID of the aggregate.
     */
    boolean contains(@Nonnull ID id);

    /**
     * Adds the given aggregate to the repository. The aggregate's ID must not exist in the repository.
     *
     * @param aggregate the aggregate to add.
     */
    void add(@Nonnull T aggregate);

    /**
     * Returns the total number of aggregates in the repository.
     */
    long size();
}
