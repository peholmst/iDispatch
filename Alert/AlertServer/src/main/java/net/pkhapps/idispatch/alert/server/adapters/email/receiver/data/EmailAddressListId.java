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

package net.pkhapps.idispatch.alert.server.adapters.email.receiver.data;

import net.pkhapps.idispatch.alert.server.data.WrappingValueObject;

import java.util.UUID;

/**
 * A value object used to identify an {@link EmailAddressList}.
 */
public class EmailAddressListId extends WrappingValueObject<String> {

    private EmailAddressListId(String wrapped) {
        super(wrapped);
    }

    /**
     * Creates a new random {@code EmailAddressListId}.
     *
     * @return a new {@code EmailAddressListId}.
     */
    public static EmailAddressListId randomEmailAddressListId() {
        return new EmailAddressListId(UUID.randomUUID().toString());
    }

    /**
     * Creates a new {@code EmailAddressListId} from the given string value.
     *
     * @param emailAddressListId the string representation of the ID, must not be {@code null}.
     * @return a new {@code EmailAddressListId}.
     */
    public static EmailAddressListId fromString(String emailAddressListId) {
        return new EmailAddressListId(emailAddressListId);
    }
}
