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
