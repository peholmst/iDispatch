package net.pkhsolutions.idispatch.mapimporter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureStore;
import org.geotools.data.h2.H2DataStoreFactory;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A small program that imports a set of Shapefiles into an H2 database.
 *
 * @author Petter Holmström
 */
public class ShapefileImporter {

    public static final String INPUT_SHAPEFILE_ADMINISTRATIVE = "/Users/peholmst/Projects/BitBucket/idispatch/Maps/finland_administrative.shp";
    public static final String INPUT_SHAPEFILE_COASTLINE = "/Users/peholmst/Projects/BitBucket/idispatch/Maps/finland_coastline.shp";
    public static final String INPUT_SHAPEFILE_HIGHWAY = "/Users/peholmst/Projects/BitBucket/idispatch/Maps/finland_highway.shp";
    public static final String INPUT_SHAPEFILE_LOCATION = "/Users/peholmst/Projects/BitBucket/idispatch/Maps/finland_location.shp";
    public static final String INPUT_SHAPEFILE_NATURAL = "/Users/peholmst/Projects/BitBucket/idispatch/Maps/finland_natural.shp";
    public static final String INPUT_SHAPEFILE_POI = "/Users/peholmst/Projects/BitBucket/idispatch/Maps/finland_poi.shp";
    public static final String OUTPUT_DATABASE_BASEDIR = "/Users/peholmst/Projects/BitBucket/idispatch/Maps/H2";
    public static final String OUTPUT_DATABASE_NAME = "iDispatchMap";

    public static void main(String[] args) throws Exception {
        final H2DataStoreFactory destinationFactory = new H2DataStoreFactory();
        destinationFactory.setBaseDirectory(new File(OUTPUT_DATABASE_BASEDIR));
        final HashMap params = new HashMap();
        params.put(H2DataStoreFactory.DATABASE.key, OUTPUT_DATABASE_NAME);
        System.out.println("Opening connection to database");
        final DataStore destination = destinationFactory.createDataStore(params);

        importShapeFile(new File(INPUT_SHAPEFILE_ADMINISTRATIVE).toURI().toURL(), destination, "ADMINISTRATIVE");
        importShapeFile(new File(INPUT_SHAPEFILE_COASTLINE).toURI().toURL(), destination, "COASTLINE");
        importShapeFile(new File(INPUT_SHAPEFILE_HIGHWAY).toURI().toURL(), destination, "HIGHWAY");
        importShapeFile(new File(INPUT_SHAPEFILE_LOCATION).toURI().toURL(), destination, "LOCATION");
        importShapeFile(new File(INPUT_SHAPEFILE_NATURAL).toURI().toURL(), destination, "NATURAL");
        importShapeFile(new File(INPUT_SHAPEFILE_POI).toURI().toURL(), destination, "POI");

        System.out.println("Closing connection to database");
        destination.dispose();
    }

    private static void importShapeFile(URL shapeFileUrl, DataStore destination, String featureTypeName)
            throws MalformedURLException, IOException, SchemaException {
        System.out.println("Importing shapefile " + shapeFileUrl + " to destination " + destination);
        final ShapefileDataStore source = new ShapefileDataStore(shapeFileUrl);

        SimpleFeatureType destFT = FeatureTypes.transform(source.getSchema(), source.getSchema().getCoordinateReferenceSystem());
        SimpleFeatureTypeBuilder destFTBuilder = new SimpleFeatureTypeBuilder();
        destFTBuilder.init(destFT);
        destFTBuilder.setName(featureTypeName);
        destFT = destFTBuilder.buildFeatureType();

        System.out.println(" - Creating schema " + destFT);
        destination.createSchema(destFT);
        FeatureStore<SimpleFeatureType, SimpleFeature> fs = (FeatureStore) destination.getFeatureSource(destFT.getTypeName());

        System.out.print(" - Importing features ... ");
        FeatureIterator<SimpleFeature> fi = source.getFeatureSource().getFeatures().features();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(fs.getSchema());
        long featureCount = 0;
        while (fi.hasNext()) {
            SimpleFeature sf = fi.next();
            fb.addAll(sf.getAttributes());
            SimpleFeature tf = fb.buildFeature(sf.getID());
            fs.addFeatures(DataUtilities.collection(tf));
            ++featureCount;
        }
        System.out.println(featureCount + " imported.");
        fi.close();
        source.dispose();
    }
}
