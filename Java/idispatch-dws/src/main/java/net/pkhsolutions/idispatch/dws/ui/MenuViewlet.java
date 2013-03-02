package net.pkhsolutions.idispatch.dws.ui;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Resource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author peholmst
 */
public class MenuViewlet extends CustomComponent {

    private VerticalLayout layout;
    private boolean initializing = false;
    private List<VerticalLayout> menuItems = new ArrayList<>();

    public static class MenuItemRegistrationEvent {

        private final MenuViewlet menu;

        public MenuItemRegistrationEvent(MenuViewlet menu) {
            this.menu = menu;
        }

        public MenuViewlet getMenu() {
            return menu;
        }
    }
    @Inject
    javax.enterprise.event.Event<MenuItemRegistrationEvent> menuItemRegistration;

    public MenuViewlet() {
        addStyleName("menu-viewlet");
        layout = new VerticalLayout();
        setCompositionRoot(layout);
    }

    @PostConstruct
    protected void init() {
        initializing = true;
        try {
            menuItemRegistration.fire(new MenuItemRegistrationEvent(this));
        } finally {
            sortAndAddItems();
            initializing = false;
        }
    }

    public void addMenuItem(final String caption, final String viewId) {
        addMenuItem(caption, viewId, null, 0);
    }

    public void addMenuItem(final String caption, final String viewId, final Resource icon, final int order) {
        if (!initializing) {
            throw new IllegalStateException("Menu items can currently only be added when the menu is being initialized");
        }
        VerticalLayout menuItem = new VerticalLayout();
        menuItem.setWidth("100%");
        menuItem.addStyleName("menu-item");
        if (icon != null) {
            menuItem.addComponent(new Image(null, icon));
        }
        menuItem.addComponent(new Label(caption));
        menuItem.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                getUI().getNavigator().navigateTo(viewId);
            }
        });
        menuItem.setData(order);
        menuItems.add(menuItem);
    }

    private void sortAndAddItems() {
        Collections.sort(menuItems, new Comparator<VerticalLayout>() {
            @Override
            public int compare(VerticalLayout o1, VerticalLayout o2) {
                return ((Integer) o1.getData()).compareTo((Integer) o2.getData());
            }
        });
        for (VerticalLayout menuItem : menuItems) {
            layout.addComponent(menuItem);
        }
    }
}
