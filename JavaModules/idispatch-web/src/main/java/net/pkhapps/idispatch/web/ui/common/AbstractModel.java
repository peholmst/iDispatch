package net.pkhapps.idispatch.web.ui.common;

import com.vaadin.shared.Registration;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * TODO Document me!
 *
 * @param <O>
 */
public abstract class AbstractModel<O extends Serializable> {

    private final List<O> observers = new ArrayList<>();

    @NonNull
    public Registration addObserver(@NonNull O observer) {
        observers.add(Objects.requireNonNull(observer));
        return (Registration) () -> observers.remove(observer);
    }

    protected void notifyObservers(@NonNull Consumer<O> notifier) {
        Objects.requireNonNull(notifier);
        new ArrayList<>(observers).forEach(notifier);
    }
}
