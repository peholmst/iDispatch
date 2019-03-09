package net.pkhapps.idispatch.identity.server.application;

import net.pkhapps.idispatch.identity.server.domain.ClientRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ClientDetailsService} that delegates to a {@link ClientRepository}.
 */
@Service
@Primary
class ClientDetailsServiceImpl implements ClientDetailsService {

    private final ClientRepository clientRepository;

    ClientDetailsServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new ClientRegistrationException("ClientId " + clientId + " does not exist"));
    }
}
