package net.pkhsolutions.idispatch.entity;

import javax.persistence.Entity;

@Entity
public class ResourceType extends AbstractEntityWithOptimisticLocking {

    private String name;

    protected ResourceType() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Builder {
        private ResourceType resourceType;

        public Builder() {
            resourceType = new ResourceType();
        }

        public Builder withName(String name) {
            resourceType.setName(name);
            return this;
        }

        public ResourceType build() {
            // TODO Validate the fields
            return resourceType;
        }
    }
}
