package net.pkhsolutions.idispatch.entity;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class DispatchNotification extends AbstractEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @XmlAttribute(name = "timestamp")
    private Calendar dispatchTimestamp;
    @ElementCollection
    @XmlElement
    private Set<String> resourceCallSigns = new HashSet<>();
    @XmlElement
    private String municipalitySv;
    @XmlElement
    private String municipalityFi;
    @XmlAttribute
    private Long ticketId;
    @XmlElement
    private String address;
    @XmlElement
    private String ticketTypeCode;
    @XmlElement
    private String ticketTypeDescriptionSv;
    @XmlElement
    private String ticketTypeDescriptionFi;
    @XmlElement
    @Enumerated
    private TicketUrgency urgency;
    @XmlElement
    private String description;

    protected DispatchNotification() {
    }

    public Calendar getDispatchTimestamp() {
        return dispatchTimestamp;
    }

    public Set<String> getResourceCallSigns() {
        return resourceCallSigns;
    }

    public String getMunicipalitySv() {
        return municipalitySv;
    }

    public String getMunicipalityFi() {
        return municipalityFi;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public String getAddress() {
        return address;
    }

    public String getTicketTypeCode() {
        return ticketTypeCode;
    }

    public String getTicketTypeDescriptionSv() {
        return ticketTypeDescriptionSv;
    }

    public String getTicketTypeDescriptionFi() {
        return ticketTypeDescriptionFi;
    }

    public TicketUrgency getUrgency() {
        return urgency;
    }

    public String getDescription() {
        return description;
    }

    public static class Builder extends AbstractEntityBuilder<DispatchNotification, Builder> {

        public Builder() {
            super(DispatchNotification.class);
        }

        public Builder(DispatchNotification original) {
            super(DispatchNotification.class, original);
            entity.address = original.address;
            entity.description = original.description;
            entity.dispatchTimestamp = clone(original.dispatchTimestamp);
            entity.municipalityFi = original.municipalityFi;
            entity.municipalitySv = original.municipalitySv;
            entity.resourceCallSigns = new HashSet<>(original.resourceCallSigns);
            entity.ticketId = original.ticketId;
            entity.ticketTypeCode = original.ticketTypeCode;
            entity.ticketTypeDescriptionFi = original.ticketTypeDescriptionFi;
            entity.ticketTypeDescriptionSv = original.ticketTypeDescriptionSv;
            entity.urgency = original.urgency;
        }

        public Builder fromTicket(Ticket ticket) {
            entity.address = ticket.getAddress();
            entity.description = ticket.getDescription();
            entity.municipalityFi = ticket.getMunicipality().getNameFi();
            entity.municipalitySv = ticket.getMunicipality().getNameSv();
            entity.ticketId = ticket.getId();
            entity.ticketTypeCode = ticket.getTicketType().getCode();
            entity.ticketTypeDescriptionFi = ticket.getTicketType().getDescriptionFi();
            entity.ticketTypeDescriptionSv = ticket.getTicketType().getDescriptionSv();
            entity.urgency = ticket.getUrgency();
            return this;
        }

        public Builder withResource(Resource resource) {
            entity.resourceCallSigns.add(resource.getCallSign());
            return this;
        }

        public Builder withResourceStatus(CurrentResourceStatus resource) {
            return withResource(resource.getResource());
        }

        public Builder withResources(Collection<Resource> resources) {
            for (Resource r : resources) {
                entity.resourceCallSigns.add(r.getCallSign());
            }
            return this;
        }

        public Builder withResourceStatuses(Collection<CurrentResourceStatus> resources) {
            for (CurrentResourceStatus r : resources) {
                entity.resourceCallSigns.add(r.getResource().getCallSign());
            }
            return this;
        }

        @Override
        public DispatchNotification build() {
            entity.dispatchTimestamp = Calendar.getInstance();
            return super.build();
        }
    }
}
