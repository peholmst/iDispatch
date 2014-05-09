package net.pkhsolutions.idispatch.ui.admin;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.boundary.ManagementService;
import net.pkhsolutions.idispatch.entity.AbstractEntity;
import net.pkhsolutions.idispatch.entity.Deactivatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Optional;

public abstract class AbstractCrudView<E extends AbstractEntity, S extends ManagementService<E>> extends VerticalLayout implements View {

    // TODO Internationalize

    @Autowired
    ApplicationContext applicationContext;

    private BeanItemContainer<E> container;

    private Table table;

    private Button refresh;

    private Button add;

    private Button edit;

    private Button delete;

    private Button restore;

    @PostConstruct
    void init() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        final Label title = new Label(getTitle());
        title.addStyleName(AdminTheme.LABEL_H1);
        addComponent(title);

        container = new BeanItemContainer<>(getEntityClass());
        table = new Table();
        table.setImmediate(true);
        table.setSelectable(true);
        table.setSizeFull();
        table.addValueChangeListener(event -> updateButtonStates());
        table.setContainerDataSource(container);
        table.setCellStyleGenerator(this::getCellOrRowStyle);
        configureTable(table);
        addComponent(table);
        setExpandRatio(table, 1f);

        final HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidth("100%");
        buttons.setSpacing(true);
        addComponent(buttons);

        buttons.addComponent(refresh = new Button("Refresh", this::onRefresh));
        buttons.setExpandRatio(refresh, 1f);
        refresh.setDisableOnClick(true);

        buttons.addComponent(add = new Button("Add...", this::onAdd));
        buttons.setComponentAlignment(add, Alignment.MIDDLE_RIGHT);
        add.setDisableOnClick(true);

        buttons.addComponent(edit = new Button("Edit...", this::onEdit));
        buttons.setComponentAlignment(edit, Alignment.MIDDLE_RIGHT);
        edit.setDisableOnClick(true);

        if (supportsDelete()) {
            buttons.addComponent(delete = new Button("Delete", this::onDelete));
            buttons.setComponentAlignment(delete, Alignment.MIDDLE_RIGHT);
            delete.setDisableOnClick(true);
        }
        if (supportsRestore()) {
            buttons.addComponent(restore = new Button("Restore", this::onRestore));
            buttons.setComponentAlignment(restore, Alignment.MIDDLE_RIGHT);
            restore.setDisableOnClick(true);
        }

        updateButtonStates();
    }

    protected abstract String getTitle();

    protected void onRefresh(Button.ClickEvent event) {
        try {
            refresh();
        } finally {
            refresh.setEnabled(true);
        }
    }

    protected void refresh() {
        container.removeAllItems();
        container.addAll(getManagementService().findAll());
        final Optional<E> selection = getSelectedEntity();
        table.setValue(null);
        if (selection.isPresent()) {
            table.setValue(container.getItem(selection.get()).getBean());
        }
    }

    protected void onAdd(Button.ClickEvent event) {
        try {
            openCreateWindow(this::save);
        } finally {
            add.setEnabled(true);
        }
    }

    protected void onEdit(Button.ClickEvent event) {
        try {
            getSelectedEntity().ifPresent(entity -> {
                E clonedEntity = getEntityClass().cast(entity.safeClone().get());
                openEditWindow(clonedEntity, AbstractCrudView.this::save);
            });
        } finally {
            updateButtonStates();
        }
    }

    protected void onDelete(Button.ClickEvent event) {
        try {
            getSelectedEntity().ifPresent(this::delete);
        } finally {
            updateButtonStates();
        }
    }

    @SuppressWarnings("unchecked")
    protected void delete(E entity) {
        final S service = getManagementService();
        if (service instanceof ManagementService.SoftDeletable) {
            ((ManagementService.SoftDeletable) service).delete(entity);
        } else if (service instanceof ManagementService.HardDeletable) {
            if (!((ManagementService.HardDeletable) service).delete(entity)) {
                Notification.show("The selected item could not be deleted");
            }
        }
        refresh();
    }

    protected void onRestore(Button.ClickEvent event) {
        try {
            getSelectedEntity().ifPresent(this::restore);
        } finally {
            updateButtonStates();
        }
    }

    @SuppressWarnings("unchecked")
    protected void restore(E entity) {
        final S service = getManagementService();
        if (service instanceof ManagementService.SoftDeletable) {
            ((ManagementService.SoftDeletable) service).restore(entity);
        }
        refresh();
    }

    protected String getCellOrRowStyle(Table source, Object itemId,
                                       Object propertyId) {
        if (itemId instanceof Deactivatable) {
            return ((Deactivatable) itemId).isActive() ? "active" : "passive";
        } else {
            return null;
        }
    }

    protected void openWindow(String title, Component content) {
        final Window window = new Window(title, content);
        window.setSizeUndefined();
        window.center();
        window.setResizable(false);
        window.setModal(true);
        getUI().addWindow(window);
    }

    protected boolean supportsDelete() {
        return (getManagementService() instanceof ManagementService.HardDeletable) || (getManagementService() instanceof ManagementService.SoftDeletable);
    }

    protected boolean supportsRestore() {
        return getManagementService() instanceof ManagementService.SoftDeletable;
    }

    protected Optional<E> getSelectedEntity() {
        return Optional.ofNullable(getEntityClass().cast(table.getValue()));
    }

    protected abstract S getManagementService();

    protected abstract Class<E> getEntityClass();

    protected abstract void openCreateWindow(SaveCallback<E> saveCallback);

    protected abstract void openEditWindow(E entity, SaveCallback<E> saveCallback);

    protected abstract void configureTable(Table table);

    protected <F extends AbstractForm<E>> F createForm(Class<F> formClass, E entity, SaveCallback<? super E> saveCallback) {
        final F form = applicationContext.getBean(formClass);
        form.init(entity, saveCallback);
        return form;
    }

    protected void save(E entity) {
        getManagementService().save(entity);
        refresh();
    }

    private void updateButtonStates() {
        boolean hasSelection = getSelectedEntity().isPresent();
        edit.setEnabled(hasSelection);
        if (restore != null) {
            if (hasSelection) {
                final boolean isActive = ((Deactivatable) getSelectedEntity().get()).isActive();
                restore.setEnabled(!isActive);
                delete.setEnabled(isActive);
            } else {
                restore.setEnabled(false);
                delete.setEnabled(false);
            }
        } else if (delete != null) {
            delete.setEnabled(hasSelection);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        onRefresh(null);
    }

    @FunctionalInterface
    protected interface SaveCallback<E> {
        void save(E entity);
    }
}
