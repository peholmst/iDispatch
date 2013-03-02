package net.pkhsolutions.idispatch.dws.ui.masterdata;

import com.github.peholmst.i18n4vaadin.annotations.Message;
import com.github.peholmst.i18n4vaadin.annotations.Messages;
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
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import net.pkhsolutions.idispatch.dws.ui.utils.ValidationUtils;
import net.pkhsolutions.idispatch.ejb.masterdata.Backend;
import net.pkhsolutions.idispatch.ejb.common.ConcurrentModificationException;
import net.pkhsolutions.idispatch.ejb.common.DeletionFailedException;
import net.pkhsolutions.idispatch.ejb.common.SaveFailedException;
import net.pkhsolutions.idispatch.ejb.common.ValidationFailedException;
import net.pkhsolutions.idispatch.entity.AbstractEntity;

/**
 *
 * @author peholmst
 */
@Messages({
    @Message(key = "saveAndClose", value = "Spara och stäng"),
    @Message(key = "closeWithoutSaving", value = "Stäng utan att spara"),
    @Message(key = "concurrentModification.message.update", value = "En annan användare har ändrat informationen du just försökte spara. Informationen har nu uppdaterats. Gör om dina ändringar och försök igen."),
    @Message(key = "saveFailed.caption", value = "Informationen kunde inte sparas"),
    @Message(key = "saveFailed.message", value = "Följande meddelande rapporterades från databasen: {0}")
})
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

            save = new Button(bundle.saveAndClose(), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    save();
                }
            });
            save.setDisableOnClick(true);
            cancel = new Button(bundle.closeWithoutSaving(), new Button.ClickListener() {
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
                ValidationUtils.setValidationErrors(fieldGroup, ex);
            } catch (ConcurrentModificationException ex) {
                setEntity(getBackend().refresh(entity));
                Notification.show(bundle.concurrentModification_caption(),
                        bundle.concurrentModification_message_update(),
                        Notification.Type.HUMANIZED_MESSAGE);
            } catch (SaveFailedException ex) {
                Notification.show(bundle.saveFailed_caption(),
                        bundle.saveFailed_message(ex.getMessage()),
                        Notification.Type.WARNING_MESSAGE);
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
    @Inject
    private AbstractMasterDataViewBundle bundle;

    public AbstractMasterDataView() {
        addStyleName("master-data-view");
        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();
        setCompositionRoot(layout);
        setSizeFull();
    }

    @Messages({
        @Message(key = "refresh", value = "Uppdatera"),
        @Message(key = "add", value = "Lägg till"),
        @Message(key = "edit", value = "Redigera"),
        @Message(key = "delete", value = "Ta bort")
    })
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

        refresh = new Button(bundle.refresh(), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                refresh();
            }
        });
        refresh.setDisableOnClick(true);

        add = new Button(bundle.add(), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                add();
            }
        });
        add.setDisableOnClick(true);

        edit = new Button(bundle.edit(), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                edit();
            }
        });
        edit.setDisableOnClick(true);

        delete = new Button(bundle.delete(), new Button.ClickListener() {
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
            table.sort();
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
            EditorWindow window = new EditorWindow(bundle.add(), createNewEntity());
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
                EditorWindow window = new EditorWindow(bundle.edit(), createCopyOfEntity(entity));
                getUI().addWindow(window);
            } finally {
                edit.setEnabled(true);
            }
        }
    }

    @Messages({
        @Message(key = "concurrentModification.caption", value = "Samtidig uppdatering upptäckt"),
        @Message(key = "concurrentModification.message.delete", value = "En annan användare har uppdaterat informationen du just försökte ta bort. Informationen har nu uppdaterats. Försök igen om du fortfarande fill ta bort informationen."),
        @Message(key = "deletionFailed.caption", value = "Informationen kunde inte tas bort"),
        @Message(key = "deletionFailed.message", value = "Följande meddelande rapporterades från databasen: {0}")
    })
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
                Notification.show(bundle.concurrentModification_caption(),
                        bundle.concurrentModification_message_delete(),
                        Notification.Type.HUMANIZED_MESSAGE);
            } catch (DeletionFailedException ex) {
                Notification.show(bundle.deletionFailed_caption(),
                        bundle.deletionFailed_message(ex.getMessage()),
                        Notification.Type.WARNING_MESSAGE);
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
