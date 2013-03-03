package net.pkhsolutions.idispatch.rest;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.pkhsolutions.idispatch.ejb.tickets.DispatchNotificationEJB;
import net.pkhsolutions.idispatch.ejb.tickets.NoSuchReceiverException;
import net.pkhsolutions.idispatch.entity.DispatchNotification;

@Path("/dispatcher")
@RequestScoped
public class DispatchNotificationREST {

    @Inject
    DispatchNotificationEJB bean;

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response fetchUnseenDispatchNotifications(@QueryParam("id") String receiverId, @QueryParam("pw") String receiverPassword) {
        try {
            List<DispatchNotification> result = bean.findUnseenNotifications(receiverId, receiverPassword);
            if (result.isEmpty()) {
                return Response.noContent().build();
            } else {
                return Response.ok(new DispatchNotificationsDTO(result)).build();
            }
        } catch (NoSuchReceiverException ex) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
