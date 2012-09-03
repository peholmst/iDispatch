package net.pkhsolutions.idispatch.mapbrowser;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.geotools.data.DataStore;
import org.geotools.data.h2.H2DataStoreFactory;
import org.geotools.styling.Style;
import org.opengis.feature.type.FeatureType;

/**
 * TODO Document me!
 *
 * @author peholmst
 */
public class Main {

    public static final String OUTPUT_DATABASE_BASEDIR = "/Users/peholmst/Projects/BitBucket/idispatch/Maps/H2";
    public static final String OUTPUT_DATABASE_NAME = "iDispatchMap";

    public static void main(String[] args) throws Exception {
        final H2DataStoreFactory dbFactory = new H2DataStoreFactory();
        dbFactory.setBaseDirectory(new File(OUTPUT_DATABASE_BASEDIR));
        final HashMap params = new HashMap();
        params.put(H2DataStoreFactory.DATABASE.key, OUTPUT_DATABASE_NAME);
        System.out.println("Opening connection to database");
        final DataStore db = dbFactory.createDataStore(params);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JFrame mainFrame = new JFrame("iDispatch Map Browser");
                mainFrame.setLayout(new BorderLayout());

                final JFeatureTypeSelectionComponent featureSelection = new JFeatureTypeSelectionComponent();
                featureSelection.setDataStore(db);
                featureSelection.setBorder(BorderFactory.createTitledBorder("Feature Types"));
                mainFrame.add(featureSelection, BorderLayout.LINE_START);

                final JMapViewer mapViewer = new JMapViewer();
                mapViewer.setDataStore(db);
                mainFrame.add(mapViewer, BorderLayout.CENTER);
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.out.println("Closing connection to database");
                        db.dispose();
                    }
                });

                featureSelection.addFeatureTypeSelectionListener(new FeatureTypeSelectionListener() {
                    @Override
                    public void featureSelected(JFeatureTypeSelectionComponent sender, FeatureType featureType) {
                        final Class<?> geomType = featureType.getGeometryDescriptor().getType().getBinding();
                        Style style;
                        if (Polygon.class.isAssignableFrom(geomType)
                                || MultiPolygon.class.isAssignableFrom(geomType)) {
                            style = MapStyleFactory.getInstance().createPolygonStyle();

                        } else if (LineString.class.isAssignableFrom(geomType)
                                || MultiLineString.class.isAssignableFrom(geomType)) {
                            style = MapStyleFactory.getInstance().createLineStyle();

                        } else {
                            style = MapStyleFactory.getInstance().createPointStyle();
                        }
                        mapViewer.showFeatureType(featureType, style);
                    }

                    @Override
                    public void featureDeselected(JFeatureTypeSelectionComponent sender, FeatureType featureType) {
                        mapViewer.hideFeatureType(featureType);
                    }
                });

                mainFrame.pack();
                mainFrame.setVisible(true);
            }
        });
    }
}
