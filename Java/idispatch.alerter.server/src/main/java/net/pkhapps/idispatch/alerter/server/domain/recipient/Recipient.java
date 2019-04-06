package net.pkhapps.idispatch.alerter.server.domain.recipient;

import lombok.Getter;
import net.pkhapps.idispatch.alerter.server.domain.DbConstants;
import net.pkhapps.idispatch.base.domain.AggregateRoot;
import net.pkhapps.idispatch.base.domain.Deactivatable;
import net.pkhapps.idispatch.base.domain.OrganizationId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.alerter.server.domain.ValidationUtils.requireNonBlankAndMaxLength;

/**
 * Base class for recipients that can receive alerts.
 */
@Entity
@Table(schema = DbConstants.SCHEMA_NAME, name = "recipient")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Getter
public abstract class Recipient<R extends Recipient<R>> extends AggregateRoot<RecipientId> implements Deactivatable {

    @Column(name = "description")
    private String description;

    @Column(name = "org_id")
    private OrganizationId organization;

    @Column(name = "active")
    private boolean active = true;

    @ElementCollection
    @CollectionTable(schema = DbConstants.SCHEMA_NAME, name = "recipient_resource",
            joinColumns = @JoinColumn(name = "recipient_id"))
    @Column(name = "resource_code")
    private Set<ResourceCode> resources = new HashSet<>();

    protected Recipient() {
        super(RecipientIdConverter.INSTANCE);
    }

    public Recipient(String description, OrganizationId organization) {
        this();
        setDescription(description);
        setOrganization(organization);
    }

    @SuppressWarnings("unchecked")
    public R setDescription(String description) {
        this.description = requireNonBlankAndMaxLength(description, 50);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R setOrganization(OrganizationId organization) {
        this.organization = requireNonNull(organization);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R addResource(ResourceCode resource) {
        resources.add(requireNonNull(resource));
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R removeResource(ResourceCode resource) {
        resources.remove(requireNonNull(resource));
        return (R) this;
    }

    @Override
    public void activate() {
        active = true;
    }

    @Override
    public void deactivate() {
        active = false;
    }
}
