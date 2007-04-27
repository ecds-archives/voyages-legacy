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