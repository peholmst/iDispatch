package net.pkhapps.idispatch.shared.domain.base;

import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Base interface for repositories of aggregate roots.
 *
 * @param <ID>       the real ID type to store in the database.
 * @param <DomainId> the {@link DomainObjectId} type to use in the API.
 * @param <T>        the aggregate root type.
 */
@NoRepositoryBean
public interface BaseRepository<ID extends Serializable, DomainId extends DomainObjectId<ID>,
        T extends BaseAggregateRoot<ID, DomainId>> extends IdentifiableDomainObjectRepository<ID, DomainId, T> {
}
