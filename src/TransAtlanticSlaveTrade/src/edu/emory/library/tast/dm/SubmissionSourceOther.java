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

import edu.emory.library.tast.util.StringUtils;

public class SubmissionSourceOther extends SubmissionSource
{
	
	private String location;
	private String title;
	private String classNumber;
	private String folioOrPage;
	
	public String getClassNumber()
	{
		return classNumber;
	}
	
	public void setClassNumber(String classNumber)
	{
		this.classNumber = classNumber;
	}
	
	public String getFolioOrPage()
	{
		return folioOrPage;
	}
	
	public void setFolioOrPage(String folioOrPage)
	{
		this.folioOrPage = folioOrPage;
	}
	
	public String getLocation()
	{
		return location;
	}
	
	public void setLocation(String location)
	{
		this.location = location;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}

	public boolean equals(Object obj)
	{

		// if both have id, the comparision is done based on ids
		if (!super.equals(obj))
			return false;
		
		if (!(obj instanceof SubmissionSourceOther))
			return false;
		
		final SubmissionSourceOther that = (SubmissionSourceOther) obj;
		
		// this is used only if both are new
		return
			StringUtils.compareStrings(this.location, that.location) &&
			StringUtils.compareStrings(this.title, that.title) &&
			StringUtils.compareStrings(this.classNumber, that.classNumber) &&
			StringUtils.compareStrings(this.folioOrPage, that.folioOrPage);
	
	}

}