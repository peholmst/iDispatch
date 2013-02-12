package net.pkhsolutions.idispatch.entity;

import javax.persistence.*;
import java.util.Calendar;

@Entity
public class Ticket extends AbstractEntityWithOptimisticLocking {

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar ticketOpened;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar ticketClosed;

    @Enumerated
    private TicketUrgency urgency;

    @ManyToOne
    private TicketType ticketType;

    private String description;
    private String municipality; // TODO Should be ManyToOne in a future version
    private String location;

    public Ticket() {
    }

    public Calendar getTicketOpened() {
        return ticketOpened;
    }

    public void setTicketOpened(Calendar ticketOpened) {
        this.ticketOpened = ticketOpened;
    }

    public Calendar getTicketClosed() {
        return ticketClosed;
    }

    public void setTicketClosed(Calendar ticketClosed) {
        this.ticketClosed = ticketClosed;
    }

    public TicketUrgency getUrgency() {
        return urgency;
    }

    public void setUrgency(TicketUrgency urgency) {
        this.urgency = urgency;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
