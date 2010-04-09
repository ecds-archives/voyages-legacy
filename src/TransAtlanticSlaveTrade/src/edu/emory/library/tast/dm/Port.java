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

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.RegionAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class Port extends LocationWithImages
{
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "Port", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "Port"));
		attributes.put("longitude", new StringAttribute("longitude", "Port"));
		attributes.put("latitude", new StringAttribute("latitude", "Port"));
		attributes.put("region", new RegionAttribute("region", "Port"));
		attributes.put("order", new NumericAttribute("order", "Port", NumericAttribute.TYPE_INTEGER));
		attributes.put("showAtZoom", new NumericAttribute("showAtZoom", "Port", NumericAttribute.TYPE_INTEGER));
		attributes.put("showOnMainMap", new BooleanAttribute("showOnMainMap", "Port"));
		attributes.put("showOnVoyageMap", new BooleanAttribute("showOnVoyageMap", "Port"));
	}

	private Region region;
	private boolean showOnMainMap;
	private boolean showOnVoyageMap;

	public Region getRegion()
	{
		return region;
	}

	public void setRegion(Region region)
	{
		this.region = region;
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(Port.class, sess, "order");
	}
	
	public static List loadAll(Session sess, String orderBy)
	{
		return Dictionary.loadAll(Port.class, sess, orderBy);
	}

	public static Port loadById(Session sess, long portId)
	{
		return (Port) Dictionary.loadById(Port.class, sess, portId);
	}

	public static Port loadById(Session sess, String portId)
	{
		return (Port) Dictionary.loadById(Port.class, sess, portId);
	}

	public static Attribute getAttribute(String name)
	{
		Attribute attr = (Attribute)attributes.get(name);
		if (attr == null) throw new RuntimeException("attribute '" + name + "' of Port does not exist");
		return attr;
	}

	public boolean isShowOnMainMap()
	{
		return showOnMainMap;
	}

	public void setShowOnMainMap(boolean showOnMainMap)
	{
		this.showOnMainMap = showOnMainMap;
	}

	public boolean isShowOnVoyageMap()
	{
		return showOnVoyageMap;
	}

	public void setShowOnVoyageMap(boolean showOnVoyageMap)
	{
		this.showOnVoyageMap = showOnVoyageMap;
	}

}
