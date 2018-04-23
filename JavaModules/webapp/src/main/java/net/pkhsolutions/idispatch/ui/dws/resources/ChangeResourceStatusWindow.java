package net.pkhsolutions.idispatch.ui.dws.resources;

import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.boundary.ResourceStatusService;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceState;
import net.pkhsolutions.idispatch.ui.common.resources.ResourceStateToStringConverter;
import net.pkhsolutions.idispatch.ui.dws.DwsTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Window for changing the state of a {@link net.pkhsolutions.idispatch.entity.Resource}.
 */
@VaadinComponent
@UIScope
public class ChangeResourceStatusWindow extends Window {

    @Autowired
    ResourceStatusService resourceStatusService;
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
        resourceStatusService.findCurrentStatus(resource).ifPresent(status -> sortStates(status.getManualValidNextStates()).forEach(this::createResourceStateButton));
        ui.addWindow(this);
        center();
        focus();
    }

    private List<ResourceState> sortStates(Collection<ResourceState> statesCollection) {
        ArrayList<ResourceState> list = new ArrayList<>(statesCollection);
        Collections.sort(list);
        return list;
    }

    private void createResourceStateButton(ResourceState resourceState) {
        final NativeButton stateButton = new NativeButton(resourceStateToStringConverter.convertToPresentation(resourceState, String.class, Locale.getDefault()));
        stateButton.addStyleName("resource-state");
        stateButton.addStyleName("state-" + resourceState.toString().toLowerCase());
        stateButton.setDisableOnClick(true);
        stateButton.addClickListener((event) -> {
            try {
                resourceStatusService.setResourceState(resource, resourceState);
            } finally {
                close();
            }
        });
        stateButton.setWidth("100%");
        stateButtons.addComponent(stateButton);
    }

}
