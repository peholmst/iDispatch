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

package net.pkhapps.idispatch.client.security;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Stream;

public interface AccessChecker {

    boolean hasPermission(@NotNull Permission permission);

    default boolean hasAll(@NotNull Stream<Permission> permissions) {
        return permissions.allMatch(this::hasPermission);
    }

    default boolean hasAll(@NotNull Collection<Permission> permissions) {
        return hasAll(permissions.stream());
    }

    default boolean hasAll(@NotNull Permission... permissions) {
        return hasAll(Stream.of(permissions));
    }

    default boolean hasAny(@NotNull Stream<Permission> permissions) {
        return permissions.anyMatch(this::hasPermission);
    }

    default boolean hasAny(@NotNull Collection<Permission> permissions) {
        return hasAny(permissions.stream());
    }

    default boolean hasAny(@NotNull Permission... permissions) {
        return hasAny(Stream.of(permissions));
    }
}
