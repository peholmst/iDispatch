package net.pkhsolutions.idispatch.mapbrowser;

import java.util.EventListener;
import org.opengis.feature.type.FeatureType;

/**
 * TODO Document me!
 *
 * @author peholmst
 */
public interface FeatureTypeSelectionListener extends EventListener {

    void featureSelected(JFeatureTypeSelectionComponent sender, FeatureType featureType);

    void featureDeselected(JFeatureTypeSelectionComponent sender, FeatureType featureType);
}
