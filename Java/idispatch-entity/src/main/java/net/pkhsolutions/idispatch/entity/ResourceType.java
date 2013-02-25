package net.pkhsolutions.idispatch.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class ResourceType extends AbstractEntityWithOptimisticLocking {

    @NotNull(message = "Please enter a Finnish name for the resource type")
    private String nameFi;
    @NotNull(message = "Please enter a Swedish name for the resource type")
    private String nameSv;

    protected ResourceType() {
    }

    public String getNameFi() {
        return nameFi;
    }

    public void setNameFi(String nameFi) {
        this.nameFi = nameFi;
    }

    public String getNameSv() {
        return nameSv;
    }

    public void setNameSv(String nameSv) {
        this.nameSv = nameSv;
    }

    public static final class Builder extends AbstractEntityWithOptimisticLockingBuilder<ResourceType, Builder> {

        public Builder() {
            super(ResourceType.class);
        }

        public Builder(ResourceType original) {
            super(ResourceType.class, original);
            entity.setNameFi(original.getNameFi());
            entity.setNameSv(original.getNameSv());
        }

        public Builder withSwedishName(String name) {
            entity.setNameSv(name);
            return this;
        }

        public Builder withFinnishName(String name) {
            entity.setNameFi(name);
            return this;
        }
    }
}
