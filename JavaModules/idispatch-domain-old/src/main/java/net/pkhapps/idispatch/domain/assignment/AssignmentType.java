package net.pkhapps.idispatch.domain.assignment;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRoot;
import net.pkhapps.idispatch.domain.base.SupportsSoftDelete;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Entity representing a assignment type (e.g. "structure fire", "motor vehicle accident").
 */
@Entity
@Table(name = "assignment_types")
public class AssignmentType extends AbstractAggregateRoot<AssignmentTypeId> implements SupportsSoftDelete {

    @Column(name = "code", unique = true, nullable = false)
    private String code = "";

    @Column(name = "description", nullable = false)
    private String description = "";

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @SuppressWarnings("unused")
    private AssignmentType() {
        // Used by JPA only.
    }

    public AssignmentType(@NonNull String code) {
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

    @NonNull
    public String getFormattedDescription() {
        return String.format("%s - %s", code, description);
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
