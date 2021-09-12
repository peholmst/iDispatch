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

import net.pkhapps.idispatch.alert.client.admin.base.Essence;

import java.util.HashSet;
import java.util.Set;

/**
 * Essence for constructing new {@link ContactList}s.
 */
public class ContactListEssence extends Essence {

    private String name;
    private Set<ContactListItem> items;

    /**
     * Creates a new, empty {@code ContactListEssence}.
     */
    public ContactListEssence() {
        items = new HashSet<>();
    }

    /**
     * Creates a new {@code ContactListEssence} and populates it with data from the given contact list.
     *
     * @param contactList the contact list to base the essence on. If {@code null}, an empty essence will be created.
     */
    public ContactListEssence(ContactList contactList) {
        if (contactList != null) {
            name = contactList.name();
            items = new HashSet<>(contactList.items());
        } else {
            items = new HashSet<>();
        }
    }

    /**
     * @see ContactList#name()
     */
    public ContactListEssence setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see ContactList#name()
     */
    public String name() {
        return name;
    }

    /**
     * @see ContactList#items()
     */
    public ContactListEssence setItems(Set<ContactListItem> items) {
        this.items = items == null ? new HashSet<>() : items;
        return this;
    }

    /**
     * @see ContactList#items()
     */
    public Set<ContactListItem> items() {
        return items;
    }

    /**
     * @see ContactList#items()
     */
    public ContactListEssence removeItem(ContactListItem item) {
        items.remove(item);
        return this;
    }

    /**
     * @see ContactList#items()
     */
    public ContactListEssence addItem(String name, EmailAddress emailAddress, PhoneNumber phoneNumber) {
        items.add(new ContactListItem(name, emailAddress, phoneNumber));
        return this;
    }

    /**
     * @see ContactList#items()
     */
    public ContactListEssence replaceItem(ContactListItem item, String name, EmailAddress emailAddress, PhoneNumber phoneNumber) {
        removeItem(item);
        addItem(name, emailAddress, phoneNumber);
        return this;
    }

    @Override
    public void validate() {
        checkNonNull(name, "name");
    }
}
