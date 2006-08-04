package edu.emory.library.tas.web.components.tabs.mapFile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import edu.emory.library.tas.AppConfig;
import edu.emory.library.tas.util.StringUtils;
import edu.emory.library.tas.web.components.tabs.map.AbstractMapItem;

public class MapFileCreator {

	private static String TIME_SYMBOL_REGEX = "\\{TIME\\}";

	private static String MAP_FILE_OUTPUT = AppConfig.getConfiguration().getString(AppConfig.MAP_FILE_OUTPUT);

	private static String PROJ_IN = StringUtils.getProjectionStringForMapFile(AppConfig.getConfiguration()
			.getStringArray(AppConfig.MAP_PROJ_IN));

	private static String PROJ_OUT = StringUtils.getProjectionStringForMapFile(AppConfig.getConfiguration()
			.getStringArray(AppConfig.MAP_PROJ_OUT));

	private String filePath;

	private MapSchemaReader schemaReader = new MapSchemaReader();

	private HashMap points = new HashMap();

	public MapFileCreator() {
		schemaReader.beginReading();
	}

	public void setMapData(AbstractMapItem[] data) {

		this.points = new HashMap();

		for (int i = 0; i < data.length; i++) {
			AbstractMapItem item = data[i];
			String symbolName = item.getSymbolName();
			if (!this.points.containsKey(symbolName)) {
				this.points.put(symbolName, new ArrayList());
			}
			ArrayList symbolPoints = (ArrayList) this.points.get(symbolName);
			symbolPoints.add(data[i]);
		}

	}

public boolean createMapFile() {
		
		this.filePath = MAP_FILE_OUTPUT;
		String time = System.currentTimeMillis() + "";
		this.filePath = this.filePath.replaceAll(TIME_SYMBOL_REGEX, time);
		
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
		
	}	private String generateLayerForPorts() {
		StringBuffer buffer = new StringBuffer();
		Set layers = this.points.keySet();
		for (Iterator iter = layers.iterator(); iter.hasNext();) {
			String symbolName = (String) iter.next();
			ArrayList layerPoints = (ArrayList) this.points.get(symbolName);
			this.generateFeature(buffer, symbolName, layerPoints);
		}
		return buffer.toString();
	}

	private void generateFeature(StringBuffer writer, String symbolName, ArrayList list) {
		if (list.size() > 0) {

			writer.append("LAYER\n");
			writer.append("PROJECTION");
			writer.append(StringUtils.getProjectionStringForMapFile(AppConfig.getConfiguration().getStringArray(AppConfig.MAP_PROJ_IN)));
			writer.append("END");
			writer.append("	TYPE POINT\n");
			writer.append("	STATUS DEFAULT\n");

			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				AbstractMapItem item = (AbstractMapItem) iter.next();
				writer.append("	FEATURE\n");
				writer.append("		POINTS " + item.getX() + " " + item.getY() + " END\n");
				writer.append("		TEXT '" + item.getMainLabel() + "'\n");
				writer.append("	END\n");
			}

			writer.append("	CLASS\n");
			writer.append("		STYLE\n");
			writer.append("			SYMBOL '" + symbolName + "'\n");
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
