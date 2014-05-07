package net.pkhsolutions.idispatch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * Entity representing a municipality.
 */
@Entity
@Table(name = "municipalities")
public class Municipality extends AbstractLockableEntity {

    public static final String PROP_NAME = "name";

    @Column(name = "name", nullable = false, unique = true)
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = nullToEmpty(name);
    }
}
