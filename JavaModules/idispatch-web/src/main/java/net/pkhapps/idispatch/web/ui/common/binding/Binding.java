package net.pkhapps.idispatch.web.ui.common.binding;

import java.io.Serializable;

/**
 * Basic interface for bindings that bind some model element and some UI control together.
 */
public interface Binding extends Serializable {

    /**
     * Breaks the binding, removing any allocated resources or listener registrations. This is especially important
     * if the model element and the UI control have different scopes. Failure to unbind a binding when it is no longer
     * in use can result in memory leaks or strange side effects when the same UI element ends up bound to multiple
     * model elements.
     */
    void unbind();
}
