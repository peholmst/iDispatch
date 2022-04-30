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
import javafx.event.ActionEvent;
import net.pkhapps.idispatch.client.gis.CRS;
import net.pkhapps.idispatch.client.gis.DegreeFormatters;
import net.pkhapps.idispatch.client.gis.MeterFormatters;
import net.pkhapps.idispatch.client.i18n.I18NProvider;
import net.pkhapps.idispatch.client.i18n.Locales;
import net.pkhapps.idispatch.client.security.AccessChecker;
import org.geotools.geometry.DirectPosition2D;
import org.jetbrains.annotations.NotNull;
import org.opengis.geometry.DirectPosition;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class StationListViewModel extends AbstractViewModel {

    private final ObservableList<StationListItemViewModel> stations = FXCollections.observableArrayList();
    private final ObservableList<StationListItemViewModel> unmodifiableStations = FXCollections.unmodifiableObservableList(stations);
    private final ObjectProperty<StationListItemViewModel> selectedStation = new SimpleObjectProperty<>();
    private final StringProperty searchTerm = new SimpleStringProperty();

    public StationListViewModel(@NotNull I18NProvider i18NProvider, @NotNull AccessChecker accessChecker) {
        super(i18NProvider, accessChecker);
        // TODO Remove this dummy data
        stations.add(new StationListItemViewModel(this, UUID.randomUUID().toString(), "VS91",
                "Pargas FBK", "Paraisten VPK",
                new DirectPosition2D(CRS.WGS84, 22.297251013, 60.310745949)));
        stations.add(new StationListItemViewModel(this, UUID.randomUUID().toString(), "VS92",
                "Lielax FBK", "Lielahden VPK",
                new DirectPosition2D(CRS.WGS84, 22.422522526, 60.317821633)));
        stations.add(new StationListItemViewModel(this, UUID.randomUUID().toString(), "VS93",
                "Nagu FBK", "Nauvon VPK",
                new DirectPosition2D(CRS.WGS84, 21.895735021, 60.198537060)));
    }

    public <P extends I18NProvider & AccessChecker> StationListViewModel(@NotNull P parentModel) {
        this(parentModel, parentModel);
    }

    public @NotNull ObservableList<StationListItemViewModel> stations() {
        return unmodifiableStations;
    }

    public @NotNull ObjectProperty<StationListItemViewModel> selectedStation() {
        return selectedStation;
    }

    public @NotNull Optional<StationListItemViewModel> getSelectedStation() {
        return Optional.ofNullable(selectedStation.getValue());
    }

    public @NotNull StringProperty searchTerm() {
        return searchTerm; // TODO Do something when the term changes
    }

    public void newStation(@NotNull ActionEvent event) {
        // TODO Implement me
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void editSelectedStation(@NotNull ActionEvent event) {
        // TODO Implement me
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void showSelectedStationOnMap(@NotNull ActionEvent event) {
        // TODO Implement me
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void copyPosition_wgs84_lat_dms(@NotNull ActionEvent event) {
        getSelectedStation().ifPresent(vm -> vm.copyNorthOrdinateToClipboard(CRS.WGS84, DegreeFormatters.DMS));
    }

    public void copyPosition_wgs84_lat_ddm(@NotNull ActionEvent event) {
        getSelectedStation().ifPresent(vm -> vm.copyNorthOrdinateToClipboard(CRS.WGS84, DegreeFormatters.DDM));
    }

    public void copyPosition_wgs84_lat_dd(@NotNull ActionEvent event) {
        getSelectedStation().ifPresent(vm -> vm.copyNorthOrdinateToClipboard(CRS.WGS84, DegreeFormatters.DD));
    }

    public void copyPosition_wgs84_lon_dms(@NotNull ActionEvent event) {
        getSelectedStation().ifPresent(vm -> vm.copyEastOrdinateToClipboard(CRS.WGS84, DegreeFormatters.DMS));
    }

    public void copyPosition_wgs84_lon_ddm(@NotNull ActionEvent event) {
        getSelectedStation().ifPresent(vm -> vm.copyEastOrdinateToClipboard(CRS.WGS84, DegreeFormatters.DDM));
    }

    public void copyPosition_wgs84_lon_dd(@NotNull ActionEvent event) {
        getSelectedStation().ifPresent(vm -> vm.copyEastOrdinateToClipboard(CRS.WGS84, DegreeFormatters.DD));
    }

    public void copyPosition_wgs84_latLon_dms(@NotNull ActionEvent event) {
        getSelectedStation().ifPresent(vm -> vm.copyPositionToClipboard(CRS.WGS84, DegreeFormatters.DMS));
    }

    public void copyPosition_wgs84_latLon_ddm(@NotNull ActionEvent event) {
        getSelectedStation().ifPresent(vm -> vm.copyPositionToClipboard(CRS.WGS84, DegreeFormatters.DDM));
    }

    public void copyPosition_wgs84_latLon_dd(@NotNull ActionEvent event) {
        getSelectedStation().ifPresent(vm -> vm.copyPositionToClipboard(CRS.WGS84, DegreeFormatters.DD));
    }

    public void copyPosition_tm35fin_y(@NotNull ActionEvent event) {
        getSelectedStation().ifPresent(vm -> vm.copyNorthOrdinateToClipboard(CRS.TM35FIN, MeterFormatters.WHOLE_METERS));
    }

    public void copyPosition_tm35fin_x(@NotNull ActionEvent event) {
        getSelectedStation().ifPresent(vm -> vm.copyEastOrdinateToClipboard(CRS.TM35FIN, MeterFormatters.WHOLE_METERS));
    }

    public void copyPosition_tm35fin_xy(@NotNull ActionEvent event) {
        getSelectedStation().ifPresent(vm -> vm.copyPositionToClipboard(CRS.TM35FIN, MeterFormatters.WHOLE_METERS));
    }

    public static class StationListItemViewModel extends AbstractViewModel implements HasPosition {
        private final StringProperty stationId = new SimpleStringProperty();
        private final StringProperty code = new SimpleStringProperty();
        private final StringProperty nameSv = new SimpleStringProperty();
        private final StringProperty nameFi = new SimpleStringProperty();
        private final ObjectProperty<DirectPosition> position = new SimpleObjectProperty<>();

        protected StationListItemViewModel(@NotNull StationListViewModel parentModel,
                                           @NotNull String stationId,
                                           @NotNull String code, @NotNull String nameSv, @NotNull String nameFi,
                                           @NotNull DirectPosition position) {
            super(parentModel);
            this.stationId.set(requireNonNull(stationId));
            this.code.set(requireNonNull(code));
            this.nameSv.set(requireNonNull(nameSv));
            this.nameFi.set(requireNonNull(nameFi));
            this.position.set(requireNonNull(position));
        }

        public @NotNull ReadOnlyStringProperty stationId() {
            return stationId;
        }

        public @NotNull ReadOnlyStringProperty code() {
            return code;
        }

        public @NotNull ReadOnlyStringProperty nameSv() {
            return nameSv;
        }

        public @NotNull ReadOnlyStringProperty nameFi() {
            return nameFi;
        }

        public @NotNull ReadOnlyStringProperty name(@NotNull Locale locale) {
            if (locale.getLanguage().equals(Locales.SWEDISH.getLanguage())) {
                return nameSv;
            } else {
                return nameFi;
            }
        }

        @Override
        public @NotNull ReadOnlyObjectProperty<DirectPosition> position() {
            return position;
        }
    }
}
