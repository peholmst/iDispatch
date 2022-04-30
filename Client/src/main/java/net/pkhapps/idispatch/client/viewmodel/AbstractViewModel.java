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

import java.util.Locale;

import static java.util.Objects.requireNonNull;

public abstract class AbstractViewModel implements I18NProvider, AccessChecker {

    private final I18NProvider i18NProvider;
    private final AccessChecker accessChecker;

    public AbstractViewModel(@NotNull I18NProvider i18NProvider,
                             @NotNull AccessChecker accessChecker) {
        this.i18NProvider = requireNonNull(i18NProvider);
        this.accessChecker = requireNonNull(accessChecker);
    }

    public <P extends I18NProvider & AccessChecker> AbstractViewModel(@NotNull P parentModel) {
        this(parentModel, parentModel);
    }

    @Override
    public @NotNull Locale locale() {
        return i18NProvider.locale();
    }

    @Override
    public @NotNull String translate(@NotNull String key, @NotNull Object... args) {
        return i18NProvider.translate(key, args);
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission) {
        return accessChecker.hasPermission(permission);
    }
}
