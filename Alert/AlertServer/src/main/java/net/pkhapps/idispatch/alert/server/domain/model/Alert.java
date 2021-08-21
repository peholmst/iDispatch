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
package net.pkhapps.idispatch.alert.server.domain.model;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Set;

import net.pkhapps.idispatch.alert.server.domain.base.Entity;

/**
 * TODO Document me
 */
public class Alert extends Entity<AlertId> {

    private final Set<ResourceIdentifier> resourcesToAlert;

    /**
     * 
     * @param id
     * @param essence
     */
    public Alert(AlertId id, Essence essence) {
        super(id);
        requireNonNull(essence, "essence cannot be null");
        if (essence.resourcesToAlert.isEmpty()) {
            throw new IllegalArgumentException("resourcesToAlert must contain at least one item");
        }
        this.resourcesToAlert = Set.copyOf(essence.resourcesToAlert);
    }

    /**
     * 
     * @return
     */
    public Set<ResourceIdentifier> resourcesToAlert() {
        return resourcesToAlert;
    }

    /**
     * 
     */
    public static class Essence {
        private Set<ResourceIdentifier> resourcesToAlert = Collections.emptySet();

        /**
         * 
         * @param resourcesToAlert
         * @return
         */
        public Essence setResourcesToAlert(Set<ResourceIdentifier> resourcesToAlert) {
            this.resourcesToAlert = requireNonNull(resourcesToAlert, "resourcesToAlert cannot be null");
            return this;
        }
    }
}
