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