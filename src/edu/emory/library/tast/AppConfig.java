package edu.emory.library.tast;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class AppConfig
{
	
	private static final String TAST_PROPERTIES = "tast.properties";
	private static Configuration conf = null;
	
	public static final String SITE_URL = "site.url";
	
	public static final String FORMAT_DATE = "format.date.cvs";
	public static final String FORMAT_DATE_CVS = "format.date.cvs";

	public static final String IMPORT_STATTRANSFER = "import.stattransfer";
	public static final String IMPORT_ROOTDIR = "import.rootdir";
	
	public static final String MAP_URL = "map.url";
	public static final String MAP_DEFAULT_X1 = "map.default.x1";
	public static final String MAP_DEFAULT_Y1 = "map.default.y1";
	public static final String MAP_DEFAULT_X2 = "map.default.x2";
	public static final String MAP_DEFAULT_Y2 = "map.default.y2";
	
	public static final String IMAGES_URL = "images.url";
	public static final String IMAGES_DIRECTORY = "images.dir";
	public static final String IMAGES_TITLE_MAXLEN = "images.title.maxlen";
	public static final String IMAGES_CREATOR_MAXLEN = "images.creator.maxlen";
	public static final String IMAGES_SOURCE_MAXLEN = "images.source.maxlen";
	public static final String IMAGES_REFERENCES_MAXLEN = "images.references.maxlen";
	public static final String IMAGES_EMORYLOCATION_MAXLEN = "images.emorylocation.maxlen";
	public static final String DEFAULT_REVISION = "database.defaultrevision";

	public static final String SLAVES_SIERRA_LEONE_ID = "slaves.sierra_leone.id";
	public static final String SLAVES_HAVANA_ID = "slaves.havana.id";

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
