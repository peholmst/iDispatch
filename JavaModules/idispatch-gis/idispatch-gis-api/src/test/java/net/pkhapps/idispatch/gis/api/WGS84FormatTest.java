package net.pkhapps.idispatch.gis.api;

import org.assertj.core.data.Offset;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link WGS84Format}.
 */
public class WGS84FormatTest {

    @Test
    public void decimalDegrees_format() {
        assertThat(WGS84Format.DD.formatLatitude(60.306717034, Locales.FINNISH)).isEqualTo("60,306717");
        assertThat(WGS84Format.DD.formatLongitude(22.300860593, Locales.FINNISH)).isEqualTo("022,300861");
    }

    @Test
    public void decimalDegrees_parse() {
        assertThat(WGS84Format.DD.parseLatitude("60,306717", Locales.FINNISH)).isEqualTo(60.306717);
        assertThat(WGS84Format.DD.parseLongitude("022,300861", Locales.FINNISH)).isEqualTo(22.300861);
    }

    @Test
    public void degreesDecimalMinutes_format() {
        assertThat(WGS84Format.DDM.formatLatitude(60.306717034, Locales.FINNISH)).isEqualTo("60° 18,40302'");
        assertThat(WGS84Format.DDM.formatLongitude(22.300860593, Locales.FINNISH)).isEqualTo("022° 18,05164'");
    }

    @Test
    public void degreesDecimalMinutes_parse() {
        assertThat(WGS84Format.DDM.parseLatitude("60° 18,40302'", Locales.FINNISH)).isEqualTo(60.306717, Offset.offset(0.000001));
        assertThat(WGS84Format.DDM.parseLongitude("022° 18,05164'", Locales.FINNISH)).isEqualTo(22.300861, Offset.offset(0.000001));
    }

    @Test
    public void degreesMinutesSeconds_format() {
        assertThat(WGS84Format.DMS.formatLatitude(60.306717034, Locales.FINNISH)).isEqualTo("60° 18' 24,181\"");
        assertThat(WGS84Format.DMS.formatLongitude(22.300860593, Locales.FINNISH)).isEqualTo("022° 18' 3,098\"");
    }

    @Test
    public void degreesMinutesSeconds_parse() {
        assertThat(WGS84Format.DMS.parseLatitude("60° 18' 24,181\"", Locales.FINNISH)).isEqualTo(60.306717, Offset.offset(0.000001));
        assertThat(WGS84Format.DMS.parseLongitude("022° 18' 3,098\"", Locales.FINNISH)).isEqualTo(22.300861, Offset.offset(0.000001));
    }
}