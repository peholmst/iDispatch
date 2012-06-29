package net.pkhsolutions.idispatch.mapbrowser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.geotools.data.DataStore;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * TODO Document me!
 *
 * @author peholmst
 */
public class JFeatureTypeSelectionComponent extends JPanel implements ActionListener {

    private DataStore dataStore;
    private final Map<JCheckBox, Name> checkBoxes = new HashMap<>();

    public JFeatureTypeSelectionComponent() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    public DataStore getDataStore() {
        return dataStore;
    }

    public void setDataStore(DataStore dataStore) {
        removeFeatureTypeCheckboxes();
        this.dataStore = dataStore;
        try {
            createFeatureTypeCheckboxes();
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void addFeatureTypeSelectionListener(FeatureTypeSelectionListener l) {
        listenerList.add(FeatureTypeSelectionListener.class, l);
    }

    public void removeFeatureTypeSelectionListener(FeatureTypeSelectionListener l) {
        listenerList.remove(FeatureTypeSelectionListener.class, l);
    }

    private void removeFeatureTypeCheckboxes() {
        for (JCheckBox checkBox : checkBoxes.keySet()) {
            checkBox.removeActionListener(this);
            remove(checkBox);
        }
        checkBoxes.clear();
    }

    private void createFeatureTypeCheckboxes() throws IOException {
        if (dataStore != null) {
            for (final Name name : dataStore.getNames()) {
                final JCheckBox checkBox = new JCheckBox(name.toString());
                checkBoxes.put(checkBox, name);
                checkBox.addActionListener(this);
                add(checkBox);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Name name = getAffectedName(e);
        if (name != null) {
            try {
                final FeatureType featureType = dataStore.getSchema(name);
                fireFeatureTypeSelectionEvent(e, featureType);
            } catch (IOException ex) {
                handleException(ex);
            }
        }
    }

    private Name getAffectedName(ActionEvent e) {
        if (e.getSource() instanceof JCheckBox) {
            final JCheckBox source = (JCheckBox) e.getSource();
            return checkBoxes.get(source);
        }
        return null;
    }

    private void fireFeatureTypeSelectionEvent(ActionEvent e, FeatureType featureType) {
        final boolean selected = ((JCheckBox) e.getSource()).isSelected();
        for (FeatureTypeSelectionListener l : listenerList.getListeners(FeatureTypeSelectionListener.class)) {
            if (selected) {
                l.featureSelected(this, featureType);
            } else {
                l.featureDeselected(this, featureType);
            }
        }
    }

    private void handleException(Exception e) {
        // TODO Show the error to the user in some way
        e.printStackTrace();
    }
}
