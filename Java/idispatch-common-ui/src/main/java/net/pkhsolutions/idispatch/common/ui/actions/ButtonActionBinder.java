package net.pkhsolutions.idispatch.common.ui.actions;

import com.vaadin.ui.Button;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author peholmst
 */
public class ButtonActionBinder extends AbstractActionBinder {

    public ButtonActionBinder(Serializable target) {
        super(target);
    }

    public Button.ClickListener bind(final String action) {
        return new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Method method = findMethodForAction(action);
                try {
                    method.setAccessible(true);
                    if (method.getParameterTypes().length == 0) {
                        method.invoke(target);
                    } else if (method.getParameterTypes().length == 1) {
                        method.invoke(target, event);
                    } else {
                        throw new IllegalStateException("Action method " + method.getName() + " takes more than one parameter");
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
