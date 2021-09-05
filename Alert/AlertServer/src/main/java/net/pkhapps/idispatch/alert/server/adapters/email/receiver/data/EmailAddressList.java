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

import net.pkhapps.idispatch.alert.server.data.Entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * An entity representing a list of e-mail addresses.
 */
public class EmailAddressList extends Entity<EmailAddressListId> implements Iterable<EmailAddressListEntry> {

    private final String name;
    private final Set<EmailAddressListEntry> entries;

    /**
     * Creates a new {@code EmailAddressList}.
     *
     * @param id      the ID of the e-mail address list, must not be {@code null}.
     * @param name    the name of the list, must not be {@code null}.
     * @param entries the entries of the list, must not be {@code null}.
     */
    public EmailAddressList(EmailAddressListId id, String name, Collection<EmailAddressListEntry> entries) {
        super(id);
        this.name = requireNonNull(name, "name must not be null");
        this.entries = Set.copyOf(requireNonNull(entries, "entries must not be null"));
    }

    /**
     * The name of the e-mail address list. This is mainly used to help human users identify the list.
     */
    public String name() {
        return name;
    }

    @Override
    public Iterator<EmailAddressListEntry> iterator() {
        return entries.iterator();
    }

    /**
     * Returns the entries of the e-mail address list as a stream.
     *
     * @return a stream of {@link EmailAddressListEntry} objects.
     */
    public Stream<EmailAddressListEntry> stream() {
        return entries.stream();
    }
}
