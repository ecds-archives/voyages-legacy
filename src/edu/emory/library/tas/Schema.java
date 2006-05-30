package edu.emory.library.tas;

import java.util.Hashtable;


public class Schema
{
	
	private SchemaColumn[] columnsArray;
	private Hashtable columnsHashtable;
	
	public SchemaColumn getColumn(int index)
	{
		if (0 <= index && columnsArray.length < index)
		{
			return columnsArray[index];
		}
		else
		{
			return null;
		}
	}
	
	public SchemaColumn getColumn(String index)
	{
		return (SchemaColumn)columnsHashtable.get(index);
	}

	public int size()
	{
		return columnsArray.length;
	}
	
	public Schema(String xmlConfigFileName)
	{
		
	}

}