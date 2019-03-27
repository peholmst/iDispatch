package net.pkhapps.idispatch.dispatcher.console.main;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import net.pkhapps.idispatch.dispatcher.console.context.ApplicationContext;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me
 */
public class MainWindowLayout extends BorderPane {

    private final StatusPane statusPane;
    private final MenuBar menuBar;

    public MainWindowLayout(@NotNull ApplicationContext applicationContext) {
        statusPane = new StatusPane(applicationContext);
        setBottom(statusPane);

        var sessionMenu = new Menu("Session");
        sessionMenu.getItems().add(new MenuItem("Change password..."));
        sessionMenu.getItems().add(new MenuItem("Server settings..."));
        sessionMenu.getItems().add(new MenuItem("Exit"));
        var viewMenu = new Menu("View");
        var helpMenu = new Menu("Help");
        helpMenu.getItems().add(new MenuItem("About iDispatch..."));

        menuBar = new MenuBar(sessionMenu, viewMenu, helpMenu);
        setTop(menuBar);
    }
}
