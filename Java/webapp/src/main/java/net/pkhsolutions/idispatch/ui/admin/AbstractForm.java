package net.pkhsolutions.idispatch.ui.admin;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.entity.AbstractEntity;
import net.pkhsolutions.idispatch.entity.ValidationFailedException;
import net.pkhsolutions.idispatch.ui.common.ValidationFailedExceptionHandler;

import javax.annotation.PostConstruct;

public abstract class AbstractForm<E extends AbstractEntity> extends VerticalLayout {

    // TODO Internationalize

    private BeanFieldGroup<E> binder;
    private Button save;
    private Button cancel;
    private AbstractCrudView.SaveCallback<? super E> saveCallback;

    @PostConstruct
    void init() {
        setSpacing(true);
        setMargin(true);

        final FormLayout formLayout = new FormLayout();
        createAndAddFields(formLayout);
        addComponent(formLayout);
        binder = new BeanFieldGroup<>(getEntityType());

        final HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        addComponent(buttons);
        setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

        buttons.addComponent(save = new Button("Save & Close", this::save));
        save.setDisableOnClick(true);
        buttons.addComponent(cancel = new Button("Discard Changes & Close", this::cancel));
        cancel.setDisableOnClick(true);
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    }

    protected abstract Class<E> getEntityType();

    protected abstract void createAndAddFields(FormLayout formLayout);

    public void init(E entity, AbstractCrudView.SaveCallback<? super E> saveCallback) {
        binder.setItemDataSource(entity);
        binder.bindMemberFields(this);
        this.saveCallback = saveCallback;
    }

    protected void save(Button.ClickEvent event) {
        try {
            binder.commit();
            saveCallback.save(binder.getItemDataSource().getBean());
            closeWindow();
        } catch (FieldGroup.CommitException e) {
            Notification.show("The changes could not be saved");
        } catch (ValidationFailedException e) {
            ValidationFailedExceptionHandler.showValidationErrors(getUI(), e);
        } finally {
            save.setEnabled(true);
        }
    }

    protected void closeWindow() {
        closeIfWindow(getParent());
    }

    private void closeIfWindow(Component parent) {
        if (parent instanceof Window) {
            ((Window) parent).close();
        } else if (parent != null) {
            closeIfWindow(parent.getParent());
        }
    }

    protected void cancel(Button.ClickEvent event) {
        closeWindow();
    }
}
