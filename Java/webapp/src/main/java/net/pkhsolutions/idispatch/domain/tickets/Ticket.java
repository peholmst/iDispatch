package net.pkhsolutions.idispatch.domain.tickets;

import com.google.common.base.Objects;
import net.pkhsolutions.idispatch.domain.AbstractLockableEntity;
import net.pkhsolutions.idispatch.domain.Municipality;

import javax.persistence.*;
import java.util.Date;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Strings.nullToEmpty;

/**
 * Entity representing a ticket.
 */
@Entity
@Table(name = "tickets")
public class Ticket extends AbstractLockableEntity {

    public static final String PROP_TICKET_OPENED = "ticketOpened";
    public static final String PROP_TICKET_CLOSED = "ticketClosed";
    public static final String PROP_URGENCY = "urgency";
    public static final String PROP_TYPE = "type";
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_MUNICIPALITY = "municipality";
    public static final String PROP_ADDRESS = "address";

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

    public Ticket() {
        ticketOpened = new Date();
    }

    public Date getTicketOpened() {
        return ticketOpened;
    }

    public Date getTicketClosed() {
        return ticketClosed;
    }

    void close() {
        ticketClosed = new Date();
    }

    public boolean isClosed() {
        return ticketClosed != null;
    }

    public TicketUrgency getUrgency() {
        return urgency;
    }

    public void setUrgency(TicketUrgency urgency) {
        this.urgency = firstNonNull(urgency, TicketUrgency.UNKNOWN);
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = nullToEmpty(description);
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
        this.address = nullToEmpty(address);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add(PROP_ID, getId())
                .add(PROP_TICKET_OPENED, ticketOpened)
                .add(PROP_TICKET_CLOSED, ticketClosed)
                .add(PROP_TYPE, type)
                .toString();
    }
}
