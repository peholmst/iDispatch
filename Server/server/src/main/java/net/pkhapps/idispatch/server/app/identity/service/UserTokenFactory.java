/*
 * iDispatch Server
 *
 * Copyright (c) 2022 Petter Holmström
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.pkhapps.idispatch.server.app.identity.service;

import net.pkhapps.idispatch.server.app.identity.domain.UserAccount;
import net.pkhapps.idispatch.server.app.infra.security.UserToken;
import org.jetbrains.annotations.NotNull;

import java.time.Clock;

/**
 * Factory interface for creating new {@link UserToken}s from {@link UserAccount}s.
 */
public interface UserTokenFactory {

    /**
     * Creates a new {@link UserToken} for the given {@code user}.
     *
     * @param user  the user for which a token should be created.
     * @param clock the clock to use for getting the current time.
     * @return a new, valid {@link UserToken}.
     */
    @NotNull UserToken createUserToken(@NotNull UserAccount user, @NotNull Clock clock);
}
