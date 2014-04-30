package net.pkhsolutions.idispatch.domain.tickets;

import net.pkhsolutions.idispatch.domain.AbstractLockableEntity;
import net.pkhsolutions.idispatch.domain.Municipality;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Strings.nullToEmpty;

/**
 * Entity representing a ticket.
 */
@Entity
@Table(name = "tickets")
public class Ticket extends AbstractLockableEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "opened", nullable = false)
    private Date ticketOpened;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "closed")
    private Date ticketClosed;
    @Enumerated(EnumType.STRING)
    @Column(name = "urgency", nullable = false)
    private TicketUrgency urgency = TicketUrgency.UNKNOWN;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private TicketType type;
    @Column(name = "description", nullable = false)
    private String description = "";
    @ManyToOne
    @JoinColumn(name = "municipality_id")
    private Municipality municipality;
    @Column(name = "address", nullable = false)
    private String address = "";

    protected Ticket() {
    }

    public Date getTicketOpened() {
        return ticketOpened;
    }

    public Date getTicketClosed() {
        return ticketClosed;
    }

    public boolean isClosed() {
        return ticketClosed != null;
    }

    public TicketUrgency getUrgency() {
        return urgency;
    }

    public TicketType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public String getAddress() {
        return address;
    }

    /**
     * Builder for creating instances of {@link Ticket}.
     */
    public static class Builder extends AbstractLockableEntityBuilder<Ticket, Builder> {

        public Builder() {
            super(Ticket.class);
            entity.ticketOpened = Calendar.getInstance().getTime();
        }

        public Builder(Ticket original) {
            super(Ticket.class, original);
            entity.description = original.description;
            entity.address = original.address;
            entity.municipality = original.municipality;
            entity.ticketClosed = clone(original.ticketClosed);
            entity.ticketOpened = clone(original.ticketOpened);
            entity.type = original.type;
            entity.urgency = original.urgency;
        }

        /**
         * This method should only be called by {@link net.pkhsolutions.idispatch.domain.tickets.TicketServiceBean}.
         * Therefore, it has package visibility.
         */
        Builder close() {
            entity.ticketClosed = Calendar.getInstance().getTime();
            return this;
        }

        public Builder withUrgency(TicketUrgency urgency) {
            entity.urgency = firstNonNull(urgency, TicketUrgency.UNKNOWN);
            return this;
        }

        public Builder withType(TicketType type) {
            entity.type = type;
            return this;
        }

        public Builder withDescription(String description) {
            entity.description = nullToEmpty(description);
            return this;
        }

        public Builder withMunicipality(Municipality municipality) {
            entity.municipality = municipality;
            return this;
        }

        public Builder withAddress(String address) {
            entity.address = nullToEmpty(address);
            return this;
        }
    }
}
