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

import net.pkhapps.idispatch.client.i18n.I18NProvider;
import net.pkhapps.idispatch.client.security.AccessChecker;
import net.pkhapps.idispatch.client.security.Permission;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static java.util.Objects.requireNonNull;

public abstract class AppModel implements I18NProvider, AccessChecker {

    private final Locale locale;
    private final ResourceBundle bundle;
    private final IncidentListViewModel incidentListViewModel = new IncidentListViewModel();
    private final StationListViewModel stationListViewModel = new StationListViewModel(this);

    protected AppModel(@NotNull Locale locale) {
        this.locale = requireNonNull(locale);
        bundle = ResourceBundle.getBundle("messages", locale);
    }

    @Override
    public @NotNull Locale locale() {
        return locale;
    }

    @Override
    public @NotNull String translate(@NotNull String key, @NotNull Object... args) {
        try {
            var format = new MessageFormat(bundle.getString(key));
            format.setLocale(locale);
            return format.format(args);
        } catch (MissingResourceException ex) {
            return "!" + key;
        }
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission) {
        return true; // TODO Implement me!
    }

    public abstract void showPrimaryWindow();

    public abstract void showSecondaryWindow();

    public void toggleFullScreen() {

    }

    public void exit() {
        // TODO Confirm exit
        System.exit(0);
    }

    public @NotNull IncidentListViewModel incidentListViewModel() {
        return incidentListViewModel;
    }

    public @NotNull StationListViewModel stationListViewModel() {
        return stationListViewModel;
    }
}
