package net.pkhsolutions.idispatch.ejb.tickets;

import java.util.Calendar;
import java.util.Objects;

public class OpenTicketDTO implements java.io.Serializable {

    private Long ticketId;
    private Calendar ticketOpened;
    private String ticketType;
    private boolean resourcesDispached;
    private boolean resourcesEnRoute;
    private boolean resourcesOnScene;

    public OpenTicketDTO(Long ticketId, Calendar ticketOpened, String ticketType, boolean resourcesDispached, boolean resourcesEnRoute, boolean resourcesOnScene) {
        this.ticketId = ticketId;
        this.ticketOpened = ticketOpened;
        this.ticketType = ticketType;
        this.resourcesDispached = resourcesDispached;
        this.resourcesEnRoute = resourcesEnRoute;
        this.resourcesOnScene = resourcesOnScene;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public Calendar getTicketOpened() {
        return ticketOpened;
    }

    public String getTicketType() {
        return ticketType;
    }

    public boolean isResourcesDispached() {
        return resourcesDispached;
    }

    public boolean isResourcesEnRoute() {
        return resourcesEnRoute;
    }

    public boolean isResourcesOnScene() {
        return resourcesOnScene;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.ticketId);
        hash = 53 * hash + Objects.hashCode(this.ticketOpened);
        hash = 53 * hash + Objects.hashCode(this.ticketType);
        hash = 53 * hash + (this.resourcesDispached ? 1 : 0);
        hash = 53 * hash + (this.resourcesEnRoute ? 1 : 0);
        hash = 53 * hash + (this.resourcesOnScene ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OpenTicketDTO other = (OpenTicketDTO) obj;
        if (!Objects.equals(this.ticketId, other.ticketId)) {
            return false;
        }
        if (!Objects.equals(this.ticketOpened, other.ticketOpened)) {
            return false;
        }
        if (!Objects.equals(this.ticketType, other.ticketType)) {
            return false;
        }
        if (this.resourcesDispached != other.resourcesDispached) {
            return false;
        }
        if (this.resourcesEnRoute != other.resourcesEnRoute) {
            return false;
        }
        if (this.resourcesOnScene != other.resourcesOnScene) {
            return false;
        }
        return true;
    }
}
