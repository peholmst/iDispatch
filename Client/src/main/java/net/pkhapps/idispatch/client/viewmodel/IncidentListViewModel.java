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

package net.pkhapps.idispatch.client.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.pkhapps.idispatch.client.model.IncidentPriority;
import net.pkhapps.idispatch.client.model.IncidentState;
import net.pkhapps.idispatch.client.model.IncidentType;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;

public class IncidentListViewModel {

    private final ObservableList<IncidentListItemViewModel> visibleIncidents = FXCollections.observableArrayList();
    private final ObservableList<IncidentListItemViewModel> unmodifiableVisibleIncidents = FXCollections.unmodifiableObservableList(visibleIncidents);

    public @NotNull ObservableList<IncidentListItemViewModel> visibleIncidents() {
        return unmodifiableVisibleIncidents;
    }

    public static class IncidentListItemViewModel {
        private final StringProperty incidentId = new SimpleStringProperty();
        private final ObjectProperty<ZonedDateTime> opened = new SimpleObjectProperty<>();
        private final ObjectProperty<ZonedDateTime> closed = new SimpleObjectProperty<>();
        private final ObjectProperty<IncidentType> type = new SimpleObjectProperty<>();
        private final ObjectProperty<IncidentPriority> priority = new SimpleObjectProperty<>();
        private final ObjectProperty<IncidentState> state = new SimpleObjectProperty<>();
        // TODO Address

        public @NotNull ReadOnlyStringProperty incidentId() {
            return incidentId;
        }

        public @NotNull ReadOnlyObjectProperty<ZonedDateTime> opened() {
            return opened;
        }

        public @NotNull ReadOnlyObjectProperty<ZonedDateTime> closed() {
            return closed;
        }

        public @NotNull ReadOnlyObjectProperty<IncidentType> type() {
            return type;
        }

        public @NotNull ReadOnlyObjectProperty<IncidentPriority> priority() {
            return priority;
        }

        public @NotNull ReadOnlyObjectProperty<IncidentState> state() {
            return state;
        }
    }
}
