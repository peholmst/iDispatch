package net.pkhapps.idispatch.gis.postgis.integrationtests;

import net.pkhapps.idispatch.gis.api.spi.GIS;
import net.pkhapps.idispatch.gis.api.spi.GISFactory;
import net.pkhapps.idispatch.gis.postgis.spi.PropertyConstants;
import org.testng.annotations.BeforeTest;

import java.util.Properties;

public class LocationFeatureLookupServiceIT {

    private GIS gis;

    @BeforeTest
    public void setUp() throws Exception {
        var properties = new Properties();
        properties.setProperty(PropertyConstants.JDBC_URL, "jdbc:postgresql://localhost/idispatch_gis");
        properties.setProperty(PropertyConstants.JDBC_USER, "idispatch_gis_importer");
        properties.setProperty(PropertyConstants.JDBC_PASSWORD, "2smart4u");
        gis = GISFactory.getInstance().createLookupServices(properties);
    }


}
