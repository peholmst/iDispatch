package net.pkhapps.idispatch.dispatcher.console;

import javafx.application.Application;
import javafx.stage.Stage;
import net.pkhapps.idispatch.dispatcher.console.io.LocalDevelopmentServerSettings;
import net.pkhapps.idispatch.dispatcher.console.io.Server;
import net.pkhapps.idispatch.dispatcher.console.io.identity.IdentityServerClient;
import net.pkhapps.idispatch.dispatcher.console.io.identity.LoginException;
import net.pkhapps.idispatch.dispatcher.console.login.LoginDialog;
import net.pkhapps.idispatch.dispatcher.console.main.MainWindowScene;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me!
 */
public class DispatcherConsoleApp extends Application {

    private Stage mainStage;

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
        var mainWindowScene = new MainWindowScene(user, server);
        mainStage.setScene(mainWindowScene);
        mainStage.setTitle(String.format("iDispatch :: Dispatcher Console (%s)", server.getName()));
        mainStage.show();
        mainStage.setMaximized(true);
    }
}
