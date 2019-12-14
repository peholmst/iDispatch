package net.pkhapps.idispatch.core.client.auth;

import com.google.protobuf.ByteString;
import io.grpc.Channel;
import net.pkhapps.idispatch.core.grpc.proto.auth.Auth;
import net.pkhapps.idispatch.core.grpc.proto.auth.AuthenticationServiceGrpc;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me
 */
class AuthenticationServiceImpl implements AuthenticationService {

    private static final String HASH_ALGORITHM = "SHA-256";
    private final AuthenticationServiceGrpc.AuthenticationServiceBlockingStub server;
    private final SecureRandom secureRandom;

    public AuthenticationServiceImpl(@NotNull Channel channel) throws NoSuchAlgorithmException {
        server = AuthenticationServiceGrpc.newBlockingStub(channel);
        secureRandom = SecureRandom.getInstanceStrong();
    }

    @Override
    public @NotNull AuthenticationProcess startAuthentication(@NotNull String username) {
        return new AuthenticationProcessImpl(username);
    }

    private class AuthenticationProcessImpl implements AuthenticationProcess {

        private final AtomicLong nextSeqNo;
        private final Auth.AuthenticationChallenge challenge;
        private String password;

        AuthenticationProcessImpl(@NotNull String username) {
            nextSeqNo = new AtomicLong(secureRandom.nextLong());
            challenge = server.initAuthentication(Auth.AuthenticationRequest.newBuilder()
                    .setSeqNo(nextSeqNo.getAndIncrement())
                    .setUsername(username)
                    .build());
            verifySequenceNumber(challenge.getSeqNo());
        }

        @Override
        public @NotNull AuthenticationProcess setPassword(@NotNull String password) {
            this.password = requireNonNull(password);
            return this;
        }

        private byte[] computeResponseHash() {
            try {
                var digest = MessageDigest.getInstance(HASH_ALGORITHM);

                // First compute a hash of the password since the server will only have the hash to compare with
                var hashedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8)); // TODO Consider using BCrypt here instead of SHA-256

                // Then compute the response hash based on the challenge received by the server
                digest.update(hashedPassword);
                digest.update(challenge.getChallenge().toByteArray());
                return digest.digest();
            } catch (NoSuchAlgorithmException ex) {
                throw new AuthenticationException("Could not compute response hash", ex);
            }
        }

        @Override
        public @NotNull AuthenticatedPrincipal authenticate() throws AuthenticationException {
            var response = server.completeAuthentication(Auth.AuthenticationResponse.newBuilder()
                    .setSeqNo(nextSeqNo.getAndIncrement())
                    .setResponse(ByteString.copyFrom(computeResponseHash()))
                    .build());
            verifySequenceNumber(response.getSeqNo());
            if (response.getAuthenticated()) {
                return new AuthenticatedPrincipalImpl(response.getPrincipal());
            } else {
                throw new AuthenticationException("Authentication failed");
            }
        }

        private void verifySequenceNumber(long actualSequenceNumber) {
            if (actualSequenceNumber != nextSeqNo.getAndIncrement()) {
                throw new AuthenticationException("Received invalid sequence number");
            }
        }
    }
}
