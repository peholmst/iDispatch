package net.pkhsolutions.idispatch.ui.admin;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.entity.AbstractEntity;
import net.pkhsolutions.idispatch.entity.Deactivatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;

public abstract class AbstractCrudView<E extends AbstractEntity, R extends JpaRepository<E, Long>> extends VerticalLayout implements View {

    // TODO Internationalize

    @Autowired
    ApplicationContext applicationContext;

    private BeanItemContainer<E> container;

    private Table table;

    private Button refresh;

    private Button add;

    private Button edit;

    private Button remove;

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

        buttons.addComponent(refresh = new Button("Refresh", this::refresh));
        buttons.setExpandRatio(refresh, 1f);
        refresh.setDisableOnClick(true);

        buttons.addComponent(add = new Button("Add...", this::add));
        buttons.setComponentAlignment(add, Alignment.MIDDLE_RIGHT);
        add.setDisableOnClick(true);

        buttons.addComponent(edit = new Button("Edit...", this::edit));
        buttons.setComponentAlignment(edit, Alignment.MIDDLE_RIGHT);
        edit.setDisableOnClick(true);

        buttons.addComponent(remove = new Button("Remove", this::remove));
        buttons.setComponentAlignment(remove, Alignment.MIDDLE_RIGHT);
        remove.setDisableOnClick(true);

        updateButtonStates();
    }

    protected abstract String getTitle();

    protected void refresh(Button.ClickEvent event) {
        try {
            container.removeAllItems();
            container.addAll(getRepository().findAll());
            final Optional<E> selection = getSelectedEntity();
            table.setValue(null);
            if (selection.isPresent()) {
                table.setValue(container.getItem(selection.get()).getBean());
            }
        } finally {
            refresh.setEnabled(true);
        }
    }

    protected void add(Button.ClickEvent event) {
        try {
            openCreateWindow(this::save);
        } finally {
            add.setEnabled(true);
        }
    }

    protected void edit(Button.ClickEvent event) {
        try {
            getSelectedEntity().ifPresent(entity -> {
                E clonedEntity = getEntityClass().cast(entity.safeClone().get());
                openEditWindow(clonedEntity, AbstractCrudView.this::save);
            });
        } finally {
            updateButtonStates();
        }
    }

    protected void remove(Button.ClickEvent event) {
        try {
            getSelectedEntity().ifPresent(entity -> {
                getRepository().delete(entity);
                container.removeItem(entity);
            });
        } finally {
            updateButtonStates();
        }
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

    protected Optional<E> getSelectedEntity() {
        return Optional.ofNullable(getEntityClass().cast(table.getValue()));
    }

    protected abstract R getRepository();

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
        getRepository().saveAndFlush(entity);
        refresh(null);
    }

    private void updateButtonStates() {
        boolean hasSelection = getSelectedEntity().isPresent();
        edit.setEnabled(hasSelection);
        remove.setEnabled(hasSelection);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        refresh(null);
    }

    @FunctionalInterface
    protected interface SaveCallback<E> {
        void save(E entity);
    }
}
