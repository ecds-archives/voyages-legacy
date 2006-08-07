package edu.emory.library.tast.web.searchJSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

public class Search
{
	
	private static long nextHistoryId = 1;
	
	public static ArrayList getSearchHistory(HttpSession session)
	{
		ArrayList hist = (ArrayList) session.getAttribute("search");
		if (hist == null)
		{
			hist = new ArrayList();
			session.setAttribute("search", hist);
		}
		return hist;
	}

	public static HistoryItem getLastHistoryItem(HttpSession session)
	{
		ArrayList hist = getSearchHistory(session);
		if (hist.size() == 0)
			return null;
		else
			return (HistoryItem) hist.get(hist.size()-1);
	}
	
	public static void deleteHistoryItem(HttpSession session, int historyId)
	{
		ArrayList history = getSearchHistory(session);
		HistoryItem toRemove = null;
		for (Iterator iterHistoryItem = history.iterator(); iterHistoryItem.hasNext();)
		{
			HistoryItem historyItem = (HistoryItem) iterHistoryItem.next();
			if (historyItem.getId() == historyId)
			{
				toRemove =historyItem;
				break;
			}
		}
		if (toRemove != null)
		{
			history.remove(toRemove);
		}
	}

	public static ResultSet search(HttpSession session, SearchCondition[] conditions, int newSearch)
	{
		
		session.setAttribute("page", new Integer(0));

		ResultSet resultSet = new ResultSet();
		resultSet.setHistory(null);

		if (newSearch > 0)
		{
			HistoryItem historyItem = new HistoryItem();
			historyItem.setConditions(conditions);
			historyItem.setId(nextHistoryId++);
			getSearchHistory(session).add(historyItem);
			resultSet.setHistory(historyItem);
		}
		
		resultSet.setResults(searchInternal(conditions, 0));
		return resultSet;
		
	}

	public static ResultSet gotoNextPage(HttpSession session)
	{
		return gotoPage(session, true);
	}

	public static ResultSet gotoPrevPage(HttpSession session)
	{
		return gotoPage(session, false);
	}
	
	private static ResultSet gotoPage(HttpSession session, boolean next)
	{
		
		int currPage = 0;
		if (session.getAttribute("page") != null)
			currPage = ((Integer)session.getAttribute("page")).intValue();
	
		if (next) currPage ++; else currPage --;
		if (currPage < 0) currPage = 0;
		session.setAttribute("page", new Integer(currPage));
		
		HistoryItem lastHistoryItem = getLastHistoryItem(session);
		
		ResultSet resultSet = new ResultSet();
		resultSet.setHistory(null);
		resultSet.setResults(searchInternal(lastHistoryItem.getConditions(), currPage));
		return resultSet;
		
	}

	private static List searchInternal(SearchCondition[] conditions, int page)
	{
		try
		{

			String[] dataFields = new String[] {
					"voyageid",
					"natinimp",
					"shipname",
					"captaina",
					"majbuyrg",
					"majbyimp"};
			
			int[] fieldsMap = new int[conditions.length];
			for (int i=0; i<conditions.length; i++)
			{
				for (int j=0; j<dataFields.length; j++)
				{
					if (dataFields[j].equals(conditions[i].getField()))
					{
						fieldsMap[i] = j;
					}
				}
			}
			
			ArrayList results = new ArrayList();

			BufferedReader rdr =
				new BufferedReader(
						new InputStreamReader(
								Search.class.getResourceAsStream("voyages.dat"), "UTF8"));
			
			String line = null;
			while ((line = rdr.readLine()) != null)
			{
				String[] voyage = line.split("\\t");
				boolean matches = true;
				for (int i=0; i<conditions.length; i++)
				{
					if (!voyage[fieldsMap[i]].toLowerCase().startsWith(conditions[i].getSearchFor().toLowerCase()))
					{
						matches = false;
						break;
					}
				}
				if (matches)
				{
					ResultRow resultRow = new ResultRow();
					resultRow.setVoyageId(voyage[0]);
					resultRow.setNatinimp(voyage[1]);
					resultRow.setShipname(voyage[2]);
					resultRow.setCaptaina(voyage[3]);
					resultRow.setMajbuyrg(voyage[4]);
					resultRow.setMajbyimp(voyage[5]);
					results.add(resultRow);
				}
			}
			
			rdr.close();
			
			int start = page * 20;
			int end = (page+1) * 20 - 1;
			
			if (start >= results.size()) return new ArrayList();
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