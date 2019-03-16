package net.pkhapps.idispatch.base.domain;

import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Interface for domain objects that can be uniquely identified.
 */
public interface IdentifiableDomainObject<ID> extends DomainObject {

    /**
     * Returns the ID of this domain object. Implementation may decide whether this method can return {@code null} or not.
     */
    ID getId();

    /**
     * Returns the ID of this domain object.
     *
     * @throws IllegalStateException if the domain object has no ID.
     */
    @NonNull
    default ID getNonNullId() {
        return getIdOptional().orElseThrow(() -> new IllegalStateException("No ID has been set yet"));
    }

    /**
     * Returns the ID of this domain object or an empty {@code Optional} if not set.
     */
    @NonNull
    default Optional<ID> getIdOptional() {
        return Optional.ofNullable(getId());
    }
}
