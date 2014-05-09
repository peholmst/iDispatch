package net.pkhsolutions.idispatch.entity;

import com.google.common.base.Objects;

import javax.persistence.*;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * Entity representing a resource.
 */
@Entity
@Table(name = "resources")
public class Resource extends AbstractLockableEntity implements Comparable<Resource>, Deactivatable {

    public static final String PROP_CALL_SIGN = "callSign";
    public static final String PROP_TYPE = "type";

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

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add(PROP_ID, getId())
                .add(PROP_VERSION, getVersion())
                .add(PROP_CALL_SIGN, callSign)
                .toString();
    }

    @Override
    public int compareTo(Resource o) {
        return callSign.compareTo(o.callSign);
    }
}
