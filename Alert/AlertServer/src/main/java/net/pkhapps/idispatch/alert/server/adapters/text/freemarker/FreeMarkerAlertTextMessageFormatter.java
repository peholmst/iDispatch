// iDispatch Alert Server
// Copyright (C) 2021 Petter Holmström
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

package net.pkhapps.idispatch.alert.server.adapters.text.freemarker;

import freemarker.template.*;
import net.pkhapps.idispatch.alert.server.application.text.AlertTextMessageFormatException;
import net.pkhapps.idispatch.alert.server.application.text.AlertTextMessageFormatter;
import net.pkhapps.idispatch.alert.server.data.*;

import java.io.IOException;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link AlertTextMessageFormatter} that uses FreeMarker to produce alert messages.
 */
public class FreeMarkerAlertTextMessageFormatter implements AlertTextMessageFormatter {

    // TODO Document the model

    public static final String MODEL_ALERT = "alert";
    public static final String MODEL_ALERT_INSTANT = "alertInstant";
    public static final String MODEL_INCIDENT_IDENTIFIER = "incidentIdentifier";
    public static final String MODEL_INCIDENT_INSTANT = "incidentInstant";
    public static final String MODEL_INCIDENT_TYPE = "incidentType";
    public static final String MODEL_INCIDENT_URGENCY = "incidentUrgency";
    public static final String MODEL_MUNICIPALITY = "municipality";
    public static final String MODEL_COORDINATES = "coordinates";
    public static final String MODEL_ADDRESS = "address";
    public static final String MODEL_ADDRESS_ROAD = "road";
    public static final String MODEL_ADDRESS_NUMBER = "number";
    public static final String MODEL_ADDRESS_INTERSECTING_ROAD = "intersectingRoad";
    public static final String MODEL_ADDRESS_COMMENTS = "comments";
    public static final String MODEL_DETAILS = "details";
    public static final String MODEL_RESOURCES = "resources";
    private static final int TEMPLATE_CACHE_MAX_SIZE = 100;
    private final Configuration cfg;
    private final ZoneId timeZone;
    private final Map<String, Template> templateCache = new ConcurrentHashMap<>();

    /**
     * Creates a new {@code FreeMarkerAlertTextMessageFormatter} with a default configuration and time zone.
     */
    public FreeMarkerAlertTextMessageFormatter() {
        this(new Configuration(Configuration.VERSION_2_3_31), ZoneId.systemDefault());
    }

    /**
     * Creates a new {@code FreeMarkerAlertTextMessageFormatter}.
     *
     * @param configuration the FreeMarker configuration to use, must not be {@code null}.
     * @param timeZone      the time zone to use when formatting timestamps, must not be {@code null}.
     */
    public FreeMarkerAlertTextMessageFormatter(Configuration configuration, ZoneId timeZone) {
        this.timeZone = requireNonNull(timeZone, "timeZone must not be null");
        cfg = requireNonNull(configuration, "configuration must not be null");
    }

    private static Map<String, Object> convertToModel(GeoPoint geoPoint) {
        return Map.of("lat", geoPoint.lat(), "lon", geoPoint.lon());
    }

    private static TemplateModel convertToModel(Address address) {
        return new AddressModel(address);
    }

    private static TemplateModel convertToModel(MultilingualString multilingualString) {
        return multilingualString == null ? EmptyTemplateHashModel.INSTANCE : new MultilingualStringModel(multilingualString);
    }

    private static TemplateModel convertToModel(String s) {
        return s == null ? TemplateModel.NOTHING : new SimpleScalar(s);
    }

    @Override
    public String formatAlertMessage(Alert alert, String template) {
        var tmpl = templateCache.computeIfAbsent(template, t -> {
            try {
                return new Template(null, t, cfg);
            } catch (IOException ex) {
                throw new AlertTextMessageFormatException("Could not parse template", ex);
            }
        });
        try {
            var writer = new StringWriter();
            tmpl.process(convertToModel(alert), writer);
            return writer.toString();
        } catch (Exception ex) {
            throw new AlertTextMessageFormatException("Could not format alert", ex);
        } finally {
            while (templateCache.size() > TEMPLATE_CACHE_MAX_SIZE) {
                // Until there is evidence of a performance impact because the wrong items are evicted from the cache,
                // we are just going to remove something from the cache to make sure it does not eat up all the memory.
                templateCache.remove(templateCache.keySet().iterator().next());
            }
        }
    }

