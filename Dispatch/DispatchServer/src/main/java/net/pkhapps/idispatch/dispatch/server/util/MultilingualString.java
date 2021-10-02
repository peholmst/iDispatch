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

package net.pkhapps.idispatch.dispatch.server.util;

import java.util.Locale;

/**
 * An interface that defines an object that contains multiple versions of the same string but in different languages.
 */
public interface MultilingualString {

    /**
     * Gets the string for the given locale. If no such string is found, the {@linkplain #defaultValue() default string}
     * is returned.
     *
     * @param locale the locale for which a string should be returned.
     * @return the string for the given locale or the default string if not found, never {@code null}.
     */
    String localizedValue(Locale locale);

    /**
     * Gets the default string.
     *
     * @return the default string, never {@code null}.
     */
    String defaultValue();
}
