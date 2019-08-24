package net.pkhapps.idispatch.core.domain.resource.application;

import net.pkhapps.idispatch.core.domain.resource.model.ResourceId;

/**
 * TODO Document me
 */
public class ResourceNotKnownException extends ResourceException {

    public ResourceNotKnownException(String message, ResourceId resource) {
        super(message, resource);
    }
}
