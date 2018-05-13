package net.pkhapps.idispatch.web.ui.dispatch.viewlet;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.StyleGenerator;
import com.vaadin.ui.VerticalLayout;
import net.pkhapps.idispatch.application.overview.ResourceOverviewDTO;
import net.pkhapps.idispatch.web.ui.common.i18n.I18N;
import net.pkhapps.idispatch.web.ui.common.ViewletTitle;
import net.pkhapps.idispatch.web.ui.dispatch.DispatchTheme;
import net.pkhapps.idispatch.web.ui.dispatch.annotation.DispatchQualifier;
import net.pkhapps.idispatch.web.ui.dispatch.model.ResourceOverviewDataProvider;
import net.pkhapps.idispatch.web.ui.dispatch.model.ResourceOverviewModel;
import net.pkhapps.idispatch.web.ui.dispatch.provider.ResourceStateStyleProvider;
import net.pkhapps.idispatch.web.ui.dispatch.provider.ResourceStateValueProvider;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Viewlet that shows all the resources in the database, their current state and current assignment.
 * TODO: What can you do in this viewlet?
 */
@SpringComponent
@UIScope
public class ResourcesViewlet extends CustomComponent {

    private static final String COL_CALL_SIGN = "callSign";
    private static final String COL_STATE = "state";
    private static final String COL_ASSIGNMENT = "assignment";

    private final ResourceOverviewModel model;
    private final I18N i18n;

    private Grid<ResourceOverviewDTO> grid;
    private ResourceOverviewDataProvider dataProvider;

    ResourcesViewlet(ResourceOverviewModel model,
                     @DispatchQualifier I18N i18n) {
        this.model = model;
        this.i18n = i18n;
    }

    @PostConstruct
    void init() {
        addStyleName(DispatchTheme.VIEWLET);

        dataProvider = new ResourceOverviewDataProvider();
        dataProvider.registerWithModel(model);

        grid = new Grid<>(dataProvider);
        grid.setSizeFull();
        grid.addColumn(ResourceOverviewDTO::getCallSign)
                .setCaption(i18n.get("resourcesViewlet.columns.resource.caption"))
                .setId(COL_CALL_SIGN);
        grid.addColumn(ResourceOverviewDTO::getState, new ResourceStateValueProvider(i18n))
                .setCaption(i18n.get("resourcesViewlet.columns.state.caption"))
                .setId(COL_STATE);
        grid.addColumn(ResourceOverviewDTO::getAssignmentSummary)
                .setCaption(i18n.get("resourcesViewlet.columns.assignment.caption"))
                .setId(COL_ASSIGNMENT);

        grid.setStyleGenerator(new StyleGenerator<ResourceOverviewDTO>() {
            private final ResourceStateStyleProvider styleProvider = new ResourceStateStyleProvider();

            @Override
            public String apply(ResourceOverviewDTO item) {
                return styleProvider.apply(item.getState());
            }
        });
        grid.sort(COL_CALL_SIGN);

        setSizeFull();

        VerticalLayout root = new VerticalLayout();
        root.setMargin(false);
        root.setSpacing(false);

        ViewletTitle title = new ViewletTitle(i18n.get("resourcesViewlet.title"));
        root.addComponent(title);

        root.addComponentsAndExpand(grid);
        setCompositionRoot(root);
    }

    @PreDestroy
    void destroy() {
        dataProvider.unregisterFromModel();
    }
}
