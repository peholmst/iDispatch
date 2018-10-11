package net.pkhapps.idispatch.application.lookup;

import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * TODO document me
 */
public abstract class AbstractLookupDTO<ID> implements Serializable {

    private final ID id;
    private final String displayName;

    public AbstractLookupDTO(@NonNull ID id, @NonNull String displayName) {
        this.id = Objects.requireNonNull(id);
        this.displayName = Objects.requireNonNull(displayName);
    }

    @NonNull
    public ID getId() {
        return id;
    }

    @NonNull
    public String getDisplayName() {
        return displayName;
    }
}
