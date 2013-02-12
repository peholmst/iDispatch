package net.pkhsolutions.idispatch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Resource extends AbstractEntityWithOptimisticLocking {

    @Column(nullable = false, unique = true)
    private String callSign;
    @ManyToOne(optional = false)
    private ResourceType resourceType;

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

    public static class Builder {
        private Resource resource;

        public Builder() {
            resource = new Resource();
        }

        public Builder withCallSign(String callSign) {
            resource.setCallSign(callSign);
            return this;
        }

        public Builder withResourceType(ResourceType resourceType) {
            resource.setResourceType(resourceType);
            return this;
        }

        public Resource build() {
            // TODO Validate the fields
            return resource;
        }
    }
}
