package net.pkhapps.idispatch.web.ui.dispatch.converter;

import com.vaadin.data.ValueContext;
import net.pkhapps.idispatch.domain.assignment.AssignmentState;
import net.pkhapps.idispatch.web.ui.common.converter.OneWayToStringConverter;
import net.pkhapps.idispatch.web.ui.common.i18n.I18N;
import net.pkhapps.idispatch.web.ui.dispatch.provider.AssignmentStateValueProvider;
import org.springframework.lang.NonNull;

/**
 * TODO Document and test me
 */
public class AssignmentStateToStringConverter extends OneWayToStringConverter<AssignmentState> {

    private final AssignmentStateValueProvider valueProvider;

    public AssignmentStateToStringConverter(@NonNull I18N i18n) {
        valueProvider = new AssignmentStateValueProvider(i18n);
    }

    @Override
    public String convertToPresentation(AssignmentState value, ValueContext context) {
        return valueProvider.apply(value);
    }
}
