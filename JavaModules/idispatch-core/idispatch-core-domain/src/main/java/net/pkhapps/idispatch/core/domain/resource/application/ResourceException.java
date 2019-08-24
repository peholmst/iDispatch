package net.pkhapps.idispatch.core.domain.resource.application;

import net.pkhapps.idispatch.core.domain.resource.model.ResourceId;

import static java.util.Objects.requireNonNull;

/**
 * Base class for exceptions that concern a particular resource.
 */
public abstract class ResourceException extends RuntimeException {

    private final ResourceId resource;

    public ResourceException(String message, ResourceId resource) {
        super(message);
        this.resource = requireNonNull(resource);
    }

    /**
     * Returns the ID of the resource that the exception concerns.
     */
    public ResourceId resource() {
        return resource;
    }
}
