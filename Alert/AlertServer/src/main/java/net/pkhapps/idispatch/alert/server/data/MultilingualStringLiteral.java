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

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link MultilingualString} that works with ordinary strings.
 */
@SuppressWarnings("ClassCanBeRecord")
public class MultilingualStringLiteral implements MultilingualString {

    private final Map<Locale, String> strings;

    private MultilingualStringLiteral(Map<Locale, String> strings) {
        this.strings = strings;
    }

    /**
     * Creates a {@code MultilingualStringLiteral} from a monolingual string.
     *
     * @param string the monolingual string, must not be {@code null}.
     * @return a new {@code MultilingualStringLiteral}.
     */
    public static MultilingualStringLiteral fromMonolingualString(String string) {
        requireNonNull(string, "string must not be null");
        return new MultilingualStringLiteral(Map.of(Locale.ROOT, string));
    }

    /**
     * Creates a {@code MultilingualStringLiteral} from two strings in different languages. None of the parameters can
     * be {@code null}.
     *
     * @param l1 the language of the first string.
     * @param s1 the first string (will also become the default string).
     * @param l2 the language of the second string.
     * @param s2 the second string.
     * @return a new {@code MultilingualStringLiteral}.
     */
    public static MultilingualStringLiteral fromBilingualString(Locale l1, String s1, Locale l2, String s2) {
        requireNonNull(l1, "l1 must not be null");
        requireNonNull(l2, "l2 must not be null");
        requireNonNull(s1, "s1 must not be null");
        requireNonNull(s2, "s2 must not be null");
        return new MultilingualStringLiteral(Map.of(l1, s1, l2, s2, Locale.ROOT, s1));
    }

    /**
     * Gets the string for the given locale. If no such string is found, the default string is returned.
     *
     * @param locale the locale for which a string should be returned.
     * @return the string for the given locale or the default string if not found, never {@code null}.
     */
    public String localizedValue(Locale locale) {
        assert strings.size() > 0;
        var s = strings.get(locale);
        return s == null ? strings.get(Locale.ROOT) : s;
    }

    /**
     * Gets the default string.
     *
     * @return the default string, never {@code null}.
     */
    public String defaultValue() {
        return strings.get(Locale.ROOT);
    }

    @Override
    public String toString() {
        return "%s{strings=%s}".formatted(getClass().getSimpleName(), strings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (MultilingualStringLiteral) o;
        return strings.equals(that.strings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strings);
    }
}
