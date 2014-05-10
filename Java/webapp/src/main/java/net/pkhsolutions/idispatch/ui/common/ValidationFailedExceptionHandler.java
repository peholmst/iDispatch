package net.pkhsolutions.idispatch.ui.common;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import net.pkhsolutions.idispatch.entity.ValidationFailedException;

public final class ValidationFailedExceptionHandler {

    private ValidationFailedExceptionHandler() {
    }

    public static void showValidationErrors(UI ui, ValidationFailedException ex) {
        final Notification notification = new Notification("Validation Failed", String.join("<br/>", ex.getViolationMessages(ui.getLocale())), Notification.Type.WARNING_MESSAGE, true);
        notification.show(ui.getPage());
    }

}
