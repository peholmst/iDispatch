package net.pkhapps.idispatch.gis.domain.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.Clock;

/**
 * TODO Document me
 */
@Service
public class AddressPointImportService extends ImportedGeographicalMaterialImportService<AddressPoint> {

    AddressPointImportService(@NotNull AddressPointRepository addressPointRepository,
                              @NotNull MaterialImportRepository materialImportRepository,
                              @NotNull Clock clock) {
        super(addressPointRepository, materialImportRepository, clock, AddressPoint.class);
    }
}
