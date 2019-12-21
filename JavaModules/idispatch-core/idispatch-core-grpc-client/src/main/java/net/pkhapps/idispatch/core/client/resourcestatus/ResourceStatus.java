package net.pkhapps.idispatch.core.client.resourcestatus;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

public class ResourceStatus {

    // TODO Implement me

    ResourceStatus(@NotNull net.pkhapps.idispatch.core.grpc.proto.resourcestatus.ResourceStatus resourceStatus) {
        Validate.isTrue(resourceStatus.hasResourceId());
        Validate.isTrue(resourceStatus.hasState());
    }
}
