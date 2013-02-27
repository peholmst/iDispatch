package net.pkhsolutions.idispatch.common.ui.actions;

import com.vaadin.data.Property;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author peholmst
 */
public class FieldActionBinder extends AbstractActionBinder {

    public FieldActionBinder(Serializable target) {
        super(target);
    }

    public Property.ValueChangeListener bind(final String action) {
        return new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Method method = findMethodForAction(action);
                try {
                    method.setAccessible(true);
                    if (method.getParameterTypes().length == 1) {
                        method.invoke(target, event.getProperty().getValue());
                    } else {
                        throw new IllegalStateException("Action method " + method.getName() + " does not take exactly one parameter");
                    }
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException("Could not invoke action method " + method.getName(), ex);
                } catch (InvocationTargetException ex) {
                    throw new RuntimeException("An exception occurred while invoking the action method " + method.getName(), ex.getCause());
                }
            }
        };
    }
}
