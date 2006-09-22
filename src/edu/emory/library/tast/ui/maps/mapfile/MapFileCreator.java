package edu.emory.library.tast.ui.maps.mapfile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.ui.maps.AbstractMapItem;
import edu.emory.library.tast.ui.maps.LegendItem;
import edu.emory.library.tast.ui.maps.LegendItemsGroup;
import edu.emory.library.tast.ui.maps.MapLayer;

public class MapFileCreator {

	private static final String MINI = "_mini.map";

	private static String TIME_SYMBOL_REGEX = "\\{TIME\\}";

	private static String MAP_FILE_OUTPUT = AppConfig.getConfiguration().getString(AppConfig.MAP_FILE_OUTPUT);

//	private static String PROJ_IN = StringUtils.getProjectionStringForMapFile(AppConfig.getConfiguration()
//			.getStringArray(AppConfig.MAP_PROJ_IN));
//
//	private static String PROJ_OUT = StringUtils.getProjectionStringForMapFile(AppConfig.getConfiguration()
//			.getStringArray(AppConfig.MAP_PROJ_OUT));

	private static final String CHECKBOX_PREFIX = "#{map.layer.status";

	private static final String CHECKBOX_KEY_SUFFIX = ".userlabel";
	
	private static final String DEFAULT_VALUE = ".default";

	private String filePath;

	private MapSchemaReader schemaReader = new MapSchemaReader();

	private HashMap points = new HashMap();

	private MapLayer[] layers;

	private LegendItemsGroup[] legend;

	private String fileSmallMapPath;

	public MapFileCreator() {
		schemaReader.beginReading();

		ArrayList list = new ArrayList();
		String[] keys = this.schemaReader.markerKeys();
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			if (key.startsWith(CHECKBOX_PREFIX)) {
				list.add(key);
			}
		}
		layers = new MapLayer[list.size()];
		for (int i = 0; i < layers.length; i++) {
			String key = (String) list.get(i);
			String userLabel = AppConfig.getConfiguration().getString(
					key.substring(2, key.length() - 1) + CHECKBOX_KEY_SUFFIX);
			String defaultValue = AppConfig.getConfiguration().getString(
					key.substring(2, key.length() - 1) + DEFAULT_VALUE);
			if (userLabel == null) {
				throw new RuntimeException("ERROR: tast.properties: missing property: " + key + CHECKBOX_KEY_SUFFIX);
			}
			layers[i] = new MapLayer(key, userLabel, defaultValue);
		}

	}

	public void setMapData(AbstractMapItem[] data) {

		this.points = new HashMap();

		for (int i = 0; i < data.length; i++) {
			AbstractMapItem item = data[i];
			String[] symbolNames = item.getSymbolNames();
			for (int j = 0; j < symbolNames.length; j++) {
				if (!this.points.containsKey(symbolNames[j])) {
					this.points.put(symbolNames[j], new ArrayList());
				}
				ArrayList symbolPoints = (ArrayList) this.points.get(symbolNames[j]);
				symbolPoints.add(data[i]);
			}
		}

	}

	public void setMapLegend(LegendItemsGroup[] legend) {
		this.legend = legend;
	}

	public boolean createMapFile() {
		
		this.filePath = MAP_FILE_OUTPUT;
		String time = System.currentTimeMillis() + "";
		this.filePath = this.filePath.replaceAll(TIME_SYMBOL_REGEX, time);

		this.fileSmallMapPath = MAP_FILE_OUTPUT + MINI;
		this.fileSmallMapPath = this.fileSmallMapPath.replaceAll(TIME_SYMBOL_REGEX, time);
		
		return this.createMapFileInt(this.filePath, true) &&  this.createMapFileInt(this.fileSmallMapPath, false);

	}

	private boolean createMapFileInt(String filePath2, boolean generatePorts) {
		this.schemaReader.clearModifications();
		
		if (generatePorts) {
			this.schemaReader.addBlockModification(MapSchemaConstants.MAP_INSERT_SECTION_NAME_BEGIN,
				MapSchemaConstants.MAP_INSERT_SECTION_NAME_END, this.generateLayerForPorts());
		}
		
		for (int i = 0; i < this.layers.length; i++) {
			this.schemaReader.addSimpleModification(layers[i].getKey(), layers[i].isEnabled() ? "ON" : "OFF");
		}

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(filePath2));
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
		Set layers = this.points.keySet();
		for (Iterator iter = layers.iterator(); iter.hasNext();) {
			String symbolName = (String) iter.next();
			ArrayList layerPoints = (ArrayList) this.points.get(symbolName);
			this.generateFeature(buffer, symbolName, layerPoints);
		}
		return buffer.toString();
	}

	private void generateFeature(StringBuffer writer, String symbolName, ArrayList list) {

		boolean enabled = true;
		for (int i = 0; i < this.legend.length; i++) {
			LegendItem[] legendItems = legend[i].getItems();
			for (int j = 0; j < legendItems.length; j++) {
				Pattern pattern = Pattern.compile(legendItems[j].getImageId());
				Matcher matcher = pattern.matcher(symbolName);
				if (matcher.find()) {
					enabled = legendItems[j].isEnabled() && enabled;
				}
			}
		}

		if (enabled) {

			if (list.size() > 0) {

				writer.append("LAYER\n");
//				writer.append("PROJECTION");
//				writer.append(StringUtils.getProjectionStringForMapFile(AppConfig.getConfiguration().getStringArray(
//						AppConfig.MAP_PROJ_IN)));
//				writer.append("END");
				writer.append("	TRANSPARENCY ALPHA\n");
				writer.append("	TYPE POINT\n");
				writer.append("	STATUS DEFAULT\n");

				Iterator iter = list.iterator();
				while (iter.hasNext()) {
					AbstractMapItem item = (AbstractMapItem) iter.next();
					writer.append("	FEATURE\n");
					writer.append("		POINTS " + item.getX() + " " + item.getY() + " END\n");
					writer.append("		TEXT \"" + item.getMainLabel() + "\"\n");
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
	}

	public String getFilePath() {
		return this.filePath;
	}
	
	public String getSmallMapFilePath() {
		return this.fileSmallMapPath;
	}

	public MapLayer[] getLayers() {
		return layers;
	}

	public void setLayers(MapLayer[] layers) {
		this.layers = layers;
	}

}
