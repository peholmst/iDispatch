package net.pkhapps.idispatch.core.client.auth;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.Subject;
import java.security.AccessController;
import java.util.Optional;
import java.util.concurrent.Executor;

/**
 * An implementation of {@link CallCredentials} that fetches an {@link AuthenticationToken} from the {@link Subject}
 * of the {@linkplain AccessController#getContext() current security context} and adds it to the headers of each
 * request. If the current security context does not contain a subject or the subject does not contain a token,
 * nothing happens. This class is a thread-safe {@linkplain #getInstance() singleton}.
 */
public final class SubjectAwareCallCredentials extends CallCredentials {

    private static final CallCredentials INSTANCE = new SubjectAwareCallCredentials();

    private SubjectAwareCallCredentials() {
    }

    /**
     * Returns the singleton instance of this object.
     *
     * @return the singleton.
     */
    public static @NotNull CallCredentials getInstance() {
        return INSTANCE;
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor appExecutor, MetadataApplier applier) {
        var headers = new Metadata();
        Optional.ofNullable(Subject.getSubject(AccessController.getContext()))
                .flatMap(subject -> subject.getPrivateCredentials(AuthenticationTokenImpl.class).stream().findAny())
                .ifPresent(token -> token.addToHeaders(headers));
        applier.apply(headers);
    }

    @Override
    public void thisUsesUnstableApi() {
        // NOP
    }
}
