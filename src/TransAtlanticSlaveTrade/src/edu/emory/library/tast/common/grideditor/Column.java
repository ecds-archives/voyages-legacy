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

public class Column
{
	
	private String label;
	private String name;
	private boolean readOnly = false;
	private String copyToColumn = null;
	private String copyToLabel = null;
	private boolean compareTo = false;
	private ColumnAction[] actions = null;
	
	public Column(String name, String label)
	{
		this.label = label;
		this.name = name;
	}

	public Column(String name, String label, boolean readOnly)
	{
		this.label = label;
		this.name = name;
		this.readOnly = readOnly;
	}

	public Column(String name, String label, boolean readOnly, boolean compareTo)
	{
		this.label = label;
		this.name = name;
		this.readOnly = readOnly;
		this.compareTo = compareTo;
	}

	public Column(String name, String label, boolean readOnly, String copyToColumnName, String copyToLabel)
	{
		this.label = label;
		this.name = name;
		this.readOnly = readOnly;
		this.copyToColumn = copyToColumnName;
		this.copyToLabel = copyToLabel;
	}

	public Column(String name, String label, boolean readOnly, String copyToColumnName, String copyToLabel, boolean compareTo)
	{
		this.label = label;
		this.name = name;
		this.readOnly = readOnly;
		this.copyToColumn = copyToColumnName;
		this.copyToLabel = copyToLabel;
		this.compareTo = compareTo;
	}

	public String getLabel()
	{
		return label;
	}
	public String getName()
	{
		return name;
	}

	public boolean isReadOnly()
	{
		return readOnly;
	}
	
	public void enableCopy(String columnName, String label)
	{
		copyToColumn = columnName;
		copyToLabel = label;
	}
	
	public boolean isCopyToEnabled()
	{
		return copyToColumn != null;
	}
	
	public boolean hasActions()
	{
		return actions != null && actions.length != 0;
	}

	public ColumnAction[] getActions()
	{
		return actions;
	}

	public void setActions(ColumnAction[] actions)
	{
		this.actions = actions;
	}

	public String getCopyToColumn()
	{
		return copyToColumn;
	}

	public void setCopyToColumn(String copyToColumn)
	{
		this.copyToColumn = copyToColumn;
	}

	public String getCopyToLabel()
	{
		return copyToLabel;
	}

	public void setCopyToLabel(String copyToLabel)
	{
		this.copyToLabel = copyToLabel;
	}

	public boolean isCompareTo()
	{
		return compareTo;
	}

	public void setCompareTo(boolean compareTo)
	{
		this.compareTo = compareTo;
	}

}
