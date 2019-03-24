package net.pkhapps.idispatch.dispatcher.console.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import net.pkhapps.idispatch.dispatcher.console.i18n.I18N;
import net.pkhapps.idispatch.dispatcher.console.io.Server;
import net.pkhapps.idispatch.dispatcher.console.io.ServerSettings;
import net.pkhapps.idispatch.dispatcher.console.io.identity.LoginException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

import static java.lang.Integer.MAX_VALUE;
import static javafx.beans.binding.Bindings.or;
import static javafx.collections.FXCollections.observableArrayList;
import static net.pkhapps.idispatch.dispatcher.console.component.ComponentFactory.*;

/**
 * Dialog that allows the user to login.
 */
public class LoginDialog extends Stage {

    private final ComboBox<Server> server;
    private final TextField userName;
    private final PasswordField password;

    public LoginDialog(@Nullable Stage owner, @NotNull ServerSettings serverSettings,
                       @NotNull LoginOperation loginOperation) {
        initOwner(owner);
        titleProperty().bind(I18N.getInstance().translate("login.LoginDialog.title"));

        var layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));
        layout.setHgap(10);
        layout.setVgap(10);

        var title = new Label("Dispatcher Console");
        title.setFont(Font.font(30));
        layout.add(title, 0, 0, 3, 1);

        var localeLbl = translatedLabel("login.LoginDialog.locale.label");
        layout.add(localeLbl, 0, 1);

        ComboBox<Locale> locale1 = comboBox(locale -> locale.getDisplayLanguage(locale));
        locale1.setItems(observableArrayList(I18N.FINNISH, I18N.SWEDISH));
        locale1.valueProperty().bindBidirectional(I18N.getInstance().localeProperty());
        locale1.setMaxWidth(MAX_VALUE);
        localeLbl.setLabelFor(locale1);
        layout.add(locale1, 1, 1);

        var serverLbl = translatedLabel("login.LoginDialog.server.label");
        layout.add(serverLbl, 0, 2);

        server = comboBox(Server::getName);
        server.setItems(serverSettings.getServers());
        server.setMaxWidth(MAX_VALUE);
        if (server.getItems().size() > 0) {
            server.setValue(server.getItems().get(0));
        }
        server.setPrefWidth(200);
        serverLbl.setLabelFor(server);
        layout.add(server, 1, 2);

        Button configureServers = translatedButton("login.LoginDialog.configureServer.label");
        configureServers.setTooltip(new Tooltip("NOT IMPLEMENTED YET")); // TODO Implement Configure Servers
        configureServers.setDisable(true);
        layout.add(configureServers, 2, 2);

        var userNameLbl = translatedLabel("login.LoginDialog.userName.label");
        layout.add(userNameLbl, 0, 3);

        userName = new TextField();
        userNameLbl.setLabelFor(userName);
        layout.add(userName, 1, 3, 2, 1);

        var passwordLbl = translatedLabel("login.LoginDialog.password.label");
        layout.add(passwordLbl, 0, 4);

        password = new PasswordField();
        passwordLbl.setLabelFor(password);
        layout.add(password, 1, 4, 2, 1);

        Button login = translatedButton("login.LoginDialog.login.label");
        login.setDefaultButton(true);

        Button cancel = translatedButton("login.LoginDialog.cancel.label");
        cancel.setCancelButton(true);

        var buttons = new HBox(10, login, cancel);
        buttons.setAlignment(Pos.BOTTOM_RIGHT);
        layout.add(buttons, 0, 5, 3, 1);

        var defaultCol = new ColumnConstraints();
        var growingCol = new ColumnConstraints();
        growingCol.setHgrow(Priority.ALWAYS);
        layout.getColumnConstraints().addAll(defaultCol, growingCol, defaultCol);

        var defaultRow = new RowConstraints();
        var growingRow = new RowConstraints();
        growingRow.setVgrow(Priority.ALWAYS);
        layout.getRowConstraints().addAll(defaultRow, defaultRow, defaultRow, defaultRow, defaultRow, growingRow);

        var scene = new Scene(layout, Color.WHITESMOKE);
        setScene(scene);
        sizeToScene();

        cancel.setOnAction(event -> close());
        // Don't allow login unless a server, username and password is provided.
        login.disableProperty().bind(or(server.valueProperty().isNull(),
                or(userName.textProperty().isEmpty(), password.textProperty().isEmpty())));
        login.setOnAction(event -> {
            try {
                // TODO Disable login button, show WAIT cursor
                loginOperation.login(userName.getText(), password.getText(), server.getValue());
                close();
            } catch (LoginException ex) {
                showLoginError(ex);
            }
        });

        userName.requestFocus();
    }

    private void showLoginError(@NotNull LoginException exception) {
        var translator = I18N.getInstance().getTranslator();
        String message;
        switch (exception.getErrorType()) {
            case INVALID_CLIENT_CREDENTIALS:
                message = translator.get("login.LoginDialog.error.invalidClientCredentials");
                break;
            case INVALID_USER_CREDENTALS:
                message = translator.get("login.LoginDialog.error.invalidUserCredentials");
                break;
            case COMMUNICATION_ERROR:
                message = translator.get("login.LoginDialog.error.communicationError");
                break;
            default:
                message = exception.getErrorType().name();
        }

        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(translator.get("login.LoginDialog.error.title"));
        alert.setTitle(alert.getHeaderText());
        alert.setContentText(message);
        alert.showAndWait();
    }
}
