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

import io.vavr.control.Either;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import net.pkhapps.idispatch.common.security.TOTP;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.time.Clock;
import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * Base class for user accounts. All users use the {@link TOTP} algorithm for authentication.
 */
@MappedSuperclass
public abstract class AbstractAccount {

    @Version
    @Column(name = "_opt_lock", nullable = false)
    private Long optLock;

    @Column(name = "not_valid_before", nullable = false)
    private Instant notValidBefore;

    @Column(name = "not_valid_after", nullable = false)
    private Instant notValidAfter;

    @Column(name = "locked", nullable = false)
    private boolean locked;

    @Column(name = "shared_secret_base64", nullable = false, length = 88) // 4 * (64/3), because the secret is 64 bytes
    private String sharedSecretBase64;

    @Column(name = "next_valid_time_step", nullable = false)
    private Long nextValidTimeStep;

    /**
     * Default constructor, only because Hibernate requires it. Application code should never use this constructor.
     */
    protected AbstractAccount() {
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
     */
    protected AbstractAccount(@NotNull Instant notValidBefore,
                              @NotNull Instant notValidAfter,
                              boolean locked,
                              @NotNull TOTP.SharedSecret sharedSecret,
                              @NotNull Clock clock) {
        this.notValidBefore = requireNonNull(notValidBefore);
        this.notValidAfter = requireNonNull(notValidAfter);
        this.locked = locked;
        this.sharedSecretBase64 = sharedSecret.toBase64String();
        this.nextValidTimeStep = TOTP.calculateTimeStep(clock.instant()).longValueExact();
    }

    /**
     * The instant before which this account is not valid, meaning all authentication attempts should be rejected even
     * though the credentials are valid.
     */
    public @NotNull Instant notValidBefore() {
        return requireNonNull(notValidBefore);
    }

    /**
     * The instant after which this account is not valid, meaning all authentication attempts should be rejected even
     * though the credentials are valid.
     */
    public @NotNull Instant notValidAfter() {
        return requireNonNull(notValidAfter);
    }

    /**
     * Returns whether this account is <b>invalid</b> at the given {@code timestamp}.
     *
     * @param timestamp the timestamp for which to check the account validity.
     * @return true if the account is invalid at the given timestamp, false if it is valid.
     * @see #notValidBefore()
     * @see #notValidAfter()
     */
    public boolean invalid(@NotNull Instant timestamp) {
        return timestamp.isBefore(notValidBefore()) || timestamp.isAfter(notValidAfter());
    }

    /**
     * The next valid time step. Exposed here for testing only.
     */
    @NotNull Long nextValidTimeStep() {
        return requireNonNull(nextValidTimeStep);
    }

    /**
     * Whether this account is currently locked or not. If the account is locked, all authentication attempts should be
     * rejected even though the credentials are valid.
     */
    public boolean locked() {
        return locked;
    }

    /**
     * <p>
     * Attempts to authenticate the user with the given one-time {@code password} using the {@link TOTP} algorithm. For
     * the authentication to succeed, the following conditions must be met:
     * </p>
     * <ul>
     *     <li>The current time step must be equal to or greater than the {@link #nextValidTimeStep()}.</li>
     *     <li>The account must not be {@link #locked()}.</li>
     *     <li>The account must be {@link #invalid(Instant) valid}.</li>
     *     <li>The password must match the password for either the current time step or the previous time step.</li>
     * </ul>
     * <p>
     * Each attempt will result in the {@link #nextValidTimeStep} being incremented at least by one, such that it is
     * always greater than the current time step. This means that the same password can never be used twice and that
     * consecutive failed authentication attempts will push the {@link #nextValidTimeStep()} further into the future.
     * </p>
     * <p>
     * Remember to persist the account after calling this method, otherwise the {@link #nextValidTimeStep()} will not
     * work as intended!
     * </p>
     *
     * @param password the password to validate.
     * @param clock    the clock to use when calculating the current time step.
     * @return an {@link Either} object containing either {@code null} if authentication was successful, or a
     * {@link AuthenticationFailureReason} if authentication failed.
     */
    public @NotNull Either<AuthenticationFailureReason, Void> authenticate(@NotNull String password, @NotNull Clock clock) {
        final var now = clock.instant();
        final var timeStep = TOTP.calculateTimeStep(now).longValueExact();
        final var nextValidTimeStep = this.nextValidTimeStep;
        if (timeStep > this.nextValidTimeStep) {
            this.nextValidTimeStep = timeStep + 1;
        } else {
            this.nextValidTimeStep++;
        }
        if (timeStep < nextValidTimeStep) {
            return Either.left(AuthenticationFailureReason.TIME_STEP_ALREADY_USED);
        }
        if (locked()) {
            return Either.left(AuthenticationFailureReason.ACCOUNT_LOCKED);
        }
        if (invalid(now)) {
            return Either.left(AuthenticationFailureReason.ACCOUNT_INVALID);
        }
        final var sharedSecret = TOTP.SharedSecret.fromBase64String(sharedSecretBase64);
        final var result = isSamePassword(sharedSecret, timeStep, password);
        if (!result && timeStep - 1 >= nextValidTimeStep) {
            return isSamePassword(sharedSecret, timeStep - 1, password) ? Either.right(null) : Either.left(AuthenticationFailureReason.PASSWORD_INCORRECT);
        } else {
            return result ? Either.right(null) : Either.left(AuthenticationFailureReason.PASSWORD_INCORRECT);
        }
    }

    private boolean isSamePassword(@NotNull TOTP.SharedSecret sharedSecret, long timeStep, @NotNull String password) {
        var expected = TOTP.generateOneTimePassword(sharedSecret, BigInteger.valueOf(timeStep));
        return expected.truncate(Math.min(8, password.length())).equals(password);
    }
}
