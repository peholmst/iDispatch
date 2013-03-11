package net.pkhsolutions.idispatch.rest.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Notification {

    @XmlAttribute
    private Long id;
    @XmlAttribute(name = "timestamp")
    private Calendar timestamp;
    @XmlElement(name = "resource")
    private Set<String> resources;
    @XmlElement(name = "ticketResource")
    private Set<String> ticketResources;
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
    private Urgency urgency;
    @XmlElement
    private String description;

    protected Notification() {
    }

    /*    public Notification(Long id, Calendar timestamp, Set<String> resources, String municipalitySv, String municipalityFi, Long ticketId, String address, String ticketTypeCode, String ticketTypeDescriptionSv, String ticketTypeDescriptionFi, Urgency urgency, String description) {
     this.id = id;
     this.timestamp = timestamp;
     this.resources = resources;
     this.municipalitySv = municipalitySv;
     this.municipalityFi = municipalityFi;
     this.ticketId = ticketId;
     this.address = address;
     this.ticketTypeCode = ticketTypeCode;
     this.ticketTypeDescriptionSv = ticketTypeDescriptionSv;
     this.ticketTypeDescriptionFi = ticketTypeDescriptionFi;
     this.urgency = urgency;
     this.description = description;
     }*/
    public Long getId() {
        return id;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public Collection<String> getResources() {
        ArrayList<String> sortedResources = new ArrayList<>(resources);
        Collections.sort(sortedResources);
        return sortedResources;
    }

    public Collection<String> getTicketResources() {
        ArrayList<String> sortedResources = new ArrayList<>(ticketResources);
        Collections.sort(sortedResources);
        return sortedResources;
    }

    public String getMunicipalitySv() {
        return nullToEmptyString(municipalitySv);
    }

    public String getMunicipalityFi() {
        return nullToEmptyString(municipalityFi);
    }

    public Long getTicketId() {
        return ticketId;
    }

    public String getAddress() {
        return nullToEmptyString(address);
    }

    public String getTicketTypeCode() {
        return nullToEmptyString(ticketTypeCode);
    }

    public String getTicketTypeDescriptionSv() {
        return nullToEmptyString(ticketTypeDescriptionSv);
    }

    public String getTicketTypeDescriptionFi() {
        return nullToEmptyString(ticketTypeDescriptionFi);
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public String getDescription() {
        return nullToEmptyString(description);
    }

    public boolean concernsAnyOf(Collection<String> resources) {
        if (this.resources == null || this.resources.isEmpty()) {
            return false;
        }
        for (String resource : resources) {
            if (this.resources.contains(resource)) {
                return true;
            }
        }
        return false;
    }

    private static String nullToEmptyString(String s) {
        return s == null ? "" : s;
    }

    @Override
    public String toString() {
        return String.format("Notification[id = %s]", id);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final Notification other = (Notification) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
