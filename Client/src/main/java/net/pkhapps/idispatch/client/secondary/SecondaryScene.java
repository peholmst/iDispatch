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

package net.pkhapps.idispatch.client.secondary;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import net.pkhapps.idispatch.client.viewmodel.AppModel;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

public class SecondaryScene extends Scene {

    public SecondaryScene(@NotNull AppModel appModel) {
        super(new SecondaryLayout(appModel));
        getStylesheets().add("secondary.css");
    }

    private static class SecondaryLayout extends AnchorPane {

        private final AppModel appModel;
        private final MenuBar menuBar;

        SecondaryLayout(@NotNull AppModel appModel) {
            this.appModel = requireNonNull(appModel);
            menuBar = new MenuBar();
            createMenuItems();

            getChildren().add(menuBar);
            setTopAnchor(menuBar, 0d);
            setLeftAnchor(menuBar, 0d);
            setRightAnchor(menuBar, 0d);
        }

        private void createMenuItems() {
            var window = new Menu("Window");
            menuBar.getMenus().add(window);
            {
                var toggleFullScreen = new MenuItem("Enter Full Screen");
                toggleFullScreen.setOnAction(event -> appModel.toggleFullScreen());
                toggleFullScreen.setAccelerator(new KeyCodeCombination(KeyCode.F12));
                window.getItems().add(toggleFullScreen);

                window.getItems().add(new SeparatorMenuItem());

                var primaryWindow = new MenuItem("Show Primary Window");
                primaryWindow.setOnAction(event -> appModel.showPrimaryWindow());
                primaryWindow.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.SHORTCUT_DOWN));
                window.getItems().add(primaryWindow);
            }
        }
    }
}
