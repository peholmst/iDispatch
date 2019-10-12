package net.pkhapps.idispatch.gis.mongodb.importer.bindings;

import net.pkhapps.idispatch.gis.mongodb.importer.types.LocalizedString;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.xsd.ComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * TODO Document me!
 */
public class GeographicalNameBinding implements ComplexBinding {

    public static final QName TARGET = new QName("http://inspire.ec.europa.eu/schemas/gn/4.0",
            "GeographicalName");
    private final Map<String, Locale> iso3ToLocaleMap = new HashMap<>();

    public GeographicalNameBinding() {
        for (var language : Locale.getISOLanguages()) {
            var locale = new Locale(language);
            iso3ToLocaleMap.put(locale.getISO3Language(), locale);
        }
    }

    @Override
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {
    }

    @Override
    public void initializeChildContext(ElementInstance childInstance, Node node, MutablePicoContainer context) {
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        var locale = iso3ToLocaleMap.get((String) node.getChild("language").getValue());
        var text = (String) node.getChild("spelling").getChild("SpellingOfName").getChild("text").getValue();
        return new LocalizedString(locale, text);
    }

    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        throw new UnsupportedOperationException("This binding does not support encoding, only parsing");
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        throw new UnsupportedOperationException("This binding does not support encoding, only parsing");
    }

    @Override
    public List getProperties(Object object, XSDElementDeclaration element) throws Exception {
        throw new UnsupportedOperationException("This binding does not support encoding, only parsing");
    }

    @Override
    public QName getTarget() {
        return TARGET;
    }

    @Override
    public Class getType() {
        return LocalizedString.class;
    }

    @Override
    public int getExecutionMode() {
        return AFTER;
    }
}
