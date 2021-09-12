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

package net.pkhapps.idispatch.alert.client.admin.resource;

import net.pkhapps.idispatch.alert.client.admin.base.Essence;

/**
 * Essence for constructing new {@link Resource}s.
 */
public class ResourceEssence extends Essence {

    private String globalIdentifier;

    /**
     * Creates a new, empty {@code ResourceEssence}.
     */
    public ResourceEssence() {
    }

    /**
     * Creates a new {@code ResourceEssence} and populates it with data from the given resource.
     *
     * @param resource the resource to base the essence on. If {@code null}, an empty essence will be created.
     */
    public ResourceEssence(Resource resource) {
        if (resource != null) {
            globalIdentifier = resource.globalIdentifier();
        }
    }

    /**
     * @see Resource#globalIdentifier()
     */
    public String globalIdentifier() {
        return globalIdentifier;
    }

    /**
     * @see Resource#globalIdentifier()
     */
    public ResourceEssence setGlobalIdentifier(String globalIdentifier) {
        this.globalIdentifier = globalIdentifier;
        return this;
    }

    @Override
    public void validate() {
        checkNonNull(globalIdentifier, "globalIdentifier");
    }
}
