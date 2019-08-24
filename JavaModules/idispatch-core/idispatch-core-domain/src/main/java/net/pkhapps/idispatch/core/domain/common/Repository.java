package net.pkhapps.idispatch.core.domain.common;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me
 *
 * @param <ID>
 * @param <T>
 */
public interface Repository<ID, T extends AggregateRoot<ID>> {
    
    /**
     * @param ids
     * @return
     */
    @NotNull Stream<T> findByIds(@NotNull Iterable<ID> ids);

    /**
     * @param id
     * @return
     */
    default @NotNull Optional<T> findById(@NotNull ID id) {
        return findByIds(Set.of(id)).findFirst();
    }

    /**
     * @param aggregateRoot
     * @return
     */
    @NotNull T save(@NotNull T aggregateRoot);

    /**
     * @param id
     */
    void deleteById(@NotNull ID id);

    /**
     * @param aggregateRoot
     */
    default void delete(@NotNull T aggregateRoot) {
        requireNonNull(aggregateRoot);
        deleteById(aggregateRoot.id());
    }
}
