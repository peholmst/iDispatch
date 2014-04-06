package net.pkhsolutions.idispatch.ejb.resources;

import javax.ejb.ApplicationException;
import net.pkhsolutions.idispatch.entity.Resource;

@ApplicationException(rollback = true)
public class NoStatusInformationFoundException extends Exception {

    private final Resource resource;

    public NoStatusInformationFoundException(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }
}
