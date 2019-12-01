package net.pkhapps.idispatch.dws.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import net.pkhapps.idispatch.gis.api.CRS;
import net.pkhapps.idispatch.gis.api.Etrs89Tm35FinFormat;
import net.pkhapps.idispatch.gis.api.GeometryUtil;
import net.pkhapps.idispatch.gis.api.WGS84Format;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.Point;

/**
 * TODO Document me
 */
public class PointField extends CustomField<Point> {

    private static final String TR_PREFIX = "components.pointField.";
    private final Select<CoordinateEditor> format;

    public PointField() {
        var tm35fin = new Tm35Fin();
        var wgs84 = new Wgs84();

        format = new Select<>(tm35fin, wgs84);
        format.setItemLabelGenerator(CoordinateEditor::getName);
        format.addValueChangeListener(event -> {
            if (event.getOldValue() != null) {
                remove(event.getOldValue().getComponents());
            }
            if (event.getValue() != null) {
                add(event.getValue().getComponents());
                event.getValue().setValue(getValue());
            }
        });
        add(format);
        format.setValue(tm35fin);
        format.setEmptySelectionAllowed(false);
    }

    private @NotNull Component northAxis() {
        return new Span(getTranslation(TR_PREFIX + "north"));
    }

    private @NotNull Component eastAxis() {
        return new Span(getTranslation(TR_PREFIX + "east"));
    }

    private @NotNull CoordinateEditor getEditor() {
        return format.getValue();
    }

    @Override
    protected Point generateModelValue() {
        return getEditor().generateModelValue();
    }

    @Override
    protected void setPresentationValue(Point newPresentationValue) {
        if (newPresentationValue == null || newPresentationValue.getSRID() == CRS.ETRS89_TM35FIN_SRID) {
            getEditor().setValue(newPresentationValue);
        } else {
            throw new IllegalArgumentException("Unsupported SRID: " + newPresentationValue.getSRID());
        }
    }

    private abstract class CoordinateEditor {

        abstract @NotNull Component[] getComponents();

        abstract @NotNull String getName();

        abstract void setValue(@Nullable Point point);

        abstract @Nullable Point generateModelValue();

        final void onFieldValueChangeEvent(ValueChangeEvent<?> event) {
            if (event.isFromClient()) {
                updateValue();
            }
        }
    }

    private class Wgs84 extends CoordinateEditor {

        private final Select<WGS84Format> format;
        private final TextField lat;
        private final TextField lon;
        private final Component[] components;
        private Point value;

        Wgs84() {
            format = new Select<>(WGS84Format.formats());
            format.setWidth("6em");
            format.setValue(WGS84Format.DDM);
            format.addValueChangeListener(event -> updateFields());
            lat = new TextField();
            lat.setWidth("8.5em");
            lat.setTitle(getTranslation(TR_PREFIX + "lat"));
            lat.addValueChangeListener(this::onFieldValueChangeEvent);
            lat.setValueChangeMode(ValueChangeMode.LAZY);
            lon = new TextField();
            lon.setTitle(getTranslation(TR_PREFIX + "lon"));
            lon.setWidth("8.5em");
            lon.addValueChangeListener(this::onFieldValueChangeEvent);
            lon.setValueChangeMode(ValueChangeMode.LAZY);
            components = new Component[]{format, lat, northAxis(), lon, eastAxis()};
        }

        @NotNull
        @Override
        Component[] getComponents() {
            return components;
        }

        @Override
        @NotNull String getName() {
            return getTranslation(TR_PREFIX + "wgs84");
        }

        @Override
        void setValue(@Nullable Point value) {
            if (value == null) {
                this.value = null;
            } else {
                assert value.getSRID() == CRS.ETRS89_TM35FIN_SRID;
                this.value = CRS.etrs89Tm35FinToWgs84(value);
            }
            updateFields();
        }

        @Override
        @Nullable Point generateModelValue() {
            if (lat.isEmpty() || lon.isEmpty()) {
                return null;
            }
            double latitude = Double.NaN;
            double longitude = Double.NaN;

            try {
                latitude = format.getValue().parseLatitude(lat.getValue(), getLocale());
                lat.setInvalid(false);
            } catch (IllegalArgumentException ex) {
                lat.setInvalid(true);
            }

            try {
                longitude = format.getValue().parseLongitude(lon.getValue(), getLocale());
                lon.setInvalid(false);
            } catch (IllegalArgumentException ex) {
                lon.setInvalid(true);
            }

            if (Double.isNaN(latitude) || Double.isNaN(longitude)) {
                return null;
            } else {
                return CRS.wgs84ToEtrs89Tm35Fin(GeometryUtil.createWgs84Point(longitude, latitude));
            }
        }

        private void updateFields() {
            if (value == null) {
                lat.setValue("");
                lon.setValue("");
            } else {
                lat.setValue(format.getValue().formatLatitude(value.getY(), getLocale()));
                lon.setValue(format.getValue().formatLongitude(value.getX(), getLocale()));
            }
        }
    }

    private class Tm35Fin extends CoordinateEditor {

        private final TextField northing;
        private final TextField easting;
        private final Component[] components;

        Tm35Fin() {
            northing = new TextField();
            northing.setTitle(getTranslation(TR_PREFIX + "northing"));
            northing.setWidth("8em");
            northing.addValueChangeListener(this::onFieldValueChangeEvent);
            northing.setValueChangeMode(ValueChangeMode.LAZY);
            easting = new TextField();
            easting.setTitle(getTranslation(TR_PREFIX + "easting"));
            easting.setWidth("8em");
            easting.addValueChangeListener(this::onFieldValueChangeEvent);
            easting.setValueChangeMode(ValueChangeMode.LAZY);
            components = new Component[]{northing, northAxis(), easting, eastAxis()};
        }

        @NotNull
        @Override
        Component[] getComponents() {
            return components;
        }

        @Override
        @NotNull String getName() {
            return getTranslation(TR_PREFIX + "tm35fin");
        }

        @Override
        void setValue(@Nullable Point value) {
            if (value == null) {
                northing.setValue("");
                easting.setValue("");
            } else {
                northing.setValue(Etrs89Tm35FinFormat.DEFAULT.formatNorthing(value.getY(), getLocale()));
                easting.setValue(Etrs89Tm35FinFormat.DEFAULT.formatEasting(value.getX(), getLocale()));
            }
        }

        @Override
        @Nullable Point generateModelValue() {
            if (easting.isEmpty() || northing.isEmpty()) {
                return null;
            }
            double n = Double.NaN;
            double e = Double.NaN;

            try {
                e = Etrs89Tm35FinFormat.DEFAULT.parseEasting(easting.getValue(), getLocale());
                easting.setInvalid(false);
            } catch (IllegalArgumentException ex) {
                easting.setInvalid(true);
            }

            try {
                n = Etrs89Tm35FinFormat.DEFAULT.parseNorthing(northing.getValue(), getLocale());
                northing.setInvalid(false);
            } catch (IllegalArgumentException ex) {
                northing.setInvalid(true);
            }

            if (Double.isNaN(n) || Double.isNaN(e)) {
                return null;
            } else {
                return GeometryUtil.createEtrs89Tm35FinPoint(e, n);
            }
        }
    }
}
