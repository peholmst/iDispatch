package net.pkhsolutions.idispatch.ejb.tickets;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import net.pkhsolutions.idispatch.entity.ResourceState;
import static net.pkhsolutions.idispatch.entity.ResourceState.ASSIGNED;
import static net.pkhsolutions.idispatch.entity.ResourceState.AT_STATION;
import static net.pkhsolutions.idispatch.entity.ResourceState.AVAILABLE;
import static net.pkhsolutions.idispatch.entity.ResourceState.DISPATCHED;
import static net.pkhsolutions.idispatch.entity.ResourceState.EN_ROUTE;
import static net.pkhsolutions.idispatch.entity.ResourceState.ON_SCENE;

public class TicketResourceDTO implements java.io.Serializable, Comparable<TicketResourceDTO> {

    private int orderNo = 0;
    private String resourceCallSign;
    private Calendar assigned;
    private Calendar dispatched;
    private Calendar enRoute;
    private Calendar onScene;
    private Calendar availableOnRadio;
    private Calendar availableAtStation;

    TicketResourceDTO() {
    }

    public TicketResourceDTO(String resourceCallSign, Calendar assigned, Calendar dispatched, Calendar enRoute, Calendar onScene, Calendar availableOnRadio, Calendar availableAtStation) {
        setResourceCallSign(resourceCallSign);
        setAssigned(assigned);
        setDispatched(dispatched);
        setEnRoute(enRoute);
        setOnScene(onScene);
        setAvailableOnRadio(availableOnRadio);
        setAvailableAtStation(availableAtStation);
    }

    public Calendar getTimestamp(ResourceState state) {
        switch (state) {
            case ASSIGNED:
                return getAssigned();
            case AT_STATION:
                return getAvailableAtStation();
            case AVAILABLE:
                return getAvailableOnRadio();
            case DISPATCHED:
                return getDispatched();
            case EN_ROUTE:
                return getEnRoute();
            case ON_SCENE:
                return getOnScene();
            default:
                return null;
        }
    }

    void setTimestamp(ResourceState state, Calendar timestamp) {
        switch (state) {
            case ASSIGNED:
                setAssigned(timestamp);
                return;
            case AT_STATION:
                setAvailableAtStation(timestamp);
                return;
            case AVAILABLE:
                setAvailableOnRadio(timestamp);
                return;
            case DISPATCHED:
                setDispatched(timestamp);
                return;
            case EN_ROUTE:
                setEnRoute(timestamp);
                return;
            case ON_SCENE:
                setOnScene(timestamp);
        }
    }

    public String getResourceCallSign() {
        return resourceCallSign;
    }

    final void setResourceCallSign(String resourceCallSign) {
        this.resourceCallSign = resourceCallSign;
    }

    public Calendar getAssigned() {
        return assigned;
    }

    final void setAssigned(Calendar assigned) {
        this.assigned = normalizeCalendar(assigned);
    }

    public Calendar getDispatched() {
        return dispatched;
    }

    final void setDispatched(Calendar dispatched) {
        this.dispatched = normalizeCalendar(dispatched);
    }

    public Calendar getEnRoute() {
        return enRoute;
    }

    final void setEnRoute(Calendar enRoute) {
        this.enRoute = normalizeCalendar(enRoute);
    }

    public Calendar getOnScene() {
        return onScene;
    }

    final void setOnScene(Calendar onScene) {
        this.onScene = normalizeCalendar(onScene);
    }

    public Calendar getAvailableOnRadio() {
        return availableOnRadio;
    }

    final void setAvailableOnRadio(Calendar availableOnRadio) {
        this.availableOnRadio = normalizeCalendar(availableOnRadio);
    }

    public Calendar getAvailableAtStation() {
        return availableAtStation;
    }

    final void setAvailableAtStation(Calendar availableAtStation) {
        this.availableAtStation = normalizeCalendar(availableAtStation);
    }

    public int getOrderNo() {
        return orderNo;
    }

    void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    private Calendar normalizeCalendar(Calendar cal) {
        if (cal == null) {
            return null;
        }
        Calendar normalized = Calendar.getInstance();
        normalized.setTime(cal.getTime());
        normalized.set(Calendar.MILLISECOND, 0);
        normalized.setTimeZone(TimeZone.getDefault());
        return normalized;
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
        return true;
    }

    @Override
    public int compareTo(TicketResourceDTO o) {
        int result = resourceCallSign.compareTo(o.resourceCallSign);
        if (result == 0) {
            return orderNo - o.orderNo;
        } else {
            return result;
        }

    }
}
