package net.pkhsolutions.idispatch.domain.resources;

import net.pkhsolutions.idispatch.domain.AbstractLockableEntity;

import javax.persistence.*;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * Entity representing a resource.
 */
@Entity
@Table(name = "resources")
public class Resource extends AbstractLockableEntity implements Comparable<Resource> {

    @Column(name = "call_sign", unique = true, nullable = false)
    private String callSign = "";
    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id", nullable = false)
    private ResourceType resourceType;
    @Column(name = "active", nullable = false)
    private boolean active = true;

    protected Resource() {
    }

    public String getCallSign() {
        return callSign;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public int compareTo(Resource o) {
        return callSign.compareTo(o.callSign);
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
            entity.resourceType = original.resourceType;
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

        public Builder withResourceType(ResourceType resourceType) {
            entity.resourceType = resourceType;
            return this;
        }
    }
}
