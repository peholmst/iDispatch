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

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.pkhapps.idispatch.client.model.IncidentPriority;
import net.pkhapps.idispatch.client.model.IncidentState;
import net.pkhapps.idispatch.client.model.IncidentType;
import net.pkhapps.idispatch.client.viewmodel.IncidentListViewModel;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class IncidentListView extends VBox {

    private final TableView<IncidentListViewModel.IncidentListItemViewModel> tableView;
    private final TableColumn<IncidentListViewModel.IncidentListItemViewModel, String> incidentIdColumn;
    private final TableColumn<IncidentListViewModel.IncidentListItemViewModel, ZonedDateTime> openedColumn;
    private final TableColumn<IncidentListViewModel.IncidentListItemViewModel, ZonedDateTime> closedColumn;
    private final TableColumn<IncidentListViewModel.IncidentListItemViewModel, IncidentType> typeColumn;
    private final TableColumn<IncidentListViewModel.IncidentListItemViewModel, IncidentPriority> priorityColumn;
    private final TableColumn<IncidentListViewModel.IncidentListItemViewModel, IncidentState> stateColumn;

    public IncidentListView(@NotNull IncidentListViewModel viewModel) {
        // TODO Translate me!
        tableView = new TableView<>(viewModel.visibleIncidents());
        tableView.getStyleClass().add("no-border");

        incidentIdColumn = new TableColumn<>("ID");
        incidentIdColumn.setCellValueFactory(vm -> vm.getValue().incidentId());

        openedColumn = new TableColumn<>("Opened");
        openedColumn.setCellValueFactory(vm -> vm.getValue().opened());

        closedColumn = new TableColumn<>("Closed");
        closedColumn.setCellValueFactory(vm -> vm.getValue().closed());

        typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(vm -> vm.getValue().type());

        priorityColumn = new TableColumn<>("Priority");
        priorityColumn.setCellValueFactory(vm -> vm.getValue().priority());

        stateColumn = new TableColumn<>("State");
        stateColumn.setCellValueFactory(vm -> vm.getValue().state());

        tableView.getColumns().setAll(List.of(
                incidentIdColumn,
                openedColumn,
                typeColumn,
                priorityColumn,
                stateColumn,
                closedColumn)
        );

        getChildren().add(tableView);
        setVgrow(tableView, Priority.ALWAYS);
    }

}
