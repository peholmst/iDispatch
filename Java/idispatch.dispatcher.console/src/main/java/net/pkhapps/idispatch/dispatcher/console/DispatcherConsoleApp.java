package net.pkhapps.idispatch.dispatcher.console;

import javafx.application.Application;
import javafx.stage.Stage;
import net.pkhapps.idispatch.dispatcher.console.io.LocalDevelopmentServerSettings;
import net.pkhapps.idispatch.dispatcher.console.login.LoginDialog;

public class DispatcherConsoleApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        var loginDialog = new LoginDialog(stage, new LocalDevelopmentServerSettings());
        loginDialog.show();

        /*
        var primaryScene = new PrimaryScene();
        var secondaryScene = new SecondaryScene();

        stage.setScene(primaryScene);
        stage.setTitle("iDispatch :: Dispatcher Console");
        stage.show();*/

        //var secondaryStage = new Stage();
        //secondaryStage.setScene(secondaryScene);
        //secondaryStage.setTitle("iDispatch :: Dispatcher Console (2)");
        //secondaryStage.show();
    }
}
