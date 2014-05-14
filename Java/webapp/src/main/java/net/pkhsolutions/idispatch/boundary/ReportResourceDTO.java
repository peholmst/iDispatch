package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.entity.Resource;

import java.io.Serializable;
import java.util.Date;

public class ReportResourceDTO implements Serializable {

    public static final String PROP_CALL_SIGN = "callSign";
    public static final String PROP_DISPATCHED = "dispatched";
    public static final String PROP_EN_ROUTE = "enRoute";
    public static final String PROP_ON_SCENE = "onScene";
    public static final String PROP_AVAILABLE = "available";
    public static final String PROP_AT_STATION = "atStation";

    private final Resource resource;
    private final Date dispatched;
    private final Date enRoute;
    private final Date onScene;
    private final Date available;
    private final Date atStation;

    public ReportResourceDTO(Resource resource, Date dispatched, Date enRoute, Date onScene, Date available, Date atStation) {
        this.resource = resource;
        this.dispatched = dispatched;
        this.enRoute = enRoute;
        this.onScene = onScene;
        this.available = available;
        this.atStation = atStation;
    }

    public Resource getResource() {
        return resource;
    }

    public String getCallSign() {
        return resource.getCallSign();
    }

    public Date getDispatched() {
        return dispatched;
    }

    public Date getEnRoute() {
        return enRoute;
    }

    public Date getOnScene() {
        return onScene;
    }

    public Date getAvailable() {
        return available;
    }

    public Date getAtStation() {
        return atStation;
    }

    public static class Builder {

        private Resource resource;
        private Date dispatched;
        private Date enRoute;
        private Date onScene;
        private Date available;
        private Date atStation;

        public Builder(Resource resource) {
            this.resource = resource;
        }

        public Date getDispatched() {
            return dispatched;
        }

        public Builder setDispatched(Date dispatched) {
            this.dispatched = dispatched;
            return this;
        }

        public Date getEnRoute() {
            return enRoute;
        }

        public Builder setEnRoute(Date enRoute) {
            this.enRoute = enRoute;
            return this;
        }

        public Date getOnScene() {
            return onScene;
        }

        public Builder setOnScene(Date onScene) {
            this.onScene = onScene;
            return this;
        }

        public Date getAvailable() {
            return available;
        }

        public Builder setAvailable(Date available) {
            this.available = available;
            return this;
        }

        public Date getAtStation() {
            return atStation;
        }

        public Builder setAtStation(Date atStation) {
            this.atStation = atStation;
            return this;
        }

        public ReportResourceDTO build() {
            return new ReportResourceDTO(resource, dispatched, enRoute, onScene, available, atStation);
        }


    }
}
