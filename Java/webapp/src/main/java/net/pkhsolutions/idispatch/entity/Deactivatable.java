package net.pkhsolutions.idispatch.entity;

public interface Deactivatable {

    String PROP_ACTIVE = "active";

    boolean isActive();

    void setActive(boolean active);
}
