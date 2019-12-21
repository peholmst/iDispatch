package net.pkhapps.idispatch.core.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

/**
 * TODO Document me
 *
 * @param <DTO>
 * @param <Filter>
 */
public interface DataListingService<DTO, ID, Filter> extends LookupService<DTO, ID> {

    @NotNull Stream<DTO> fetch(@Nullable Filter filter, @Nullable List<SortOrder> sortOrder, int limit, long offset);

    long count(@Nullable Filter filter);

    @NotNull ListenerHandle registerChangeListener(@NotNull Runnable onChange, @Nullable Filter filter);
}
