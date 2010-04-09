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
package edu.emory.library.tast.faq;

import java.util.LinkedList;
import java.util.List;

public class FAQList
{
	
	private List categories = new LinkedList();
	private String[] keywords;
	
	public String[] getKeywords()
	{
		return keywords;
	}

	public void setKeywords(String[] keywords)
	{
		this.keywords = keywords;
	}

	public FAQListCategory addCategory(String name)
	{
		FAQListCategory cat = new FAQListCategory(name);
		categories.add(cat);
		return cat;
	}

	public List getCategories()
	{
		return categories;
	}

	public void setCategories(List categories)
	{
		this.categories = categories;
	}

}
