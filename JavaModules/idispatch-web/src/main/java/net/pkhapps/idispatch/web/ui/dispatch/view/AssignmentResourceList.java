package net.pkhapps.idispatch.web.ui.dispatch.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.VerticalLayout;
import net.pkhapps.idispatch.web.ui.dispatch.model.AssignmentModel;

import javax.annotation.PostConstruct;

@SpringComponent
@ViewScope
class AssignmentResourceList extends VerticalLayout {

    private final AssignmentModel model;

    AssignmentResourceList(AssignmentModel model) {
        this.model = model;
    }

    @PostConstruct
    void init() {

    }
}
