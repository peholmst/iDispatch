package net.pkhapps.idispatch.core.domain.geo;

import net.pkhapps.idispatch.core.domain.i18n.Locales;
import org.testng.annotations.Test;

/**
 * Unit test for {@link LocationName}.
 */
public class LocationNameTest {

    @Test
    public void createWithSingleName() {
        var name = new LocationName(Locales.SWEDISH, "namn");

    }

}
