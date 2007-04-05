package edu.emory.library.tast.common.grideditor;

import java.util.HashMap;
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

}