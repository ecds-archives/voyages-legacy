package edu.emory.library.tast.ui;

import java.util.HashMap;
import java.util.Map;

public class LookupSources
{
	
	private static Map sources = new HashMap();
	
	public static synchronized void registerLookupSource(String id, LookupSource source)
	{
		sources.put(id, source);
	}

	public static synchronized void removeLookupSource(String id, LookupSource source)
	{
		sources.remove(id);
	}
	
	public static synchronized LookupSource getLookupSource(String id)
	{
		return (LookupSource) sources.get(id);
	}

}
