package net.pkhsolutions.idispatch.entity;

import javax.persistence.*;

@Entity
public class ArchivedResourceStatus extends AbstractResourceStatus {

    @ManyToOne(optional = false)
    private Resource resource;

    protected ArchivedResourceStatus() {
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    protected void setResource(Resource resource) {
        this.resource = resource;
    }

    public static class Builder extends AbstractResourceStatusBuilder<ArchivedResourceStatus, Builder> {

        public Builder() {
            super(ArchivedResourceStatus.class);
        }

        public Builder(ArchivedResourceStatus original) {
            super(ArchivedResourceStatus.class, original);
        }

        public Builder(CurrentResourceStatus original) {
            super(ArchivedResourceStatus.class, original);
        }
    }
}
