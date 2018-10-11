package net.pkhsolutions.idispatch.ui.common;

import com.vaadin.ui.UI;

/**
 * TODO Document me!
 */
public interface UIAttachable {

    void attachedToUI(UI ui);

    void detachedFromUI(UI ui);
}
