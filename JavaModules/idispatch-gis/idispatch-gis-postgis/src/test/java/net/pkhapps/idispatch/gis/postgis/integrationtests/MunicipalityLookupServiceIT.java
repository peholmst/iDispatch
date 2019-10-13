package net.pkhapps.idispatch.gis.postgis.integrationtests;

import net.pkhapps.idispatch.gis.api.CRS;
import net.pkhapps.idispatch.gis.api.lookup.Municipality;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.gis.api.spi.GIS;
import net.pkhapps.idispatch.gis.api.spi.GISFactory;
import net.pkhapps.idispatch.gis.postgis.spi.PropertyConstants;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Locale;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService}. This assumes that real
 * data has been imported into the database.
 */
public class MunicipalityLookupServiceIT {

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
    public void findByNationalCode() {
        var result = gis.getMunicipalityLookupService().findByNationalCode("445");
        assertThat(result).isPresent();
        assertThat(result).hasValueSatisfying(this::assertPargas);
    }

    @Test
    public void findMunicipalityOfPoint() {
        var factory = new GeometryFactory(new PrecisionModel(), CRS.ETRS89_TM35FIN_SRID);
        var coordinate = new Coordinate(240476.375, 6694821.625); // Segelrondellen i Pargas centrum
        var point = factory.createPoint(coordinate);
        var result = gis.getMunicipalityLookupService().findMunicipalityOfPoint(point);
        assertThat(result).isPresent();
        assertThat(result).hasValueSatisfying(this::assertPargas);
    }

    @Test
    public void findMunicipalityOfPoint_wgs84() {
        var factory = new GeometryFactory(new PrecisionModel(), CRS.WGS84_SRID);
        var coordinate = new Coordinate(22.300955145, 60.306705319); // Segelrondellen i Pargas centrum
        var point = factory.createPoint(coordinate);
        var result = gis.getMunicipalityLookupService().findMunicipalityOfPoint(point);
        assertThat(result).isPresent();
        assertThat(result).hasValueSatisfying(this::assertPargas);
    }

    @Test
    public void findByNamePrefix_emptyString_noResult() {
        var result = gis.getMunicipalityLookupService().findByName("p",
                MunicipalityLookupService.NameMatchStrategy.PREFIX);
        assertThat(result).isEmpty();
    }

    @Test
    public void findByNamePrefix_oneCharacter_noResult() {
        var result = gis.getMunicipalityLookupService().findByName("p",
                MunicipalityLookupService.NameMatchStrategy.PREFIX);
        assertThat(result).isEmpty();
    }

    @Test
    public void findByNamePrefix() {
        var result = gis.getMunicipalityLookupService().findByName("pa",
                MunicipalityLookupService.NameMatchStrategy.PREFIX);
        assertThat(result).hasSize(6);
        assertThat(result).anySatisfy(this::assertPargas);
    }

    @Test
    public void findByName() {
        var result = gis.getMunicipalityLookupService().findByName("pargas",
                MunicipalityLookupService.NameMatchStrategy.EXACT);
        assertThat(result).hasOnlyOneElementSatisfying(this::assertPargas);
    }

    private void assertPargas(@NotNull Municipality municipality) {
        assertThat(municipality.getNationalCode()).isEqualTo("445");
        assertThat(municipality.getName(new Locale("fi"))).contains("Parainen");
        assertThat(municipality.getName(new Locale("sv"))).contains("Pargas");
        assertThat(municipality.getCenter().getSRID()).isEqualTo(CRS.ETRS89_TM35FIN_SRID);
    }
}
