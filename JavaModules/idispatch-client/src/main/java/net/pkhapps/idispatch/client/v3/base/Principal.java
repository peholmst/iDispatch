package net.pkhapps.idispatch.client.v3.base;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Value object representing a principal (a "user") in the system.
 */
@Immutable
public class Principal implements java.security.Principal, Serializable, IdentifiableDomainObject<String> {

    private String id;
    private String type;
    private String fullName;

    public Principal(@Nonnull String id, @Nonnull String type, @Nullable String fullName) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.fullName = fullName;
    }

    @Override
    public String getName() {
        return id();
    }

    @Nonnull
    @Override
    public String id() {
        return id;
    }

    @Nonnull
    public String type() {
        return type;
    }

    @Nonnull
    public String fullName() {
        return fullName != null ? fullName : id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Principal principal = (Principal) o;
        return Objects.equals(id, principal.id) &&
                Objects.equals(type, principal.type) &&
                Objects.equals(fullName, principal.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, fullName);
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s, type=%s]", getClass().getSimpleName(), id, type);
    }
}
