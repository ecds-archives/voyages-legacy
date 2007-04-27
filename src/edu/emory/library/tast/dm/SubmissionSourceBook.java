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
