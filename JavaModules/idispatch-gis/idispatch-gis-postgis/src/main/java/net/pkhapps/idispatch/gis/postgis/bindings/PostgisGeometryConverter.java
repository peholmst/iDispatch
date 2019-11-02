package net.pkhapps.idispatch.gis.postgis.bindings;

import org.jooq.Converter;
import org.postgis.Geometry;
import org.postgis.PGgeometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * JOOQ converter for PostGIS {@link Geometry} types.
 */
@SuppressWarnings("SpellCheckingInspection")
class PostgisGeometryConverter implements Converter<Object, Geometry> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostgisGeometryConverter.class);

    @Override
    public Geometry from(Object databaseObject) {
        if (databaseObject == null) {
            return null;
        }
        try {
            return PGgeometry.geomFromString(databaseObject.toString());
        } catch (SQLException ex) {
            LOGGER.error("Error converting database object to geometry object", ex);
            return null;
        }
    }

    @Override
    public Object to(Geometry userObject) {
        if (userObject == null) {
            return null;
        }
        return new PGgeometry(userObject).getValue();
    }

    @Override
    public Class<Object> fromType() {
        return Object.class;
    }

    @Override
    public Class<Geometry> toType() {
        return Geometry.class;
    }
}
