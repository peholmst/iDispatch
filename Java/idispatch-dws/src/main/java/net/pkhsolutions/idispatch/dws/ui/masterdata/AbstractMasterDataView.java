package net.pkhsolutions.idispatch.dws.ui.masterdata;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import net.pkhsolutions.idispatch.ejb.masterdata.Backend;
import net.pkhsolutions.idispatch.ejb.masterdata.ConcurrentModificationException;
import net.pkhsolutions.idispatch.ejb.masterdata.ValidationFailedException;
import net.pkhsolutions.idispatch.entity.AbstractEntity;

/**
 *
 * @author peholmst
 */
public abstract class AbstractMasterDataView<E extends AbstractEntity> extends CustomComponent implements View {

    public class EditorWindow extends Window {

        private VerticalLayout layout;
        private Button save;
        private Button cancel;
        private FieldGroup fieldGroup;
        private E entity;

        public EditorWindow(String caption, E entity) {
            super(caption);
            layout = new VerticalLayout();
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

            FormLayout formLayout = new FormLayout();
            layout.addComponent(formLayout);

            fieldGroup = new FieldGroup();
            setUpFormFields(formLayout, fieldGroup);

            save = new Button("Save and Close", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    save();
                }
            });
            save.setDisableOnClick(true);
            cancel = new Button("Close Without Saving", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    cancel();
                }
            });
            cancel.setDisableOnClick(true);
            HorizontalLayout buttons = new HorizontalLayout(save, cancel);
            buttons.setSpacing(true);
            layout.addComponent(buttons);
            layout.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);

            setResizable(false);
            center();

            if (formLayout.getComponentCount() > 0) {
                ((Focusable) formLayout.getComponent(0)).focus();
            }
            setEntity(entity);
        }

        protected void save() {
            try {
                fieldGroup.commit();
                getBackend().save(entity);
                refresh();
                getUI().removeWindow(this);
            } catch (FieldGroup.CommitException ex) {
                Logger.getLogger(AbstractMasterDataView.class.getCanonicalName()).log(Level.WARNING, "Error committing form", ex);
            } catch (ValidationFailedException ex) {
                setValidationErrors(fieldGroup, ex);
            } catch (ConcurrentModificationException ex) {
                setEntity(getBackend().refresh(entity));
                Notification.show("Concurrent modification detected",
                        "Another user has modified the data you just attempted to save. "
                        + "The data has now been refreshed. Please remake your changes and try again.",
                        Notification.Type.HUMANIZED_MESSAGE);
            } finally {
                save.setEnabled(true);
            }
        }

        protected void cancel() {
            getUI().removeWindow(this);
        }

        private void setEntity(E entity) {
            this.entity = entity;
            fieldGroup.setItemDataSource(new BeanItem<>(entity));
        }
    }
    private VerticalLayout layout;
    private Table table;
    private Button refresh;
    private Button delete;
    private Button add;
    private Button edit;
    private BeanItemContainer<E> container;

    public AbstractMasterDataView() {
        addStyleName("master-data-view");
        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();
        setCompositionRoot(layout);
        setSizeFull();
    }

    @PostConstruct
    protected void init() {
        Label title = new Label(getTitle());
        title.setSizeUndefined();
        title.addStyleName(Reindeer.LABEL_H1);
        layout.addComponent(title);


        container = new BeanItemContainer<>(getEntityClass());
        table = new Table();
        table.setImmediate(true);
        table.setSelectable(true);
        table.setContainerDataSource(container);
        setUpTable(table);
        table.setSizeFull();
        table.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                updateButtonStates();
            }
        });
        layout.addComponent(table);
        layout.setExpandRatio(table, 1);

        refresh = new Button("Refresh", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                refresh();
            }
        });
        refresh.setDisableOnClick(true);

        add = new Button("Add", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                add();
            }
        });
        add.setDisableOnClick(true);

        edit = new Button("Edit", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                edit();
            }
        });
        edit.setDisableOnClick(true);

        delete = new Button("Delete", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                delete();
            }
        });
        delete.setDisableOnClick(true);

        HorizontalLayout buttons = new HorizontalLayout(refresh, add, edit, delete);
        buttons.setWidth("100%");
        buttons.setSpacing(true);
        buttons.setExpandRatio(refresh, 1);
        buttons.setComponentAlignment(add, Alignment.MIDDLE_RIGHT);
        buttons.setComponentAlignment(edit, Alignment.MIDDLE_RIGHT);
        buttons.setComponentAlignment(delete, Alignment.MIDDLE_RIGHT);
        layout.addComponent(buttons);
    }

    protected abstract String getTitle();

    protected EditorWindow createEditorWindow(String caption, E entity) {
        return new EditorWindow(caption, entity);
    }

    protected void setValidationErrors(FieldGroup fieldGroup, ValidationFailedException ex) {
        Map<Object, String> errorMessages = new HashMap<>();

        for (ConstraintViolation<?> violation : ex.getViolations()) {
            String propertyId = violation.getPropertyPath().toString();
            StringBuilder sb = new StringBuilder();
            if (errorMessages.containsKey(propertyId)) {
                sb.append(errorMessages.get(propertyId));
            }
            if (sb.length() > 0) {
                sb.append("<br/>");
            }
            sb.append(violation.getMessage());
            errorMessages.put(propertyId, sb.toString());
        }

        for (Object propertyId : fieldGroup.getBoundPropertyIds()) {
            Field field = fieldGroup.getField(propertyId);
            if (field instanceof AbstractComponent) {
                UserError error = null;
                if (errorMessages.containsKey(propertyId)) {
                    error = new UserError(errorMessages.get(propertyId));
                }
                ((AbstractComponent) field).setComponentError(error);
            }
        }
    }

    protected abstract Backend<E> getBackend();

    protected abstract void setUpFormFields(FormLayout formLayout, FieldGroup fieldGroup);

    protected abstract void setUpTable(Table table);

    protected abstract Class<E> getEntityClass();

    protected abstract E createNewEntity();

    protected abstract E createCopyOfEntity(E entity);

    protected void refresh() {
        try {
            container.removeAllItems();
            container.addAll(getBackend().findAll());
            table.setValue(null);
        } finally {
            refresh.setEnabled(true);
        }
    }

    protected E getSelectedEntity() {
        Object selection = table.getValue();
        if (selection == null) {
            return null;
        } else {
            return container.getItem(selection).getBean();
        }
    }

    protected void add() {
        try {
            EditorWindow window = new EditorWindow("Add", createNewEntity());
            getUI().addWindow(window);
        } finally {
            add.setEnabled(true);
        }
    }

    protected void edit() {
        if (table.getValue() != null) {
            try {
                E entity = getSelectedEntity();
                getBackend().refresh(entity);
                EditorWindow window = new EditorWindow("Edit", createCopyOfEntity(entity));
                getUI().addWindow(window);
            } finally {
                edit.setEnabled(true);
            }
        }
    }

    protected void delete() {
        if (table.getValue() != null) {
            E entity = getSelectedEntity();
            try {
                getBackend().delete(entity);
                container.removeItem(entity);
            } catch (ConcurrentModificationException ex) {
                container.removeItem(entity);
                entity = getBackend().refresh(entity);
                container.addItem(entity);
                Notification.show("Concurrent modification detected",
                        "Another user has modified the data you just attempted to delete. "
                        + "The data has now been refreshed. Please try again if you still wish to delete the data.",
                        Notification.Type.HUMANIZED_MESSAGE);
            } finally {
                delete.setEnabled(true);
            }
        }
    }

    private void updateButtonStates() {
        boolean selected = table.getValue() != null;
        edit.setEnabled(selected);
        delete.setEnabled(selected);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        refresh();
        updateButtonStates();
    }
}
