package net.pkhapps.idispatch.web.ui.dispatch.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.VerticalLayout;
import net.pkhapps.idispatch.web.ui.common.I18N;
import net.pkhapps.idispatch.web.ui.dispatch.annotation.DispatchQualifier;

import javax.annotation.PostConstruct;

/**
 * TODO document me
 */
@SpringView(name = "assignment")
class AssignmentDetailsView extends VerticalLayout implements View { // TODO Implement me

    private final I18N i18n;
    private final AssignmentDetailsForm form;

    AssignmentDetailsView(@DispatchQualifier I18N i18n, AssignmentDetailsForm form) {
        this.i18n = i18n;
        this.form = form;
    }

    @PostConstruct
    void init() {
        setSizeFull();
        addComponent(form);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
