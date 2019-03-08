package net.pkhapps.idispatch.identity.server.domain;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import static net.pkhapps.idispatch.identity.server.domain.AggregateRootTestUtils.assertDomainEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link User}.
 */
public class UserTest {

    private MockDomainServices domainServices;

    @BeforeClass
    public static void setUpClass() {
        DomainServices.setInstanceHolderStrategy(DomainServices.ThreadLocalInstanceHolderStrategy.class);
    }

    @Before
    public void setUp() {
        domainServices = new MockDomainServices();
        DomainServices.setInstance(domainServices);
    }

    @Test
    public void stateAfterCreation() {
        var clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        domainServices.setClock(clock);

        var org = createTestOrganization();
        var user = new User("joecool", UserType.INDIVIDUAL, org);
        assertThat(user.getUsername()).isEqualTo("joecool");
        assertThat(user.getFullName()).isEqualTo("joecool");
        assertThat(user.getPassword()).isNull();
        assertThat(user.getAuthorities()).isEmpty();
        assertThat(user.getUserType()).isEqualTo(UserType.INDIVIDUAL);
        assertThat(user.getOrganization()).isEqualTo(org);
        assertThat(user.getLockedAt()).isNull();
        assertThat(user.getValidFrom()).isEqualTo(clock.instant());
        assertThat(user.getValidTo()).isNull();
        assertThat(user.isEnabled()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();

        assertDomainEvent(user, User.UsernameChangedEvent.class, event -> assertThat(event.getNewValue()).isEqualTo("joecool"));
        assertDomainEvent(user, User.FullNameChangedEvent.class, event -> assertThat(event.getNewValue()).isEqualTo("joecool"));
        assertDomainEvent(user, User.OrganizationChangedEvent.class, event -> assertThat(event.getNewValue()).isEqualTo(org));
        assertDomainEvent(user, User.UserTypeChangedEvent.class, event -> assertThat(event.getNewValue()).isEqualTo(UserType.INDIVIDUAL));
        assertDomainEvent(user, User.ValidFromChangedEvent.class, event -> assertThat(event.getNewValue()).isEqualTo(clock.instant()));
        assertDomainEvent(user, User.AccountEnabledEvent.class);
    }

    @Test(expected = IllegalStateException.class)
    public void changePassword_noCurrentPassword_exceptionThrown() throws Exception {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());

        user.changePassword("current", "new");
    }

    @Test(expected = User.InvalidCurrentPasswordException.class)
    public void changePassword_currentPasswordIncorrect_exceptionThrown() throws Exception {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.setPassword("{noop}current");

        user.changePassword("wrong current", "new");
    }

    @Test
    public void changePassword_currentPasswordCorrect_passwordChanged() throws Exception {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.setPassword("{noop}current");
        user.clearDomainEvents();

        user.changePassword("current", "new");
        assertThat(user.getPassword()).startsWith("{bcrypt}");
        assertDomainEvent(user, User.PasswordChangedEvent.class);
    }

