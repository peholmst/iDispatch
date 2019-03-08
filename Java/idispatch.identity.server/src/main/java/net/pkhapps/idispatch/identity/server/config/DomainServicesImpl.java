package net.pkhapps.idispatch.identity.server.config;

import net.pkhapps.idispatch.identity.server.domain.DomainProperties;
import net.pkhapps.idispatch.identity.server.domain.DomainServices;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;

/**
 * Implementation of {@link DomainServices} that looks up the domain services from the Spring application context.
 */
@Service
class DomainServicesImpl extends DomainServices {

    private final Clock clock;
    private final DomainProperties domainProperties;
    private final PasswordEncoder passwordEncoder;

    DomainServicesImpl(Clock clock, DomainProperties domainProperties, PasswordEncoder passwordEncoder) {
        this.clock = clock;
        this.domainProperties = domainProperties;
        this.passwordEncoder = passwordEncoder;
        DomainServices.setInstance(this);
    }

    @Override
    public Clock clock() {
        return clock;
    }

    @Override
    public DomainProperties properties() {
        return domainProperties;
    }

    @Override
    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }
}
