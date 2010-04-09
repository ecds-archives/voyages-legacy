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
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class ImageCategory extends Dictionary
{
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "ImageCategory", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "ImageCategory"));
	}
	
	private Set images;
	
	public static List loadAll(Session sess, String orderBy)
	{
		return Dictionary.loadAll(ImageCategory.class, sess, orderBy);
	}

	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(ImageCategory.class, sess, "name");
	}
	
	public static ImageCategory loadById(Session sess, long catId)
	{
		return (ImageCategory) Dictionary.loadById(ImageCategory.class, sess, catId);
	}

	public static ImageCategory loadById(Session sess, String portId)
	{
		return (ImageCategory) Dictionary.loadById(ImageCategory.class, sess, portId);
	}

	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}

	public Set getImages()
	{
		return images;
	}

	public void setImages(Set images)
	{
		this.images = images;
	}
}
