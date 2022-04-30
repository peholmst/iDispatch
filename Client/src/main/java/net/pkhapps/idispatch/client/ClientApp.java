/*
 * iDispatch Client
 *
 * Copyright (c) 2022 Petter Holmström
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.pkhapps.idispatch.client;

import javafx.application.Application;
import javafx.stage.Stage;
import net.pkhapps.idispatch.client.i18n.Locales;
import net.pkhapps.idispatch.client.primary.PrimaryScene;
import net.pkhapps.idispatch.client.secondary.SecondaryScene;
import net.pkhapps.idispatch.client.viewmodel.AppModel;

public class ClientApp extends Application {

    private static final int DEFAULT_WIDTH = 1000;
    private static final int DEFAULT_HEIGHT = 500;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        var secondaryStage = new Stage();
        var model = new AppModel(Locales.SWEDISH) {

            @Override
            public void showPrimaryWindow() {
                primaryStage.show();
                primaryStage.requestFocus();
            }

            @Override
            public void showSecondaryWindow() {
                secondaryStage.show();
                secondaryStage.requestFocus();
            }
        };

        var primaryScene = new PrimaryScene(model);
        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("iDispatch Client (Primary)");
        primaryStage.setWidth(DEFAULT_WIDTH);
        primaryStage.setHeight(DEFAULT_HEIGHT);
        primaryStage.show();

        var secondaryScene = new SecondaryScene(model);
        secondaryStage.setScene(secondaryScene);
        secondaryStage.setTitle("iDispatch Client (Secondary)");
        secondaryStage.setWidth(DEFAULT_WIDTH);
        secondaryStage.setHeight(DEFAULT_HEIGHT);

        secondaryStage.show();

        // TODO If there are two monitors, show the scenes on different monitors.
        //  If there is only one monitor, cascade the scenes on top of each other so that you can
        //  click on both of them.

        primaryStage.requestFocus();
    }
}
