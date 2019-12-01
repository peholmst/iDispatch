package net.pkhapps.idispatch.gis.api;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Locale;

/**
 * TODO Document me
 */
public interface AxisFormat extends Serializable {

    /**
     * @param coordinate
     * @param locale
     * @return
     */
    @NotNull String format(double coordinate, @NotNull Locale locale);

    /**
     * @param coordinate
     * @param locale
     * @return
     * @throws IllegalArgumentException
     */
    double parse(@NotNull String coordinate, @NotNull Locale locale);

    /**
     * @param coordinate
     * @return
     */
    double[] splitComponents(double coordinate);

    /**
     * @param components
     * @return
     * @throws IllegalArgumentException
     */
    double joinComponents(double... components);

    /**
     * @param componentIndex
     * @param componentValue
     * @param locale
     * @return
     */
    String formatComponent(int componentIndex, double componentValue, @NotNull Locale locale);

    /**
     * @return
     */
    int getComponentCount();

    /**
     * @param componentIndex
     * @param locale
     * @return
     */
    @NotNull String getComponentUnit(int componentIndex, @NotNull Locale locale);

    /**
     * @param coordinate
     * @param locale
     * @return
     */
    @NotNull String getAxisSuffix(double coordinate, @NotNull Locale locale);
}
