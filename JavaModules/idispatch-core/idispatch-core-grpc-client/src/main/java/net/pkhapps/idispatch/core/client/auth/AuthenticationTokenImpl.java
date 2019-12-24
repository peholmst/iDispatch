package net.pkhapps.idispatch.core.client.auth;

import io.grpc.Metadata;
import net.pkhapps.idispatch.core.grpc.proto.auth.Subject;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation of {@link AuthenticationToken}. For internal use only, cliens should never access this class
 * directly. The only interesting method is {@link #addToHeaders(Metadata)}.
 */
final class AuthenticationTokenImpl implements AuthenticationToken {

    /**
     * The gRPC header key that will contain the binary authentication token.
     */
    static final Metadata.Key<byte[]> AUTH_TOKEN_KEY =
            Metadata.Key.of("x-idispatch-auth-token" + Metadata.BINARY_HEADER_SUFFIX,
                    Metadata.BINARY_BYTE_MARSHALLER);
    private final Subject subject;

    AuthenticationTokenImpl(@NotNull Subject subject) {
        this.subject = requireNonNull(subject);
    }

    @Override
    public @NotNull Instant getTokenValidFrom() {
        return Instant.ofEpochSecond(subject.getTokenValidFrom().getSeconds(),
                subject.getTokenValidFrom().getNanos());
    }

    @Override
    public @NotNull Instant getTokenValidTo() {
        return Instant.ofEpochSecond(subject.getTokenValidTo().getSeconds(),
                subject.getTokenValidTo().getNanos());
    }

    /**
     * Adds the authentication token to the given headers. Used by {@link SubjectAwareCallCredentials}.
     *
     * @param headers the headers to add the token to.
     */
    void addToHeaders(@NotNull Metadata headers) {
        headers.put(AUTH_TOKEN_KEY, subject.getToken().toByteArray());
    }

    /**
     * Returns the authentication token as a byte array.
     *
     * @return a new array containing the token bytes. Changes made to this array will not write through to the
     * original token array.
     */
    @NotNull byte[] getToken() {
        return subject.getToken().toByteArray();
    }
}
