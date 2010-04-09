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
package edu.emory.library.tast.reditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Schema
{
	
	private List fields = new ArrayList();
	private Map lists = new HashMap();
	
	public Schema()
	{
	}
	
	public void addField(FieldSchema field)
	{
		fields.add(field);
	}
	
	public void registerList(String id, ListItem[] items)
	{
		lists.put(id, items);
	}

	public List getFields()
	{
		return fields;
	}

	public ListItem[] getListById(String id)
	{
		return (ListItem[]) lists.get(id);
	}
	
	public Map getLists()
	{
		return lists;
	}
	
	public FieldSchemaState[] getSerializableState()
	{
		FieldSchemaState[] names = new FieldSchemaState[fields.size()];
		int i = 0;
		for (Iterator iter = fields.iterator(); iter.hasNext();)
		{
			FieldSchema field = (FieldSchema) iter.next();
			names[i++] = new FieldSchemaState(field);
		}
		return names;
	}
	
}