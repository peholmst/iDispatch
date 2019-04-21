package net.pkhapps.idispatch.alerter.server.application;

import net.pkhapps.idispatch.alerter.server.domain.alert.Alert;
import net.pkhapps.idispatch.alerter.server.domain.recipient.Recipient;

public interface SingleRecipientAlertDispatcher {

    boolean supports(Class<? extends Recipient> recipientClass);

    void dispatch(Alert alert, Recipient<?> recipient);
}
