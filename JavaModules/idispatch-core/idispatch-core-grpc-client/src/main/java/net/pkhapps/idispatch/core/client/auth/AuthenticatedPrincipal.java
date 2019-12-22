package net.pkhapps.idispatch.core.client.auth;

import net.pkhapps.idispatch.core.client.organization.OrganizationId;
import org.jetbrains.annotations.NotNull;

import java.security.Principal;
import java.time.Instant;
import java.util.Set;

/**
 * TODO Document me
 */
public interface AuthenticatedPrincipal extends Principal {

    @NotNull String getFullName();

    @NotNull Set<OrganizationId> getOrganizations();

    boolean hasAuthorityInOrganization(@NotNull String authority, @NotNull OrganizationId organizationId);

    @NotNull Instant getTokenValidFrom();

    @NotNull Instant getTokenValidTo();
}
