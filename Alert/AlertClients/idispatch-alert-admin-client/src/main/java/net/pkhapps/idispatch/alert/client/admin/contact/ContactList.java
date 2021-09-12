/*
 * iDispatch Alert Clients
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

package net.pkhapps.idispatch.alert.client.admin.contact;

import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * A list of e-mail addresses and phone numbers that is used for sending alerts through e-mail and SMS.
 *
 * @param id    the ID of the contact list, must not be {@code null}.
 * @param name  the name of the contact list, must not be {@code null}.
 * @param items the items of the contact list, must not be {@code null}.
 */
public record ContactList(ContactListId id, String name, Set<ContactListItem> items) {

    public ContactList {
        requireNonNull(id, "id must not be null");
        requireNonNull(name, "name must not be null");
        requireNonNull(items, "items must not be null");
    }
}
