package net.pkhapps.idispatch.gis.domain.model;

import com.vividsolutions.jts.geom.Envelope;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * TODO document me
 */
@Service
public class MapTileService {

    MapTileService() throws Exception {
    }

    public @NotNull Pair<Integer, Integer> calculateColAndRow(@NotNull Envelope envelope) {
        var col = (int) (envelope.getMaxX() / envelope.getWidth());
        var row = (int) (envelope.getMaxY() / envelope.getHeight());
        return Pair.of(col, row);
    }

    public int calculateZoomLevel(double scale) {
        // You can calculate the binary logarithm of integers like this (https://en.wikipedia.org/wiki/Binary_logarithm)
        return 31 - Integer.numberOfLeadingZeros((int) (Math.abs(scale) * 2));
    }

    public double calculateScale(int zoom) {
        return Math.pow(2, zoom) / 2d;
    }

    public @NotNull MapTile createMapTile(@NotNull BufferedImage tile, float scaleX, float scaleY, @NotNull Envelope envelope) throws IOException {
        var zoom = calculateZoomLevel(scaleX);
        var colRow = calculateColAndRow(envelope);
        try (var bos = new ByteArrayOutputStream()) {
            ImageIO.write(tile, "png", bos);
            return new MapTile(zoom, colRow.getFirst(), colRow.getSecond(), envelope, scaleX, scaleY, bos.toByteArray(),
                    MimeTypeUtils.IMAGE_PNG_VALUE);
        }
    }
}
