package net.pkhsolutions.idispatch.mapbrowserweb.components.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;

/**
 * TODO document me!
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class MapBrowserWidget extends Widget {
    
    /** CSS class name to allow styling */
    public static final String CLASSNAME = "mapbrowser";
    
    public MapBrowserWidget() {
        setElement(Document.get().createDivElement());
        setStyleName(CLASSNAME);
    }
}
