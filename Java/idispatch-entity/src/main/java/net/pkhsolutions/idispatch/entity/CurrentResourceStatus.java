package net.pkhsolutions.idispatch.entity;

import javax.persistence.*;

@Entity
public class CurrentResourceStatus extends AbstractResourceStatus {

    @OneToOne(optional = false)
    private Resource resource;
    @Version
    private Long version;

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

    public Long getVersion() {
        return version;
    }

    protected void setVersion(Long version) {
        this.version = version;
    }

    public static class Builder extends AbstractResourceStatusBuilder<CurrentResourceStatus, Builder> {

        public Builder() {
            super(CurrentResourceStatus.class);
        }

        public Builder(CurrentResourceStatus original) {
            super(CurrentResourceStatus.class, original);
            entity.setVersion(original.getVersion());
        }
    }
}
