package net.pkhapps.idispatch.identity.server.domain;

import net.pkhapps.idispatch.base.domain.AggregateRoot;
import net.pkhapps.idispatch.base.domain.UserId;
import net.pkhapps.idispatch.base.domain.UserIdConverter;
import net.pkhapps.idispatch.base.domain.event.AggregateRootDomainEvent;
import net.pkhapps.idispatch.base.domain.event.AggregateRootPropertyChangeEvent;
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
import static net.pkhapps.idispatch.base.domain.util.Strings.requireMaxLength;

/**
 * Aggregate root representing a user of the system.
 */
@Entity
@Table(name = "user", schema = "idispatch_identity")
public class User extends AggregateRoot<UserId> implements UserDetails {

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
    private Set<String> authorities = new HashSet<>();

    User() {
        super(new UserIdConverter());
    }

    public User(@NonNull String username, @NonNull UserType userType, @NonNull Organization organization) {
        this();
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

    @Nullable
    public Instant getLockedAt() {
        return lockedAt;
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

    @NonNull
    public String getFullName() {
        return fullName;
    }

    public void setFullName(@NonNull String fullName) {
        requireNonNull(requireMaxLength(fullName, STRING_MAX_LENGTH));
        if (!fullName.equals(this.fullName)) {
            var old = this.fullName;
            this.fullName = fullName;
            registerEvent(new FullNameChangedEvent(this, old, fullName));
        }
    }

    @NonNull
    public Instant getValidFrom() {
        return validFrom;
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

    @Nullable
    public Instant getValidTo() {
        return validTo;
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

    @NonNull
    public UserType getUserType() {
        return userType;
    }

    public void setUserType(@NonNull UserType userType) {
        requireNonNull(userType);
        if (!userType.equals(this.userType)) {
            var old = this.userType;
            this.userType = userType;
            registerEvent(new UserTypeChangedEvent(this, old, userType));
        }
    }

    @NonNull
    public Organization getOrganization() {
        return organization;
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

    public static class AuthorityAddedEvent extends AggregateRootDomainEvent<User> {

        private final GrantedAuthority addedAuthority;

        AuthorityAddedEvent(@NonNull User user, @NonNull GrantedAuthority addedAuthority) {
            super(user, DomainServices.getInstance().clock());
            this.addedAuthority = addedAuthority;
        }

        @NonNull
        public GrantedAuthority getAddedAuthority() {
            return addedAuthority;
        }
    }

    public static class AuthorityRemovedEvent extends AggregateRootDomainEvent<User> {

        private final GrantedAuthority removedAuthority;

        AuthorityRemovedEvent(@NonNull User user, @NonNull GrantedAuthority removedAuthority) {
            super(user, DomainServices.getInstance().clock());
            this.removedAuthority = removedAuthority;
        }

        @NonNull
        public GrantedAuthority getRemovedAuthority() {
            return removedAuthority;
        }
    }

    public static class PasswordChangedEvent extends AggregateRootDomainEvent<User> {

        PasswordChangedEvent(@NonNull User user) {
            super(user, DomainServices.getInstance().clock());
        }
    }

    public static class AccountLockedEvent extends AggregateRootDomainEvent<User> {

        AccountLockedEvent(@NonNull User user) {
            super(user, DomainServices.getInstance().clock());
        }
    }

    public static class AccountUnlockedEvent extends AggregateRootDomainEvent<User> {

        AccountUnlockedEvent(@NonNull User user) {
            super(user, DomainServices.getInstance().clock());
        }
    }

    public static class AccountEnabledEvent extends AggregateRootDomainEvent<User> {

        AccountEnabledEvent(@NonNull User user) {
            super(user, DomainServices.getInstance().clock());
        }
    }

    public static class AccountDisabledEvent extends AggregateRootDomainEvent<User> {

        AccountDisabledEvent(@NonNull User user) {
            super(user, DomainServices.getInstance().clock());
        }
    }

    public static class UsernameChangedEvent extends AggregateRootPropertyChangeEvent<String, User> {

        UsernameChangedEvent(@NonNull User user, @Nullable String oldUsername, @NonNull String newUsername) {
            super(user, DomainServices.getInstance().clock(), oldUsername, newUsername);
        }
    }

    public static class OrganizationChangedEvent extends AggregateRootPropertyChangeEvent<Organization, User> {

        OrganizationChangedEvent(@NonNull User user, @Nullable Organization oldOrganization, @NonNull Organization newOrganization) {
            super(user, DomainServices.getInstance().clock(), oldOrganization, newOrganization);
        }
    }

    public static class UserTypeChangedEvent extends AggregateRootPropertyChangeEvent<UserType, User> {

        UserTypeChangedEvent(@NonNull User user, @Nullable UserType oldUserType, @NonNull UserType newUserType) {
            super(user, DomainServices.getInstance().clock(), oldUserType, newUserType);
        }
    }

    public static class FullNameChangedEvent extends AggregateRootPropertyChangeEvent<String, User> {

        FullNameChangedEvent(@NonNull User user, @Nullable String oldFullName, @NonNull String newFullName) {
            super(user, DomainServices.getInstance().clock(), oldFullName, newFullName);
        }
    }

    public static class ValidFromChangedEvent extends AggregateRootPropertyChangeEvent<Instant, User> {

        ValidFromChangedEvent(@NonNull User user, @Nullable Instant oldValue, @NonNull Instant newValue) {
            super(user, DomainServices.getInstance().clock(), oldValue, newValue);
        }
    }

    public static class ValidToChangedEvent extends AggregateRootPropertyChangeEvent<Instant, User> {

        ValidToChangedEvent(@NonNull User user, @Nullable Instant oldValue, @Nullable Instant newValue) {
            super(user, DomainServices.getInstance().clock(), oldValue, newValue);
        }
    }
}
