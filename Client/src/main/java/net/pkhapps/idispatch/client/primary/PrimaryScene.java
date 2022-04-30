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

package net.pkhapps.idispatch.client.primary;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.pkhapps.idispatch.client.viewmodel.AppModel;
import net.pkhapps.idispatch.client.views.*;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

public class PrimaryScene extends Scene {

    public PrimaryScene(@NotNull AppModel appModel) {
        super(new PrimaryLayout(appModel));
        getStylesheets().add("primary.css");
    }

    private static class PrimaryLayout extends VBox {

        private final AppModel appModel;
        private final MenuBar menuBar;
        private final SplitPane splitPane;
        private final TabPane tabPane;
        private final IncidentDetailsView incidentDetailsView;
        private final IncidentListView incidentListView;
        private final ResourceListView resourceListView;
        private final StationListView stationListView;
        private final LogView logView;


        PrimaryLayout(@NotNull AppModel appModel) {
            this.appModel = requireNonNull(appModel);

            // Views
            incidentDetailsView = new IncidentDetailsView();
            incidentListView = new IncidentListView(appModel.incidentListViewModel());
            resourceListView = new ResourceListView();
            stationListView = new StationListView(appModel.stationListViewModel());
            logView = new LogView();

            // Layout elements
            tabPane = new TabPane(
                    createNonClosableTab(appModel.translate("primary-layout.tabs.incident-list-view"), incidentListView),
                    createNonClosableTab(appModel.translate("primary-layout.tabs.resource-list-view"), resourceListView),
                    createNonClosableTab(appModel.translate("primary-layout.tabs.station-list-view"), stationListView),
                    createNonClosableTab(appModel.translate("primary-layout.tabs.log-view"), logView)
            );

            splitPane = new SplitPane(incidentDetailsView, tabPane);
            splitPane.setOrientation(Orientation.HORIZONTAL);

            menuBar = new MenuBar();
            createMenuItems();

            getChildren().addAll(menuBar, splitPane);
            VBox.setVgrow(splitPane, Priority.ALWAYS);
        }

        private @NotNull Tab createNonClosableTab(@NotNull String text, @NotNull Node content) {
            var tab = new Tab(text, content);
            tab.setClosable(false);
            return tab;
        }

        private void createMenuItems() {
            var file = new Menu(appModel.translate("primary-layout.menu-bar.file"));
            menuBar.getMenus().add(file);
            {
                var newIncident = new MenuItem(appModel.translate("primary-layout.menu-bar.file.new-incident"));
                // TODO Action
                newIncident.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
                file.getItems().add(newIncident);

                file.getItems().add(new SeparatorMenuItem());

                var exit = new MenuItem(appModel.translate("primary-layout.menu-bar.file.exit"));
                exit.setOnAction(event -> appModel.exit());
                exit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.SHORTCUT_DOWN));
                file.getItems().add(exit);
            }

            var view = new Menu(appModel.translate("primary-layout.menu-bar.view"));
            menuBar.getMenus().add(view);
            {
                // TODO Actions
                var incidentListView = new MenuItem(appModel.translate("primary-layout.menu-bar.view.incident-list-view"));
                incidentListView.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.SHORTCUT_DOWN));
                view.getItems().add(incidentListView);

                var resourceListView = new MenuItem(appModel.translate("primary-layout.menu-bar.view.resource-list-view"));
                resourceListView.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.SHORTCUT_DOWN));
                view.getItems().add(resourceListView);

                var stationListView = new MenuItem(appModel.translate("primary-layout.menu-bar.view.station-list-view"));
                stationListView.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT5, KeyCombination.SHORTCUT_DOWN));
                view.getItems().add(stationListView);

                var logView = new MenuItem(appModel.translate("primary-layout.menu-bar.view.log-view"));
                logView.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT6, KeyCombination.SHORTCUT_DOWN));
                view.getItems().add(logView);
            }

            var window = new Menu(appModel.translate("primary-layout.menu-bar.window"));
            menuBar.getMenus().add(window);
            {
                // TODO Checkbox
                var toggleFullScreen = new MenuItem(appModel.translate("primary-layout.menu-bar.window.full-screen"));
                toggleFullScreen.setOnAction(event -> appModel.toggleFullScreen());
                toggleFullScreen.setAccelerator(new KeyCodeCombination(KeyCode.F12));
                window.getItems().add(toggleFullScreen);

                window.getItems().add(new SeparatorMenuItem());

                var secondaryWindow = new MenuItem(appModel.translate("primary-layout.menu-bar.window.secondary-window"));
                secondaryWindow.setOnAction(event -> appModel.showSecondaryWindow());
                secondaryWindow.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.SHORTCUT_DOWN));
                window.getItems().add(secondaryWindow);
            }
        }
    }
}
