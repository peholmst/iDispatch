package net.pkhsolutions.idispatch.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
    public Response fetchUnseenDispatchNotifications(@QueryParam("id") String receiverId, @QueryParam("sc") String securityCode) {
        try {
            List<DispatchNotification> result = bean.findUnseenNotifications(receiverId, securityCode);
            if (result.isEmpty()) {
                return Response.noContent().build();
            } else {
                return Response.ok(new DispatchNotificationsDTO(result)).build();
            }
        } catch (NoSuchReceiverException ex) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void markAsSeen(@FormParam("id") String receiverId, @FormParam("sc") String securityCode, @FormParam("ids") String seenNotifications) {
        String[] seenIds = seenNotifications.split(":");
        Set<Long> ids = new HashSet<>();
        for (String idAsString : seenIds) {
            try {
                ids.add(Long.parseLong(idAsString));
            } catch (NumberFormatException e) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        }
        try {
            bean.markNotificationsAsSeen(receiverId, securityCode, ids);
        } catch (NoSuchReceiverException ex) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
