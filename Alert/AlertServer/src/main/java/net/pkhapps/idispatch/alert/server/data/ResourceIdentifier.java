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
 * A value object used to identify a resource (such as a rescue unit).
 */
public class ResourceIdentifier extends WrappingValueObject<String> {

    private ResourceIdentifier(String wrapped) {
        super(wrapped);
    }

    /**
     * Creates a new {@code ResourceIdentifier} from the given string value.
     *
     * @param resourceIdentifier the string representation of the resource identifier, must not be {@code null}.
     * @return a new {@code ResourceIdentifier} object.
     */
    public static ResourceIdentifier fromString(String resourceIdentifier) {
        return new ResourceIdentifier(resourceIdentifier);
    }
}
