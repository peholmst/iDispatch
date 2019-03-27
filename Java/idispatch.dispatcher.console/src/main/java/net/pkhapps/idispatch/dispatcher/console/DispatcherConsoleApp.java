package net.pkhapps.idispatch.dispatcher.console;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.pkhapps.idispatch.dispatcher.console.context.ApplicationContext;
import net.pkhapps.idispatch.dispatcher.console.io.LocalDevelopmentServerSettings;
import net.pkhapps.idispatch.dispatcher.console.io.Server;
import net.pkhapps.idispatch.dispatcher.console.io.SystemStatusChecker;
import net.pkhapps.idispatch.dispatcher.console.io.SystemStatusSummary;
import net.pkhapps.idispatch.dispatcher.console.io.identity.IdentityServerClient;
import net.pkhapps.idispatch.dispatcher.console.io.identity.LoginException;
import net.pkhapps.idispatch.dispatcher.console.login.LoginDialog;
import net.pkhapps.idispatch.dispatcher.console.main.MainWindowLayout;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me!
 */
public class DispatcherConsoleApp extends Application {

    private Stage mainStage;
    private SystemStatusChecker systemStatusChecker;

    @Override
    public void start(Stage stage) throws Exception {
        this.mainStage = stage;

        var loginDialog = new LoginDialog(stage, new LocalDevelopmentServerSettings(), this::login);
        loginDialog.show();
    }

    private void login(@NotNull String username, @NotNull String password, @NotNull Server server)
            throws LoginException {
        var client = new IdentityServerClient(server);
        var user = client.login(username, password);

        var systemStatusSummary = new SystemStatusSummary();
        systemStatusChecker = new SystemStatusChecker(server, systemStatusSummary);
        var applicationContext = new ApplicationContext(server, user, systemStatusSummary);
        var mainWindow = new MainWindowLayout(applicationContext);

        mainStage.setScene(new Scene(mainWindow));
        mainStage.setTitle(String.format("iDispatch :: Dispatcher Console (%s)", server.getName()));
        mainStage.setMaximized(true);
        mainStage.show();

        systemStatusChecker.start();
    }
}
