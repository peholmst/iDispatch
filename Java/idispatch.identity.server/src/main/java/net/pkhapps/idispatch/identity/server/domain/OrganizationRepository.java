package net.pkhapps.idispatch.identity.server.domain;

import net.pkhapps.idispatch.base.domain.OrganizationId;
import net.pkhapps.idispatch.base.domain.Repository;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Repository interface for {@link Organization}.
 */
public interface OrganizationRepository extends Repository<Organization, OrganizationId> {

    @NonNull
    Optional<Organization> findByName(@NonNull String name);

    boolean existsByName(@NonNull String name);
}
