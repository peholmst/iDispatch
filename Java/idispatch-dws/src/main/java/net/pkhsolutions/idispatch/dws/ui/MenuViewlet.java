package net.pkhsolutions.idispatch.dws.ui;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author peholmst
 */
public class MenuViewlet extends CustomComponent {

    private VerticalLayout layout;

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
        menuItemRegistration.fire(new MenuItemRegistrationEvent(this));
    }

    public void addMenuItem(String caption, final String viewId) {
        HorizontalLayout menuItem = new HorizontalLayout();
        menuItem.setWidth("100%");
        menuItem.addStyleName("menu-item");
        menuItem.addComponent(new Label(caption));
        menuItem.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                getUI().getNavigator().navigateTo(viewId);
            }
        });
        this.layout.addComponent(menuItem);
    }
}
