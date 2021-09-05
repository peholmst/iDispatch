// iDispatch Alert Server
// Copyright (C) 2021 Petter Holmström
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
package net.pkhapps.idispatch.alert.server.data;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Base class for entities. An entity can always be uniquely identified by an ID and two entities of the same class
 * having the same ID are considered equal, regardless of what other data they contain.
 *
 * @param <I> the type of the entity ID.
 */
public abstract class Entity<I> {

    private final I id;

    /**
     * Creates a new {@code Entity}.
     *
     * @param id the ID to assign to the entity, must not be {@code null}.
     */
    protected Entity(I id) {
        this.id = requireNonNull(id, "id must not be null");
    }

    /**
     * Gets the ID of this entity.
     *
     * @return the ID, never {@code null}.
     */
    public I id() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        var other = (Entity<?>) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "%s{%s}".formatted(getClass().getSimpleName(), id);
    }
}
