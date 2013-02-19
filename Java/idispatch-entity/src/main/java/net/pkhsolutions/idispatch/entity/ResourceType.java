package net.pkhsolutions.idispatch.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class ResourceType extends AbstractEntityWithOptimisticLocking {

    @NotNull(message = "Please enter a name for the resource type")
    private String name;

    protected ResourceType() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final class Builder extends AbstractEntityWithOptimisticLockingBuilder<ResourceType, Builder> {

        public Builder() {
            super(ResourceType.class);
        }

        public Builder(ResourceType original) {
            super(ResourceType.class, original);
            entity.setName(original.getName());
        }

        public Builder withName(String name) {
            entity.setName(name);
            return this;
        }
    }
}
