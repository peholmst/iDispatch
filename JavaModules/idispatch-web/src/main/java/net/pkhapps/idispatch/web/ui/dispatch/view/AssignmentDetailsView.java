package net.pkhapps.idispatch.web.ui.dispatch.view;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import net.pkhapps.idispatch.web.ui.common.i18n.I18N;
import net.pkhapps.idispatch.web.ui.dispatch.DispatchTheme;
import net.pkhapps.idispatch.web.ui.dispatch.annotation.DispatchQualifier;
import net.pkhapps.idispatch.web.ui.dispatch.model.AssignmentModel;
import net.pkhapps.idispatch.web.ui.dispatch.navigation.AssignmentDetailsViewNavigationRequest;

import javax.annotation.PostConstruct;

/**
 * TODO document me
 */
@SpringView(name = AssignmentDetailsViewNavigationRequest.VIEW_NAME)
class AssignmentDetailsView extends VerticalLayout implements ViewWithTitle { // TODO Implement me

    private final I18N i18n;
    private final AssignmentModel model;
    private final AssignmentDetailsForm form;
    private final AssignmentResourceList resourceList;

    AssignmentDetailsView(@DispatchQualifier I18N i18n,
                          AssignmentModel model,
                          AssignmentDetailsForm form,
                          AssignmentResourceList resourceList) {
        this.i18n = i18n;
        this.model = model;
        this.form = form;
        this.resourceList = resourceList;
    }

    @PostConstruct
    void init() {
        setSizeFull();
        addStyleName(DispatchTheme.VIEW);
        addComponent(form);
        addComponentsAndExpand(resourceList);

        Button selectAll = new Button(i18n.get("assignmentDetailsView.selectAll.caption"));
        Button selectUndispatched = new Button(i18n.get("assignmentDetailsView.selectUndispatched.caption"));
        Button dispatch = new Button(i18n.get("assignmentDetailsView.dispatch.caption"));
        dispatch.addStyleName(DispatchTheme.BUTTON_FRIENDLY);

        HorizontalLayout dispatchButtons = new HorizontalLayout();
        dispatchButtons.setMargin(false);

        dispatchButtons.addComponent(selectAll);
        dispatchButtons.addComponent(selectUndispatched);
        dispatchButtons.addComponent(dispatch);
        addComponent(dispatchButtons);
        setComponentAlignment(dispatchButtons, Alignment.BOTTOM_RIGHT);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        AssignmentDetailsViewNavigationRequest request = new AssignmentDetailsViewNavigationRequest(event);
        if (request.getAssignmentId() != null) {
            model.loadAssignmentIntoModel(request.getAssignmentId());
        }
        if (model.isEmpty()) {
            // TODO Show error
        }
    }

    @Override
    public String getTitle() {
        return i18n.get("assignmentDetailsView.title");
    }
}
