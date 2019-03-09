package net.pkhapps.idispatch.identity.server.domain;

import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import org.springframework.data.util.ProxyUtils;

import javax.persistence.*;
import java.util.Collection;

/**
 * Base class for aggregate roots.
 *
 * @param <A> the concrete aggregate root type (used to create a fluent API).
 */
@MappedSuperclass
public abstract class AggregateRoot<A extends AggregateRoot<A>> extends AbstractAggregateRoot<A> implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    // Visible for unit tests
    @Override
    protected Collection<Object> domainEvents() {
        return super.domainEvents();
    }

    // Visible for unit tests
    @Override
    protected void clearDomainEvents() {
        super.clearDomainEvents();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!this.getClass().equals(ProxyUtils.getUserClass(obj))) {
            return false;
        } else {
            var that = (AggregateRoot) obj;
            return null != this.getId() && this.getId().equals(that.getId());
        }
    }

    @Override
    public int hashCode() {
        return ProxyUtils.getUserClass(this).hashCode() + (null == this.getId() ? 0 : this.getId().hashCode() * 31);
    }
}
