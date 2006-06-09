package edu.emory.library.tas.web.search3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

public class Search
{
	
	public static SearchCondition[] getLastQuery(HttpSession session)
	{
		return (SearchCondition[]) session.getAttribute("search");
	}

	public static List newSearch(HttpSession session, SearchCondition[] conditions)
	{
		session.setAttribute("search", conditions);
		return searchInternal(conditions, 0);
	}

	public static List gotoPage(HttpSession session, int page)
	{
		SearchCondition[] conditions = (SearchCondition[]) session.getAttribute("search");
		if (conditions == null) return null;
		return searchInternal(conditions, page);
	}
	
	private static List searchInternal(SearchCondition[] conditions, int page)
	{
		try
		{
			ArrayList results = new ArrayList();

			BufferedReader rdr =
				new BufferedReader(
						new InputStreamReader(
								Search.class.getResourceAsStream("words.txt")));
			
			String line = null;
			while ((line = rdr.readLine()) != null)
			{
				for (int i=0; i<conditions.length; i++)
				{
					if (line.startsWith(conditions[i].getSearchFor()))
					{
						results.add(line);
						break;
					}
				}
			}
			
			rdr.close();
			
			int start = page * 50;
			int end = (page+1) * 50 - 1;
			
			if (start >= results.size()) return null;
			if (end >= results.size()) end = results.size() - 1; 
			
			return results.subList(start, end);
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

}