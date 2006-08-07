package edu.emory.library.tast.web.test;

import java.util.List;

public class Search
{
	
	public static String search(SearchRequest request)
	{
		List conditions = request.getConditions();
		return ((SearchCondition) conditions.get(1)).getFieldName();
	}

	public static String test()
	{
		return "hmmm";
	}

}
