package net.pkhsolutions.idispatch.entity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class ResourceStatus extends AbstractEntity {

    @ManyToOne(optional = false)
    private Resource resource;
    @ManyToOne(optional = false)
    private ResourceState state;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Calendar timestamp;
    @ManyToOne
    private Ticket ticket;
    private String comment;

    protected ResourceStatus() {

    }

    public Resource getResource() {
        return resource;
    }

    public ResourceState getState() {
        return state;
    }

    public Calendar getTimestamp() {
        return (Calendar) timestamp.clone();
    }

    public String getComment() {
        return comment;
    }

    /**
     * Returns the ticket that the resource is assigned to, or {@code null} if the
     * resource is not assigned at the moment.
     */
    public Ticket getTicket() {
        return ticket;
    }

    public static class Builder {
        private ResourceStatus status;

        public Builder() {
            status = new ResourceStatus();
        }

        public Builder withResource(Resource resource) {
            status.resource = resource;
            return this;
        }

        public Builder withState(ResourceState state) {
            status.state = state;
            return this;
        }

        public Builder withTimestamp(Date timestamp) {
            status.timestamp = Calendar.getInstance();
            status.timestamp.setTime(timestamp);
            return this;
        }

        public Builder withTicket(Ticket ticket) {
            status.ticket = ticket;
            return this;
        }

        public Builder withComment(String comment) {
            status.comment = comment;
            return this;
        }

        public ResourceStatus build() {
            if (status.timestamp == null) {
                status.timestamp = Calendar.getInstance();
                // TODO Validate the rest of the fields
            }
            return status;
        }
    }
}
