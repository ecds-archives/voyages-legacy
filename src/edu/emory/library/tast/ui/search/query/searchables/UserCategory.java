package edu.emory.library.tast.ui.search.query.searchables;

public class UserCategory
{

	private static final int BEGINNERS = 1;
	private static final int GENERAL = 2;

	public static final UserCategory Beginners = new UserCategory(BEGINNERS); 
	public static final UserCategory General = new UserCategory(GENERAL);
	public static final UserCategory[] AllCategories = new UserCategory[] {Beginners, General};  
	
	private int category;

	private UserCategory(int mode)
	{
		this.category = mode;
	}
	
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof UserCategory)
		{
			return ((UserCategory)obj).category == category;
		}
		else
		{
			return false;
		}
	}
	
	public int hashCode()
	{
		return category;
	}
	
	public boolean isGeneral()
	{
		return category == GENERAL;
	}
	
	public boolean isBeginners()
	{
		return category == BEGINNERS;
	}
	
	public String toString()
	{
		if (category == GENERAL)
			return "general";
		else
			return "beginners";
	}
	
	public static UserCategory parse(String value)
	{
		if (value == null || value.equals("general"))
			return General;
		else
			return Beginners;
	}
	
	public static UserCategory[] getAllCategories()
	{
		return new UserCategory[] {Beginners, General};
	}
	
}