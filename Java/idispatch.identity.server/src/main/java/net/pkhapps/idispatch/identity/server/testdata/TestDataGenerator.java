package net.pkhapps.idispatch.identity.server.testdata;

import net.pkhapps.idispatch.identity.server.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
@Profile("devel")
class TestDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataGenerator.class);
    private final ClientRepository clientRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TestDataGenerator(ClientRepository clientRepository, OrganizationRepository organizationRepository,
                             UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    void generateData() {
        var clientId = "local-dev-dispatcher-console";
        LOGGER.warn("Generating test data");
        if (!clientRepository.existsByClientId(clientId)) {
            var client = new Client(clientId);
            client.setClientSecret(passwordEncoder.encode("notasecret"));
            client.addGrantType(GrantType.PASSWORD);
            clientRepository.save(client);
        }

        var orgName = "Development Organization";
        var organization = organizationRepository.findByName(orgName).orElseGet(() ->
                organizationRepository.save(new Organization(orgName)));

        var username = "joecool";
        userRepository.findByUsername(username).orElseGet(() -> {
            var u = new User(username, UserType.INDIVIDUAL, organization);
            u.setFullName("Joe Cool");
            u.setPassword(passwordEncoder.encode("notasecret"));
            u.addAuthority(new SimpleGrantedAuthority("ROLE_DISPATCHER"));
            return userRepository.save(u);
        });
    }
}
