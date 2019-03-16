package net.pkhapps.idispatch.identity.server.domain;

import net.pkhapps.idispatch.base.domain.AggregateRoot;
import net.pkhapps.idispatch.base.domain.OrganizationId;
import net.pkhapps.idispatch.base.domain.OrganizationIdConverter;
import net.pkhapps.idispatch.base.domain.event.AggregateRootDomainEvent;
import net.pkhapps.idispatch.base.domain.event.AggregateRootPropertyChangeEvent;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.base.domain.util.Strings.requireMaxLength;

/**
 * Entity representing an organization that uses iDispatch.
 */
@Entity
@Table(name = "organization", schema = "idispatch_identity")
public class Organization extends AggregateRoot<OrganizationId> {

    private static final int STRING_MAX_LENGTH = 255;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    Organization() {
        super(new OrganizationIdConverter());
    }

    public Organization(@NonNull String name) {
        this();
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

    public static class NameChangedEvent extends AggregateRootPropertyChangeEvent<String, Organization> {

        NameChangedEvent(@NonNull Organization organization, @Nullable String oldValue, @NonNull String newValue) {
            super(organization, DomainServices.getInstance().clock(), oldValue, newValue);
        }
    }

    public static class EnabledEvent extends AggregateRootDomainEvent<Organization> {

        EnabledEvent(@NonNull Organization organization) {
            super(organization, DomainServices.getInstance().clock());
        }
    }

    public static class DisabledEvent extends AggregateRootDomainEvent<Organization> {

        DisabledEvent(@NonNull Organization organization) {
            super(organization, DomainServices.getInstance().clock());
        }
    }
}
