package net.pkhapps.idispatch.identity.server.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link Organization}.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
