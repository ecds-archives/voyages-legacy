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
package edu.emory.library.tast.images.site;

public class ImageLinkedVoyageInfo
{
	
	private long voyageIid;
	private int voyageId;
	private String info;
	
	public ImageLinkedVoyageInfo(long voyageIid, int voyageId, String info)
	{
		this.voyageIid = voyageIid;
		this.voyageId = voyageId;
		this.info = info;
	}

	public String getInfo()
	{
		return info;
	}

	public void setInfo(String info)
	{
		this.info = info;
	}
	
	public int getVoyageId()
	{
		return voyageId;
	}
	
	public void setVoyageId(int voyageId)
	{
		this.voyageId = voyageId;
	}

	public long getVoyageIid()
	{
		return voyageIid;
	}

	public void setVoyageIid(long voyageIid)
	{
		this.voyageIid = voyageIid;
	}

}
