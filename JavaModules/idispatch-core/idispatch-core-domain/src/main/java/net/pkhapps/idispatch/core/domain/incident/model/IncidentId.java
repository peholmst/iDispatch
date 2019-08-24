package net.pkhapps.idispatch.core.domain.incident.model;

import net.pkhapps.idispatch.core.domain.common.ValueObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me
 */
public class IncidentId implements ValueObject {

    private final String id;

    private IncidentId(@NotNull String id) {
        this.id = requireNonNull(id);
    }

    /**
     * @return
     */
    public static @NotNull IncidentId random() {
        return new IncidentId(UUID.randomUUID().toString());
    }

    /**
     * @param id
     * @return
     */
    public static @NotNull IncidentId fromString(@NotNull String id) {
        return new IncidentId(id);
    }

    /**
     * @param id
     * @return
     */
    public static @NotNull IncidentId fromLong(@NotNull Long id) {
        return new IncidentId(String.valueOf(id));
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IncidentId that = (IncidentId) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
