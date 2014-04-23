package net.pkhsolutions.idispatch.domain.resources;

import net.pkhsolutions.idispatch.domain.AbstractLockableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * Entity representing a resource type.
 */
@Entity
@Table(name = "resource_types")
public class ResourceType extends AbstractLockableEntity {

    @Column(name = "code", unique = true, nullable = false)
    private String code = "";
    @Column(name = "description", nullable = false)
    private String description;

    protected ResourceType() {
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Builder for creating instances of {@link net.pkhsolutions.idispatch.domain.resources.ResourceType}.
     */
    public static final class Builder extends AbstractLockableEntityBuilder<ResourceType, Builder> {

        public Builder() {
            super(ResourceType.class);
        }

        public Builder(ResourceType original) {
            super(ResourceType.class, original);
            entity.code = original.code;
            entity.description = original.description;
        }

        public Builder withCode(String code) {
            entity.code = nullToEmpty(code);
            return this;
        }

        public Builder withDescription(String description) {
            entity.description = nullToEmpty(description);
            return this;
        }
    }
}
