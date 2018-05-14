package net.pkhapps.idispatch.web.ui.common.binding;

import com.vaadin.server.SerializableConsumer;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import net.pkhapps.idispatch.web.ui.common.model.Action;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * Binding that binds a Vaadin {@link Button} and an {@link Action} together.
 */
public class ButtonBinding implements Binding {

    private final Button button;
    private Registration registration;
    private Action<?> action;

    private ButtonBinding(@NonNull Button button) {
        this.button = Objects.requireNonNull(button, "button must not be null");
    }

    /**
     * Creates a new binding for the given button.
     */
    @NonNull
    public static ButtonBinding forButton(@NonNull Button button) {
        return new ButtonBinding(button);
    }

    /**
     * Binds the button to the given action. The button will be enabled whenever the action is executable and the action
     * will be executed whenever the button is clicked. Any result returned by the action is discarded.
     *
     * @param action the action to bind to.
     * @throws IllegalStateException if this binding has already been bound to an action.
     * @see #bind(Action, SerializableConsumer)
     * @see #unbind()
     */
    public <T> void bind(@NonNull Action<T> action) {
        bind(action, null);
    }

    /**
     * Binds the button to the given action. The button will be enabled whenever the action is executable and the action
     * will be executed whenever the button is clicked. Any non-null result returned by the action will be passed to
     * the given result handler if set.
     *
     * @param action        the action to bind to.
     * @param resultHandler the result handler that will take care of the action result.
     * @throws IllegalStateException if this binding has already been bound to an action.
     * @see #bind(Action)
     * @see #unbind()
     */
    public <T> void bind(@NonNull Action<T> action, @Nullable SerializableConsumer<T> resultHandler) {
        if (this.action != null) {
            throw new IllegalStateException("This button has already been bound to an action");
        }
        this.action = Objects.requireNonNull(action, "action must not be null");
        var isExecutableRegistration = action.isExecutable().addPropertyListenerAndFireEvent(event -> button.setEnabled(event.getValue()));
        button.setDisableOnClick(true);
        var buttonClickRegistration = button.addClickListener(event -> {
            try {
                var result = action.execute();
                if (resultHandler != null && result != null) {
                    resultHandler.accept(result);
                }
            } finally {
                // The button has disableOnClick set to true so we need to reset the enabled flag
                button.setEnabled(action.isExecutable().getValue());
            }
        });
        this.registration = (Registration) () -> {
            isExecutableRegistration.remove();
            buttonClickRegistration.remove();
        };
    }

    @Override
    public void unbind() {
        if (registration != null) {
            registration.remove();
            registration = null;
            action = null;
        }
    }
}
