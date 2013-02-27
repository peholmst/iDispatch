package net.pkhsolutions.idispatch.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Municipality extends AbstractEntityWithOptimisticLocking {

    @NotNull(message = "Please enter a Finnish name for the municipality")
    private String nameFi;
    @NotNull(message = "Please enter a Swedish name for the municipality")
    private String nameSv;

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

    public String getBothNames() {
        return String.format("%s / %s", nameFi, nameSv);
    }

    public static final class Builder extends AbstractEntityWithOptimisticLockingBuilder<Municipality, Builder> {

        public Builder() {
            super(Municipality.class);
        }

        public Builder(Municipality original) {
            super(Municipality.class, original);
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
