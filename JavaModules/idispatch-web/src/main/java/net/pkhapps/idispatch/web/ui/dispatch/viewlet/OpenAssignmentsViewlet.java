package net.pkhapps.idispatch.web.ui.dispatch.viewlet;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import net.pkhapps.idispatch.application.overview.AssignmentOverviewDTO;
import net.pkhapps.idispatch.web.ui.common.I18N;
import net.pkhapps.idispatch.web.ui.common.ViewletTitle;
import net.pkhapps.idispatch.web.ui.dispatch.DispatchTheme;
import net.pkhapps.idispatch.web.ui.dispatch.annotation.DispatchQualifier;
import net.pkhapps.idispatch.web.ui.dispatch.model.AssignmentOverviewDataProvider;
import net.pkhapps.idispatch.web.ui.dispatch.model.AssignmentOverviewModel;
import net.pkhapps.idispatch.web.ui.dispatch.provider.AssignmentStateStyleProvider;
import net.pkhapps.idispatch.web.ui.dispatch.provider.AssignmentStateValueProvider;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Viewlet that shows all the open assignments in the database.
 * TODO: What can you do in this viewlet?
 */
@UIScope
@SpringComponent
public class OpenAssignmentsViewlet extends CustomComponent {

    private static final String COL_ASSIGNMENT_TYPE_CODE = "assignmentTypeCode";
    private static final String COL_STATE = "state";
    private static final String COL_MUNICIPALITY_NAME = "municipalityName";
    private static final String COL_ADDRESS = "address";

    private final AssignmentOverviewModel model;
    private final I18N i18n;

    private Grid<AssignmentOverviewDTO> grid;
    private AssignmentOverviewDataProvider dataProvider;

    OpenAssignmentsViewlet(AssignmentOverviewModel model,
                           @DispatchQualifier I18N i18n) {
        this.model = model;
        this.i18n = i18n;
    }

    @PostConstruct
    void init() {
        addStyleName(DispatchTheme.VIEWLET);

        dataProvider = new AssignmentOverviewDataProvider();
        dataProvider.registerWithModel(model);

        grid = new Grid<>(dataProvider);
        grid.setSizeFull();
        grid.addColumn(AssignmentOverviewDTO::getAssignmentTypeCode)
                .setCaption(i18n.get("openAssignmentsViewlet.columns.type.caption"))
                .setId(COL_ASSIGNMENT_TYPE_CODE);
        grid.addColumn(AssignmentOverviewDTO::getState, new AssignmentStateValueProvider(i18n))
                .setCaption(i18n.get("openAssignmentsViewlet.columns.state.caption"))
                .setId(COL_STATE);
        grid.addColumn(AssignmentOverviewDTO::getMunicipalityName)
                .setCaption(i18n.get("openAssignmentsViewlet.columns.municipality.caption"))
                .setId(COL_MUNICIPALITY_NAME);
        grid.addColumn(AssignmentOverviewDTO::getAddress)
                .setCaption(i18n.get("openAssignmentsViewlet.columns.address.caption"))
                .setId(COL_ADDRESS);

        grid.setStyleGenerator(new StyleGenerator<AssignmentOverviewDTO>() {
            private final AssignmentStateStyleProvider styleProvider = new AssignmentStateStyleProvider();

            @Override
            public String apply(AssignmentOverviewDTO item) {
                return styleProvider.apply(item.getState());
            }
        });
        grid.sort(COL_STATE);

        setSizeFull();

        VerticalLayout root = new VerticalLayout();
        root.setMargin(false);
        root.setSpacing(false);

        ViewletTitle title = new ViewletTitle(i18n.get("openAssignmentsViewlet.title"));

        Button newAssignment = new Button(i18n.get("openAssignmentsViewlet.newAssignment.caption"));
        newAssignment.setDescription(i18n.get("openAssignmentsViewlet.newAssignment.description"));
        newAssignment.addStyleName(DispatchTheme.BUTTON_FRIENDLY);
        newAssignment.addStyleName(DispatchTheme.BUTTON_TINY);
        // TODO Click listener
        title.addComponent(newAssignment);

        Button searchAssignment = new Button(VaadinIcons.SEARCH);
        searchAssignment.setDescription(i18n.get("openAssignmentsViewlet.searchAssignment.description"));
        searchAssignment.addStyleName(DispatchTheme.BUTTON_TINY);
        // TODO Click listener
        title.addComponent(searchAssignment);

        root.addComponent(title);

        root.addComponentsAndExpand(grid);
        setCompositionRoot(root);
    }

    @PreDestroy
    void destroy() {
        dataProvider.unregisterFromModel();
    }
}
