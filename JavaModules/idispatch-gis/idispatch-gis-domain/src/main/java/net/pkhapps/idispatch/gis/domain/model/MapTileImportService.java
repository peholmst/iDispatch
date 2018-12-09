package net.pkhapps.idispatch.gis.domain.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.Clock;

/**
 * TODO Document me
 */
@Service
public class MapTileImportService extends ImportedGeographicalMaterialImportService<MapTile> {

    MapTileImportService(@NotNull MapTileRepository mapTileRepository,
                         @NotNull MaterialImportRepository materialImportRepository,
                         @NotNull Clock clock) {
        super(mapTileRepository, materialImportRepository, clock, MapTile.class);
    }
}
