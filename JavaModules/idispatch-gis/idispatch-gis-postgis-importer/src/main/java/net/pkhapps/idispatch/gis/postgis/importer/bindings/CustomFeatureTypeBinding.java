package net.pkhapps.idispatch.gis.postgis.importer.bindings;

import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml3.XSDIdRegistry;
import org.geotools.gml3.bindings.AbstractFeatureTypeBinding;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.xsd.*;
import org.opengis.feature.simple.SimpleFeature;

/**
 * TODO Document me
 */
public class CustomFeatureTypeBinding extends AbstractFeatureTypeBinding {

    public CustomFeatureTypeBinding(FeatureTypeCache ftCache, BindingWalkerFactory bwFactory, SchemaIndex schemaIndex,
                                    Configuration configuration, XSDIdRegistry idRegistry,
                                    GML3EncodingUtils encodingUtils) {
        super(ftCache, bwFactory, schemaIndex, configuration, idRegistry, encodingUtils);
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        var result = (SimpleFeature) super.parse(instance, node, value);
        result.getProperties().forEach(property -> {
            // This is a workaround for properties with multiple values. The original parser only fetches one of the
            // attributes and ignores the rest. We want all of them.
            var childValues = node.getChildValues(property.getName().getLocalPart());
            if (childValues.size() > 1) {
                property.setValue(childValues);
            }
        });
        return result;
    }
}
