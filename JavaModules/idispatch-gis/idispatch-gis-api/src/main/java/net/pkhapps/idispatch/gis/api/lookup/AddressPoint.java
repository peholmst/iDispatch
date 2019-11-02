package net.pkhapps.idispatch.gis.api.lookup;

import net.pkhapps.idispatch.gis.api.lookup.code.AddressPointClass;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Address points are named locations used in places where there typically are no roads, such as on smaller islands.
 * They are used to make navigation easier.
 */
@SuppressWarnings("unused")
public interface AddressPoint extends PointFeature, NamedFeature {

    /**
     * The class of this address point
     */
    @NotNull AddressPointClass getAddressPointClass();

    /**
     * The number of this address point if applicable
     */
    @NotNull Optional<String> getNumber();
}
