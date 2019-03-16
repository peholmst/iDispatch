package net.pkhapps.idispatch.base.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Base interface for repository interfaces.
 */
@NoRepositoryBean
public interface Repository<T extends AggregateRoot<ID>, ID extends DomainObjectId> extends JpaRepository<T, Long>,
        JpaSpecificationExecutor<T> {

    @NonNull
    default Optional<T> findById(@NonNull ID id) {
        return findById(id.toLong());
    }

    default void deleteById(@NonNull ID id) {
        deleteById(id.toLong());
    }
}
