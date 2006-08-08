package edu.emory.library.tast.ui.search.query.searchables;

public abstract class SearchableAttribute
{
	
	private String userLabel;
	
	public SearchableAttribute(String userLabel)
	{
		super();
		this.userLabel = userLabel;
	}

	public String getUserLabel()
	{
		return userLabel;
	}

	public void setUserLabel(String userLabel)
	{
		this.userLabel = userLabel;
	}

}
