/*
 * iDispatch Dispatch Server
 * Copyright (C) 2021 Petter Holmström
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.pkhapps.idispatch.dispatch.server.util;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MultilingualStringLiteralTest {

    @Test
    void monolingualString_allLocalesReturnSameValue() {
        var s = MultilingualStringLiteral.fromMonolingualString("foo");
        assertThat(s.localizedValue(Locale.ENGLISH)).isEqualTo("foo");
        assertThat(s.localizedValue(Locale.FRENCH)).isEqualTo("foo");
        assertThat(s.defaultValue()).isEqualTo("foo");
    }

    @Test
    void bilingualString_correctValuesReturned() {
        var s = MultilingualStringLiteral.fromBilingualString(Locale.ENGLISH, "foo", Locale.FRENCH, "bar");
        assertThat(s.localizedValue(Locale.ENGLISH)).isEqualTo("foo");
        assertThat(s.localizedValue(Locale.FRENCH)).isEqualTo("bar");
        assertThat(s.defaultValue()).isEqualTo("foo");
    }

    @Test
    void bilingualString_otherLocalesReturnFirstValue() {
        var s = MultilingualStringLiteral.fromBilingualString(Locale.ENGLISH, "foo", Locale.FRENCH, "bar");
        assertThat(s.localizedValue(Locale.ITALIAN)).isEqualTo("foo");
    }

    @Test
    void bilingualString_sameLocales_exceptionThrown() {
        assertThatThrownBy(() -> MultilingualStringLiteral.fromBilingualString(Locale.ENGLISH, "foo", Locale.ENGLISH, "bar"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void anyValueMatches() {
        var s = MultilingualStringLiteral.fromBilingualString(Locale.ENGLISH, "foo", Locale.FRENCH, "bar");
        assertThat(s.anyValueMatches(s1 -> s1.equals("foo"))).isTrue();
        assertThat(s.anyValueMatches(s1 -> s1.equals("bar"))).isTrue();
        assertThat(s.anyValueMatches(s1 -> s1.equals("nonexistent"))).isFalse();
    }
}
