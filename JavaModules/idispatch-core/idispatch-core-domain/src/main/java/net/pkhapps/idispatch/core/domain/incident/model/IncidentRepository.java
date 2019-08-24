package net.pkhapps.idispatch.core.domain.incident.model;

import net.pkhapps.idispatch.core.domain.common.Repository;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * Repository interface for {@link Incident}.
 */
public interface IncidentRepository extends Repository<IncidentId, Incident> {

    @NotNull Stream<Incident> findByParent(@NotNull IncidentId parent);
}
