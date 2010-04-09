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
import edu.emory.library.tast.dm.attributes.EstimatesImportAreaAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class EstimatesImportRegion extends EstimatesRegion
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "EstimatesImportRegion", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "EstimatesImportRegion"));
		attributes.put("area", new EstimatesImportAreaAttribute("area", "EstimatesImportRegion"));
		attributes.put("order", new NumericAttribute("order", "EstimatesImportRegion", NumericAttribute.TYPE_INTEGER));
		attributes.put("showAtZoom", new NumericAttribute("showAtZoom", "EstimatesImportRegion", NumericAttribute.TYPE_INTEGER));
		attributes.put("longitude", new NumericAttribute("longitude", "EstimatesImportRegion", NumericAttribute.TYPE_FLOAT));
		attributes.put("latitude", new NumericAttribute("latitude", "EstimatesImportRegion", NumericAttribute.TYPE_FLOAT));
		attributes.put("showOnMap", new BooleanAttribute("showOnMap", "EstimatesImportRegion"));
	}

	private EstimatesImportArea area;
	
	public EstimatesImportArea getArea()
	{
		return area;
	}

	public void setArea(EstimatesImportArea area)
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
		return Dictionary.loadAll(EstimatesImportRegion.class, sess, "order");
	}
	
	public static EstimatesImportRegion loadById(Session sess, long portId)
	{
		return (EstimatesImportRegion) Dictionary.loadById(EstimatesImportRegion.class, sess, portId);
	}

	public static EstimatesImportRegion loadById(Session sess, String portId)
	{
		return (EstimatesImportRegion) Dictionary.loadById(EstimatesImportRegion.class, sess, portId);
	}

}