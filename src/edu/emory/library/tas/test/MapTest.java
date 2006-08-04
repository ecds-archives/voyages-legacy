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
			
			
 			mapObj map = new mapObj(args[0]);
			map.setSize(1024, 768);
//			map.setExtent(-18000000, -9000000, 18000000, 9000000);

			imageObj img = map.draw();
			img.save(args[1], map);
	  }
}
