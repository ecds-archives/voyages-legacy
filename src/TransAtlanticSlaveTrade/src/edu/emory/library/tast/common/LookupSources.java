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
package edu.emory.library.tast.common;

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
