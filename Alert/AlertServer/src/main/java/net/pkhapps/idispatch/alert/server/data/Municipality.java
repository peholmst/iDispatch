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

/**
 * A value object representing a municipality. Currently, only the municipality name (in multiple languages if needed)
 * is included.
 */
public class Municipality extends WrappingValueObject<MultilingualString> {

    private Municipality(MultilingualString wrapped) {
        super(wrapped);
    }

    /**
     * Creates a new {@code Municipality} with a monolingual name.
     *
     * @param monolingualName the name of the municipality, must not be {@code null}.
     * @return a new {@code Municipality}.
     */
    public static Municipality fromMonolingualName(String monolingualName) {
        return new Municipality(MultilingualStringLiteral.fromMonolingualString(monolingualName));
    }

    /**
     * Creates a new {@code Municipality} with a multilingual name.
     *
     * @param name the name of the municipality, must not be {@code null}.
     * @return a new {@code Municipality}.
     */
    public static Municipality fromName(MultilingualString name) {
        return new Municipality(name);
    }
}
