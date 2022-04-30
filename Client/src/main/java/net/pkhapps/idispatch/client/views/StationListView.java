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

package net.pkhapps.idispatch.client.views;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.pkhapps.idispatch.client.gis.CRS;
import net.pkhapps.idispatch.client.gis.CoordinateUtils;
import net.pkhapps.idispatch.client.gis.DegreeFormatters;
import net.pkhapps.idispatch.client.security.Permission;
import net.pkhapps.idispatch.client.viewmodel.StationListViewModel;
import net.pkhapps.idispatch.client.viewmodel.StationListViewModel.StationListItemViewModel;
import org.jetbrains.annotations.NotNull;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import org.opengis.geometry.DirectPosition;

import java.util.Comparator;
import java.util.List;

public class StationListView extends VBox {

    public StationListView(@NotNull StationListViewModel viewModel) {
        var tableView = new TableView<>(viewModel.stations());
        tableView.getStyleClass().add("no-border");
        tableView.setPlaceholder(new Label(viewModel.translate("station-list-view.table-view.placeholder")));

        var codeColumn = new TableColumn<StationListItemViewModel, String>(viewModel.translate("station-list-view.table-view.columns.code"));
        codeColumn.setSortable(true);
        codeColumn.setComparator(Comparator.naturalOrder());
        codeColumn.setCellValueFactory(vm -> vm.getValue().code());

        var nameColumn = new TableColumn<StationListItemViewModel, String>(viewModel.translate("station-list-view.table-view.columns.name"));
        nameColumn.setSortable(true);
        nameColumn.setComparator(Comparator.naturalOrder());
        nameColumn.setCellValueFactory(vm -> vm.getValue().name(viewModel.locale()));

        var positionColumn = new TableColumn<StationListItemViewModel, DirectPosition>(viewModel.translate("station-list-view.table-view.columns.position"));
        positionColumn.setCellValueFactory(vm -> vm.getValue().position());
        positionColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(DirectPosition item, boolean empty) {
                if (item == getItem()) return;

                super.updateItem(item, empty);

                if (item == null) {
                    super.setText(null);
                } else {
                    super.setText(DegreeFormatters.DDM.format(CoordinateUtils.transform(item, CRS.WGS84)));
                }
            }
        });

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        viewModel.selectedStation().bind(tableView.getSelectionModel().selectedItemProperty());
        // TODO Currently, the binding from view to view model is unidirectional. If we
        //  want to set the selection in the table view from the view model, we have to make
        //  this binding bidirectional and this is currently not supported out of the box.
        tableView.getColumns().setAll(List.of(codeColumn, nameColumn, positionColumn));

        // TODO Add support for sorting the table

        var toolbar = new ToolBar();

        var searchField = new TextField();
        searchField.setPromptText(viewModel.translate("station-list-view.tool-bar.search.prompt"));
        searchField.textProperty().bindBidirectional(viewModel.searchTerm());

        var showOnMapButton = new Button();
        showOnMapButton.setGraphic(new FontIcon(FontAwesomeSolid.MAP_MARKED_ALT));
        showOnMapButton.setTooltip(new Tooltip(viewModel.translate("station-list-view.tool-bar.show-on-map.tooltip")));
        showOnMapButton.setOnAction(viewModel::showSelectedStationOnMap);
        showOnMapButton.disableProperty().bind(viewModel.selectedStation().isNull());

        // TODO Turn this menu into a reusable component
        var copyPositionButton = new MenuButton(viewModel.translate("station-list-view.tool-bar.copy-position.text"));
        copyPositionButton.setGraphic(new FontIcon(FontAwesomeSolid.COPY));
        copyPositionButton.disableProperty().bind(viewModel.selectedStation().isNull());
        {
            var wgs84 = new Menu("WGS84");

            var lat = new Menu(viewModel.translate("station-list-view.tool-bar.copy-position.wgs84.latitude.text"));
            var lon = new Menu(viewModel.translate("station-list-view.tool-bar.copy-position.wgs84.longitude.text"));
            var latLon = new Menu(viewModel.translate("station-list-view.tool-bar.copy-position.wgs84.lat-lon.text"));
            wgs84.getItems().addAll(lat, lon, latLon);

            var lat_dms = new MenuItem(viewModel.translate("station-list-view.tool-bar.copy-position.wgs84.dms.text"));
            lat_dms.setOnAction(viewModel::copyPosition_wgs84_lat_dms);
            var lat_ddm = new MenuItem(viewModel.translate("station-list-view.tool-bar.copy-position.wgs84.ddm.text"));
            lat_ddm.setOnAction(viewModel::copyPosition_wgs84_lat_ddm);
            var lat_dd = new MenuItem(viewModel.translate("station-list-view.tool-bar.copy-position.wgs84.dd.text"));
            lat_dd.setOnAction(viewModel::copyPosition_wgs84_lat_dd);
            lat.getItems().addAll(lat_dms, lat_ddm, lat_dd);

            var lon_dms = new MenuItem(viewModel.translate("station-list-view.tool-bar.copy-position.wgs84.dms.text"));
            lon_dms.setOnAction(viewModel::copyPosition_wgs84_lon_dms);
            var lon_ddm = new MenuItem(viewModel.translate("station-list-view.tool-bar.copy-position.wgs84.ddm.text"));
            lon_ddm.setOnAction(viewModel::copyPosition_wgs84_lon_ddm);
            var lon_dd = new MenuItem(viewModel.translate("station-list-view.tool-bar.copy-position.wgs84.dd.text"));
            lon_dd.setOnAction(viewModel::copyPosition_wgs84_lon_dd);
            lon.getItems().addAll(lon_dms, lon_ddm, lon_dd);

            var latLon_dms = new MenuItem(viewModel.translate("station-list-view.tool-bar.copy-position.wgs84.dms.text"));
            latLon_dms.setOnAction(viewModel::copyPosition_wgs84_latLon_dms);
            var latLon_ddm = new MenuItem(viewModel.translate("station-list-view.tool-bar.copy-position.wgs84.ddm.text"));
            latLon_ddm.setOnAction(viewModel::copyPosition_wgs84_latLon_ddm);
            var latLon_dd = new MenuItem(viewModel.translate("station-list-view.tool-bar.copy-position.wgs84.dd.text"));
            latLon_dd.setOnAction(viewModel::copyPosition_wgs84_latLon_dd);
            latLon.getItems().addAll(latLon_dms, latLon_ddm, latLon_dd);

            copyPositionButton.getItems().add(wgs84);
        }
        {
            var tm35fin = new Menu("TM35FIN");

            var x = new MenuItem(viewModel.translate("station-list-view.tool-bar.copy-position.tm35fin.y.text"));
            x.setOnAction(viewModel::copyPosition_tm35fin_y);
            var y = new MenuItem(viewModel.translate("station-list-view.tool-bar.copy-position.tm35fin.x.text"));
            y.setOnAction(viewModel::copyPosition_tm35fin_x);
            var xy = new MenuItem(viewModel.translate("station-list-view.tool-bar.copy-position.tm35fin.xy.text"));
            xy.setOnAction(viewModel::copyPosition_tm35fin_xy);
            tm35fin.getItems().addAll(x, y, xy);

            copyPositionButton.getItems().add(tm35fin);
        }

        var separator = new Separator();
        separator.setVisible(viewModel.hasPermission(Permission.MANAGE_STATIONS));

        var newStationButton = new Button();
        newStationButton.setGraphic(new FontIcon(FontAwesomeSolid.PLUS_CIRCLE));
        newStationButton.setTooltip(new Tooltip(viewModel.translate("station-list-view.tool-bar.new-station.tooltip")));
        newStationButton.setOnAction(viewModel::newStation);
        newStationButton.setVisible(viewModel.hasPermission(Permission.MANAGE_STATIONS));

        var editStationButton = new Button();
        editStationButton.setGraphic(new FontIcon(FontAwesomeSolid.EDIT));
        editStationButton.setTooltip(new Tooltip(viewModel.translate("station-list-view.tool-bar.edit-station.tooltip")));
        editStationButton.disableProperty().bind(viewModel.selectedStation().isNull());
        editStationButton.setOnAction(viewModel::editSelectedStation);
        editStationButton.setVisible(viewModel.hasPermission(Permission.MANAGE_STATIONS));

        var glue = new HBox();
        HBox.setHgrow(glue, Priority.ALWAYS);

        toolbar.getItems().addAll(showOnMapButton, copyPositionButton,
                new Separator(),
                newStationButton, editStationButton,
                glue,
                searchField);

        // TODO Add context menu (right-click) with same options as in toolbar

        getChildren().addAll(toolbar, tableView);
        setVgrow(tableView, Priority.ALWAYS);
    }
}
