package net.pkhsolutions.idispatch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Resource extends AbstractEntityWithOptimisticLocking implements Comparable<Resource> {

    @Column(nullable = false, unique = true)
    @NotNull(message = "Please enter a call sign for the resource")
    private String callSign;
    @ManyToOne(optional = false)
    @NotNull(message = "Please select a type for the resource")
    private ResourceType resourceType;
    @Column(nullable = false)
    private boolean active = true;

    protected Resource() {
    }

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int compareTo(Resource o) {
        return callSign.compareTo(o.callSign);
    }

    public static final class Builder extends AbstractEntityWithOptimisticLockingBuilder<Resource, Builder> {

        public Builder() {
            super(Resource.class);
        }

        public Builder(Resource original) {
            super(Resource.class, original);
            entity.setCallSign(original.getCallSign());
            entity.setResourceType(original.getResourceType());
            entity.setActive(original.isActive());
        }

        public Builder withCallSign(String callSign) {
            entity.setCallSign(callSign);
            return this;
        }

        public Builder active() {
            entity.setActive(true);
            return this;
        }

        public Builder inactive() {
            entity.setActive(false);
            return this;
        }

        public Builder withResourceType(ResourceType resourceType) {
            entity.setResourceType(resourceType);
            return this;
        }
    }
}
