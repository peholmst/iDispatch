package net.pkhapps.idispatch.dispatcher.console.io.identity;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me!
 */
public class User {

    private final String fullName;
    private final String organization;
    private final String accessToken;
    private final Set<String> authorities;

    public User(@NotNull String fullName, @NotNull String organization, @NotNull String accessToken, @NotNull Set<String> authorities) {
        this.fullName = requireNonNull(fullName);
        this.organization = requireNonNull(organization);
        this.accessToken = requireNonNull(accessToken);
        this.authorities = requireNonNull(authorities);
    }

    public @NotNull String getFullName() {
        return fullName;
    }

    public @NotNull String getOrganization() {
        return organization;
    }

    public @NotNull String getAccessToken() {
        return accessToken;
    }

    public boolean hasAuthority(@NotNull String authority) {
        return authorities.contains(authority);
    }
}
