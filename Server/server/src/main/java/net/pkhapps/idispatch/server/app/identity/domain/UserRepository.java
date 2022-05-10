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

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Repository of {@link UserAccount}s. A data storage adapter should implement this interface.
 */
public interface UserRepository {

    /**
     * Looks up the {@link UserAccount} with the given {@code username}.
     *
     * @param username the username to look up.
     * @return a {@link Mono} emitting a single {@link UserAccount} if found, or no value if not found.
     */
    @NotNull Mono<UserAccount> findByUsername(@NotNull String username);

    /**
     * Saves the given {@code userAccount}.
     *
     * @param userAccount the user account to save.
     * @return a {@link Mono} emitting a single {@link UserAccount} (can be a different instance than
     * {@code userAccount}) on success, or an error.
     */
    @NotNull Mono<UserAccount> save(@NotNull UserAccount userAccount);
}
