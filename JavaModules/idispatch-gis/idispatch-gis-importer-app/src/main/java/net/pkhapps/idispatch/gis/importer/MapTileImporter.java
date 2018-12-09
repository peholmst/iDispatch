package net.pkhapps.idispatch.gis.importer;

import com.vividsolutions.jts.geom.Envelope;
import net.pkhapps.idispatch.gis.domain.model.MapTile;
import net.pkhapps.idispatch.gis.domain.model.MapTileImportService;
import net.pkhapps.idispatch.gis.domain.model.MapTileService;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

/**
 * TODO Implement me
 */
@Service
public class MapTileImporter extends DirectoryScanningImporter {

    private static final int TILE_WIDTH_PX = 200;
    private static final int TILE_HEIGHT_PX = 200;
    private final MapTileService mapTileService;
    private final MapTileImportService mapTileImportService;

    public MapTileImporter(@NotNull PlatformTransactionManager platformTransactionManager,
                           @NotNull MapTileService mapTileService,
                           @NotNull MapTileImportService mapTileImportService) {
        super(platformTransactionManager, "*.png");
        this.mapTileService = mapTileService;
        this.mapTileImportService = mapTileImportService;
    }

    @Override
    protected void importDataFromFile(@NotNull File file) {
        logger().info("Reading map file from {}", file);
        var format = GridFormatFinder.findFormat(file);
        var reader = format.getReader(file);
        try {
            final var coverage = reader.read(null);
            final var image = coverage.getRenderedImage();

            final var width = image.getWidth();
            final var height = image.getHeight();

            if (width % TILE_WIDTH_PX != 0) {
                throw new IllegalArgumentException("Raster width is not evenly divisible by the tile width");
            }
            if (height % TILE_HEIGHT_PX != 0) {
                throw new IllegalArgumentException("Raster height is not evenly divisible by the tile height");
            }

            final var envelope = coverage.getEnvelope2D();
            final var scaleX = envelope.getWidth() / width;
            final var scaleY = -envelope.getHeight() / height;

            final var cols = width / TILE_WIDTH_PX;
            final var rows = height / TILE_HEIGHT_PX;

            logger().trace("Raster will be split up into {} tiles, scale X is {} and scale Y is {}",
                    cols * rows, scaleX, scaleY);

            final var raster = image.getData();
            var tiles = new ArrayList<MapTile>();
            for (int y = 0; y < rows; ++y) {
                for (int x = 0; x < cols; ++x) {
                    final var tileRect = new Rectangle(x * TILE_WIDTH_PX, y * TILE_HEIGHT_PX,
                            TILE_WIDTH_PX, TILE_HEIGHT_PX);
                    final var tileRaster = raster.createChild(tileRect.x, tileRect.y, tileRect.width,
                            tileRect.height, 0, 0, null);
                    final var writableRaster = tileRaster.createCompatibleWritableRaster();
                    writableRaster.setDataElements(0, 0, tileRaster);
                    final var rasterImage = new BufferedImage(image.getColorModel(), writableRaster,
                            image.getColorModel().isAlphaPremultiplied(), null);

                    final var x1 = envelope.getMinX() + scaleX * tileRect.x;
                    final var x2 = x1 + tileRect.width * scaleX;
                    final var y1 = envelope.getMinY() + scaleY * tileRect.y;
                    final var y2 = y1 + tileRect.height * scaleY;
                    final var mapTile = mapTileService.createMapTile(rasterImage, (float) scaleX, (float) scaleY, new Envelope(x1, x2, y1, y2));
                    tiles.add(mapTile);
                }
            }
            // TODO Turn for loop into an iterator and pass it into importData()
            mapTileImportService.importData(file.getName(), Instant.ofEpochMilli(file.lastModified()), tiles.iterator());
        } catch (IOException ex) {
            logger().error("Error importing " + file, ex);
        } finally {
            reader.dispose();
        }
    }
}