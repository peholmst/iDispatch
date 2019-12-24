package net.pkhapps.idispatch.core.client.auth;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import io.grpc.BindableService;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;
import net.pkhapps.idispatch.core.client.AbstractGrpcTest;
import net.pkhapps.idispatch.core.grpc.proto.auth.*;
import net.pkhapps.idispatch.core.grpc.proto.organization.OrganizationId;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PrivilegedAction;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * gRPC unit test for {@link AuthenticationService}.
 */
public class AuthenticationServiceTest extends AbstractGrpcTest {

    private static final UUID ORGANIZATION_UUID = UUID.randomUUID();
    private static final Instant VALID_FROM = Instant.now();
    private static final Instant VALID_TO = VALID_FROM.plusSeconds(60 * 60 * 24);

    private byte[] lastInvalidatedToken = null;

    private final AuthenticationServiceGrpc.AuthenticationServiceImplBase serviceImpl =
            new AuthenticationServiceGrpc.AuthenticationServiceImplBase() {

                private final AtomicLong nextConversation = new AtomicLong();
                private final Random random = new Random();
                private final Map<Long, Conversation> conversations = new ConcurrentHashMap<>();

                @Override
                public void initAuthentication(AuthenticationRequest request, StreamObserver<AuthenticationChallenge> responseObserver) {
                    var conversation = new Conversation(request.getUsername(), request.getSeqNo());
                    conversations.put(conversation.getConversationNo(), conversation);
                    responseObserver.onNext(conversation.challenge());
                    responseObserver.onCompleted();
                }

                @Override
                public void completeAuthentication(AuthenticationResponse request, StreamObserver<AuthenticationOutcome> responseObserver) {
                    try {
                        var conversation = conversations.remove(request.getConversationNo());
                        responseObserver.onNext(conversation.response(request.getResponse(), request.getSeqNo()));
                        responseObserver.onCompleted();
                    } catch (Exception ex) {
                        responseObserver.onError(ex);
                    }
                }

                @Override
                public void invalidateToken(Empty request, StreamObserver<Empty> responseObserver) {
                    lastInvalidatedToken = getHeaders().get(AuthenticationTokenImpl.AUTH_TOKEN_KEY);
                    responseObserver.onNext(Empty.getDefaultInstance());
                    responseObserver.onCompleted();
                }

                class Conversation {
                    private final String username;
                    private final AtomicLong seqNo;
                    private final long conversationNo;
                    private final byte[] challenge;
                    private final byte[] token;

                    Conversation(String username, long initialSeqNo) {
                        this.username = username;
                        this.seqNo = new AtomicLong(initialSeqNo);
                        this.conversationNo = nextConversation.getAndIncrement();
                        this.challenge = new byte[8];
                        random.nextBytes(this.challenge);
                        this.token = new byte[16];
                        random.nextBytes(this.token);
                    }

                    long getConversationNo() {
                        return conversationNo;
                    }

                    AuthenticationChallenge challenge() {
                        return AuthenticationChallenge.newBuilder()
                                .setConversationNo(conversationNo)
                                .setSeqNo(seqNo.incrementAndGet())
                                .setChallenge(ByteString.copyFrom(challenge))
                                .build();
                    }

                    AuthenticationOutcome response(ByteString response, long seqNo) throws Exception {
                        if (this.seqNo.incrementAndGet() != seqNo) {
                            throw new IllegalStateException("Invalid sequence number");
                        }
                        var digest = MessageDigest.getInstance("SHA-256");
                        var hashedPassword = digest.digest("password".getBytes(StandardCharsets.UTF_8));
                        digest.update(hashedPassword);
                        digest.update(challenge);
                        var hash = digest.digest();
                        var authenticated = Arrays.equals(hash, response.toByteArray()) && username.equals("joecool");
                        var builder = AuthenticationOutcome.newBuilder()
                                .setConversationNo(conversationNo)
                                .setSeqNo(this.seqNo.incrementAndGet())
                                .setAuthenticated(authenticated);
                        if (authenticated) {
                            builder.setSubject(Subject.newBuilder()
                                    .setUsername("joecool")
                                    .setFullName("Joe Cool")
                                    .addOrganizationAuthorities(OrganizationAuthorities.newBuilder()
                                            .setOrganization(OrganizationId.newBuilder()
                                                    .setUuid(ORGANIZATION_UUID.toString())
                                            )
                                            .addAuthority("FOO")
                                            .addAuthority("BAR")
                                    )
                                    .setToken(ByteString.copyFrom(token))
                                    .setTokenValidFrom(Timestamp.newBuilder()
                                            .setSeconds(VALID_FROM.getEpochSecond())
                                            .setNanos(VALID_FROM.getNano())
                                    )
                                    .setTokenValidTo(Timestamp.newBuilder()
                                            .setSeconds(VALID_TO.getEpochSecond())
                                            .setNanos(VALID_TO.getNano())
                                    )
                            );
                        }
                        return builder.build();
                    }
                }
            };

    private AuthenticationService client;

    @Override
    protected @NotNull Stream<BindableService> setUpServerSide() {
        return Stream.of(serviceImpl);
    }

    @Override
    protected void setUpClientSide(@NotNull Channel channel) throws Exception {
        client = new AuthenticationServiceImpl(channel);
    }

    @Test
    public void authenticate_successful() {
        var process = client.startAuthentication("joecool");
        process.setPassword("password");
        var subject = process.authenticate();
        var principal = subject.getPrincipals(AuthenticatedPrincipal.class).stream().findFirst().orElseThrow();
        assertThat(principal.getName()).isEqualTo("joecool");
        assertThat(principal.getFullName()).isEqualTo("Joe Cool");
        assertThat(principal.hasAuthorityInOrganization("FOO",
                new net.pkhapps.idispatch.core.client.organization.OrganizationId(ORGANIZATION_UUID.toString()))).isTrue();
        assertThat(principal.hasAuthorityInOrganization("BAR",
                new net.pkhapps.idispatch.core.client.organization.OrganizationId(ORGANIZATION_UUID.toString()))).isTrue();

        var token = subject.getPrivateCredentials(AuthenticationTokenImpl.class).stream().findFirst().orElseThrow();
        assertThat(token.getTokenValidFrom()).isEqualTo(VALID_FROM);
        assertThat(token.getTokenValidTo()).isEqualTo(VALID_TO);

        javax.security.auth.Subject.doAs(subject, (PrivilegedAction<Void>) () -> {
            client.logout();
            return null;
        });
        assertThat(lastInvalidatedToken).containsExactly(token.getToken());
    }

    @Test(expected = AuthenticationException.class)
    public void authenticate_invalidUsername_failed() {
        var process = client.startAuthentication("johnsmith");
        process.setPassword("password");
        process.authenticate();
    }

    @Test(expected = AuthenticationException.class)
    public void authenticate_invalidPassword_failed() {
        var process = client.startAuthentication("joecool");
        process.setPassword("incorrect");
        process.authenticate();
    }
}
