package net.pkhapps.idispatch.dws.ui.components;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import net.pkhapps.idispatch.gis.api.AxisFormat;
import net.pkhapps.idispatch.gis.api.CoordinateFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.Point;

import java.util.Collections;
import java.util.List;

/**
 * TODO Document me
 */
public class DegreesPointField extends CustomField<Point> {

    private final Select<CoordinateFormat> format;
    private final FlexLayout xAxisComponents;
    private final FlexLayout yAxisComponents;
    private final Span xAxisSuffix;
    private final Span yAxisSuffix;
    private List<CoordinateFormat> coordinateFormats = Collections.emptyList();
    private List<TextField> xAxisFields = Collections.emptyList();
    private List<TextField> yAxisFields = Collections.emptyList();

    public DegreesPointField() {
        format = new Select<>();
        format.setItemLabelGenerator(item -> item.getName(getLocale()));
        format.addValueChangeListener(event -> changeFormat(event.getValue()));
        xAxisComponents = new FlexLayout();
        xAxisSuffix = new Span();
        xAxisSuffix.addClassName("suffix");

        yAxisComponents = new FlexLayout();
        yAxisSuffix = new Span();
        yAxisSuffix.addClassName("suffix");

        add(format, yAxisComponents, yAxisSuffix, xAxisComponents, xAxisSuffix);
    }

    /**
     * @return
     */
    public @NotNull List<CoordinateFormat> getCoordinateFormats() {
        return coordinateFormats;
    }

    /**
     * @param coordinateFormats
     */
    public void setCoordinateFormats(@NotNull CoordinateFormat... coordinateFormats) {
        setCoordinateFormats(List.of(coordinateFormats));
    }

    /**
     * @param coordinateFormats
     */
    public void setCoordinateFormats(@NotNull List<CoordinateFormat> coordinateFormats) {
        if (coordinateFormats.isEmpty()) {
            throw new IllegalArgumentException("You have to specify at least one CoordinateFormat");
        }
        this.coordinateFormats = List.copyOf(coordinateFormats);
        format.setItems(coordinateFormats);
        format.setVisible(coordinateFormats.size() > 1);
        format.setValue(coordinateFormats.get(0));
    }

    private void changeFormat(@NotNull CoordinateFormat format) {
        yAxisComponents.removeAll();
        yAxisFields.clear();
        createAxisFields(format.getYAxis(), yAxisFields, yAxisComponents);

        xAxisComponents.removeAll();
        xAxisFields.clear();
        createAxisFields(format.getXAxis(), xAxisFields, xAxisComponents);

        updateFieldValues(format, getValue());
    }

    private void createAxisFields(@NotNull AxisFormat format, @NotNull List<TextField> fields,
                                  @NotNull HasComponents targetLayout) {
        for (var componentIndex = 0; componentIndex < format.getComponentCount(); ++componentIndex) {
            var field = new TextField();
            field.setValueChangeMode(ValueChangeMode.LAZY);
            // TODO Field width
            var unit = new Span(format.getComponentUnit(componentIndex, getLocale()));
            unit.addClassName("unit");
            fields.add(field);
            targetLayout.add(field, unit);
        }
    }

    private void updateFieldValues(@Nullable CoordinateFormat format, @Nullable Point value) {
        if (value == null || format == null) {
            yAxisFields.forEach(HasValue::clear);
            xAxisFields.forEach(HasValue::clear);
        } else {
            updateComponentValues(format.getYAxis(), yAxisFields, value.getY());
            updateComponentValues(format.getXAxis(), xAxisFields, value.getX());
        }
    }

    private void updateComponentValues(@NotNull AxisFormat format, @NotNull List<TextField> fields, double value) {
        var values = format.splitComponents(value);
        for (int i = 0; i < values.length; ++i) {
            fields.get(i).setValue(format.formatComponent(i, values[i], getLocale()));
        }
    }

    @Override
    protected Point generateModelValue() {
        return null;
    }

    @Override
    protected void setPresentationValue(Point newPresentationValue) {
        updateFieldValues(format.getValue(), newPresentationValue);
    }
}
