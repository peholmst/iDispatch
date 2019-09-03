package net.pkhapps.idispatch.core.domain.incident.model;

import net.pkhapps.idispatch.core.domain.common.AggregateRoot;
import net.pkhapps.idispatch.core.domain.i18n.MultilingualString;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Implement & document me
 */
public class IncidentType extends AggregateRoot<IncidentTypeId> {

    private MultilingualString name;

    IncidentType(@NotNull IncidentTypeId incidentTypeId) {
        super(incidentTypeId);
    }

    IncidentType(@NotNull Essence essence) {
        super(essence);
    }

    public static class Essence extends AggregateRoot.Essence<IncidentTypeId> {

        Essence() {
        }

        Essence(@NotNull IncidentType source) {
            super(source);
        }
    }
}
