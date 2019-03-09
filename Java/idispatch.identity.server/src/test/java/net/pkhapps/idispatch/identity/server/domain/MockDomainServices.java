package net.pkhapps.idispatch.identity.server.domain;

import org.mockito.Mockito;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

/**
 * Mock implementation of {@link DomainServices} designed for testing only. Defaults:
 * <ul>
 * <li>The {@link Clock#systemUTC() system UTC} {@link Clock}</li>
 * <li>A {@link Mockito#mock(Class) mock} of {@link DomainProperties}</li>
 * <li>A {@link PasswordEncoderFactories#createDelegatingPasswordEncoder() delegating} {@link PasswordEncoder}.</li>
 * </ul>
 * Developers can replace the defaults by invoking their respective setter methods.
 */
public class MockDomainServices extends DomainServices {

    private Clock clock = Clock.systemUTC();
    private DomainProperties properties = Mockito.mock(DomainProperties.class);
    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public Clock clock() {
        return clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    @Override
    public DomainProperties properties() {
        return properties;
    }

    public void setProperties(DomainProperties properties) {
        this.properties = properties;
    }

    @Override
    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
