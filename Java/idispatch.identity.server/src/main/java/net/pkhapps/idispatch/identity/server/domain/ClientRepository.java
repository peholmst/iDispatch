package net.pkhapps.idispatch.identity.server.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Repository interface for {@link Client}.
 */
public interface ClientRepository extends JpaRepository<Client, Long> {

    @NonNull
    Optional<Client> findByClientId(@NonNull String clientId);
}
