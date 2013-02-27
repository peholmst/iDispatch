package net.pkhsolutions.idispatch.entity;

import javax.persistence.*;
import java.util.Calendar;
import javax.validation.constraints.NotNull;

@Entity
public class Ticket extends AbstractEntityWithOptimisticLocking {

    public static class DispatchValidation {
    }
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar ticketOpened;
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar ticketClosed;
    @Enumerated
    @NotNull(message = "Please select an urgency level for the ticket", groups = DispatchValidation.class)
    private TicketUrgency urgency;
    @ManyToOne
    @NotNull(message = "Please select a ticket type for the ticket", groups = DispatchValidation.class)
    private TicketType ticketType;
    private String description;
    @ManyToOne
    @NotNull(message = "Please select a municipality for the ticket", groups = DispatchValidation.class)
    private Municipality municipality;
    @NotNull(message = "Please enter an address for the ticket", groups = DispatchValidation.class)
    private String address;

    protected Ticket() {
    }

    public Calendar getTicketOpened() {
        return ticketOpened;
    }

    public Calendar getTicketClosed() {
        return ticketClosed;
    }

    public boolean isClosed() {
        return ticketClosed != null;
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

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static class Builder extends AbstractEntityWithOptimisticLockingBuilder<Ticket, Builder> {

        public Builder() {
            super(Ticket.class);
            entity.ticketOpened = Calendar.getInstance();
        }

        public Builder(Ticket original) {
            super(Ticket.class, original);
            entity.description = original.description;
            entity.address = original.address;
            entity.municipality = original.municipality;
            entity.ticketClosed = clone(original.ticketClosed);
            entity.ticketOpened = clone(original.ticketOpened);
            entity.ticketType = original.ticketType;
            entity.urgency = original.urgency;
        }

        public Builder close() {
            entity.ticketClosed = Calendar.getInstance();
            return this;
        }
    }
}
