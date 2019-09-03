package net.pkhapps.idispatch.core.mongodb.geo;

import net.pkhapps.idispatch.core.domain.geo.Location;
import org.bson.Document;
import org.jetbrains.annotations.Contract;

public class LocationMapper {

    @Contract("null -> null")
    public Document toDocument(Location location) {
        return null;
    }

    @Contract("null -> null")
    public Location toLocation(Document document) {
        return null;
    }

}
