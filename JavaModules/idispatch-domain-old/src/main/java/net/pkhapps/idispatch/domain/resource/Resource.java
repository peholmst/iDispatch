package net.pkhapps.idispatch.domain.resource;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRoot;
import net.pkhapps.idispatch.domain.base.SupportsSoftDelete;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Aggregate root representing a resource (e.g. a particular rescue unit).
 */
@Entity
@Table(name = "resources")
public class Resource extends AbstractAggregateRoot<ResourceId> implements SupportsSoftDelete {

    @Column(name = "call_sign", unique = true, nullable = false)
    private String callSign = "";

    @Column(name = "type_id", nullable = false)
    private ResourceTypeId type;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @SuppressWarnings("unused")
    private Resource() {
        // Used by JPA only.
    }

    public Resource(@NonNull String callSign, @NonNull ResourceTypeId type) {
        this.callSign = Objects.requireNonNull(callSign);
        this.type = Objects.requireNonNull(type);
    }

    @NonNull
    public String getCallSign() {
        return callSign;
    }

    public void changeCallSign(@NonNull String newCallSign) {
        callSign = Objects.requireNonNull(newCallSign);
    }

    @NonNull
    public ResourceTypeId getType() {
        return type;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void undelete() {
        active = true;
    }

    @Override
    public void delete() {
        active = false;
    }
}
