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
