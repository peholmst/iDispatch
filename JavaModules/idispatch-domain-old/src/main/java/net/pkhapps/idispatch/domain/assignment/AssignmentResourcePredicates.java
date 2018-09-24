package net.pkhapps.idispatch.domain.assignment;

import net.pkhapps.idispatch.domain.resource.ResourceId;
import net.pkhapps.idispatch.domain.status.ResourceState;
import org.springframework.lang.NonNull;

import java.util.function.Predicate;

/**
 * Utility class with different {@link Predicate}s for filtering {@link AssignmentResource}s.
 */
class AssignmentResourcePredicates {

    private AssignmentResourcePredicates() {
    }

    @NonNull
    static Predicate<AssignmentResource> withResource(@NonNull ResourceId resource) {
        return ar -> ar.getResource().equals(resource);
    }

    @NonNull
    static Predicate<AssignmentResource> withSettableTimestampFor(@NonNull ResourceState state) {
        return ar -> ar.canSetTimestampFor(state);
    }
}
