package edu.emory.library.tast.misc.tests;

import java.io.IOException;

import edu.umn.gis.mapscript.imageObj;
import edu.umn.gis.mapscript.mapObj;

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
