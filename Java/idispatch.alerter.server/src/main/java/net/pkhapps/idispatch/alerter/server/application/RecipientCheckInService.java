package net.pkhapps.idispatch.alerter.server.application;

import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientId;

import java.time.Instant;
import java.util.Optional;

public interface RecipientCheckInService {

    void checkIn(RecipientId recipient);

    Optional<Instant> getLastCheckInDate(RecipientId recipient);
}
