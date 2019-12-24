package net.pkhapps.idispatch.core.client.auth;

import org.jetbrains.annotations.NotNull;

import java.security.AccessControlContext;

/**
 * Service for authenticating users with the iDispatch Core server.
 */
public interface AuthenticationService {

    /**
     * Starts a new authentication process for the given user. The process will timeout eventually if the
     * authentication is not completed. The timeout depends on the implementation.
     *
     * @param username the username of the user that is going to be authenticated.
     * @return an instance of {@link AuthenticationProcess} for providing the necessary credentials and
     * {@linkplain AuthenticationProcess#authenticate() completing} the authentication process.
     */
    @NotNull AuthenticationProcess startAuthentication(@NotNull String username);

    /**
     * Invalidates the token for the {@linkplain javax.security.auth.Subject#getSubject(AccessControlContext) current}
     * subject on the server. After calling this, the {@link AuthenticationToken} of the current user can no longer
     * be used for authentication.
     */
    void logout();
}
