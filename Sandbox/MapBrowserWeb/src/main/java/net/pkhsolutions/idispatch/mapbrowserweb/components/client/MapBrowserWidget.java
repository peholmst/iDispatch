/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.mapbrowserweb.components.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.communication.RpcProxy;

/**
 * TODO document me!
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class MapBrowserWidget extends Widget {
    
    /** CSS class name to allow styling */
    public static final String CLASSNAME = "v-mapbrowser";
        
    private final Canvas canvas;
    
    public MapBrowserWidget() {
        setElement(Document.get().createDivElement());
        setStyleName(CLASSNAME);
        
        canvas = Canvas.createIfSupported();
        if (canvas != null) {
            getElement().appendChild(canvas.getElement());
            canvas.setVisible(true);            
            canvas.setCoordinateSpaceHeight(50);
            canvas.setCoordinateSpaceWidth(50);
        } else {
            getElement().setInnerHTML("Canvas not supported");
        }
        
        // TODO Support for touch events
        
        sinkEvents(Event.MOUSEEVENTS);
    }

    @Override
    public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEDOWN:
                VConsole.log("Mouse down");
                break;
            case Event.ONMOUSEMOVE:
                VConsole.log("Mouse move");
                break;
            case Event.ONMOUSEUP:
                VConsole.log("Mouse up");
                break;
        }
    }
        
}
