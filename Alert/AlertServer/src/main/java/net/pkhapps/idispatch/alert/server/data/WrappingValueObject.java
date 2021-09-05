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
 * A base class for value objects that wrap a single value.
 *
 * @param <T> the type of the wrapped value.
 */
public abstract class WrappingValueObject<T> {

    private final T wrapped;

    /**
     * Creates a new value object.
     *
     * @param wrapped the value to wrap, must not be {@code null}.
     */
    protected WrappingValueObject(T wrapped) {
        this.wrapped = requireNonNull(wrapped, "the wrapped value must not be null");
    }

    /**
     * Unwraps the value object.
     *
     * @return the value that is being wrapped, never {@code null}.
     */
    public T unwrap() {
        return wrapped;
    }

    @Override
    public String toString() {
        return "%s{%s}".formatted(getClass().getSimpleName(), wrapped);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (WrappingValueObject<?>) o;
        return wrapped.equals(that.wrapped);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wrapped);
    }
}
