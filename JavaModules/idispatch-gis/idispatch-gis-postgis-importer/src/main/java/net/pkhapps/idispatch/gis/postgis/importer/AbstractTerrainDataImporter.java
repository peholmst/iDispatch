package net.pkhapps.idispatch.gis.postgis.importer;

import net.pkhapps.idispatch.gis.postgis.importer.base.AbstractImporter;
import org.geotools.appschema.resolver.xml.AppSchemaConfiguration;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xml.resolver.SchemaResolver;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * TODO Document me
 */
public abstract class AbstractTerrainDataImporter extends AbstractImporter {

    private static final String NAMESPACE_URI = "http://xml.nls.fi/XML/Namespace/Maastotietojarjestelma/SiirtotiedostonMalli/2011-02";
    private static final String SCHEMA_LOCATION = "http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Maastotiedot.xsd";

    public AbstractTerrainDataImporter(@NotNull Connection connection, @NotNull String localPart)
            throws SQLException, IOException {
        super(connection, new QName(NAMESPACE_URI, localPart));
    }

    @Contract("null -> null")
    protected static Integer toInteger(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Integer) {
            return (Integer) o;
        } else if (o instanceof Long) {
            return ((Long) o).intValue();
        } else if (o instanceof String) {
            return Integer.valueOf((String) o);
        } else {
            return Integer.valueOf(o.toString());
        }
    }

    @Contract("null -> null")
    protected static String toString(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof String) {
            return (String) o;
        } else if (o instanceof Map) {
            return toString(((Map) o).get(null));
        } else {
            return o.toString();
        }
    }

    @Override
    protected @NotNull AppSchemaConfiguration createAppSchemaConfiguration(@NotNull SchemaResolver resolver) {
        var configuration = new AppSchemaConfiguration(NAMESPACE_URI, SCHEMA_LOCATION, resolver);
        configuration.addDependency(new GMLConfiguration());
        return configuration;
    }
}
