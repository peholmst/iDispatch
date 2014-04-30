package net.pkhsolutions.idispatch.domain;

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

    /**
     * Builder for creating instances of {@link net.pkhsolutions.idispatch.domain.Municipality}.
     */
    public static final class Builder extends AbstractLockableEntityBuilder<Municipality, Builder> {

        public Builder() {
            super(Municipality.class);
        }

        public Builder(Municipality original) {
            super(Municipality.class, original);
            entity.name = original.name;
        }

        public Builder withName(String name) {
            entity.name = nullToEmpty(name);
            return this;
        }
    }
}
