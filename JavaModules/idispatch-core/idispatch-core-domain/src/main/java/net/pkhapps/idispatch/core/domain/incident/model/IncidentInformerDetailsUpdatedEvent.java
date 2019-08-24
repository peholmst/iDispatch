package net.pkhapps.idispatch.core.domain.incident.model;

import net.pkhapps.idispatch.core.domain.common.PhoneNumber;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Domain event published when the details of the informer that called in an {@link Incident} have been updated.
 */
public final class IncidentInformerDetailsUpdatedEvent extends IncidentEvent {

    private final String informerName;
    private final PhoneNumber informerPhoneNumber;

    IncidentInformerDetailsUpdatedEvent(@NotNull Incident incident) {
        super(incident);
        informerName = incident.informerName().orElse(null);
        informerPhoneNumber = incident.informerPhoneNumber().orElse(null);
    }

    /**
     * The name of the informer.
     */
    public @NotNull Optional<String> informerName() {
        return Optional.ofNullable(informerName);
    }

    /**
     * The phone number of the informer.
     */
    public @NotNull Optional<PhoneNumber> informerPhoneNumber() {
        return Optional.ofNullable(informerPhoneNumber);
    }
}
