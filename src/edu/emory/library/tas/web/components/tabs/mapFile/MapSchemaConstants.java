package edu.emory.library.tas.web.components.tabs.mapFile;

import edu.emory.library.tas.AppConfig;

public class MapSchemaConstants {
	
	public static final String MAP_INSERT_SECTION_NAME_BEGIN = "#{generated_code_begin}";
	public static final String MAP_INSERT_SECTION_NAME_END = "#{generated_code_end}";
	
	public static final String SHAPE_CIRCLE = "circle";
	public static final String SHAPE_SQUARE = "square";
	public static final String SHAPE_TRIANGLE = "triangle";
	
	public static final String COLOR1 = "202 66 35";
	public static final String COLOR2 = "87 27 29";
	public static final String COLOR3 = "255 0 0";
	public static final String COLOR4 = "130 85 220";
	public static final String COLOR5 = "85 205 215";
		
	public static String getConstantValue(String fileKey) {
		String value = AppConfig.getConfiguration().getString(fileKey);
		if (value == null) {
			throw new RuntimeException("Constant: " + fileKey + " missing in configuration file! (tast.properties)");
		}
		return value;
	}
	
	public static String[] getAvailableShapes() {
		return new String[] {SHAPE_CIRCLE, SHAPE_SQUARE, SHAPE_TRIANGLE};
	}
	
	public static String[] getAvailableColors() {
		return new String[] {COLOR1, COLOR2, COLOR3, COLOR4, COLOR5};
	}
	
	public static String getLayerStatusPrefix() {
		return "map.layer.";
	}
}