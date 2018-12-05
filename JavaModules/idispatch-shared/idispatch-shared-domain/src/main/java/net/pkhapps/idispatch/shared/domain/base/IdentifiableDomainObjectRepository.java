package net.pkhapps.idispatch.shared.domain.base;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;

/**
 * Base interface for repositories of {@link IdentifiableDomainObject}s that are not necessarily aggregates (such as
 * standard types and other master/lookup data) or domain events.
 *
 * @param <ID>       the real ID type to store in the database.
 * @param <DomainId> the {@link DomainObjectId} type to use in the API.
 * @param <T>        the domain object type.
 * @see BaseRepository
 */
@NoRepositoryBean
public interface IdentifiableDomainObjectRepository<ID extends Serializable, DomainId extends DomainObjectId<ID>,
        T extends IdentifiableDomainObject<DomainId>> extends JpaRepository<T, ID> {

    @NotNull
    default Optional<T> findById(@NotNull DomainId id) {
        return findById(id.unwrap());
    }

    default boolean existsById(@NotNull DomainId id) {
        return existsById(id.unwrap());
    }
}
