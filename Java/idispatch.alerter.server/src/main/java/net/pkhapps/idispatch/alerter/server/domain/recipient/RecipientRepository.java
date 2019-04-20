package net.pkhapps.idispatch.alerter.server.domain.recipient;

import net.pkhapps.idispatch.base.domain.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

/**
 * Repository interface for {@link Recipient}.
 */
public interface RecipientRepository extends Repository<Recipient<?>, RecipientId> {

    @Query("select distinct r from Recipient r where :resource member of r.resources and r.active = true")
    Stream<Recipient<?>> findByResource(ResourceCode resource);
}
