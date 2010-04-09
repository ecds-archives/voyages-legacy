/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
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
