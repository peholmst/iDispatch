package net.pkhsolutions.idispatch.dws.ui.utils;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Field;
import java.util.HashMap;
import java.util.Map;
import javax.validation.ConstraintViolation;
import net.pkhsolutions.idispatch.ejb.common.ValidationFailedException;

/**
 *
 * @author peholmst
 */
public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static void setValidationErrors(FieldGroup fieldGroup, ValidationFailedException ex) {
        Map<Object, String> errorMessages = new HashMap<>();

        if (ex != null) {
            for (ConstraintViolation<?> violation : ex.getViolations()) {
                String propertyId = violation.getPropertyPath().toString();
                StringBuilder sb = new StringBuilder();
                if (errorMessages.containsKey(propertyId)) {
                    sb.append(errorMessages.get(propertyId));
                }
                if (sb.length() > 0) {
                    sb.append("<br/>");
                }
                sb.append(violation.getMessage());
                errorMessages.put(propertyId, sb.toString());
            }
        }

        for (Object propertyId : fieldGroup.getBoundPropertyIds()) {
            Field field = fieldGroup.getField(propertyId);
            if (field instanceof AbstractComponent) {
                UserError error = null;
                if (errorMessages.containsKey(propertyId)) {
                    error = new UserError(errorMessages.get(propertyId));
                }
                ((AbstractComponent) field).setComponentError(error);
            }
        }
    }
}
