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
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class EstimatesImportArea extends EstimatesArea
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "EstimatesImportArea", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "EstimatesImportArea"));
		attributes.put("longitude", new NumericAttribute("longitude", "EstimatesImportArea", NumericAttribute.TYPE_FLOAT));
		attributes.put("latitude", new NumericAttribute("latitude", "EstimatesImportArea", NumericAttribute.TYPE_FLOAT));
		attributes.put("order", new NumericAttribute("order", "EstimatesImportArea", NumericAttribute.TYPE_INTEGER));
		attributes.put("showOnMap", new BooleanAttribute("showOnMap", "EstimatesImportArea"));
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(EstimatesImportArea.class, sess, "order");
	}
	
	public static EstimatesImportArea loadById(Session sess, long portId)
	{
		return (EstimatesImportArea) Dictionary.loadById(EstimatesImportArea.class, sess, portId);
	}
	
	public static Area loadById(Session sess, String portId)
	{
		return (Area) Dictionary.loadById(EstimatesImportArea.class, sess, portId);
	}

	public boolean equals(Object that)
	{
		if (that instanceof EstimatesImportArea)
		{
			EstimatesImportArea thatArea = (EstimatesImportArea) that;
			return getId().equals(thatArea.getId());
		}
		else
		{
			return false;
		}
	}

}