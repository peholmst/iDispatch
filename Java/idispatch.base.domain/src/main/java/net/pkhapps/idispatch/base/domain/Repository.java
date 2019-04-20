package net.pkhapps.idispatch.base.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @NonNull
    default List<T> findByIds(@NonNull Stream<ID> ids) {
        return findAllById(ids.map(DomainObjectId::toLong).collect(Collectors.toSet()));
    }

    default void deleteById(@NonNull ID id) {
        deleteById(id.toLong());
    }
}
