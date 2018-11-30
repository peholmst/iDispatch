package net.pkhapps.idispatch.shared.domain.base;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;

/**
 * Base interface for repositories.
 *
 * @param <ID>       the real ID type to store in the database.
 * @param <DomainId> the {@link DomainObjectId} type to use in the API.
 * @param <T>        the aggregate root type.
 */
@NoRepositoryBean
public interface BaseRepository<ID extends Serializable, DomainId extends DomainObjectId<ID>,
        T extends BaseAggregateRoot<ID, DomainId>> extends JpaRepository<T, ID> {

    @NotNull
    default Optional<T> findById(@NotNull DomainId id) {
        return findById(id.unwrap());
    }
}
