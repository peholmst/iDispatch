package net.pkhapps.idispatch.gis.domain.model;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MapTileServiceTest {

    private MapTileService mapTileService;

    @Before
    public void setUp() throws Exception {
        mapTileService = new MapTileService();
    }

    @Test
    public void calculateZoomLevel() {
        assertThat(mapTileService.calculateZoomLevel(0.5)).isEqualTo(0);
        assertThat(mapTileService.calculateZoomLevel(1)).isEqualTo(1);
        assertThat(mapTileService.calculateZoomLevel(2)).isEqualTo(2);
        assertThat(mapTileService.calculateZoomLevel(4)).isEqualTo(3);
        assertThat(mapTileService.calculateZoomLevel(8)).isEqualTo(4);
        assertThat(mapTileService.calculateZoomLevel(16)).isEqualTo(5);
        assertThat(mapTileService.calculateZoomLevel(32)).isEqualTo(6);
        assertThat(mapTileService.calculateZoomLevel(64)).isEqualTo(7);
        assertThat(mapTileService.calculateZoomLevel(128)).isEqualTo(8);
    }

    @Test
    public void calculateScale() {
        assertThat(mapTileService.calculateScale(0)).isEqualTo(0.5);
        assertThat(mapTileService.calculateScale(1)).isEqualTo(1);
        assertThat(mapTileService.calculateScale(2)).isEqualTo(2);
        assertThat(mapTileService.calculateScale(3)).isEqualTo(4);
        assertThat(mapTileService.calculateScale(4)).isEqualTo(8);
        assertThat(mapTileService.calculateScale(5)).isEqualTo(16);
        assertThat(mapTileService.calculateScale(6)).isEqualTo(32);
        assertThat(mapTileService.calculateScale(7)).isEqualTo(64);
        assertThat(mapTileService.calculateScale(8)).isEqualTo(128);
    }
}
