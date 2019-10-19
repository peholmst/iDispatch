package net.pkhapps.idispatch.gis.api.lookup;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * TODO Document me
 */
public interface AddressNumberRange {

    @NotNull Optional<Integer> getMinimum();

    @NotNull Optional<Integer> getMaximum();
}
