package net.pkhapps.idispatch.identity.server.integrationtest;

import net.pkhapps.idispatch.identity.server.domain.Client;
import net.pkhapps.idispatch.identity.server.domain.ClientRepository;
import net.pkhapps.idispatch.identity.server.domain.GrantType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"test", "devel"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientDetailsServiceIntegrationTest {

    @Autowired
    ClientDetailsService clientDetailsService;

    @Autowired
    ClientRepository clientRepository;

    @Test(expected = ClientRegistrationException.class)
    public void loadClientByClientId_clientDoesNotExist_exceptionThrown() {
        clientDetailsService.loadClientByClientId("nonexistent client");
    }

    @Test
    public void loadClientByClientId_clientExists_clientReturned() {
        var clientName = getClass().getName() + ".TestClient";
        var client = clientRepository.findByClientId(clientName).orElseGet(() -> {
            var c = new Client(clientName);
            c.addGrantType(GrantType.AUTHORIZATION_CODE);
            c.addResourceId("hello world");
            c.addRedirectUri("http://foo.bar/");
            return clientRepository.save(c);
        });

        var result = clientDetailsService.loadClientByClientId(clientName);
        assertThat(result).isEqualTo(client);
    }
}
