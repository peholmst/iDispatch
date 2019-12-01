package net.pkhapps.idispatch.gis.api;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Locale;

/**
 * TODO Document me
 */
public interface CoordinateFormat extends Serializable {

    /**
     * @param locale
     * @return
     */
    @NotNull String getName(@NotNull Locale locale);

    /**
     * @return
     */
    @NotNull AxisFormat getXAxis();

    /**
     * @return
     */
    @NotNull AxisFormat getYAxis();
}
