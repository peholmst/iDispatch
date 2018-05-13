package net.pkhapps.idispatch.web.ui.common.binding;

import com.vaadin.server.SerializableConsumer;
import net.pkhapps.idispatch.web.ui.common.model.Action;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * TODO Implement and document me!
 */
public class ButtonBinding implements Binding {

    public <T> void bind(@NonNull Action<T> action) {
        bind(action, null);
    }

    public <T> void bind(@NonNull Action<T> action, @Nullable SerializableConsumer<T> resultHandler) {

    }

    @Override
    public void unbind() {

    }
}
