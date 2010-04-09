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

public class SubmissionSourceBook extends SubmissionSource
{
	
	private String authors;
	private String title;
	private String publisher;
	private String placeOfPublication;
	private int year;
	private int pageFrom;
	private int pageTo;
	
	public String getAuthors()
	{
		return authors;
	}
	
	public void setAuthors(String authors)
	{
		this.authors = authors;
	}
	
	public int getPageFrom()
	{
		return pageFrom;
	}
	
	public void setPageFrom(int pageFrom)
	{
		this.pageFrom = pageFrom;
	}
	
	public int getPageTo()
	{
		return pageTo;
	}
	
	public void setPageTo(int pageTo)
	{
		this.pageTo = pageTo;
	}
	
	public String getPlaceOfPublication()
	{
		return placeOfPublication;
	}
	
	public void setPlaceOfPublication(String placeOfPublication)
	{
		this.placeOfPublication = placeOfPublication;
	}
	
	public String getPublisher()
	{
		return publisher;
	}
	
	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public int getYear()
	{
		return year;
	}
	
	public void setYear(int year)
	{
		this.year = year;
	}

	public boolean equals(Object obj)
	{
		
		// if both have id, the comparision is done based on ids
		if (!super.equals(obj))
			return false;
		
		if (!(obj instanceof SubmissionSourceBook))
			return false;
		
		final SubmissionSourceBook that = (SubmissionSourceBook) obj;
		
		// this is used only if both are new
		return
			StringUtils.compareStrings(this.authors, that.authors) &&
			StringUtils.compareStrings(this.title, that.title) &&
			StringUtils.compareStrings(this.placeOfPublication, that.placeOfPublication) &&
			StringUtils.compareStrings(this.publisher, that.publisher) &&
			this.year == that.year &&
			this.pageFrom == that.pageFrom &&
			this.pageTo == that.pageTo;
		
	}

}
