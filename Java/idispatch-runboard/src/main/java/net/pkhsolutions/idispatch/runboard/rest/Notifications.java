package net.pkhsolutions.idispatch.runboard.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Notifications {

    @XmlElement(name = "notification")
    private List<Notification> notifications;

    protected Notifications() {
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public List<Notification> getNotificationsForResources(String... resources) {
        return getNotificationsForResources(Arrays.asList(resources));
    }

    public List<Notification> getNotificationsForResources(Collection<String> resources) {
        List<Notification> result = new ArrayList<>();
        if (notifications != null) {
            for (Notification notification : notifications) {
                if (notification.concernsAnyOf(resources)) {
                    result.add(notification);
                }
            }
        }
        return result;
    }
}
