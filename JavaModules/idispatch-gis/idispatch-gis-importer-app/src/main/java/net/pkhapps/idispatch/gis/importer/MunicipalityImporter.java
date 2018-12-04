package net.pkhapps.idispatch.gis.importer;

import net.pkhapps.idispatch.gis.domain.model.MaterialImportRepository;
import net.pkhapps.idispatch.gis.domain.model.Municipality;
import net.pkhapps.idispatch.gis.domain.model.MunicipalityRepository;
import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Importer that imports the municipality names and codes from the NLS XML schema.
 */
@Component
public class MunicipalityImporter extends BaseImporter<Void> {

    private static final String SOURCE = "http://xml.nls.fi/Nimisto/Nimistorekisteri/kunta.xsd";
    private final MunicipalityRepository municipalityRepository;

    public MunicipalityImporter(Clock clock,
                                PlatformTransactionManager platformTransactionManager,
                                MaterialImportRepository materialImportRepository,
                                MunicipalityRepository municipalityRepository) {
        super(clock, platformTransactionManager, materialImportRepository);
        this.municipalityRepository = municipalityRepository;
    }

    @Override
    public void importData(Void argument) {
        var count = new AtomicInteger();
        var importId = createMaterialImport(SOURCE);
        logger().info("Reading data from {}", SOURCE);
        XMLInputFactory inputFactory = XMLInputFactory.newFactory();
        try (InputStream is = new URL(SOURCE).openStream()) {
            XMLStreamReader reader = inputFactory.createXMLStreamReader(is);
            while (reader.hasNext()) {
                int eventType = reader.next();
                if (eventType == START_ELEMENT && reader.getLocalName().equals("enumeration")) {
                    readEnumeration(importId, reader, this::importMunicipality, count);
                }
            }
            logger().info("Processed {} municipalities", count.get());
        } catch (Exception ex) {
            logger().error("Error importing municipalities", ex);
        }
    }

    private void importMunicipality(@NotNull Municipality municipality) {
        transactionTemplate().execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                if (!municipalityRepository.existsByCode(municipality.code())) {
                    logger().trace("Importing {}", municipality);
                    municipalityRepository.save(municipality);
                } else {
                    logger().trace("{} already exists in the database, skipping", municipality);
                }
            }
        });
    }

    private void readEnumeration(@NotNull MaterialImportId materialImportId, @NotNull XMLStreamReader reader,
                                 @NotNull Consumer<Municipality> consumer, @NotNull AtomicInteger count)
            throws Exception {
        int code = Integer.parseInt(reader.getAttributeValue(null, "value"));
        String nameFi = "";
        String nameSv = "";
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == START_ELEMENT && reader.getLocalName().equals("documentation")) {
                String lang = reader.getAttributeValue(null, "lang");
                String value = reader.getElementText();
                if ("fin".equals(lang)) {
                    nameFi = value;
                } else if ("swe".equals(lang)) {
                    nameSv = value;
                }
            } else if (eventType == END_ELEMENT && reader.getLocalName().equals("enumeration")) {
                consumer.accept(new Municipality(code, nameFi, nameSv, LocalDate.now(), materialImportId));
                count.incrementAndGet();
                return;
            }
        }
    }
}
