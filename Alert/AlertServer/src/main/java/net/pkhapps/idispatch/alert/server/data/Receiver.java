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

import java.util.Collection;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * An entity representing a receiver of an alert. There can be multiple types of receivers that receive alerts in
 * different ways, such as by e-mail, SMS, a message broker, etc.
 */
public abstract class Receiver extends Entity<ReceiverId> {

    private final boolean enabled;
    private final Set<ResourceIdentifier> resources;

    /**
     * Creates a new {@code Receiver}.
     *
     * @param id        the ID of the receiver, must not be {@code null}.
     * @param enabled   whether the receiver is enabled or not.
     * @param resources the resources that can be alerted through this receiver, must not be {@code null}.
     */
    public Receiver(ReceiverId id, boolean enabled, Collection<ResourceIdentifier> resources) {
        super(id);
        this.enabled = enabled;
        this.resources = Set.copyOf(requireNonNull(resources, "resources must not be null"));
    }

    /**
     * Whether this receiver is enabled or not. Alerts are only distributed to enabled receivers.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * The resources that can be alerted through this receiver.
     *
     * @return an immutable set of {@link ResourceIdentifier}s, never {@code null}.
     */
    public Set<ResourceIdentifier> resources() {
        return resources;
    }
}
