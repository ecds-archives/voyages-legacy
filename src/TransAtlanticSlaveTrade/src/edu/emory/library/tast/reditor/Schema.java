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