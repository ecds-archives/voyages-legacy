package edu.emory.library.tas.maps.test;

import edu.umn.gis.mapscript.classObj;
import edu.umn.gis.mapscript.imageObj;
import edu.umn.gis.mapscript.layerObj;
import edu.umn.gis.mapscript.lineObj;
import edu.umn.gis.mapscript.mapObj;
import edu.umn.gis.mapscript.mapscriptConstants;
import edu.umn.gis.mapscript.pointObj;
import edu.umn.gis.mapscript.shapeObj;
import edu.umn.gis.mapscript.styleObj;

public class Test
{
	
	public static void main(String[] args)
	{
		
		String mapFile = "/home/juri/gis/tests/voyages_new1.map";
		String imgFile = "/home/juri/gis/tests/voyages_new1.gif";
//		String mapFile = "D:\\Library\\SlaveTrade\\shapefiles\\voyages.map";
//		String imgFile = "D:\\Library\\SlaveTrade\\test.gif";
		
//		String mapScript = "C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\mapserver\\mapscript.dll";
//		System.load(mapScript);

//		System.out.println(System.getProperty("java.library.path"));

//		System.out.println(System.getProperty("java.library.path"));
//		String mapServerDir = "C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\mapserver";
//		System.setProperty("java.library.path",
//				".;" +
//				"C:\\Program Files\\Java\\j2sdk1.4.2_12\\bin;" +
//				"C:\\WINDOWS;" +
//				"C:\\WINDOWS\\system32;"  +
//				mapServerDir + "\\Apache\\cgi-bin;" +
//				mapServerDir + "\\tools\\gdal-ogr;"  +
//				mapServerDir + "\\tools\\mapserv;"  +
//				mapServerDir + "\\tools\\shapelib;"  +
//				mapServerDir + "\\proj\\bin;"  +
//				mapServerDir + "\\tools\\shp2tile;" +
//				mapServerDir + "\\tools\\shpdiff;" +
//				mapServerDir + "\\Apache\\cgi-bin\\mapscript\\java");
		
//		System.out.println(System.getProperty("java.library.path"));
		
		System.loadLibrary("mapscript");

		mapObj map = new mapObj(mapFile);
		map.setSize(600, 600);
		map.setExtent(0, 0, 60, 60);
		
		layerObj layerEurope = new layerObj(map);
		map.insertLayer(layerEurope, 2);
		
		layerEurope.setName("europe");
		//layerEurope.setType(mapscriptConstants.MS_LAYER_POINT);
		layerEurope.setStatus(mapscriptConstants.MS_DEFAULT);
//		layerEurope.setData("europe");
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