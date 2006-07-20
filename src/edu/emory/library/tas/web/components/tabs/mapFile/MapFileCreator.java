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
import edu.emory.library.tas.web.components.tabs.MapBean.MapItem;

public class MapFileCreator {

	private static String COLOR1 = "202 66 35"; // "45 230 200";
	private static String COLOR2 = "87 27 29"; //"230 230 45";

	private static String TIME_SYMBOL_REGEX = "\\{TIME\\}";

	private static String MAP_FILE_SKELETON = AppConfig.getConfiguration().getString(AppConfig.MAP_FILE_SKELETON);

	private static String MAP_FILE_OUTPUT = AppConfig.getConfiguration().getString(AppConfig.MAP_FILE_OUTPUT);

	private static String MAP_INSERT_SECTION_NAME_BEGIN = "#Generated_code_begin";

	private static String MAP_INSERT_SECTION_NAME_END = "#Generated_code_end";

	private static int circleRanges = 10;

	private MapItem[] data;

	private String filePath;

	// private float min;
	// private float max;
	private float[] rangeBoundries;

	private ArrayList[] pointsC1 = null;
	private ArrayList[] pointsC2 = null;

	public MapFileCreator() {
		this.filePath = MAP_FILE_OUTPUT;
		String time = System.currentTimeMillis() + "";
		this.filePath = this.filePath.replaceAll(TIME_SYMBOL_REGEX, time);
	}

	public void setMapData(MapItem[] data, float max, float min) {
		this.data = data;

		this.rangeBoundries = new float[circleRanges];
		this.pointsC1 = new ArrayList[circleRanges];
		this.pointsC2 = new ArrayList[circleRanges];

		float step = (max - min) / circleRanges;
		for (int i = 0; i < circleRanges; i++) {
			this.rangeBoundries[i] = i * step + step + min;
			this.pointsC1[i] = new ArrayList();
			this.pointsC2[i] = new ArrayList();
		}

		for (int i = 0; i < this.data.length; i++) {
			int index = circleRanges - 1;
			float size = this.data[i].size;
			for (int j = 1; j < circleRanges; j++) {
				if (size > this.rangeBoundries[j]) {
					index = j - 1;
				}
			}
			if (this.data[i].portType == MapBean.PORT_DEPARTURE) {
				this.pointsC1[index].add(this.data[i]);
			} else {
				this.pointsC2[index].add(this.data[i]);
			}
		}

	}

	public boolean createMapFile() {
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(MAP_FILE_SKELETON));
			writer = new BufferedWriter(new FileWriter(this.filePath));
			String line = null;
			boolean deleteMode = false;

			while ((line = reader.readLine()) != null) {
				if (line.trim().indexOf(MAP_INSERT_SECTION_NAME_BEGIN) != -1) {
					deleteMode = true;
					this.generateLayerForPorts(writer);
					continue;
				}
				if (line.trim().indexOf(MAP_INSERT_SECTION_NAME_END) != -1) {
					deleteMode = false;
					continue;
				}
				if (deleteMode) {
					continue;
				}
				writer.write(line);
				writer.write("\n");
			}

			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private void generateLayerForPorts(BufferedWriter writer) throws IOException {
		if (this.data != null) {

			for (int i = 0; i < circleRanges; i++) {

				generateFeature(writer, this.pointsC1[i], COLOR1, i + 1);
				generateFeature(writer, this.pointsC2[i], COLOR2, i + 1);
				
			}
		}
	}

	private void generateFeature(BufferedWriter writer, ArrayList list, String color, int size) throws IOException {
		if (list.size() > 0) {
			writer.write("LAYER\n");
			writer.write("	TYPE POINT\n");
			writer.write("	STATUS DEFAULT\n");

			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				MapItem item = (MapItem) iter.next();
				writer.write("	FEATURE\n");
				writer.write("		POINTS " + item.x + " " + item.y + " END\n");
				writer.write("		TEXT '" + item.label + "'\n");
				writer.write("	END\n");
			}

			writer.write("	CLASS\n");
			writer.write("		STYLE\n");
			writer.write("			SYMBOL 'circle" + size + "'\n");
			writer.write("			COLOR " + color + "\n");
			writer.write("		END\n");
			writer.write("		LABEL\n");
			writer.write("			POSITION ul\n");
			writer.write("			TYPE BITMAP\n");
			writer.write("		END\n");
			writer.write("	END\n");
			writer.write("END\n");
		}
	}

	public String getFilePath() {
		return this.filePath;
	}
}
