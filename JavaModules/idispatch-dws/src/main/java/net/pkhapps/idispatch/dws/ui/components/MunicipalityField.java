package net.pkhapps.idispatch.dws.ui.components;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.HasValueChangeMode;
import com.vaadin.flow.data.value.ValueChangeMode;
import net.pkhapps.idispatch.gis.api.Locales;
import net.pkhapps.idispatch.gis.api.lookup.Municipality;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.gis.api.lookup.NameMatchStrategy;
import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

public class MunicipalityField extends CustomField<MunicipalityCode> {

    private final MunicipalityLookupService municipalityLookupService;
    private final Grid<Municipality> resultGrid;

    public MunicipalityField(@NotNull MunicipalityLookupService municipalityLookupService) {
        this.municipalityLookupService = requireNonNull(municipalityLookupService);
        var searchField = new TextField();

        resultGrid = new Grid<>();
        resultGrid.addColumn(m -> m.getName(Locales.FINNISH).orElse("")).setAutoWidth(true);
        resultGrid.addColumn(m -> m.getName(Locales.SWEDISH).orElse("")).setAutoWidth(true);
        resultGrid.addColumn(m -> m.getNationalCode().getCode()).setAutoWidth(true);
        resultGrid.getStyle().set("position", "absolute");
        resultGrid.setWidth("400px");
        resultGrid.setHeightByRows(true);

        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.setValueChangeTimeout(HasValueChangeMode.DEFAULT_CHANGE_TIMEOUT);
        searchField.addValueChangeListener(event -> {
            resultGrid.setDataProvider(DataProvider.ofCollection(municipalityLookupService.findByName(event.getValue(), NameMatchStrategy.PREFIX)));
            showResultGrid();
        });
        add(searchField, resultGrid);

        searchField.addKeyDownListener(Key.ESCAPE, event -> hideResultGrid());
        searchField.addKeyDownListener(Key.ARROW_DOWN, event -> resultGrid.focus());
        addBlurListener(event -> hideResultGrid());

        hideResultGrid();
    }

    private void showResultGrid() {
        //resultGrid.getStyle().set("top", getElement().getStyle())
        resultGrid.setVisible(true);

    }

    private void hideResultGrid() {
        resultGrid.setVisible(false);
    }

    public @NotNull MunicipalityField withLabel(@Nullable String label) {
        setLabel(label);
        return this;
    }

    @Override
    protected MunicipalityCode generateModelValue() {
        return null;
    }

    @Override
    protected void setPresentationValue(MunicipalityCode municipalityCode) {

    }
}
