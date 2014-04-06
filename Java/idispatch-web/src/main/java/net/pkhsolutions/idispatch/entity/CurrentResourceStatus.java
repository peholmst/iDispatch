package net.pkhsolutions.idispatch.entity;

import java.util.Calendar;
import javax.persistence.*;

@Entity
public class CurrentResourceStatus extends AbstractResourceStatus {

    @OneToOne(optional = false)
    @JoinColumn(unique = true, nullable = false)
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
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void setResourceState(ResourceState resourceState) {
        super.setResourceState(resourceState); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setStateChangeTimestamp(Calendar stateChangeTimestamp) {
        super.setStateChangeTimestamp(stateChangeTimestamp);
    }

    @Override
    public void setTicket(Ticket ticket) {
        super.setTicket(ticket);
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