    private Map<String, Object> convertToModel(Alert alert) {
        var alertModel = new HashMap<String, Object>();
        alertModel.put(MODEL_ALERT_INSTANT, formatInstant(alert.alertInstant()));
        alertModel.put(MODEL_INCIDENT_IDENTIFIER, alert.incidentIdentifier()
                .map(IncidentIdentifier::unwrap)
                .orElse(""));
        alertModel.put(MODEL_INCIDENT_INSTANT, alert.incidentInstant()
                .map(this::formatInstant)
                .orElse(""));
        alertModel.put(MODEL_INCIDENT_TYPE, alert.incidentType().unwrap());
        alertModel.put(MODEL_INCIDENT_URGENCY, alert.incidentUrgency().unwrap());
        alertModel.put(MODEL_MUNICIPALITY, convertToModel(alert.municipality().unwrap()));
        alertModel.put(MODEL_COORDINATES, convertToModel(alert.coordinates()));
        alertModel.put(MODEL_ADDRESS, alert.address()
                .map(FreeMarkerAlertTextMessageFormatter::convertToModel)
                .orElse(EmptyTemplateHashModel.INSTANCE));
        alertModel.put(MODEL_DETAILS, alert.details());
        alertModel.put(MODEL_RESOURCES, alert.assignedResources()
                .stream()
                .map(ResourceIdentifier::unwrap)
                .sorted()
                .collect(Collectors.toList()));
        return Map.of(MODEL_ALERT, alertModel);
    }

    private String formatInstant(Instant instant) {
        return LocalDateTime
                .ofInstant(instant, timeZone)
                .withNano(0) // In this context, we don't need nanoseconds
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private record MultilingualStringModel(
            MultilingualString multilingualString) implements TemplateScalarModel, TemplateHashModel {

        @Override
        public TemplateModel get(String key) {
            return new SimpleScalar(multilingualString.localizedValue(Locale.forLanguageTag(key)));
        }

        @Override
        public boolean isEmpty() {
            return false; // Never empty; a multilingual string always contains something
        }

        @Override
        public String getAsString() {
            return multilingualString.defaultValue();
        }
    }

    private static class EmptyTemplateHashModel implements TemplateScalarModel, TemplateHashModel {

        static final TemplateModel INSTANCE = new EmptyTemplateHashModel();

        @Override
        public TemplateModel get(String key) {
            return INSTANCE;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public String getAsString() {
            return "";
        }
    }

    private static class AddressModel implements TemplateScalarModel, TemplateHashModel {

        private final Address address;
        private final MultilingualString addressAsString;

        private AddressModel(Address address) {
            assert address != null;
            this.address = address;
            if (address.isIntersection()) {
                addressAsString = FormattedMultilingualString.format("{0} || {1}",
                        address.roadOrAddressPointName(), address.intersectingRoadName());
            } else if (address.isUnnamed()) {
                addressAsString = MultilingualStringLiteral.fromMonolingualString(address.comments());
            } else if (address.isExact()) {
                addressAsString = FormattedMultilingualString.format("{0} {1}", address.roadOrAddressPointName(),
                        address.number());
            } else {
                addressAsString = address.roadOrAddressPointName();
            }
        }

        @Override
        public TemplateModel get(String key) {
            return switch (key) {
                case MODEL_ADDRESS_ROAD -> convertToModel(address.roadOrAddressPointName());
                case MODEL_ADDRESS_NUMBER -> convertToModel(address.number());
                case MODEL_ADDRESS_INTERSECTING_ROAD -> convertToModel(address.intersectingRoadName());
                case MODEL_ADDRESS_COMMENTS -> convertToModel(address.comments());
                default -> new SimpleScalar(addressAsString.localizedValue(Locale.forLanguageTag(key)));
            };
        }

        @Override
        public boolean isEmpty() {
            return false; // An address is never empty
        }

        @Override
        public String getAsString() {
            return addressAsString.defaultValue();
        }
    }
}
