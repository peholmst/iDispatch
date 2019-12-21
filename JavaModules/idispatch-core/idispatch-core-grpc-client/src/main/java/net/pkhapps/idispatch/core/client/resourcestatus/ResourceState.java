package net.pkhapps.idispatch.core.client.resourcestatus;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * Immutable data transfer object representing a single resource state, such as "on scene", "en route", "available",
 * etc.
 */
public final class ResourceState implements Comparable<ResourceState> {

    private final ResourceStateId resourceStateId;
    private final String nameFin;
    private final String nameSwe;
    private final String color;
    private final boolean dispatcherAssignable;
    private final int order;

    ResourceState(@NotNull net.pkhapps.idispatch.core.grpc.proto.resourcestatus.ResourceState resourceState) {
        Validate.isTrue(resourceState.hasResourceStateId());
        Validate.isTrue(resourceState.hasName());
        Validate.isTrue(resourceState.hasColor());

        this.resourceStateId = new ResourceStateId(resourceState.getResourceStateId());
        if (resourceState.getName().hasFin()) {
            nameFin = resourceState.getName().getFin().getValue();
        } else {
            nameFin = "";
        }
        if (resourceState.getName().hasSwe()) {
            nameSwe = resourceState.getName().getSwe().getValue();
        } else {
            nameSwe = "";
        }
        color = String.format("#%02x%02x%02x",
                (int) resourceState.getColor().getRed(),
                (int) resourceState.getColor().getGreen(),
                (int) resourceState.getColor().getBlue());
        dispatcherAssignable = resourceState.getDispatcherAssignable();
        order = resourceState.getOrder();
    }

    /**
     * Gets the ID of this resource state.
     */
    public @NotNull ResourceStateId getResourceStateId() {
        return resourceStateId;
    }

    /**
     * Gets the Finnish name of this resource state.
     */
    public @NotNull String getNameFin() {
        return nameFin;
    }

    /**
     * Gets the Swedish name of this resource state.
     */
    public @NotNull String getNameSwe() {
        return nameSwe;
    }

    /**
     * Gets the color of this resource state as an HTML/CSS hex string ({@code #rrggbb}).
     */
    public @NotNull String getColor() {
        return color;
    }

    /**
     * Checks whether this resource state can be assigned by the dispatcher or if this state is only assigned by
     * system operations.
     */
    public boolean isDispatcherAssignable() {
        return dispatcherAssignable;
    }

    @Override
    public int compareTo(@NotNull ResourceState o) {
        return order - o.order;
    }
}
