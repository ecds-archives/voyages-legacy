package edu.emory.library.tast;

import java.util.HashMap;
import java.util.Map;

public class SimpleCache
{
	
	private Map lists = new HashMap();
	private static SimpleCache instance;
	
	public final static String VOYAGES_PREFIX = "database:";
	public final static String ESTIMATES_PREFIX = "estimates:";
	
	private SimpleCache()
	{
	}
	
	public synchronized static SimpleCache getInstance()
	{
		if (instance == null) instance = new SimpleCache();
		return instance;
	}
	
	static synchronized public Object get(String id)
	{
		return getInstance().lists.get(id);
	}
	
	static synchronized public void set(String id, Object obj)
	{
		getInstance().lists.put(id, obj);		
	}

}
