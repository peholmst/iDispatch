package net.pkhapps.idispatch.gis.domain.model;

import com.vividsolutions.jts.geom.*;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * TODO Document me
 */
@Entity
@Table(name = "map_tile", schema = "gis")
public class MapTile extends ImportedGeographicalMaterial {

    @Column(name = "zoom", nullable = false)
    private int zoom;

    @Column(name = "col", nullable = false)
    private int col;

    @Column(name = "row", nullable = false)
    private int row;

    @Column(name = "envelope", nullable = false)
    private Polygon envelope;

    @Column(name = "south_west", nullable = false)
    private Point southWest;

    @Column(name = "north_east", nullable = false)
    private Point northEast;

    @Column(name = "scale_x", nullable = false)
    private float scaleX;

    @Column(name = "scale_y", nullable = false)
    private float scaleY;

    @Column(name = "image", nullable = false)
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] image;

    @Column(name = "mime_type", nullable = false, length = 50)
    private String mimeType;

    public MapTile(int zoom, int col, int row, @NotNull Envelope envelope, float scaleX, float scaleY,
                   @NotNull byte[] image, @NotNull String mimeType) {
        setZoom(zoom);
        setCol(col);
        setRow(row);
        setEnvelope(envelope);
        setScaleX(scaleX);
        setScaleY(scaleY);
        setImage(image);
        setMimeType(mimeType);
    }

    public int zoom() {
        return zoom;
    }

    private void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public int col() {
        return col;
    }

    private void setCol(int col) {
        this.col = col;
    }

    public int row() {
        return row;
    }

    private void setRow(int row) {
        this.row = row;
    }

    private void setEnvelope(@NotNull Envelope envelope) {
        var geometryFactory = new GeometryFactory(new PrecisionModel(), CRS.ETRS89_TM35FIN_SRID);// NLS material use this SRID
        southWest = geometryFactory.createPoint(new Coordinate(envelope.getMinX(), envelope.getMinY()));
        northEast = geometryFactory.createPoint(new Coordinate(envelope.getMaxX(), envelope.getMaxY()));
        this.envelope = geometryFactory.createPolygon(new Coordinate[]{
                new Coordinate(envelope.getMinX(), envelope.getMinY()),
                new Coordinate(envelope.getMinX(), envelope.getMaxY()),
                new Coordinate(envelope.getMaxX(), envelope.getMaxY()),
                new Coordinate(envelope.getMaxX(), envelope.getMinY()),
                new Coordinate(envelope.getMinX(), envelope.getMinY()),
        });
    }

    public @NotNull Envelope envelope() {
        return new Envelope(southWest.getCoordinate(), northEast.getCoordinate());
    }

    public @NotNull Point southWest() {
        return southWest;
    }

    public @NotNull Point northEast() {
        return northEast;
    }

    public float scaleX() {
        return scaleX;
    }

    private void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float scaleY() {
        return scaleY;
    }

    private void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    private void setImage(@NotNull byte[] image) {
        Objects.requireNonNull(image, "image must not be null");
        if (image.length == 0) {
            throw new IllegalArgumentException("image must not be empty");
        }
        this.image = image;
    }

    private void setMimeType(@NotNull String mimeType) {
        this.mimeType = Objects.requireNonNull(mimeType, "mimeType must not be null");
    }
}
