package net.pkhapps.idispatch.adapter.rest.v3.lookup;

import net.pkhapps.idispatch.client.v3.geo.CoordinateReferenceSystem;
import net.pkhapps.idispatch.client.v3.geo.GeographicLocation;
import net.pkhapps.idispatch.client.v3.type.Station;
import net.pkhapps.idispatch.client.v3.type.StationId;
import net.pkhapps.idispatch.client.v3.type.StationLookupService;
import net.pkhapps.idispatch.client.v3.util.MultilingualString;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

import static net.pkhapps.idispatch.client.v3.util.Locales.FINNISH;
import static net.pkhapps.idispatch.client.v3.util.Locales.SWEDISH;

/**
 * REST controller for looking up {@link Station}s.
 */
@Path(StationLookupService.RELATIVE_SERVICE_URL)
public class StationLookupController {

    // TODO Replace with real data

    @SuppressWarnings("WeakerAccess")
    public static DummyDataContainer<StationId, Station> DUMMY_DATA = new DummyDataContainer<>() {
        {
            // These fire stations are made up and don't exist in real life.
            createStation("Torgbrons räddningstation", "Torisillan pelastusasema", 6693873, 240642);
            createStation("Sattmark räddningstation", "Sattmarkin pelastusasema", 6688829, 236771);
            createStation("Sydmo räddningstation", "Sydmon pelastusasema", 6694506, 230816);
            createStation("Attu räddningstation", "Atun pelastusasema", 6680212, 239980);
            createStation("Lemlax räddningstation", "Lemlahden pelastusasema", 6687755, 246042);
        }

        private void createStation(String swedishName, String finnishName, double lat, double lng) {
            add(new Station(new StationId(nextFreeId()),
                    new MultilingualString.Builder()
                            .withValue(SWEDISH, swedishName)
                            .withValue(FINNISH, finnishName)
                            .build(),
                    new GeographicLocation(CoordinateReferenceSystem.ETRS_TM35FIN, lat, lng), true));
        }
    };

    @GET
    @Path("/active")
    public List<Station> activeStations() {
        return DUMMY_DATA.all();
    }

    @GET
    @Path("/{id}")
    @SuppressWarnings("RestParamTypeInspection") // There is a custom provider that takes care of StationId
    public Station byId(@NotNull(message = "Valid station ID is required") @PathParam("id") StationId id) {
        var station = DUMMY_DATA.getById(id);
        if (station == null) {
            throw new NotFoundException("Station not found");
        }
        return station;
    }
}
