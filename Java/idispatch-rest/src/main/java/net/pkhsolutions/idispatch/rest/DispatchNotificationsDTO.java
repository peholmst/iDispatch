package net.pkhsolutions.idispatch.rest;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.pkhsolutions.idispatch.entity.DispatchNotification;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DispatchNotificationsDTO {

    @XmlElement(name = "notification")
    public List<DispatchNotification> notifications;

    protected DispatchNotificationsDTO() {
    }

    public DispatchNotificationsDTO(List<DispatchNotification> notifications) {
        this.notifications = notifications;
    }
}
