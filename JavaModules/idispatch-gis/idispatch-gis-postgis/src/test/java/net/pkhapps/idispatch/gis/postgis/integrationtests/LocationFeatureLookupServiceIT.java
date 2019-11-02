package net.pkhapps.idispatch.gis.postgis.integrationtests;

import net.pkhapps.idispatch.gis.api.CRS;
import net.pkhapps.idispatch.gis.api.Locales;
import net.pkhapps.idispatch.gis.api.lookup.AddressPoint;
import net.pkhapps.idispatch.gis.api.lookup.NameMatchStrategy;
import net.pkhapps.idispatch.gis.api.lookup.RoadSegment;
import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import net.pkhapps.idispatch.gis.api.spi.GIS;
import net.pkhapps.idispatch.gis.api.spi.GISFactory;
import net.pkhapps.idispatch.gis.postgis.spi.PropertyConstants;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link net.pkhapps.idispatch.gis.api.lookup.LocationFeatureLookupService}. This assumes that
 * real data has been imported into the database.
 */
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

    @Test
    public void findFeaturesByName_addressPoint_exactMatchWithNumber() {
        var result = gis.getLocationFeatureLookupService().findFeaturesByName(MunicipalityCode.of(445), "storpensar",
                NameMatchStrategy.EXACT, "557");
        assertThat(result).hasSize(1);
        assertThat(result).hasOnlyOneElementSatisfying(feature -> {
            assertThat(feature).isInstanceOf(AddressPoint.class);
            var ap = (AddressPoint) feature;
            assertThat(ap.getMunicipality()).contains(MunicipalityCode.of(445));
            assertThat(ap.getName(Locales.SWEDISH)).contains("Storpensar");
            assertThat(ap.getNumber()).contains("557");
            assertThat(ap.getLocation()).isPresent();
            assertThat(ap.getLocation()).hasValueSatisfying(location -> assertThat(location.getSRID()).isEqualTo(CRS.ETRS89_TM35FIN_SRID));
        });
        System.out.println(result);
    }

    @Test
    public void findFeaturesByName_addressPoinat_exactMatchWithoutNumber() {
        var result = gis.getLocationFeatureLookupService().findFeaturesByName(MunicipalityCode.of(445), "elvsö",
                NameMatchStrategy.EXACT, null);
        assertThat(result).hasSize(20);
        System.out.println(result);
    }

    @Test
    public void findFeaturesByName_roadSegment_exactMatchWithNumber() {
        var result = gis.getLocationFeatureLookupService().findFeaturesByName(MunicipalityCode.of(445), "strandvägen",
                NameMatchStrategy.EXACT, "32");
        assertThat(result).hasSize(1);
        assertThat(result).hasOnlyOneElementSatisfying(feature -> {
            assertThat(feature).isInstanceOf(RoadSegment.class);
            var rs = (RoadSegment) feature;
            assertThat(rs.getMunicipality()).contains(MunicipalityCode.of(445));
            assertThat(rs.getName(Locales.SWEDISH)).contains("Strandvägen");
            assertThat(rs.getName(Locales.FINNISH)).contains("Rantatie");
            assertThat(rs.getAddressNumbersLeft()).hasValueSatisfying(range -> range.contains(32));
            assertThat(rs.getLocation()).isPresent();
            assertThat(rs.getLocation()).hasValueSatisfying(location -> assertThat(location.getSRID()).isEqualTo(CRS.ETRS89_TM35FIN_SRID));
        });
        System.out.println(result);
    }

    @Test
    public void findFeaturesByName_roadSegment_exactMatchWithoutNumber() {
        var result = gis.getLocationFeatureLookupService().findFeaturesByName(MunicipalityCode.of(445), "saturnusgatan",
                NameMatchStrategy.EXACT, null);
        assertThat(result).hasSize(2);
        System.out.println(result);
    }
}
