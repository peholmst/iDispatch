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

    protected Resource() {
    }

    public String getCallSign() {
        return callSign;
    }

    public ResourceType getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Builder for creating instances of {@link net.pkhsolutions.idispatch.domain.resources.Resource}.
     */
    public static final class Builder extends AbstractLockableEntityBuilder<Resource, Builder> {

        public Builder() {
            super(Resource.class);
        }

        public Builder(Resource original) {
            super(Resource.class, original);
            entity.callSign = original.callSign;
            entity.type = original.type;
            entity.active = original.active;
        }

        public Builder withCallSign(String callSign) {
            entity.callSign = nullToEmpty(callSign);
            return this;
        }

        public Builder active() {
            entity.active = true;
            return this;
        }

        public Builder inactive() {
            entity.active = false;
            return this;
        }

        public Builder withType(ResourceType type) {
            entity.type = type;
            return this;
        }
    }
}
