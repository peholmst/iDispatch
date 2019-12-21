package net.pkhapps.idispatch.core.client;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * TODO Document me!
 *
 * @param <DTO>
 * @param <ID>
 */
public interface CachedDataListingService<DTO, ID> extends LookupService<DTO, ID> {

    @NotNull Stream<DTO> fetchAll();

    @NotNull ListenerHandle registerChangeListener(@NotNull Runnable onChange);
}
