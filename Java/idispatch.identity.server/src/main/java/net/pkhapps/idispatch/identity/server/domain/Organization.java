package net.pkhapps.idispatch.identity.server.domain;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

/**
 * Entity representing an organization that uses iDispatch.
 */
@Entity
@Table(name = "organization", schema = "idispatch_identity")
@Getter
public class Organization extends AggregateRoot<Organization> {

    @NotEmpty
    private String name;
    private boolean enabled;

    Organization() {
    }

    public Organization(@NonNull String name) {
        setName(name);
        setEnabled(true);
    }

    public void setName(@NonNull String name) {
        Objects.requireNonNull(name, "name must not be null");
        if (!name.equals(this.name)) {
            var old = this.name;
            this.name = name;
            registerEvent(new NameChangedEvent(this, old, name));
        }
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

    @Getter
    public static abstract class OrganizationDomainEvent extends DomainEvent {

        private final Organization organization;

        public OrganizationDomainEvent(@NonNull Organization organization) {
            this.organization = Objects.requireNonNull(organization, "organization must not be null");
        }
    }

    @Getter
    public static class NameChangedEvent extends OrganizationDomainEvent {

        private final String oldValue;
        private final String newValue;

        NameChangedEvent(@NonNull Organization organization, @Nullable String oldValue, @NonNull String newValue) {
            super(organization);
            this.oldValue = oldValue;
            this.newValue = newValue;
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
