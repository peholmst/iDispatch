package net.pkhsolutions.idispatch.ejb.tickets;

import javax.ejb.ApplicationException;
import net.pkhsolutions.idispatch.entity.Resource;

@ApplicationException(rollback = true)
public class ResourceNotAvailableException extends Exception {

    private final Resource resource;

    public ResourceNotAvailableException(Resource resource) {
        this.resource = resource;
    }

    public ResourceNotAvailableException(Resource resource, Throwable cause) {
        super(cause);
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }
}
