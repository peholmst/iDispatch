package net.pkhapps.idispatch.dws.ui.viewlets;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.shared.Registration;
import net.pkhapps.appmodel4flow.AppModel;
import net.pkhapps.appmodel4flow.property.DefaultProperty;
import net.pkhapps.appmodel4flow.property.Property;
import net.pkhapps.appmodel4flow.selection.SelectionModel;
import net.pkhapps.idispatch.core.client.DataListingService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TODO Document me!
 *
 * @param <DTO>
 * @param <Filter>
 */
public abstract class AbstractGridViewlet<DTO, Filter> extends Div {

    private final DataListingProvider<DTO, Filter> dataProvider;
    private final Grid<DTO> grid;
    private final Property<Filter> filter = new DefaultProperty<>();
    private Registration selectionModelRegistration;

    /**
     * @param dataListingService
     */
    public AbstractGridViewlet(@NotNull DataListingService<DTO, Filter> dataListingService) {
        addClassName("grid-viewlet");
        setSizeFull();

        dataProvider = new DataListingProvider<>(dataListingService);
        filter.addValueChangeListener(event -> dataProvider.setFilter(event.getValue()));
        grid = new Grid<>();
        grid.addClassName("grid");
        grid.setDataProvider(dataProvider);
        configureGrid(grid);

        var toolbar = new Div();
        toolbar.addClassName("toolbar");
        configureToolbar(toolbar);

        add(toolbar, grid);
    }

    /**
     * @param grid
     */
    protected abstract void configureGrid(@NotNull Grid<DTO> grid);

    /**
     * @param toolbar
     */
    protected abstract void configureToolbar(@NotNull Div toolbar);

    /**
     * @param selectionModel
     */
    public void setSelectionModel(@Nullable SelectionModel<DTO> selectionModel) {
        if (selectionModelRegistration != null) {
            selectionModelRegistration.remove();
            selectionModelRegistration = null;
        }
        if (selectionModel != null) {
            selectionModelRegistration = AppModel.bind(selectionModel, grid);
        }
    }

    /**
     * @return
     */
    protected final @NotNull Property<Filter> filter() {
        return filter;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        dataProvider.attach();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        dataProvider.detach();
    }
}
