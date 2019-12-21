package net.pkhapps.idispatch.core.client;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * TODO Document me
 *
 * @param <DTO>
 * @param <ID>
 */
public interface LookupService<DTO, ID> {

    @NotNull Optional<DTO> fetchById(@NotNull ID id);

    default @NotNull DTO getById(@NotNull ID id) {
        return fetchById(id).orElseThrow(() -> new DataNotFoundException("Record with ID " + id + " does not exist"));
    }
}
