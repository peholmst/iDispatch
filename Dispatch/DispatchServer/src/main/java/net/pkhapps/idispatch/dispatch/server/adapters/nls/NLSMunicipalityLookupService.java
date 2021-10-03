/*
 * iDispatch Dispatch Server
 * Copyright (C) 2021 Petter Holmström
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.pkhapps.idispatch.dispatch.server.adapters.nls;

import net.pkhapps.idispatch.dispatch.server.ports.gis.Municipality;
import net.pkhapps.idispatch.dispatch.server.ports.gis.MunicipalityCode;
import net.pkhapps.idispatch.dispatch.server.ports.gis.MunicipalityLookupService;
import net.pkhapps.idispatch.dispatch.server.util.Locales;
import net.pkhapps.idispatch.dispatch.server.util.MultilingualStringLiteral;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import java.net.URI;
import java.util.*;
import java.util.stream.Stream;

/**
 * Implementation of {@link MunicipalityLookupService} that reads the municipalities from an XSD file published by the
 * National Land Survey of Finland.
 */
public class NLSMunicipalityLookupService implements MunicipalityLookupService {

    private static final URI MUNICIPALITY_SCHEMA = URI.create("http://xml.nls.fi/Nimisto/Nimistorekisteri/kunta.xsd");

    private final List<Municipality> municipalities;

    /**
     * Creates a new instance of {@code NLSMunicipalityLookupService}, fetching the municipality data from the web and
     * storing it in memory.
     *
     * @throws IllegalStateException if the municipalities could not be loaded.
     */
    public NLSMunicipalityLookupService() {
        this(MUNICIPALITY_SCHEMA);
    }

    protected NLSMunicipalityLookupService(URI source) {
        try {
            var saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            var parser = saxParserFactory.newSAXParser();
            var reader = parser.getXMLReader();
            reader.setContentHandler(new MunicipalityHandler());

            municipalities = new ArrayList<>();
            reader.parse(source.toString());
        } catch (Exception ex) {
            throw new IllegalStateException("Could not initialize list of municipalities", ex);
        }
    }

    @Override
    public Stream<Municipality> findAll() {
        return municipalities.stream();
    }

    private class MunicipalityHandler extends DefaultHandler {

        private final Map<Locale, String> names = new HashMap<>();
        private String code;
        private Locale currentLocale;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if ("enumeration".equals(localName)) {
                code = attributes.getValue("value");
            } else if ("documentation".equals(localName) && code != null) {
                switch (attributes.getValue("xml:lang")) {
                    case "fin" -> currentLocale = Locales.FINNISH;
                    case "swe" -> currentLocale = Locales.SWEDISH;
                    default -> currentLocale = null;
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (code != null && currentLocale != null) {
                names.put(currentLocale, String.valueOf(ch, start, length));
                currentLocale = null;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if ("enumeration".equals(localName) && code != null) {
                municipalities.add(new Municipality(
                                new MunicipalityCode(code),
                                MultilingualStringLiteral.fromBilingualString(
                                        Locales.FINNISH, names.get(Locales.FINNISH),
                                        Locales.SWEDISH, names.get(Locales.SWEDISH)
                                )
                        )
                );
                code = null;
                names.clear();
            }
        }
    }
}
