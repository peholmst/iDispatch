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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AddressTest {

    @Test
    void fromMonolingualStreetAddress_noNumber_inexactAddressCreated() {
        var address = Address.fromMonolingualStreetAddress("Foo", null);
        assertThat(address.roadOrAddressPointName().defaultValue()).isEqualTo("Foo");
        assertThat(address.number()).isNull();
        assertThat(address.isExact()).isFalse();
        assertThat(address.isIntersection()).isFalse();
        assertThat(address.isUnnamed()).isFalse();
    }

    @Test
    void fromMonolingualStreetAddress_withNumber_exactAddressCreated() {
        var address = Address.fromMonolingualStreetAddress("Foo", "123");
        assertThat(address.roadOrAddressPointName().defaultValue()).isEqualTo("Foo");
        assertThat(address.number()).isEqualTo("123");
        assertThat(address.isExact()).isTrue();
        assertThat(address.isIntersection()).isFalse();
        assertThat(address.isUnnamed()).isFalse();
    }

    @Test
    void fromIntersection_exactAddressCreated() {
        var address = Address.fromIntersection(MultilingualStringLiteral.fromMonolingualString("Foo"),
                MultilingualStringLiteral.fromMonolingualString("Bar"));
        assertThat(address.roadOrAddressPointName().defaultValue()).isEqualTo("Foo");
        assertThat(address.intersectingRoadName().defaultValue()).isEqualTo("Bar");
        assertThat(address.isExact()).isTrue();
        assertThat(address.isIntersection()).isTrue();
        assertThat(address.isUnnamed()).isFalse();
    }

    @Test
    void fromUnnamedLocation_inexactAddressCreated() {
        var address = Address.fromUnnamedLocation("Foo");
        assertThat(address.comments()).isEqualTo("Foo");
        assertThat(address.isExact()).isFalse();
        assertThat(address.isIntersection()).isFalse();
        assertThat(address.isUnnamed()).isTrue();
    }
}
