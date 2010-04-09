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