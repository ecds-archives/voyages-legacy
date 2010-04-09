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
package edu.emory.library.tast.spss;

import java.util.ArrayList;

public class STSchemaVariable
{
	
	private String name;
	private int startColumn;
	private int endColumn;
	private int type;
	private String label;
	private String tag;
	private boolean doImport;
	private ArrayList labels = new ArrayList();
	//private Hashtable dictionaryCache = new Hashtable();
	
	public final static int TYPE_STRING = 0; 
	public final static int TYPE_NUMERIC = 1; 
	public final static int TYPE_DATE = 2; 
	
	public STSchemaVariable()
	{
	}

	public STSchemaVariable(String name, int type)
	{
		this.name = name;
		this.type = type;
	}

	public STSchemaVariable(String name, int startColumn, int endColumn)
	{
		this.name = name;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
		this.type = TYPE_STRING;
	}

	public STSchemaVariable(String name, int startColumn, int endColumn, int type)
	{
		this.name = name;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
		this.type = type;
	}

	public STSchemaVariable(String name, int startColumn, int endColumn, String tag)
	{
		this.name = name;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
		this.type = TYPE_NUMERIC;
		this.tag = tag;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public String getLabel()
	{
		return label;
	}

	public void setStartColumn(int startColumn)
	{
		this.startColumn = startColumn;
	}
	
	public int getStartColumn()
	{
		return startColumn;
	}
	
	public void setEndColumn(int endColumn)
	{
		this.endColumn = endColumn;
	}
	
	public int getEndColumn()
	{
		return endColumn;
	}
	
	public int getLength()
	{
		return endColumn - startColumn + 1;
	}

	public void setType(int type)
	{
		this.type = type;
	}
	
	public int getType()
	{
		return type;
	}
	
	public void setTag(String tag)
	{
		this.tag = tag;
	}
	
	public String getTag()
	{
		return tag;
	}
	
	public boolean hasTag()
	{
		return tag != null;
	}

	public void setLabels(ArrayList labels)
	{
		this.labels = labels;
	}
	
	public ArrayList getLabels()
	{
		return labels;
	}

	public boolean hasLabels()
	{
		return labels != null && labels.size() > 0;
	}

	public void setDoImport(boolean doImport)
	{
		this.doImport = doImport;
	}

	public boolean isDoImport()
	{
		return doImport;
	}

}