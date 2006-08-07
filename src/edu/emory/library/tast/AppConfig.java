package edu.emory.library.tast;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class AppConfig
{
	
	private static final String TAST_PROPERTIES = "tast.properties";
	private static Configuration conf = null;
	
	public static final String IMPORT_CLASSPATH = "import.classpath";
	public static final String IMPORT_JAVA_MEMORY = "import.java.memory";
	public static final String IMPORT_STATTRANSFER = "import.stattransfer";
	public static final String IMPORT_ROOTDIR = "import.rootdir";
	public static final String IMPORT_DATEFORMAT = "import.dateformat";
	public static final String IMPORT_LOGITEM_DATEFORMAT = "import.logitem.dateformat";
	
	public static final String MAP_FILE_SKELETON = "mapserver.mapfile.skeleton";
	public static final String MAP_FILE_OUTPUT = "mapserver.mapfile.output";
	public static final String MAP_PROJ_IN = "map.projection.in";
	public static final String MAP_PROJ_OUT = "map.projection.out";
	public static final String MAP_DEFAULT_EXTENT_X_MIN = "map.default.extent.x.min";
	public static final String MAP_DEFAULT_EXTENT_X_MAX = "map.default.extent.x.max";
	public static final String MAP_DEFAULT_EXTENT_Y_MIN = "map.default.extent.y.min";
	public static final String MAP_DEFAULT_EXTENT_Y_MAX = "map.default.extent.y.max";
	public static final String MAP_SCALE_FACTOR = "map.scale.factor";
	public static final String MAP_MAX_MAGNIFICATION = "map.max.magnification";	
	
	private static void load()
	{
		try
		{
			conf = new PropertiesConfiguration(TAST_PROPERTIES);
		}
		catch (ConfigurationException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static Configuration getConfiguration()
	{
		if (conf == null) load();
		return conf;
	}
	
}