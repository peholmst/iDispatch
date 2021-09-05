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

package net.pkhapps.idispatch.alert.server.adapters.email.data;

import net.pkhapps.idispatch.alert.server.data.Receiver;
import net.pkhapps.idispatch.alert.server.data.ReceiverId;
import net.pkhapps.idispatch.alert.server.data.ResourceIdentifier;

import java.util.Collection;

import static java.util.Objects.requireNonNull;

/**
 * A receiver specifying which e-mail addresses an alert should be sent to. Each receiver is associated with an {@link
 * EmailAddressList} to which the alert messages will be sent. That way, the same e-mail address list can be used for
 * multiple receivers if needed.
 */
public class EmailReceiver extends Receiver {

    private final EmailAddressListId emailAddressList;

    /**
     * Creates a new {@code EmailReceiver}.
     *
     * @param id               the ID of the receiver, must not be {@code null}.
     * @param enabled          whether the receiver is enabled or not.
     * @param resources        the resources that can be alerted through this receiver, must not be {@code null}.
     * @param emailAddressList the ID of the {@link EmailAddressList} that alerts should be sent to, must not be {@code
     *                         null}.
     */
    public EmailReceiver(ReceiverId id,
                         boolean enabled,
                         Collection<ResourceIdentifier> resources,
                         EmailAddressListId emailAddressList) {
        super(id, enabled, resources);
        this.emailAddressList = requireNonNull(emailAddressList, "emailAddressList must not be null");
    }

    /**
     * The ID of the {@link EmailAddressList} that alerts should be sent to.
     */
    public EmailAddressListId emailAddressList() {
        return emailAddressList;
    }
}
