package net.pkhapps.idispatch.identity.server.domain;

import net.pkhapps.idispatch.base.domain.DomainObjectId;
import net.pkhapps.idispatch.base.domain.Repository;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Repository interface for {@link Client}.
 */
public interface ClientRepository extends Repository<Client, DomainObjectId> {

    @NonNull
    Optional<Client> findByClientId(@NonNull String clientId);
}
