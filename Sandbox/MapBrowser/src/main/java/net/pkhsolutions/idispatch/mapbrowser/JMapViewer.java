package net.pkhsolutions.idispatch.mapbrowser;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.Style;
import org.opengis.feature.type.FeatureType;

/**
 * TODO Document me!
 *
 * @author peholmst
 */
public class JMapViewer extends JPanel {

    private DataStore dataStore;
    private final GTRenderer renderer;
    private MapContent map;
    private final List<FeatureType> visibleFeatureTypes = new ArrayList<>();
    private final Map<FeatureType, Layer> featureTypeLayers = new HashMap<>();

    public JMapViewer() {
        renderer = new StreamingRenderer();
    }

    public void setDataStore(final DataStore dataStore) {
        if (map != null) {
            map.dispose();
        }
        map = new MapContent();
        renderer.setMapContent(map);
        visibleFeatureTypes.clear();
        featureTypeLayers.clear();
        this.dataStore = dataStore;
        repaint();
    }

    public DataStore getDataStore() {
        return this.dataStore;
    }

    public void showFeatureType(final FeatureType featureType, final Style style) {
        try {
            if (dataStore != null && !visibleFeatureTypes.contains(featureType)) {
                final SimpleFeatureSource featureSource = dataStore.getFeatureSource(featureType.getName());
                final Layer featureLayer = new FeatureLayer(featureSource, style);
                map.addLayer(featureLayer);

                visibleFeatureTypes.add(featureType);
                featureTypeLayers.put(featureType, featureLayer);

                repaint();
            }
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void hideFeatureType(final FeatureType featureType) {
        if (visibleFeatureTypes.remove(featureType)) {
            map.removeLayer(featureTypeLayers.get(featureType));
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (dataStore != null && map.layers().size() > 0) {
            renderer.paint(((Graphics2D) g), getBounds(), map.getViewport().getBounds());
        } else {
            // TODO Render "No map data available" or something like that
        }    
    }

    private void handleException(Exception e) {
        // TODO Show the error to the user in some way
        e.printStackTrace();
    }
}
