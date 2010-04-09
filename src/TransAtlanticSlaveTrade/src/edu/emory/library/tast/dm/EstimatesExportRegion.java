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
import edu.emory.library.tast.dm.attributes.EstimatesExportAreaAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class EstimatesExportRegion extends EstimatesRegion
{
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "EstimatesExportRegion", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "EstimatesExportRegion"));
		attributes.put("area", new EstimatesExportAreaAttribute("area", "EstimatesExportRegion"));
		attributes.put("order", new NumericAttribute("order", "EstimatesExportRegion", NumericAttribute.TYPE_INTEGER));
		attributes.put("longitude", new NumericAttribute("longitude", "EstimatesExportRegion", NumericAttribute.TYPE_FLOAT));
		attributes.put("latitude", new NumericAttribute("latitude", "EstimatesExportRegion", NumericAttribute.TYPE_FLOAT));
		attributes.put("showAtZoom", new NumericAttribute("showAtZoom", "EstimatesExportRegion", NumericAttribute.TYPE_INTEGER));
		attributes.put("showOnMap", new BooleanAttribute("showOnMap", "EstimatesExportRegion"));
	}
	
	private EstimatesExportArea area;
	
	public EstimatesExportArea getArea()
	{
		return area;
	}

	public void setArea(EstimatesExportArea area)
	{
		this.area = area;
	}
	
	public EstimatesArea getAbstractArea()
	{
		return area;
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(EstimatesExportRegion.class, sess, "order");
	}
	
	public static EstimatesExportRegion loadById(Session sess, long portId)
	{
		return (EstimatesExportRegion) Dictionary.loadById(EstimatesExportRegion.class, sess, portId);
	}

	public static EstimatesExportRegion loadById(Session sess, String portId)
	{
		return (EstimatesExportRegion) Dictionary.loadById(EstimatesExportRegion.class, sess, portId);
	}

}