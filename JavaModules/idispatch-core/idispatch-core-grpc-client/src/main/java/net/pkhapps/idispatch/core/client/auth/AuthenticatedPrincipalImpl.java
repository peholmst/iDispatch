package net.pkhapps.idispatch.core.client.auth;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import net.pkhapps.idispatch.core.client.organization.OrganizationId;
import net.pkhapps.idispatch.core.grpc.proto.auth.OrganizationAuthorities;
import net.pkhapps.idispatch.core.grpc.proto.auth.UserPrincipal;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * TODO Implement me
 */
class AuthenticatedPrincipalImpl extends CallCredentials implements AuthenticatedPrincipal {

    private final UserPrincipal userPrincipal;

    AuthenticatedPrincipalImpl(@NotNull UserPrincipal userPrincipal) {
        this.userPrincipal = requireNonNull(userPrincipal);
    }

    @Override
    public String getName() {
        return userPrincipal.getUsername();
    }

    @Override
    public @NotNull String getFullName() {
        return userPrincipal.getFullName();
    }

    @Override
    public @NotNull Set<OrganizationId> getOrganizations() {
        return userPrincipal.getOrganizationAuthoritiesList().stream()
                .map(OrganizationAuthorities::getOrganization)
                .map(OrganizationId::new)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean hasAuthorityInOrganization(@NotNull String authority, @NotNull OrganizationId organizationId) {
        return userPrincipal.getOrganizationAuthoritiesList().stream()
                .filter(ua -> new OrganizationId(ua.getOrganization()).equals(organizationId))
                .flatMap(ua -> ua.getAuthorityList().stream())
                .anyMatch(s -> s.equals(authority));
    }

    @Override
    public @NotNull Instant getTokenValidFrom() {
        return Instant.ofEpochSecond(userPrincipal.getTokenValidFrom().getSeconds(),
                userPrincipal.getTokenValidFrom().getNanos());
    }

    @Override
    public @NotNull Instant getTokenValidTo() {
        return Instant.ofEpochSecond(userPrincipal.getTokenValidTo().getSeconds(),
                userPrincipal.getTokenValidTo().getNanos());
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor appExecutor, MetadataApplier applier) {
        var headers = new Metadata();
        var key = Metadata.Key.of("x-custom-auth-token" + Metadata.BINARY_HEADER_SUFFIX,
                Metadata.BINARY_BYTE_MARSHALLER);
        headers.put(key, userPrincipal.getToken().toByteArray());
        applier.apply(headers);
    }

    @Override
    public void thisUsesUnstableApi() {
        // NOP
    }
}
