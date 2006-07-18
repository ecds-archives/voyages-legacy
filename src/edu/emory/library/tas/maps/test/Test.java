package edu.emory.library.tas.maps.test;

import edu.umn.gis.mapscript.classObj;
import edu.umn.gis.mapscript.imageObj;
import edu.umn.gis.mapscript.layerObj;
import edu.umn.gis.mapscript.mapObj;
import edu.umn.gis.mapscript.mapscriptConstants;
import edu.umn.gis.mapscript.styleObj;

public class Test
{
	
	public static void main(String[] args)
	{
		
//		String mapFile = "C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\shapefiles\\voyages.map";
//		String imgFile = "C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\test.gif";
		String mapFile = "D:\\Library\\SlaveTrade\\shapefiles\\voyages.map";
		String imgFile = "D:\\Library\\SlaveTrade\\test.gif";
		
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
		
		layerObj layerEurope = new layerObj(map);
		map.insertLayer(layerEurope, 2);
		
		layerEurope.setName("europe");
		layerEurope.setType(mapscriptConstants.MS_LAYER_POINT);
		layerEurope.setStatus(mapscriptConstants.MS_DEFAULT);
		layerEurope.setData("europe");
		layerEurope.setLabelitem("CITY_NAME");
		
		classObj classCities = new classObj(layerEurope);
		layerEurope.insertClass(classCities, 0);
		
		styleObj styleCities = new styleObj(classCities);
		classCities.insertStyle(styleCities, 0);
		styleCities.setSymbolname("circle");
		styleCities.setSize(2);
		
		imageObj img = map.draw();
		img.save(imgFile, map);
		
	}

}