package net.pkhsolutions.idispatch.entity;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.Date;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.nullToEmpty;

/**
 * Entity representing a ticket.
 */
@Entity
@Table(name = "assignments")
public class Assignment extends AbstractLockableEntity {

    public static final String PROP_OPENED = "opened";
    public static final String PROP_CLOSED = "closed";
    public static final String PROP_URGENCY = "urgency";
    public static final String PROP_TYPE = "type";
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_MUNICIPALITY = "municipality";
    public static final String PROP_ADDRESS = "address";

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "opened", nullable = false)
    private Date opened;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "closed")
    private Date closed;
    @Enumerated(EnumType.STRING)
    @Column(name = "urgency", nullable = false)
    private AssignmentUrgency urgency = AssignmentUrgency.UNKNOWN;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private AssignmentType type;
    @Column(name = "description", nullable = false)
    private String description = "";
    @ManyToOne
    @JoinColumn(name = "municipality_id")
    private Municipality municipality;
    @Column(name = "address", nullable = false)
    private String address = "";

    public Assignment() {
        opened = new Date();
    }

    public Date getOpened() {
        return opened;
    }

    public Date getClosed() {
        return closed;
    }

    public boolean isOpen() {
        return closed == null;
    }

    public boolean isClosed() {
        return closed != null;
    }

    public void setClosed(Date closed) {
        this.closed = checkNotNull(closed);
    }

    public AssignmentUrgency getUrgency() {
        return urgency;
    }

    public void setUrgency(AssignmentUrgency urgency) {
        this.urgency = firstNonNull(urgency, AssignmentUrgency.UNKNOWN);
    }

    public AssignmentType getType() {
        return type;
    }

    public void setType(AssignmentType type) {
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
                .add(PROP_OPENED, opened)
                .add(PROP_CLOSED, closed)
                .add(PROP_TYPE, type)
                .toString();
    }
}
