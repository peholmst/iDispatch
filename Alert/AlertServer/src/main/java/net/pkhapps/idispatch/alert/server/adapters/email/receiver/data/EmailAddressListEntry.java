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

import net.pkhapps.idispatch.alert.server.adapters.email.data.EmailAddress;
import net.pkhapps.idispatch.alert.server.data.AlertTextMessageTemplate;
import net.pkhapps.idispatch.alert.server.data.AlertTextMessageTemplateId;

import static java.util.Objects.requireNonNull;

/**
 * A record representing an entry in an {@link EmailAddressList}.
 *
 * @param name         the name of the recipient, must not be {@code null}.
 * @param emailAddress the e-mail address of the recipient, must not be {@code null}.
 * @param templateId   the ID of the {@link AlertTextMessageTemplate} to use when sending alert messages by e-mail to
 *                     this recipient.
 */
public record EmailAddressListEntry(String name, EmailAddress emailAddress, AlertTextMessageTemplateId templateId) {

    public EmailAddressListEntry {
        requireNonNull(name, "name must not be null");
        requireNonNull(emailAddress, "emailAddress must not be null");
        requireNonNull(templateId, "templateId must not be null");
    }
}
