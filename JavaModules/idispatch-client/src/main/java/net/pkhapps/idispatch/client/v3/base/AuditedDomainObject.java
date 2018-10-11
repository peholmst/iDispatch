package net.pkhapps.idispatch.client.v3.base;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.time.Instant;

/**
 * Interface implemented by domain objects that are audited.
 */
public interface AuditedDomainObject extends Serializable {

    @Nonnull
    Principal createdBy();

    @Nonnull
    Instant createdOn();

    @Nonnull
    Principal lastModifiedBy();

    @Nonnull
    Instant lastModifiedOn();
}
