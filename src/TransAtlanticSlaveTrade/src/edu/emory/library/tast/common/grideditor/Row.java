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

public class Row
{
	
	private String type;
	private String name;
	private String label;
	private String description;
	private String groupName;
	private String copyToRow;
	private String copyToLabel;
	private boolean readOnly = false;
	private boolean noteEnabled = false;
	private boolean compareTo = false;

	public Row(String type, String name, String label, String description, String groupName, boolean compareTo)
	{
		this.type = type;
		this.name = name;
		this.label = label;
		this.description = description;
		this.groupName = groupName;
		this.compareTo = compareTo;
	}
	
	public Row(String type, String name, String label, String description, String groupName, boolean readOnly, String copyToRowName, String copyToLabel)
	{
		this.type = type;
		this.name = name;
		this.label = label;
		this.description = description;
		this.groupName = groupName;
		this.readOnly = readOnly;
		this.copyToRow = copyToRowName;
		this.copyToLabel = copyToLabel;
	}
	
	public Row(String type, String name, String label, String description, boolean readOnly, String groupName)
	{
		this.type = type;
		this.name = name;
		this.label = label;
		this.description = description;
		this.groupName = groupName;
		this.readOnly = readOnly;
	}

	public Row(String type, String name, String label, String description)
	{
		this.type = type;
		this.name = name;
		this.label = label;
		this.description = description;
	}

	public Row(String type, String name, String label)
	{
		this.type = type;
		this.name = name;
		this.label = label;
	}
	
	public Row(String type, String name, String label, boolean readOnly)
	{
		this.type = type;
		this.name = name;
		this.label = label;
		this.readOnly = readOnly;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public boolean isNoteEnabled()
	{
		return noteEnabled;
	}

	public void setNoteEnabled(boolean noteEnabled)
	{
		this.noteEnabled = noteEnabled;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getCopyToRow() {
		return this.copyToRow;
	}

	public String getCopyToLabel() {
		return copyToLabel;
	}

	public boolean isCopyEnabled() {
		return this.copyToRow != null;
	}

	public boolean isCompareTo() {
		return compareTo ;
	}

}
