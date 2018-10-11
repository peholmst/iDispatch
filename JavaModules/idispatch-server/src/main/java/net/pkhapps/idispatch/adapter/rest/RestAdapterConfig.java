package net.pkhapps.idispatch.adapter.rest;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Jersey resource configuration for the iDispatch server REST adapter.
 */
@ApplicationPath("/") // The servlet mapping specifies the root path of the REST adapter.
public class RestAdapterConfig extends ResourceConfig {

    public RestAdapterConfig() {
        // This will pick up all the providers and the REST resources.
        packages(getClass().getPackageName());
    }
}
