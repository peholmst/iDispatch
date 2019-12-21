package net.pkhapps.idispatch.core.client.organization;

import net.pkhapps.idispatch.core.client.ObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * ID identifying an organization that uses iDispatch.
 */
public final class OrganizationId extends ObjectId {

    public OrganizationId(@NotNull String uuid) {
        super(uuid);
    }

    public OrganizationId(@NotNull net.pkhapps.idispatch.core.grpc.proto.organization.OrganizationId organizationId) {
        this(organizationId.getUuid());
    }
}
