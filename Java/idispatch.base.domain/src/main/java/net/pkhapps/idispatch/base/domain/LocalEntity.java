package net.pkhapps.idispatch.base.domain;

import org.springframework.lang.Nullable;

import javax.persistence.*;

/**
 * Base class for local entities. Local entities are always stored and retrieved as a part of an aggregate and are
 * never referenced from outside the owning aggregate. Because of this, they don't have IDs that are based on
 * {@link DomainObjectId}.
 */
@MappedSuperclass
public abstract class LocalEntity extends Entity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Override
    @Nullable
    public Long getId() {
        return id;
    }

    protected void setId(@Nullable Long id) {
        this.id = id;
    }
}
