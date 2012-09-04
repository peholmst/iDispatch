/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.domain.common.entity;

import java.math.BigDecimal;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Unit test for {@link GeoLocation}.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class GeoLocationTest {

    @Test
    public void NULL() {
        GeoLocation n = GeoLocation.NULL();
        assertTrue(n.isNull());
        assertNull(n.getLatitude());
        assertNull(n.getLongitude());
    }

    @Test
    public void buildNew() {
        GeoLocation.Builder builder = new GeoLocation.Builder();
        GeoLocation n = builder
                .withLatitude(new BigDecimal("45.12345"))
                .withLongitude(new BigDecimal("89.67890"))
                .build();
        assertFalse(n.isNull());
        assertEquals(n.getLatitude(), new BigDecimal("45.12345"));
        assertEquals(n.getLongitude(), new BigDecimal("89.67890"));

        GeoLocation n2 = builder.build();
        assertNotSame(n, n2);
        assertTrue(n.hasSameValue(n2));
        assertEquals(n, n2);
        assertEquals(n.hashCode(), n2.hashCode());
    }

    @Test
    public void deriveNull() {
        GeoLocation n = GeoLocation.NULL().derive()
                .withLongitude(new BigDecimal("89.67890"))
                .build();
        assertFalse(n.isNull());
        assertNull(n.getLatitude());
        assertEquals(n.getLongitude(), new BigDecimal("89.67890"));
    }
}