    @Test
    public void isAccountNonExpired_validFromIsNow_accountValid() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        assertThat(user.isAccountNonExpired()).isTrue();
    }

    @Test
    public void isAccountNonExpired_validFromIsInTheFuture_accountExpired() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        domainServices.setClock(Clock.fixed(domainServices.clock().instant().minusSeconds(1), ZoneId.systemDefault()));
        assertThat(user.isAccountNonExpired()).isFalse();
    }

    @Test
    public void isAccountNonExpired_validFromIsInThePast_accountValid() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        domainServices.setClock(Clock.fixed(domainServices.clock().instant().plusSeconds(1), ZoneId.systemDefault()));
        assertThat(user.isAccountNonExpired()).isTrue();
    }

    @Test
    public void isAccountNonExpired_validToIsInTheFuture_accountValid() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.setValidTo(user.getValidFrom().plusSeconds(2));
        domainServices.setClock(Clock.fixed(domainServices.clock().instant().plusSeconds(1), ZoneId.systemDefault()));
        assertThat(user.isAccountNonExpired()).isTrue();
    }

    @Test
    public void isAccountNonExpired_validToIsNow_accountExpired() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.setValidTo(user.getValidFrom().plusSeconds(1));
        domainServices.setClock(Clock.fixed(user.getValidTo(), ZoneId.systemDefault()));
        assertThat(user.isAccountNonExpired()).isFalse();
    }

    @Test
    public void isAccountNonExpired_validToIsInThePast_accountExpired() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.setValidTo(user.getValidFrom().plusSeconds(1));
        domainServices.setClock(Clock.fixed(user.getValidTo().plusSeconds(1), ZoneId.systemDefault()));
        assertThat(user.isAccountNonExpired()).isFalse();
    }

    @Test
    public void setFullName() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.clearDomainEvents();
        user.setFullName("Joe Cool");

        assertThat(user.getFullName()).isEqualTo("Joe Cool");
        assertDomainEvent(user, User.FullNameChangedEvent.class, event -> {
            assertThat(event.getOldValue()).isEqualTo("joecool");
            assertThat(event.getNewValue()).isEqualTo("Joe Cool");
        });
    }

    @Test
    public void setUserType() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.clearDomainEvents();
        user.setUserType(UserType.ROLE);

        assertThat(user.getUserType()).isEqualTo(UserType.ROLE);
        assertDomainEvent(user, User.UserTypeChangedEvent.class, event -> {
            assertThat(event.getOldValue()).isEqualTo(UserType.INDIVIDUAL);
            assertThat(event.getNewValue()).isEqualTo(UserType.ROLE);
        });
    }

    @Test
    public void addAuthority() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.clearDomainEvents();
        user.addAuthority(new SimpleGrantedAuthority("ROLE_FOOBAR"));

        assertThat(user.getAuthorities().stream().map(GrantedAuthority::getAuthority)).containsExactly("ROLE_FOOBAR");
        assertDomainEvent(user, User.AuthorityAddedEvent.class, event -> assertThat(event.getAddedAuthority().getAuthority()).isEqualTo("ROLE_FOOBAR"));
    }

    @Test
    public void removeAuthority() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.addAuthority(new SimpleGrantedAuthority("ROLE_FOOBAR"));
        user.clearDomainEvents();
        user.removeAuthority(new SimpleGrantedAuthority("ROLE_FOOBAR"));

        assertThat(user.getAuthorities()).isEmpty();
        assertDomainEvent(user, User.AuthorityRemovedEvent.class, event -> assertThat(event.getRemovedAuthority().getAuthority()).isEqualTo("ROLE_FOOBAR"));
    }

    @Test
    public void lock() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.lock();

        assertThat(user.getLockedAt()).isEqualTo(domainServices.clock().instant());
        assertDomainEvent(user, User.AccountLockedEvent.class);
    }

    @Test
    public void unlock() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.lock();
        user.clearDomainEvents();
        user.unlock();

        assertThat(user.getLockedAt()).isNull();
        assertDomainEvent(user, User.AccountUnlockedEvent.class);
    }

    @Test
    public void isAccountNonLocked_nowAtStartOfLockDuration_locked() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.lock();
        when(domainServices.properties().getUserAccountLockDuration()).thenReturn(Duration.ofSeconds(10));

        assertThat(user.isAccountNonLocked()).isFalse();
    }

    @Test
    public void isAccountNonLocked_nowInMiddleOfLockDuration_locked() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.lock();
        when(domainServices.properties().getUserAccountLockDuration()).thenReturn(Duration.ofSeconds(10));
        domainServices.setClock(Clock.fixed(domainServices.clock().instant().plusSeconds(5), ZoneId.systemDefault()));

        assertThat(user.isAccountNonLocked()).isFalse();
    }

    @Test
    public void isAccountNonLocked_nowAtEndOfLockDuration_locked() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.lock();
        when(domainServices.properties().getUserAccountLockDuration()).thenReturn(Duration.ofSeconds(10));
        domainServices.setClock(Clock.fixed(domainServices.clock().instant().plusSeconds(10), ZoneId.systemDefault()));

        assertThat(user.isAccountNonLocked()).isFalse();
    }

    @Test
    public void isAccountNonLocked_nowPastEndOfLockDuration_unlocked() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.lock();
        when(domainServices.properties().getUserAccountLockDuration()).thenReturn(Duration.ofSeconds(10));
        domainServices.setClock(Clock.fixed(domainServices.clock().instant().plusSeconds(11), ZoneId.systemDefault()));

        assertThat(user.isAccountNonLocked()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void setValidTo_validToBeforeValidFrom_exceptionThrown() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.setValidTo(user.getValidFrom().minusSeconds(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setValidFrom_validFormAfterValidTo_exceptionThrown() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.setValidTo(user.getValidFrom().plusSeconds(10));
        user.setValidFrom(user.getValidTo().plusSeconds(10));
    }

    @Test
    public void disableUser() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.clearDomainEvents();
        user.setEnabled(false);

        assertThat(user.isEnabled()).isFalse();
        assertDomainEvent(user, User.AccountDisabledEvent.class);
    }

    @Test
    public void enableUser() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.setEnabled(false);
        user.clearDomainEvents();
        user.setEnabled(true);

        assertThat(user.isEnabled()).isTrue();
        assertDomainEvent(user, User.AccountEnabledEvent.class);
    }

    @Test
    public void isEnabled_organizationIsDisabled_userIsDisabled() {
        var user = new User("joecool", UserType.INDIVIDUAL, createTestOrganization());
        user.getOrganization().setEnabled(false);

        assertThat(user.isEnabled()).isFalse();
    }

    private Organization createTestOrganization() {
        return new Organization("Test Organization");
    }
}