package net.pkhapps.idispatch.core.client.auth;

import net.pkhapps.idispatch.core.client.organization.OrganizationId;
import org.jetbrains.annotations.NotNull;

import java.security.Principal;
import java.util.Set;

/**
 * Interface declaring the principal that represents a user of iDispatch Core.
 *
 * @see AuthenticationService#startAuthentication(String)
 * @see AuthenticationProcess#authenticate()
 * @see AuthenticationToken
 */
public interface AuthenticatedPrincipal extends Principal {

    /**
     * Returns the full name of this principal (as opposed to {@link #getName()} that only returns the username).
     *
     * @return the full name of this principal.
     */
    @NotNull String getFullName();

    /**
     * Returns the IDs of the organizations that this principal is allowed to access.
     *
     * @return an unmodifiable set of organization IDs.
     */
    @NotNull Set<OrganizationId> getOrganizations();

    /**
     * Checks if this principal has the given authority in the given organization.
     *
     * @param authority      the authority to check.
     * @param organizationId the organization in which to check.
     * @return true if the principal has the authority, false if not.
     */
    boolean hasAuthorityInOrganization(@NotNull String authority, @NotNull OrganizationId organizationId);
}
