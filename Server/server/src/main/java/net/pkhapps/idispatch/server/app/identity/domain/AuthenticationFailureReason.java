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

package net.pkhapps.idispatch.server.app.identity.domain;

import java.time.Instant;

/**
 * Enumeration of authentication failures. These should not be returned to the client for security reasons (we don't
 * want to give away why the authentication attempt failed), but can be used for audit logging on the server.
 */
public enum AuthenticationFailureReason {
    /**
     * Authentication failed because the account was {@linkplain AbstractAccount#locked() locked}.
     */
    ACCOUNT_LOCKED,
    /**
     * Authentication failed because the account was {@linkplain AbstractAccount#invalid(Instant) invalid}.
     */
    ACCOUNT_INVALID,
    /**
     * Authentication failed because the current time step had already been used.
     *
     * @see AbstractAccount#nextValidTimeStep()
     */
    TIME_STEP_ALREADY_USED,
    /**
     * Authentication failed because the one-time password was incorrect.
     */
    PASSWORD_INCORRECT
}
