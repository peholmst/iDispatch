package net.pkhapps.idispatch.client.v3.integration;

import net.pkhapps.idispatch.client.v3.Services;
import net.pkhapps.idispatch.client.v3.Station;
import net.pkhapps.idispatch.client.v3.StationLookupService;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link StationLookupService}. The server must be running
 * on {@code localhost}.
 */
public class StationLookupServiceIT {

    private final Services services = new Services("http://localhost:8080/server/api/", "integration-test");

    @Test
    public void queryIntegrationTest() throws IOException {
        findByStationId(findActiveStations());
    }

    private Station findActiveStations() throws IOException {
        var response = services.stationLookupService().findActiveStations().execute();
        assertThat(response.isSuccessful()).isTrue();
        var stations = response.body();
        assertThat(stations).isNotNull();
        assertThat(stations).isNotEmpty();
        return stations.get(0);
    }

    private void findByStationId(Station expected) throws IOException {
        var response = services.stationLookupService().findByStationId(expected.id()).execute();
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.body()).isEqualTo(expected);
    }
}
