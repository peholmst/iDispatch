package net.pkhapps.idispatch.identity.server.domain;

import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.*;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.identity.server.util.Strings.requireMaxLength;

/**
 * Aggregate root representing a client of the system, i.e. an application that uses the identity server for
 * authentication and authorization.
 */
@Entity
@Table(name = "client", schema = "idispatch_identity")
public class Client extends AggregateRoot<Client> implements ClientDetails {

    private static final int STRING_MAX_LENGTH = 255;
    private static final String DEFAULT_SCOPE = "default";

    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "client_resource_id", schema = "idispatch_identity", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "resource_id", nullable = false)
    private Set<String> resourceIds = new HashSet<>();
    @Column(name = "client_secret")
    private String clientSecret;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "client_grant_type", schema = "idispatch_identity", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "grant_type", nullable = false)
    private Set<String> grantTypes = new HashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "client_redirect_uri", schema = "idispatch_identity", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "redirect_uri", nullable = false)
    private Set<String> redirectUris = new HashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "client_authority", schema = "idispatch_identity", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "authority", nullable = false)
    private Set<String> authorities = new HashSet<>();
    @Column(name = "access_token_validity", nullable = false)
    private int accessTokenValiditySeconds = (int) Duration.ofHours(1).toSeconds();
    @Column(name = "refresh_token_validity", nullable = false)
    private int refreshTokenValiditySeconds = (int) Duration.ofHours(12).toSeconds();

    Client() {
    }

    public Client(@NonNull String clientId) {
        this.clientId = requireNonNull(requireMaxLength(clientId, STRING_MAX_LENGTH));
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        return Set.copyOf(resourceIds);
    }

    public void addResourceId(@NonNull String resourceId) {
        this.resourceIds.add(requireNonNull(requireMaxLength(resourceId, STRING_MAX_LENGTH)));
    }

    public void removeResourceId(@NonNull String resourceId) {
        this.resourceIds.remove(requireNonNull(resourceId));
    }

    @Override
    public boolean isSecretRequired() {
        return clientSecret != null;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    // For some reason, isScoped() returning false and using an empty scope will always throw an InvalidScopeException.
    // Therefore, we use a default scope that does not really mean anything at all.

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        return Set.of(DEFAULT_SCOPE);
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return Set.copyOf(grantTypes);
    }

    public void addGrantType(@NonNull String grantType) {
        this.grantTypes.add(requireNonNull(requireMaxLength(grantType, STRING_MAX_LENGTH)));
    }

    public void addGrantType(@NonNull GrantType grantType) {
        addGrantType(requireNonNull(grantType).getGrantTypeString());
    }

    public void removeGrantType(@NonNull String grantType) {
        this.grantTypes.remove(requireNonNull(grantType));
    }

    public void removeGrantType(@NonNull GrantType grantType) {
        removeGrantType(requireNonNull(grantType).getGrantTypeString());
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return Set.copyOf(redirectUris);
    }

    public void addRedirectUri(@NonNull String redirectUri) {
        this.redirectUris.add(requireNonNull(requireMaxLength(redirectUri, STRING_MAX_LENGTH)));
    }

    public void removeRedirectUri(@NonNull String redirectUri) {
        this.redirectUris.remove(requireNonNull(redirectUri));
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    public void addAuthority(@NonNull GrantedAuthority authority) {
        requireNonNull(authority);
        requireMaxLength(authority.getAuthority(), STRING_MAX_LENGTH);
        authorities.add(authority.getAuthority());
    }

    public void removeAuthority(@NonNull GrantedAuthority authority) {
        authorities.remove(requireNonNull(authority).getAuthority());
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String s) {
        return true;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return Collections.emptyMap();
    }

    // TODO Add domain events when anything is changed
}
