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

import static java.util.Objects.requireNonNull;

/**
 * A value object that represents an address that a human would be able to locate:
 * <ul>
 *     <li>A street address with a road or address point name an optionally a number</li>
 *     <li>An intersection between two named roads</li>
 *     <li>An unnamed location described in such a way that locals would be able to find it</li>
 * </ul>
 *
 * @param roadOrAddressPointName the name of the road or address point, may be {@code null}.
 * @param number                 the number of the road or address point, may be {@code null}.
 * @param intersectingRoadName   the name of the intersecting road, may be {@code null}.
 * @param comments               any comments explaining the address, may be {@code null}.
 */
public record Address(MultilingualString roadOrAddressPointName,
                      String number,
                      MultilingualString intersectingRoadName,
                      String comments) {

    public Address {
        if (number != null && roadOrAddressPointName == null) {
            throw new IllegalArgumentException("Cannot specify a number without a road or address point name");
        }
        if (intersectingRoadName != null && roadOrAddressPointName == null) {
            throw new IllegalArgumentException("Cannot specify an intersecting road name without a road name");
        }
        if (intersectingRoadName != null && number != null) {
            throw new IllegalArgumentException("Cannot specify a number for an intersection");
        }
        if (roadOrAddressPointName == null && comments == null) {
            throw new IllegalArgumentException("Comments are required if there is no road or address point name");
        }
    }

    /**
     * Creates a new {@code Address} that is located along a single road or at a named address point, where the name is
     * monolingual.
     *
     * @param name   the monolingual name of the road or address point, must not be {@code null}.
     * @param number the number of the road or address point, or {@code null} if not known or not applicable.
     * @return a new {@code Address}.
     */
    public static Address fromMonolingualStreetAddress(String name, String number) {
        return fromStreetAddress(MultilingualStringLiteral.fromMonolingualString(name), number);
    }

    /**
     * Creates a new {@code Address} that is located along a single road or at a named address point.
     *
     * @param name   the name of the road or address point, must not be {@code null}.
     * @param number the number of the road or address point, or {@code null} if not known or not applicable.
     * @return a new {@code Address}.
     */
    public static Address fromStreetAddress(MultilingualString name, String number) {
        requireNonNull(name, "name must not be null");
        return new Address(name, number, null, null);
    }

    /**
     * Creates a new {@code Address} that is an intersection between two named roads.
     *
     * @param mainRoadName         the name of the main road, must not be {@code null}.
     * @param intersectingRoadName the name of the intersecting road, must not be {@code null}.
     * @return a new {@code Address}.
     */
    public static Address fromIntersection(MultilingualString mainRoadName, MultilingualString intersectingRoadName) {
        requireNonNull(mainRoadName, "mainRoadName must not be null");
        requireNonNull(intersectingRoadName, "intersectingRoadName must not be null");
        return new Address(mainRoadName, null, intersectingRoadName, null);
    }

    /**
     * Creates a new {@code Address} that has no name, only freely written comments that explain where it is.
     *
     * @param comments the comments, must not be {@code null}.
     * @return a new {@code Address}.
     */
    public static Address fromUnnamedLocation(String comments) {
        requireNonNull(comments, "comments must not be null");
        return new Address(null, null, null, comments);
    }

    /**
     * Whether this address points to an intersection between to roads.
     *
     * @see #roadOrAddressPointName()
     * @see #intersectingRoadName()
     */
    public boolean isIntersection() {
        return roadOrAddressPointName != null && intersectingRoadName != null;
    }

    /**
     * Whether this address is exact or approximate.
     */
    public boolean isExact() {
        return roadOrAddressPointName != null && (number != null || intersectingRoadName != null);
    }

    /**
     * Whether this address is unnamed.
     *
     * @see #comments()
     */
    public boolean isUnnamed() {
        return roadOrAddressPointName == null && intersectingRoadName == null;
    }
}
