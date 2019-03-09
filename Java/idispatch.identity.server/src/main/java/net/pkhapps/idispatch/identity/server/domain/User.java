package net.pkhapps.idispatch.identity.server.domain;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.identity.server.util.Strings.requireMaxLength;

/**
 * Aggregate root representing a user of the system.
 */
@Entity
@Table(name = "user", schema = "idispatch_identity")
@Getter
public class User extends AggregateRoot<User> implements UserDetails {

    private static final int STRING_MAX_LENGTH = 255;

    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "enabled")
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;
    @Column(name = "valid_from", nullable = false)
    private Instant validFrom;
    @Column(name = "valid_to")
    private Instant validTo;
    @Column(name = "locked_at")
    private Instant lockedAt;
    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authority", schema = "idispatch_identity", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authority", nullable = false)
    @Getter(AccessLevel.NONE)
    private Set<String> authorities = new HashSet<>();

    User() {
    }

    public User(@NonNull String username, @NonNull UserType userType, @NonNull Organization organization) {
        setUsername(username);
        setFullName(username);
        setUserType(userType);
        setOrganization(organization);
        setValidFrom(DomainServices.getInstance().clock().instant());
        setEnabled(true);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password without checking the current password. This method would typically be called by admins.
     *
     * @param encodedPassword the encoded password or {@code null}.
     */
    public void setPassword(@Nullable String encodedPassword) {
        this.password = requireMaxLength(encodedPassword, STRING_MAX_LENGTH);
        registerEvent(new PasswordChangedEvent(this));
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        requireNonNull(requireMaxLength(username, STRING_MAX_LENGTH));
        if (!username.equals(this.username)) {
            var old = this.username;
            this.username = username;
            registerEvent(new UsernameChangedEvent(this, old, username));
        }
    }

    @Override
    public boolean isAccountNonExpired() {
        var now = DomainServices.getInstance().clock().instant();
        return validFrom.compareTo(now) <= 0 && (validTo == null || validTo.isAfter(now));
    }

    @Override
    public boolean isAccountNonLocked() {
        if (lockedAt != null) {
            var now = DomainServices.getInstance().clock().instant();
            var duration = DomainServices.getInstance().properties().getUserAccountLockDuration();
            return now.isAfter(lockedAt.plus(duration));
        }
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // We don't support this yet
    }

    @Override
    public boolean isEnabled() {
        return enabled && organization.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                registerEvent(new AccountEnabledEvent(this));
            } else {
                registerEvent(new AccountDisabledEvent(this));
            }
        }
    }

    public void lock() {
        var now = DomainServices.getInstance().clock().instant();
        if (!now.equals(this.lockedAt)) {
            this.lockedAt = now;
            registerEvent(new AccountLockedEvent(this));
        }
    }

    public void unlock() {
        if (this.lockedAt != null) {
            this.lockedAt = null;
            registerEvent(new AccountUnlockedEvent(this));
        }
    }

    public void setFullName(@NonNull String fullName) {
        requireNonNull(requireMaxLength(fullName, STRING_MAX_LENGTH));
        if (!fullName.equals(this.fullName)) {
            var old = this.fullName;
            this.fullName = fullName;
            registerEvent(new FullNameChangedEvent(this, old, fullName));
        }
    }

    public void setValidFrom(@NonNull Instant validFrom) {
        requireNonNull(validFrom);
        if (validTo != null && validFrom.isAfter(validTo)) {
            throw new IllegalArgumentException("validFrom cannot be after validTo");
        }
        if (!validFrom.equals(this.validFrom)) {
            var old = this.validFrom;
            this.validFrom = validFrom;
            registerEvent(new ValidFromChangedEvent(this, old, validFrom));
        }
    }

    public void setValidTo(@Nullable Instant validTo) {
        if (validTo != null && validTo.isBefore(validFrom)) {
            throw new IllegalArgumentException("validTo cannot be before validFrom");
        }
        if (!Objects.equals(validTo, this.validTo)) {
            var old = this.validTo;
            this.validTo = validTo;
            registerEvent(new ValidToChangedEvent(this, old, validTo));
        }
    }

    /**
     * Changes an existing password to a new one. This method would typically be called by users.
     *
     * @param currentPassword the current password.
     * @param newPassword     the new password.
     * @throws InvalidCurrentPasswordException if the {@code currentPassword} does not match.
     * @throws IllegalStateException           if this user account currently has no password to change (need to use {@link #setPassword(String)} instead in this case).
     */
    public void changePassword(@NonNull String currentPassword, @NonNull String newPassword) throws InvalidCurrentPasswordException {
        requireNonNull(newPassword);
        requireNonNull(currentPassword);
        // TODO Add password validation
        if (this.password == null) {
            throw new IllegalStateException("This user account has no password to change");
        }

        var encoder = DomainServices.getInstance().passwordEncoder();
        if (!encoder.matches(currentPassword, this.password)) {
            throw new InvalidCurrentPasswordException("The provided password does not match the stored one");
        }
        setPassword(encoder.encode(newPassword));
    }

    public void setUserType(@NonNull UserType userType) {
        requireNonNull(userType);
        if (!userType.equals(this.userType)) {
            var old = this.userType;
            this.userType = userType;
            registerEvent(new UserTypeChangedEvent(this, old, userType));
        }
    }

    public void setOrganization(@NonNull Organization organization) {
        requireNonNull(organization);
        if (!organization.equals(this.organization)) {
            var old = this.organization;
            this.organization = organization;
            registerEvent(new OrganizationChangedEvent(this, old, organization));
        }
    }

    public void addAuthority(@NonNull GrantedAuthority authority) {
        requireNonNull(authority);
        requireMaxLength(authority.getAuthority(), STRING_MAX_LENGTH);
        if (this.authorities.add(authority.getAuthority())) {
            registerEvent(new AuthorityAddedEvent(this, authority));
        }
    }

    public void removeAuthority(@NonNull GrantedAuthority authority) {
        requireNonNull(authority);
        if (this.authorities.remove(authority.getAuthority())) {
            registerEvent(new AuthorityRemovedEvent(this, authority));
        }
    }

    public static class InvalidCurrentPasswordException extends Exception {

        InvalidCurrentPasswordException(String message) {
            super(message);
        }
    }

    @Getter
    public static abstract class UserDomainEvent extends DomainEvent {

        private final User user;

        UserDomainEvent(@NonNull User user) {
            this.user = requireNonNull(user, "user must not be null");
        }
    }

    @Getter
    public static class AuthorityAddedEvent extends UserDomainEvent {

        private final GrantedAuthority addedAuthority;

        AuthorityAddedEvent(@NonNull User user, @NonNull GrantedAuthority addedAuthority) {
            super(user);
            this.addedAuthority = addedAuthority;
        }
    }

    @Getter
    public static class AuthorityRemovedEvent extends UserDomainEvent {

        private final GrantedAuthority removedAuthority;

        AuthorityRemovedEvent(@NonNull User user, @NonNull GrantedAuthority removedAuthority) {
            super(user);
            this.removedAuthority = removedAuthority;
        }
    }

    public static class PasswordChangedEvent extends UserDomainEvent {

        PasswordChangedEvent(@NonNull User user) {
            super(user);
        }
    }

    public static class AccountLockedEvent extends UserDomainEvent {

        AccountLockedEvent(@NonNull User user) {
            super(user);
        }
    }

    public static class AccountUnlockedEvent extends UserDomainEvent {

        AccountUnlockedEvent(@NonNull User user) {
            super(user);
        }
    }

    public static class AccountEnabledEvent extends UserDomainEvent {

        AccountEnabledEvent(@NonNull User user) {
            super(user);
        }
    }

    public static class AccountDisabledEvent extends UserDomainEvent {

        AccountDisabledEvent(@NonNull User user) {
            super(user);
        }
    }

    @Getter
    public static abstract class UserPropertyChangeEvent<T> extends UserDomainEvent {

        private final T oldValue;
        private final T newValue;

        UserPropertyChangeEvent(@NonNull User user, T oldValue, T newValue) {
            super(user);
            this.oldValue = oldValue;
            this.newValue = newValue;
        }
    }

    public static class UsernameChangedEvent extends UserPropertyChangeEvent<String> {

        UsernameChangedEvent(@NonNull User user, @Nullable String oldUsername, @NonNull String newUsername) {
            super(user, oldUsername, newUsername);
        }
    }

    public static class OrganizationChangedEvent extends UserPropertyChangeEvent<Organization> {

        OrganizationChangedEvent(@NonNull User user, @Nullable Organization oldOrganization, @NonNull Organization newOrganization) {
            super(user, oldOrganization, newOrganization);
        }
    }

    public static class UserTypeChangedEvent extends UserPropertyChangeEvent<UserType> {

        UserTypeChangedEvent(@NonNull User user, @Nullable UserType oldUserType, @NonNull UserType newUserType) {
            super(user, oldUserType, newUserType);
        }
    }

    public static class FullNameChangedEvent extends UserPropertyChangeEvent<String> {

        FullNameChangedEvent(@NonNull User user, @Nullable String oldFullName, @NonNull String newFullName) {
            super(user, oldFullName, newFullName);
        }
    }

    public static class ValidFromChangedEvent extends UserPropertyChangeEvent<Instant> {

        ValidFromChangedEvent(@NonNull User user, @Nullable Instant oldValue, @NonNull Instant newValue) {
            super(user, oldValue, newValue);
        }
    }

    public static class ValidToChangedEvent extends UserPropertyChangeEvent<Instant> {

        ValidToChangedEvent(@NonNull User user, @Nullable Instant oldValue, @Nullable Instant newValue) {
            super(user, oldValue, newValue);
        }
    }
}
