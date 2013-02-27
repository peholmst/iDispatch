package net.pkhsolutions.idispatch.entity;

import javax.persistence.*;

@Entity
public class CurrentResourceStatus extends AbstractResourceStatus {

    @OneToOne(optional = false)
    private Resource resource;

    protected CurrentResourceStatus() {
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    protected void setResource(Resource resource) {
        this.resource = resource;
    }

    public static class Builder extends AbstractResourceStatusBuilder<CurrentResourceStatus, Builder> {

        public Builder() {
            super(CurrentResourceStatus.class);
        }

        public Builder(CurrentResourceStatus original) {
            super(CurrentResourceStatus.class, original);
        }
    }
}
