package net.pkhapps.idispatch.gis.api.lookup;

import net.pkhapps.idispatch.gis.api.lookup.code.AddressPointClass;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * TODO Document me
 */
public interface AddressPoint extends PointFeature, NamedFeature {

    @NotNull AddressPointClass getAddressPointClass();

    @NotNull Optional<String> getNumber();
}
