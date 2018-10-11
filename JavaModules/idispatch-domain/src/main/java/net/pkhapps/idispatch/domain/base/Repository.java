package net.pkhapps.idispatch.domain.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base interface for {@link AbstractAggregateRoot aggregate} repositories.
 */
@NoRepositoryBean
public interface Repository<T extends AbstractAggregateRoot<ID>, ID extends AbstractAggregateRootId>
        extends JpaRepository<T, ID> {
}
