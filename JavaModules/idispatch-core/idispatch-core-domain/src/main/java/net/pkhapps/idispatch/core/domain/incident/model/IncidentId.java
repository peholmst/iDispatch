package net.pkhapps.idispatch.core.domain.incident.model;

import net.pkhapps.idispatch.core.domain.common.WrappedIdentifier;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me
 */
public class IncidentId extends WrappedIdentifier {

    /**
     * @param identifier
     */
    public IncidentId(@NotNull Object identifier) {
        super(identifier);
    }
}
