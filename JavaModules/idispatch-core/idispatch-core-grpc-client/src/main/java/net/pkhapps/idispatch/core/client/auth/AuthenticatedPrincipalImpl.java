package net.pkhapps.idispatch.core.client.auth;

import net.pkhapps.idispatch.core.client.organization.OrganizationId;
import net.pkhapps.idispatch.core.grpc.proto.auth.OrganizationAuthorities;
import net.pkhapps.idispatch.core.grpc.proto.auth.Subject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation of {@link AuthenticatedPrincipal}. For internal use only, clients should never access this
 * class directly.
 */
final class AuthenticatedPrincipalImpl implements AuthenticatedPrincipal {

    private final Subject subject;

    AuthenticatedPrincipalImpl(@NotNull Subject subject) {
        this.subject = requireNonNull(subject);
    }

    @Override
    public String getName() {
        return subject.getUsername();
    }

    @Override
    public @NotNull String getFullName() {
        return subject.getFullName();
    }

    @Override
    public @NotNull Set<OrganizationId> getOrganizations() {
        return subject.getOrganizationAuthoritiesList().stream()
                .map(OrganizationAuthorities::getOrganization)
                .map(OrganizationId::new)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean hasAuthorityInOrganization(@NotNull String authority, @NotNull OrganizationId organizationId) {
        return subject.getOrganizationAuthoritiesList().stream()
                .filter(ua -> new OrganizationId(ua.getOrganization()).equals(organizationId))
                .flatMap(ua -> ua.getAuthorityList().stream())
                .anyMatch(s -> s.equals(authority));
    }
}
