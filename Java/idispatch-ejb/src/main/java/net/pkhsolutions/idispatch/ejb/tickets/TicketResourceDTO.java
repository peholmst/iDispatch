package net.pkhsolutions.idispatch.ejb.tickets;

import java.util.Calendar;
import java.util.Objects;

/**
 *
 * @author peholmst
 */
public class TicketResourceDTO implements java.io.Serializable {

    private String resourceCallSign;
    private Calendar assigned;
    private Calendar dispatched;
    private Calendar enRoute;
    private Calendar onScene;
    private Calendar availableOnRadio;
    private Calendar availableAtStation;
    private boolean detached;

    public TicketResourceDTO(String resourceCallSign, Calendar assigned, Calendar dispatched, Calendar enRoute, Calendar onScene, Calendar availableOnRadio, Calendar availableAtStation, boolean detached) {
        this.resourceCallSign = resourceCallSign;
        this.assigned = assigned;
        this.dispatched = dispatched;
        this.enRoute = enRoute;
        this.onScene = onScene;
        this.availableOnRadio = availableOnRadio;
        this.availableAtStation = availableAtStation;
        this.detached = detached;
    }

    public String getResourceCallSign() {
        return resourceCallSign;
    }

    public Calendar getAssigned() {
        return assigned;
    }

    public Calendar getDispatched() {
        return dispatched;
    }

    public Calendar getEnRoute() {
        return enRoute;
    }

    public Calendar getOnScene() {
        return onScene;
    }

    public Calendar getAvailableOnRadio() {
        return availableOnRadio;
    }

    public Calendar getAvailableAtStation() {
        return availableAtStation;
    }

    public boolean isDetached() {
        return detached;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.resourceCallSign);
        hash = 79 * hash + Objects.hashCode(this.assigned);
        hash = 79 * hash + Objects.hashCode(this.dispatched);
        hash = 79 * hash + Objects.hashCode(this.enRoute);
        hash = 79 * hash + Objects.hashCode(this.onScene);
        hash = 79 * hash + Objects.hashCode(this.availableOnRadio);
        hash = 79 * hash + Objects.hashCode(this.availableAtStation);
        hash = 79 * hash + Objects.hashCode(this.detached);
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
        final TicketResourceDTO other = (TicketResourceDTO) obj;
        if (!Objects.equals(this.resourceCallSign, other.resourceCallSign)) {
            return false;
        }
        if (!Objects.equals(this.assigned, other.assigned)) {
            return false;
        }
        if (!Objects.equals(this.dispatched, other.dispatched)) {
            return false;
        }
        if (!Objects.equals(this.enRoute, other.enRoute)) {
            return false;
        }
        if (!Objects.equals(this.onScene, other.onScene)) {
            return false;
        }
        if (!Objects.equals(this.availableOnRadio, other.availableOnRadio)) {
            return false;
        }
        if (!Objects.equals(this.availableAtStation, other.availableAtStation)) {
            return false;
        }
        if (!Objects.equals(this.detached, other.detached)) {
            return false;
        }
        return true;
    }
}
