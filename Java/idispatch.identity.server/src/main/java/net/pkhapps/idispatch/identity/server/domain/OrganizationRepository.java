package net.pkhapps.idispatch.identity.server.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Repository interface for {@link Organization}.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @NonNull
    Optional<Organization> findByName(@NonNull String name);
}
