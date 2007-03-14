package edu.emory.library.tast.database.query.searchables;


public class UserCategories
{
	
	private boolean[] membership;
	
	public UserCategories()
	{
		membership = new boolean[UserCategory.AllCategories.length];
	}
	
	public void addTo(UserCategory category)
	{
		UserCategory[] allCats = UserCategory.AllCategories;
		for (int i = 0; i < allCats.length; i++)
		{
			if (allCats[i].equals(category))
			{
				membership[i] = true;
				return;
			}
		}
	}

	public void removeFrom(UserCategory category)
	{
		UserCategory[] allCats = UserCategory.AllCategories;
		for (int i = 0; i < allCats.length; i++)
		{
			if (allCats[i].equals(category))
			{
				membership[i] = false;
				return;
			}
		}
	}

	public boolean isIn(UserCategory category)
	{
		UserCategory[] allCats = UserCategory.AllCategories;
		for (int i = 0; i < allCats.length; i++)
		{
			if (allCats[i].equals(category) && membership[i])
			{
				return true;
			}
		}
		return false;
	}

}