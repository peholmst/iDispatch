package net.pkhapps.idispatch.identity.server.domain;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.identity.server.util.Strings.requireMaxLength;

/**
 * Entity representing an organization that uses iDispatch.
 */
@Entity
@Table(name = "organization", schema = "idispatch_identity")
public class Organization extends AggregateRoot<Organization> {

    private static final int STRING_MAX_LENGTH = 255;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    Organization() {
    }

    public Organization(@NonNull String name) {
        setName(name);
        setEnabled(true);
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        requireNonNull(requireMaxLength(name, STRING_MAX_LENGTH));
        if (!name.equals(this.name)) {
            var old = this.name;
            this.name = name;
            registerEvent(new NameChangedEvent(this, old, name));
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                registerEvent(new EnabledEvent(this));
            } else {
                registerEvent(new DisabledEvent(this));
            }
        }
    }

    public static abstract class OrganizationDomainEvent extends DomainEvent {

        private final Organization organization;

        OrganizationDomainEvent(@NonNull Organization organization) {
            this.organization = requireNonNull(organization, "organization must not be null");
        }

        @NonNull
        public Organization getOrganization() {
            return organization;
        }
    }

    public static class NameChangedEvent extends OrganizationDomainEvent {

        private final String oldValue;
        private final String newValue;

        NameChangedEvent(@NonNull Organization organization, @Nullable String oldValue, @NonNull String newValue) {
            super(organization);
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        @Nullable
        public String getOldValue() {
            return oldValue;
        }

        @NonNull
        public String getNewValue() {
            return newValue;
        }
    }

    public static class EnabledEvent extends OrganizationDomainEvent {

        EnabledEvent(@NonNull Organization organization) {
            super(organization);
        }
    }

    public static class DisabledEvent extends OrganizationDomainEvent {

        DisabledEvent(@NonNull Organization organization) {
            super(organization);
        }
    }
}
