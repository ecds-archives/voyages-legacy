package edu.emory.library.tas.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import edu.umn.gis.mapscript.classObj;
import edu.umn.gis.mapscript.imageObj;
import edu.umn.gis.mapscript.layerObj;
import edu.umn.gis.mapscript.lineObj;
import edu.umn.gis.mapscript.mapObj;
import edu.umn.gis.mapscript.mapscriptConstants;
import edu.umn.gis.mapscript.pointObj;
import edu.umn.gis.mapscript.shapeObj;
import edu.umn.gis.mapscript.styleObj;

public class MapTest {
	public static void usage() {
	    System.err.println("Usage: DrawMap {mapfile} {outfile}");
	    System.exit(-1);
	  }

	  public static void main(String[] args) throws IOException {
			String mapFile = "/home/juri/gis/tests/voyages_new1.map";
			String imgFile = "/home/juri/gis/tests/voyages_new1.gif";
			
			
 			mapObj map = new mapObj(mapFile);
			map.setSize(1024, 768);
			map.setExtent(0, 0, 90, 90);
			
			layerObj layerEurope = new layerObj(map);
			map.insertLayer(layerEurope, 2);
			
			layerEurope.setName("ports");
			layerEurope.setType(mapscriptConstants.MS_LAYER_POINT);
			layerEurope.setStatus(mapscriptConstants.MS_DEFAULT);
			layerEurope.setLabelitem("CITY_NAME");
			
			classObj classCities = new classObj(layerEurope);
			layerEurope.insertClass(classCities, 0);
			
			styleObj styleCities = new styleObj(classCities);
			classCities.insertStyle(styleCities, 0);
			styleCities.setSymbolname("circle");
			styleCities.setSize(200);
			
			
			lineObj lineKrakow = new lineObj();
			lineKrakow.add(new pointObj(19.945, 50.065, 0));
			shapeObj shapeKrakow = new shapeObj(mapscriptConstants.MS_SHAPEFILE_POINT);
			shapeKrakow.add(lineKrakow);
			shapeKrakow.setValue(0, "Krakow");
			shapeKrakow.setValue(1, "abc");
			layerEurope.addFeature(shapeKrakow);
			
			lineObj linePrague = new lineObj();
			linePrague.add(new pointObj(14.3, 50.05, 0));
			shapeObj shapePrague = new shapeObj(mapscriptConstants.MS_SHAPEFILE_POINT);
			shapePrague.add(linePrague);
			shapePrague.setValue(0, "Praha");
			shapeKrakow.setValue(1, "xyz");
			layerEurope.addFeature(shapePrague);

			imageObj img = map.draw();
			img.save(imgFile, map);
	  }
}
