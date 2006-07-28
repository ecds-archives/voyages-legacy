package edu.emory.library.tas.web.components.tabs.mapFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import edu.emory.library.tas.AppConfig;
import edu.emory.library.tas.web.components.tabs.MapBean;
import edu.emory.library.tas.web.components.tabs.MapItem;

public class MapFileCreator {

	private static String TIME_SYMBOL_REGEX = "\\{TIME\\}";
	private static String MAP_FILE_OUTPUT = AppConfig.getConfiguration().getString(AppConfig.MAP_FILE_OUTPUT);

	private static int circleRanges = 5;

	private MapItem[] data;

	private String filePath;
	
	private MapSchemaReader schemaReader = new MapSchemaReader();

	// private float min;
	// private float max;
	private double[] rangeBoundries;

	private ArrayList[][][] points = null;

	public MapFileCreator() {
		this.filePath = MAP_FILE_OUTPUT;
		String time = System.currentTimeMillis() + "";
		this.filePath = this.filePath.replaceAll(TIME_SYMBOL_REGEX, time);
		schemaReader.beginReading();
	}

	public void setMapData(MapItem[] data, double min, double max) {
		this.data = data;

		this.points = new ArrayList[MapSchemaConstants.getAvailableColors().length][][];
		this.rangeBoundries = new double[circleRanges + 1];
		for (int i = 0; i < MapSchemaConstants.getAvailableColors().length; i++) {
			ArrayList[][] lists = new ArrayList[MapSchemaConstants.getAvailableShapes().length][];
			for (int j = 0; j < MapSchemaConstants.getAvailableShapes().length; j++) {
				ArrayList[] listsSizes = new ArrayList[circleRanges];
				for (int k = 0; k < listsSizes.length; k++) {
					listsSizes[k] = new ArrayList();
				}
				lists[j] = listsSizes;
			}
			this.points[i] = lists;
		}

		double step = (max - min) / circleRanges;
		for (int i = 0; i < circleRanges; i++) {
			this.rangeBoundries[i + 1] = i * step + step + min;
		}
		
		this.rangeBoundries[0] = min;

		for (int i = 0; i < this.data.length; i++) {
			if (this.data[i].isAutoSized()) {
				int index = circleRanges - 1;
				double size = this.data[i].getMaxVal();
				for (int j = 0; j < circleRanges; j++) {
					if (size >= this.rangeBoundries[j] && size < this.rangeBoundries[j + 1]) {
						index = j;
					}

				}
				this.points[this.data[i].getColor()][this.data[i].getShape()][index].add(this.data[i]);
			} else {
				this.points[this.data[i].getColor()][this.data[i].getShape()][this.data[i].getMaxVal()].add(this.data[i]);
			}
		}

	}

	public boolean createMapFile() {
		this.schemaReader.clearModifications();
		this.schemaReader.addBlockModification(MapSchemaConstants.MAP_INSERT_SECTION_NAME_BEGIN,
				MapSchemaConstants.MAP_INSERT_SECTION_NAME_END, this.generateLayerForPorts());
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(this.filePath));
			writer.write(this.schemaReader.applyModifications().toString());
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
		
	}

	private String generateLayerForPorts() {
		StringBuffer buffer = new StringBuffer();
		if (this.data != null) {
			System.out.println("Here!");
			for (int i = 0; i < this.points.length; i++) {
				for (int j = 0; j < this.points[i].length; j++) {
					for (int k = 0; k < this.points[i][j].length; k++) {
						generateFeature(buffer, this.points[i][j][k], i, j, k);
					}
				}
			}
		}
		return buffer.toString();
	}

	private void generateFeature(StringBuffer writer, ArrayList list, int color, int shape, int size) {
		if (list.size() > 0) {
			
			writer.append("LAYER\n");
			writer.append("	TYPE POINT\n");
			writer.append("	STATUS DEFAULT\n");

			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				MapItem item = (MapItem) iter.next();
				writer.append("	FEATURE\n");
				writer.append("		POINTS " + item.x + " " + item.y + " END\n");
				writer.append("		TEXT '" + item.label + "'\n");
				writer.append("	END\n");
			}

			writer.append("	CLASS\n");
			writer.append("		STYLE\n");
			writer.append("			SYMBOL '" + MapSchemaConstants.getAvailableShapes()[shape] + (size + 1) + "'\n");
			writer.append("			COLOR " + MapSchemaConstants.getAvailableColors()[color] + "\n");
			writer.append("		END\n");
			writer.append("		LABEL\n");
			writer.append("			POSITION ur\n");
			writer.append("			TYPE BITMAP\n");
			writer.append("		END\n");
			writer.append("	END\n");
			writer.append("END\n");
		}
	}

	public String getFilePath() {
		return this.filePath;
	}
}
