package net.pkhsolutions.idispatch.runboard.client;

import java.util.*;

@SuppressWarnings("unchecked")
public class Notification {

    private final Map<String, Object> notificationData;

    public Notification(Map<String, Object> notificationData) {
        this.notificationData = notificationData;
    }

    public Number getId() {
        return (Number) notificationData.get("id");
    }

    public Date getTimestamp() {
        return new Date((Long) notificationData.getOrDefault("timestamp", System.currentTimeMillis()));
    }

    public Collection<String> getResources() {
        return (Collection<String>) notificationData.getOrDefault("resources", Collections.emptyList());
    }

    public Number getAssignmentId() {
        return (Number) notificationData.getOrDefault("assignment_id", -1);
    }

    public String getAssignmentTypeCode() {
        return (String) notificationData.getOrDefault("assignment_type_code", "");
    }

    public String getAssignmentTypeDescription() {
        return (String) notificationData.getOrDefault("assignment_type_descr", "");
    }

    public String getUrgency() {
        return (String) notificationData.getOrDefault("urgency", "");
    }

    public String getMunicipality() {
        return (String) notificationData.getOrDefault("municipality", "");
    }

    public String getAddress() {
        return (String) notificationData.getOrDefault("address", "");
    }

    public String getDescription() {
        return (String) notificationData.getOrDefault("description", "");
    }

    @Override
    public String toString() {
        return String.format("Notification[id = %s]", getId());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.getId());
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
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        return true;
    }
}
