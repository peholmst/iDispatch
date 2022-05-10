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

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import net.pkhapps.idispatch.common.security.TOTP;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Clock;
import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * Account for human users that interact with the system "as themselves".
 *
 * @see DeviceAccount
 */
@Table(name = "users")
public class UserAccount extends AbstractAccount {

    private static final int USERNAME_MAX_LENGTH = 250;
    private static final int DISPLAY_NAME_MAX_LENGTH = 250;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "username", nullable = false, unique = true, length = USERNAME_MAX_LENGTH)
    private String username;

    @Column(name = "display_name", nullable = false, length = DISPLAY_NAME_MAX_LENGTH)
    private String displayName;

    /**
     * Default constructor, only because Hibernate requires it. Application code should never use this constructor.
     */
    protected UserAccount() {
    }

    /**
     * Initializing constructor. Application code should use this constructor. The {@link #nextValidTimeStep()} will be
     * initialized to the current time step according to the given {@code clock}.
     *
     * @param notValidBefore the instant before which the account is not valid.
     * @param notValidAfter  the instant after which the account is not valid.
     * @param locked         whether the account is locked or not.
     * @param sharedSecret   the shared secret of the account.
     * @param clock          the clock to use for getting the current time.
     * @param userId         a unique ID used to identify the user. This will never change.
     * @param username       a unique username (max {@value #USERNAME_MAX_LENGTH} characters) used by the user to
     *                       identify themselves. This can be changed later.
     * @param displayName    the name (max {@value #DISPLAY_NAME_MAX_LENGTH} characters) to show to other users. This
     *                       can be changed later.
     */
    public UserAccount(@NotNull Instant notValidBefore,
                       @NotNull Instant notValidAfter,
                       boolean locked,
                       @NotNull TOTP.SharedSecret sharedSecret,
                       @NotNull Clock clock,
                       @NotNull Long userId,
                       @NotNull String username,
                       @NotNull String displayName) {
        super(notValidBefore, notValidAfter, locked, sharedSecret, clock);
        this.userId = requireNonNull(userId);
        this.username = requireNonNull(StringUtils.truncate(username, USERNAME_MAX_LENGTH));
        this.displayName = requireNonNull(StringUtils.truncate(displayName, DISPLAY_NAME_MAX_LENGTH));
    }

    /**
     * A unique, numeric ID used to identify the user. This will never change.
     */
    public @NotNull Long userId() {
        return requireNonNull(userId);
    }

    /**
     * A unique username used by the user to identify themselves. This can be changed later and should only be used to
     * identify the user while attempting {@link #authenticate(String, Clock) authentication}.
     */
    public @NotNull String username() {
        return requireNonNull(username);
    }

    /**
     * The name to show to other users. This can be changed later and is not even required to be unique.
     */
    public @NotNull String displayName() {
        return requireNonNull(displayName);
    }
}
