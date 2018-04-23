package net.pkhsolutions.idispatch.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * Entity representing a municipality.
 */
@Entity
@Table(name = "municipalities")
public class Municipality extends AbstractLockableEntity implements Deactivatable {

    public static final String PROP_NAME = "name";

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank(message = "Please enter a name")
    private String name = "";
    @Column(name = "active", nullable = false)
    private boolean active = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = nullToEmpty(name);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}
