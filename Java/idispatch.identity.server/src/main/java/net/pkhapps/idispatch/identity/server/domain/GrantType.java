package net.pkhapps.idispatch.identity.server.domain;

import org.springframework.lang.NonNull;

/**
 * Enumeration of supported grant types.
 */
public enum GrantType {
    AUTHORIZATION_CODE,
    IMPLICIT,
    PASSWORD,
    CLIENT_CREDENTIALS,
    REFRESH_TOKEN;

    @NonNull
    public String getGrantTypeString() {
        return name().toLowerCase();
    }
}
