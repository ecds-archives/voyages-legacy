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
package edu.emory.library.tast.common.grideditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Values
{
	
	private Map columns = new HashMap();
	
	public Value getValue(String columnName, String rowName)
	{
		
		Map columnValues = (Map) columns.get(columnName);
		if (columnValues == null) throw new RuntimeException("invalid column name " + columnName);
		
		Value value = (Value) columnValues.get(rowName);
		if (value == null) new RuntimeException("invalid row name " + rowName);
		
		return value;

	}
	
	public Map getColumnValues(String columnName)
	{
		
		Map columnValues = (Map) columns.get(columnName);
		if (columnValues == null) throw new RuntimeException("invalid column name " + columnName);

		return columnValues;
		
	}

	public void setValue(String columnName, String rowName, Value value)
	{
		
		Map columnValues = (Map) columns.get(columnName);
		if (columnValues == null)
		{
			columnValues = new HashMap();
			columns.put(columnName, columnValues);
		}
		
		columnValues.put(rowName, value);

	}

	public List getValues() {
		List vals = new ArrayList();
		for (Iterator iter = columns.values().iterator(); iter.hasNext();) {
			Map element = (Map) iter.next();
			vals.addAll(element.values());
		}
		return vals;
	}

}