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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class Area extends Location
{
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "Area", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "Area"));
		attributes.put("latitude", new NumericAttribute("latitude", "Area", NumericAttribute.TYPE_FLOAT));
		attributes.put("longitude", new NumericAttribute("longitude", "Area", NumericAttribute.TYPE_FLOAT));
		attributes.put("regions", new NumericAttribute("regions", "Area", NumericAttribute.TYPE_LONG));
		attributes.put("order", new NumericAttribute("order", "Area", NumericAttribute.TYPE_INTEGER));
		attributes.put("showAtZoom", new NumericAttribute("showAtZoom", "Area", NumericAttribute.TYPE_INTEGER));
		attributes.put("showOnMap", new BooleanAttribute("showOnMap", "Area"));
	}
	
	private Set regions;
	private boolean showOnMap;

	public boolean isShowOnMap()
	{
		return showOnMap;
	}

	public void setShowOnMap(boolean showOnMap)
	{
		this.showOnMap = showOnMap;
	}

	public Set getRegions()
	{
		return regions;
	}

	public void setRegions(Set regions)
	{
		this.regions = regions;
	}

	public static Attribute getAttribute(String name)
	{
		if (!attributes.containsKey(name)) {
			throw new RuntimeException("area attribute not defined: " + name);
		}
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(Area.class, sess, "order");
	}
	
	public static List loadAll(Session sess, String orderBy)
	{
		return Dictionary.loadAll(Area.class, sess, orderBy);
	}

	public static Area loadById(Session sess, long portId)
	{
		return (Area) Dictionary.loadById(Area.class, sess, portId);
	}

	public static Area loadById(Session sess, String portId)
	{
		return (Area) Dictionary.loadById(Area.class, sess, portId);
	}

}
