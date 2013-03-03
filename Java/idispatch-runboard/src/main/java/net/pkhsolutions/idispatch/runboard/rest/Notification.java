package net.pkhsolutions.idispatch.runboard.rest;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Notification {

    @XmlAttribute(name = "timestamp")
    private Calendar timestamp;
    @XmlElement(name = "resource")
    private Set<String> resources;
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

    public Calendar getTimestamp() {
        return timestamp;
    }

    public Set<String> getResources() {
        return resources;
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

    public Urgency getUrgency() {
        return urgency;
    }

    public String getDescription() {
        return description;
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
}
