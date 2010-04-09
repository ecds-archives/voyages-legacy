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
package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class Nation extends DictionaryOrdered
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "Nation", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "Nation"));
		attributes.put("order", new NumericAttribute("order", "Nation", NumericAttribute.TYPE_INTEGER));
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(Nation.class, sess);
	}
	
	public static List loadAll(Session sess, String orderBy)
	{
		return Dictionary.loadAll(Nation.class, sess, orderBy);
	}

	public static Nation loadById(Session sess, long nationId)
	{
		return (Nation) Dictionary.loadById(Nation.class, sess, nationId);
	}
	
	public static Nation loadById(Session sess, String nationId)
	{
		return (Nation) Dictionary.loadById(Nation.class, sess, nationId);
	}

	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static String[] nationNamesToArray(List nations)
	{
		return nationNamesToArray(nations, null);
	}

	public static String[] nationNamesToArray(List nations, Set includeOnly)
	{
		String[] names = new String[nations.size()];
		
		int i = 0;
		for (Iterator iter = nations.iterator(); iter.hasNext();)
		{
			Nation nation = (Nation) iter.next();
			if (includeOnly == null || includeOnly.contains(nation.getId()))
			{
				names[i++] = nation.getName();
			}
		}
		
		return names;
	}

	public static Map createIdIndexMap(List nations)
	{
		Map map = new HashMap();
		
		int i = 0;
		for (Iterator iter = nations.iterator(); iter.hasNext();)
		{
			Nation nation = (Nation) iter.next();
			map.put(nation.getId(), new Integer(i++));
		}
		
		return map;
	}

}