package net.pkhsolutions.idispatch.domain.resources;

import net.pkhsolutions.idispatch.domain.AbstractLockableEntity;

import javax.persistence.*;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * Entity representing a resource.
 */
@Entity
@Table(name = "resources")
public class Resource extends AbstractLockableEntity {

    public static final String PROP_CALL_SIGN = "callSign";
    public static final String PROP_TYPE = "type";
    public static final String PROP_ACTIVE = "active";

    @Column(name = "call_sign", unique = true, nullable = false)
    private String callSign = "";
    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id", nullable = false)
    private ResourceType type;
    @Column(name = "active", nullable = false)
    private boolean active = true;

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = nullToEmpty(callSign);
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
