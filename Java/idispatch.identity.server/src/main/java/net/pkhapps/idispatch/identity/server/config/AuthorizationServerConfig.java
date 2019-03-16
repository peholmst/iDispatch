package net.pkhapps.idispatch.identity.server.config;

import net.pkhapps.idispatch.identity.server.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration for the OAuth 2 server.
 */
@EnableAuthorizationServer
@Configuration
class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final ClientDetailsService clientDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final KeyStore keyStore;

    @Value("${application.jwt.key-pair.alias}")
    private String keyPairAlias;
    @Value("${application.jwt.key-pair.password}")
    private String keyPairPassword;

    AuthorizationServerConfig(ClientDetailsService clientDetailsService,
                              AuthenticationManager authenticationManager,
                              PasswordEncoder passwordEncoder,
                              KeyStore keyStore) {
        this.clientDetailsService = clientDetailsService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.keyStore = keyStore;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        var tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(List.of(new CustomTokenEnhancer(), accessTokenConverter()));

        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(passwordEncoder)
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public JwtTokenStore tokenStore() throws Exception {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() throws Exception {
        var converter = new JwtAccessTokenConverter();
        converter.setKeyPair(jwtKeyPair());
        converter.setAccessTokenConverter(new CustomTokenConverter());
        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() throws Exception {
        var tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        return tokenServices;
    }

    private KeyPair jwtKeyPair() throws Exception {
        var privateKey = keyStore.getKey(keyPairAlias, keyPairPassword.toCharArray());
        if (privateKey == null) {
            throw new IllegalStateException("No private key found with alias: " + keyPairAlias);
        }

        var certificate = keyStore.getCertificate(keyPairAlias);
        if (certificate == null) {
            throw new IllegalStateException("No certificate found with alias: " + keyPairAlias);
        }

        return new KeyPair(certificate.getPublicKey(), (PrivateKey) privateKey);
    }

    /**
     * Token enhancer that adds additional information from the {@link User} object to the JWT.
     */
    static class CustomTokenEnhancer implements TokenEnhancer {

        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
            var principal = authentication.getUserAuthentication().getPrincipal();
            if (principal instanceof User && accessToken instanceof DefaultOAuth2AccessToken) {
                var user = (User) principal;
                var additionalInfo = new HashMap<String, Object>();
                additionalInfo.put("user_id", user.getNonNullId().toLong());
                additionalInfo.put("full_name", user.getFullName());
                additionalInfo.put("user_type", user.getUserType());
                additionalInfo.put("organization", user.getOrganization().getName());
                additionalInfo.put("organization_id", user.getOrganization().getNonNullId().toLong());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            }
            return accessToken;
        }
    }

    /**
     * Token converter that extracts the additional information from the JWT and adds it to the authentication. This
     * is not really needed here on the server but included to make integration testing easier. Clients of the
     * identity server would need to do this in order to get access to the additional user information.
     */
    static class CustomTokenConverter extends DefaultAccessTokenConverter {

        @Override
        public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
            var authentication = super.extractAuthentication(map);
            authentication.setDetails(map);
            return authentication;
        }
    }
}
