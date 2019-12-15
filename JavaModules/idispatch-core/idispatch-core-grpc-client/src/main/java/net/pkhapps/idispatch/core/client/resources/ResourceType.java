package net.pkhapps.idispatch.core.client.resources;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * A resource type is used to describe a {@link Resource}. A real-world dispatch system such as ERICA would not use
 * types but instead assign different qualities or capabilities to a certain resource. However, iDispatch is not
 * a real-world system and so using a single type to describe a resource is more than enough. This type could be e.g.
 * "pumper", "water tender", "command", "ladder", "basic life support ambulance","advanced life support ambulance", etc.
 */
public class ResourceType {

    private final ResourceTypeId resourceTypeId;
    private final String nameFin;
    private final String nameSwe;
    private final boolean active;
    private final String comments;

    ResourceType(@NotNull net.pkhapps.idispatch.core.grpc.proto.resource.ResourceType resourceType) {
        Validate.isTrue(resourceType.hasResourceTypeId());
        Validate.isTrue(resourceType.hasName());

        resourceTypeId = new ResourceTypeId(resourceType.getResourceTypeId());
        nameFin = resourceType.getName().hasFin() ? resourceType.getName().getFin().getValue() : "";
        nameSwe = resourceType.getName().hasSwe() ? resourceType.getName().getSwe().getValue() : "";
        active = resourceType.getActive();
        comments = resourceType.hasComments() ? resourceType.getComments().getValue() : "";
    }

    /**
     * Gets the ID of this resource type.
     */
    public @NotNull ResourceTypeId getResourceTypeId() {
        return resourceTypeId;
    }

    /**
     * Gets the Finnish name of this resource type.
     */
    public @NotNull String getNameFin() {
        return nameFin;
    }

    /**
     * Gets the Swedish name of this resource type.
     */
    public @NotNull String getNameSwe() {
        return nameSwe;
    }

    /**
     * Checks whether this resource type is active (can be used in filters and assigned to new resources) or not.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Gets any comments regarding this resource type.
     */
    public @NotNull String getComments() {
        return comments;
    }
}
