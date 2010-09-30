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
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;


public class Source
{
	
	private long iid;
	private String sourceId;
	private String name;
	private int type;
	
	public static final int TYPE_DOCUMENTARY_SOURCE = 0;
	public static final int TYPE_NEWSPAPER = 1;
	public static final int TYPE_PUBLISHED_SOURCE = 2;
	public static final int TYPE_UNPUBLISHED_SECONDARY_SOURCE = 3; 
	public static final int TYPE_PRIVATE_NOTE_OR_COLLECTION = 4;
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", null, NumericAttribute.TYPE_LONG));
		attributes.put("sourceId", new StringAttribute("sourceId", null));
		attributes.put("name", new StringAttribute("name", null));
		attributes.put("type", new NumericAttribute("type", null, NumericAttribute.TYPE_LONG));
	}
	
	public Source() {
		
	}
	
	public Source(String sourceId, String name, int type) {
		this.sourceId = sourceId;
		this.name = name;
		this.type = type;
	}
	
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}
	
	public static Source loadById(Session session, Long checkedSourceId) {
		return (Source) Dictionary.loadById(Source.class, session, checkedSourceId.longValue());
	}
	
	public static Source loadBySourceId(Session session, String newSourceId) {
		TastDbConditions c = new TastDbConditions();
		c.addCondition(getAttribute("sourceId"), newSourceId, TastDbConditions.OP_EQUALS);
		TastDbQuery qVal = new TastDbQuery("Source", c);
		Object[] objects = qVal.executeQuery(session);
		if (objects.length == 0) {
			return null;
		}
		return (Source) objects[0];
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String description)
	{
		this.name = description;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public long getIid()
	{
		return iid;
	}

	public void setIid(long internalId)
	{
		this.iid = internalId;
	}

	public String getSourceId()
	{
		return sourceId;
	}

	public void setSourceId(String souceId)
	{
		this.sourceId = souceId;
	}
	
}
