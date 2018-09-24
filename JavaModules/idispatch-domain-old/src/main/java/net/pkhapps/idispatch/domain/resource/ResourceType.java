package net.pkhapps.idispatch.domain.resource;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRoot;
import net.pkhapps.idispatch.domain.base.SupportsSoftDelete;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Aggregate root representing a resource type (e.g. "pumper", "command unit", "water tender", etc.).
 */
@Entity
@Table(name = "resource_types")
public class ResourceType extends AbstractAggregateRoot<ResourceTypeId> implements SupportsSoftDelete {

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "description", nullable = false)
    private String description = "";

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @SuppressWarnings("unused")
    private ResourceType() {
        // Used by JPA only.
    }

    public ResourceType(@NonNull String code) {
        this.code = Objects.requireNonNull(code);
    }

    @NonNull
    public String getCode() {
        return code;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void changeDescription(@NonNull String newDescription) {
        description = Objects.requireNonNull(newDescription);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void undelete() {
        this.active = true;
    }

    @Override
    public void delete() {
        this.active = false;
    }
}
