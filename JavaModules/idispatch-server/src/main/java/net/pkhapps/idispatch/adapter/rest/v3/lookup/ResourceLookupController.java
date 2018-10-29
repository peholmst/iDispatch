package net.pkhapps.idispatch.adapter.rest.v3.lookup;

import net.pkhapps.idispatch.client.v3.Resource;
import net.pkhapps.idispatch.client.v3.ResourceId;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Path("/v3/lookup/resources")
public class ResourceLookupController {

    private final AtomicLong nextId = new AtomicLong(1L);
    private final Map<ResourceId, Resource> dummyData = new HashMap<>();

    ResourceLookupController() {

    }

    @GET
    public List<Resource> allActiveResources() {
        return List.copyOf(dummyData.values());
    }

    @GET
    @Path("/{id}")
    public Resource resourceById(@PathParam("id") ResourceId resourceId) {
        // TODO How to deal with not found error?
        return dummyData.get(resourceId);
    }
}
