package net.pkhapps.idispatch.dws.ui.viewlets;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import net.pkhapps.idispatch.core.client.DataListingService;
import net.pkhapps.idispatch.core.client.ListenerHandle;
import net.pkhapps.idispatch.core.client.SortOrder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me
 *
 * @param <DTO>
 * @param <Filter>
 */
public class DataListingProvider<DTO, Filter> extends AbstractBackEndDataProvider<DTO, Filter> {

    private final DataListingService<DTO, Filter> dataListingService;
    private Filter filter;
    private ListenerHandle changeHandle;

    /**
     * @param dataListingService
     */
    public DataListingProvider(@NotNull DataListingService<DTO, Filter> dataListingService) {
        this.dataListingService = requireNonNull(dataListingService);
    }

    private static @NotNull SortOrder toSortOrder(@NotNull QuerySortOrder querySortOrder) {
        if (querySortOrder.getDirection() == SortDirection.ASCENDING) {
            return SortOrder.asc(querySortOrder.getSorted());
        } else {
            return SortOrder.desc(querySortOrder.getSorted());
        }
    }

    @Override
    protected Stream<DTO> fetchFromBackEnd(Query<DTO, Filter> query) {
        return dataListingService.fetch(getFilter(query),
                query.getSortOrders().stream().map(DataListingProvider::toSortOrder).collect(Collectors.toList()),
                query.getLimit(), query.getOffset());
    }

    @Override
    protected int sizeInBackEnd(Query<DTO, Filter> query) {
        return (int) dataListingService.count(getFilter(query));
    }

    /**
     * @param query
     * @return
     */
    protected @Nullable Filter getFilter(@Nullable Query<DTO, Filter> query) {
        if (filter != null) {
            return filter;
        } else if (query != null) {
            return query.getFilter().orElse(null);
        } else {
            return null;
        }
    }

    /**
     * @return
     */
    public @Nullable Filter getFilter() {
        return filter;
    }

    /**
     * @param filter
     */
    public void setFilter(@Nullable Filter filter) {
        unregisterChangeListener();
        this.filter = filter;
        refreshAll();
        registerChangeListener();
    }

    /**
     *
     */
    public void attach() {
        registerChangeListener();
    }

    /**
     *
     */
    public void detach() {
        unregisterChangeListener();
    }

    private void registerChangeListener() {
        if (changeHandle == null) {
            changeHandle = dataListingService.registerChangeListener(this::refreshAll, filter);
        }
    }

    private void unregisterChangeListener() {
        if (changeHandle != null) {
            changeHandle.unregister();
            changeHandle = null;
        }
    }
}
