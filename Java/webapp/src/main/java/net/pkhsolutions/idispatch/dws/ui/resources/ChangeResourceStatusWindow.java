package net.pkhsolutions.idispatch.dws.ui.resources;

import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.common.ui.resources.ResourceStateToStringConverter;
import net.pkhsolutions.idispatch.domain.resources.Resource;
import net.pkhsolutions.idispatch.domain.resources.ResourceService;
import net.pkhsolutions.idispatch.domain.resources.ResourceState;
import net.pkhsolutions.idispatch.dws.ui.DwsTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;

import javax.annotation.PostConstruct;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Window for changing the state of a {@link net.pkhsolutions.idispatch.domain.resources.Resource}.
 */
@VaadinComponent
@UIScope
public class ChangeResourceStatusWindow extends Window {

    @Autowired
    ResourceService resourceService;
    @Autowired
    ResourceStateToStringConverter resourceStateToStringConverter;

    private Resource resource;
    private Label resourceName;
    private VerticalLayout stateButtons;
    private Button cancel;

    @PostConstruct
    void init() {
        // TODO Internationalize
        setModal(true);
        setResizable(false);
        setCaption("Change State");
        center();

        setContent(new VerticalLayout() {{
            setMargin(true);
            setSpacing(true);
            setWidth("200px");

            resourceName = new Label();
            resourceName.addStyleName(DwsTheme.LABEL_H2);
            addComponent(resourceName);

            stateButtons = new VerticalLayout();
            stateButtons.setSpacing(true);
            addComponent(stateButtons);

            cancel = new Button("Cancel", (event) -> close());
            addComponent(cancel);
            setComponentAlignment(cancel, Alignment.BOTTOM_RIGHT);
        }});
    }

    /**
     * Shows the Change Resource Status window for the specified resource.
     */
    public void open(UI ui, Resource resource) {
        this.resource = checkNotNull(resource);
        resourceName.setValue(resource.getCallSign());
        stateButtons.removeAllComponents();
        resourceService.getPossibleResourceStateTransitions(resource).forEach(this::createResourceStateButton);
        ui.addWindow(this);
    }

    private void createResourceStateButton(ResourceState resourceState) {
        final Button stateButton = new Button(resourceStateToStringConverter.convertToPresentation(resourceState, String.class, Locale.getDefault()));
        stateButton.addStyleName("state-" + resourceState.toString().toLowerCase());
        stateButton.setDisableOnClick(true);
        stateButton.addClickListener((event) -> {
            try {
                resourceService.setResourceState(resource, resourceState);
            } finally {
                close();
            }
        });
        stateButton.setWidth("100%");
        stateButtons.addComponent(stateButton);
    }

}
