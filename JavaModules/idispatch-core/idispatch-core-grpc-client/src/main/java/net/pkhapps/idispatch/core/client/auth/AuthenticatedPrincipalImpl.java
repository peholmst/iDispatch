package net.pkhapps.idispatch.core.client.auth;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import net.pkhapps.idispatch.core.grpc.proto.auth.UserPrincipal;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

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
    public @NotNull String getDispatchCenterName() {
        return userPrincipal.getDispatchCenterName();
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
