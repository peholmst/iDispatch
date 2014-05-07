package net.pkhsolutions.idispatch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TODO Document me!
 */
@Entity
@Table(name = "runboard_destinations")
public class RunboardDestination extends Destination {

    @Column(name = "runboard_key", nullable = false, unique = true)
    private String runboardKey = "";

    public String getRunboardKey() {
        return runboardKey;
    }

    public void setRunboardKey(String runboardKey) {
        this.runboardKey = runboardKey;
    }
}
