package net.pkhapps.idispatch.identity.server.config;

import lombok.Setter;
import net.pkhapps.idispatch.identity.server.domain.DomainProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Implementation of {@link DomainProperties} that uses {@link ConfigurationProperties} to read application
 * properties.
 */
@Component
@Setter
@ConfigurationProperties(prefix = "idispatch.identity-server.domain")
class DomainPropertiesImpl implements DomainProperties {

    private Duration userAccountLockDuration = Duration.ofMinutes(15);

    @Override
    public Duration getUserAccountLockDuration() {
        return userAccountLockDuration;
    }
}
