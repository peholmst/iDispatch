/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.domain.common.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import net.pkhsolutions.idispatch.domain.AbstractEntity;

/**
 * Entity representing a municipality.
 *
 * @author Petter Holmström
 * @since 1.0
 */
@Entity
public class Municipality extends AbstractEntity {

    @Column(unique = true, nullable = false)
    @Basic(optional = false)
    @NotNull
    private String name;
    private boolean active = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
