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
package edu.emory.library.tast.common.table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.dm.DictionaryOrdered;
import edu.emory.library.tast.dm.attributes.Attribute;

public abstract class GrouperSimpleDictionary extends Grouper
{
	
	private Label[] labels;
	private List dictionary;
	private Map lookupTable;
	
	public GrouperSimpleDictionary(int resultIndex, boolean omitEmpty, List nations)
	{
		super(resultIndex, omitEmpty);
		this.dictionary = nations;
	}
	
	public Attribute[] addExtraAttributes(int index)
	{
		return new Attribute[] {};
	}
	
	protected Long getRowId(Object[] dataRow)
	{
		return new Long(((Number)dataRow[resultIndex]).longValue());
	}

	public void initSlots(Object[] dataTable)
	{

		Set idsInDataTable = new HashSet();
		for (int i = 0; i < dataTable.length; i++)
		{
			Long id = getRowId((Object[])dataTable[i]);
			idsInDataTable.add(id);
		}
		
		int noOfDictItems = omitEmpty ? 
				idsInDataTable.size() :
					dictionary.size();

		lookupTable = new HashMap();
		labels = new Label[noOfDictItems];
		
		int i = 0;
		for (Iterator iter = dictionary.iterator(); iter.hasNext();)
		{
			DictionaryOrdered dictItem = (DictionaryOrdered) iter.next();
			Long id = dictItem.getId();
			if (!omitEmpty || idsInDataTable.contains(id))
			{
				labels[i] = new Label(dictItem.getName());
				lookupTable.put(id, new Integer(i));
				i++;
			}
		}

	}

	public int lookupIndex(Object[] dataRow)
	{
		Long id = getRowId(dataRow);
		if (id == null || lookupTable.get(id) == null) return 0;
		return ((Integer) lookupTable.get(id)).intValue();
	}

	public int getLeafLabelsCount()
	{
		return labels.length;
	}

	public int getBreakdownDepth()
	{
		return 1;
	}

	public Label[] getLabels()
	{
		return labels;
	}

}