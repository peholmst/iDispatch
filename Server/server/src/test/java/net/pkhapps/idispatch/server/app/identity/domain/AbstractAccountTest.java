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

import net.pkhapps.idispatch.common.security.TOTP;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractAccountTest {

    @Test
    void authenticate_unlocked_success() {
        var clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        var secret = TOTP.SharedSecret.fromRandom();
        var account = new TestAccount(
                clock.instant().minusSeconds(1000),
                clock.instant().plusSeconds(1000),
                false,
                secret,
                clock);
        var password = TOTP.generateOneTimePassword(secret, clock.instant()).truncate(8);
        assertThat(account.authenticate(password, clock).isRight()).isTrue();
        assertThat(account.nextValidTimeStep()).isEqualTo(TOTP.calculateTimeStep(clock.instant()).longValueExact() + 1);
    }

    @Test
    void authenticate_locked_failure() {
        var clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        var secret = TOTP.SharedSecret.fromRandom();
        var account = new TestAccount(
                clock.instant().minusSeconds(1000),
                clock.instant().plusSeconds(1000),
                true,
                secret,
                clock);
        var password = TOTP.generateOneTimePassword(secret, clock.instant()).truncate(8);
        assertThat(account.authenticate(password, clock).getLeft()).isEqualTo(AuthenticationFailureReason.ACCOUNT_LOCKED);
        assertThat(account.nextValidTimeStep()).isEqualTo(TOTP.calculateTimeStep(clock.instant()).longValueExact() + 1);
    }

    @Test
    void authenticate_beforeValid_failure() {
        var clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        var secret = TOTP.SharedSecret.fromRandom();
        var account = new TestAccount(
                clock.instant().plusMillis(1),
                clock.instant().plusSeconds(2000),
                false,
                secret,
                clock);
        var password = TOTP.generateOneTimePassword(secret, clock.instant()).truncate(8);
        assertThat(account.authenticate(password, clock).getLeft()).isEqualTo(AuthenticationFailureReason.ACCOUNT_INVALID);
        assertThat(account.nextValidTimeStep()).isEqualTo(TOTP.calculateTimeStep(clock.instant()).longValueExact() + 1);
    }

    @Test
    void authenticate_afterValid_failure() {
        var clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        var secret = TOTP.SharedSecret.fromRandom();
        var account = new TestAccount(
                clock.instant().minusSeconds(2000),
                clock.instant().minusMillis(1),
                false,
                secret,
                clock);
        var password = TOTP.generateOneTimePassword(secret, clock.instant()).truncate(8);
        assertThat(account.authenticate(password, clock).getLeft()).isEqualTo(AuthenticationFailureReason.ACCOUNT_INVALID);
        assertThat(account.nextValidTimeStep()).isEqualTo(TOTP.calculateTimeStep(clock.instant()).longValueExact() + 1);
    }

    @Test
    void authenticate_sameTimeStep_failure() {
        var clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        var secret = TOTP.SharedSecret.fromRandom();
        var account = new TestAccount(
                clock.instant().minusSeconds(1000),
                clock.instant().plusSeconds(1000),
                false,
                secret,
                clock);
        var password = TOTP.generateOneTimePassword(secret, clock.instant()).truncate(8);
        assertThat(account.authenticate(password, clock).isRight()).isTrue();
        assertThat(account.authenticate(password, clock).getLeft()).isEqualTo(AuthenticationFailureReason.TIME_STEP_ALREADY_USED);
        assertThat(account.nextValidTimeStep()).isEqualTo(TOTP.calculateTimeStep(clock.instant()).longValueExact() + 2);
    }

    @Test
    void authenticate_previousTimeStepButStillValid_success() {
        var pastClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        var secret = TOTP.SharedSecret.fromRandom();
        var account = new TestAccount(
                pastClock.instant().minusSeconds(1000),
                pastClock.instant().plusSeconds(1000),
                false,
                secret,
                pastClock);

        var currentClock = Clock.fixed(pastClock.instant().plusSeconds(65), ZoneId.systemDefault());
        var password = TOTP.generateOneTimePassword(secret,
                currentClock.instant().minusSeconds(30)).truncate(8);
        assertThat(account.authenticate(password, currentClock).isRight()).isTrue();
        assertThat(account.nextValidTimeStep()).isEqualTo(TOTP.calculateTimeStep(currentClock.instant()).longValueExact() + 1);
    }

    static class TestAccount extends AbstractAccount {
        public TestAccount(@NotNull Instant notValidBefore,
                           @NotNull Instant notValidAfter,
                           boolean locked,
                           @NotNull TOTP.SharedSecret sharedSecret,
                           @NotNull Clock clock) {
            super(notValidBefore, notValidAfter, locked, sharedSecret, clock);
        }
    }
}
