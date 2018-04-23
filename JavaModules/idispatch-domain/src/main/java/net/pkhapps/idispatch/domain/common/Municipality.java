package net.pkhapps.idispatch.domain.common;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRoot;
import net.pkhapps.idispatch.domain.base.SupportsSoftDelete;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Aggregate root representing a municipality.
 */
@Entity
@Table(name = "municipalities")
public class Municipality extends AbstractAggregateRoot<MunicipalityId> implements SupportsSoftDelete {

    @Column(name = "name", nullable = false, unique = true)
    private String name = "";

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @SuppressWarnings("unused")
    private Municipality() {
        // Used by JPA only.
    }

    public Municipality(@NonNull String name) {
        this.name = Objects.requireNonNull(name);
    }

    @NonNull
    public String getName() {
        return name;
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
