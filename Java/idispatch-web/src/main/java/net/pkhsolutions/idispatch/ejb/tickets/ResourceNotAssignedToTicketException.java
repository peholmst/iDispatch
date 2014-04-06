package net.pkhsolutions.idispatch.ejb.tickets;

import javax.ejb.ApplicationException;
import net.pkhsolutions.idispatch.entity.Resource;

@ApplicationException(rollback = true)
public class ResourceNotAssignedToTicketException extends Exception {

    private final Resource resource;

    public ResourceNotAssignedToTicketException(Resource resource) {
        this.resource = resource;
    }

    public ResourceNotAssignedToTicketException(Resource resource, Throwable cause) {
        super(cause);
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }
}
