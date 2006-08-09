package edu.emory.library.tast.ui.search.query.searchables;

public abstract class SearchableAttribute
{
	
	private String userLabel;
	private String id;
	private UserCategory userCategory;
	
	public SearchableAttribute(String id, String userLabel, UserCategory userCategory)
	{
		this.id = id;
		this.userLabel = userLabel;
		this.userCategory = userCategory;
	}

	public String getUserLabel()
	{
		return userLabel;
	}

	public String getId()
	{
		return id;
	}

	public UserCategory getUserCategory()
	{
		return userCategory;
	}

}