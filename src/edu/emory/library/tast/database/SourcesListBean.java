package edu.emory.library.tast.database;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.common.SimpleTableCell;
import edu.emory.library.tast.dm.Source;
import edu.emory.library.tast.util.HibernateUtil;

public class SourcesListBean
{

	private String type;
	
	private SimpleTableCell[][] loadSimpleSources(Session sess, int type)
	{
		
		List sourcesDb = sess.
			createCriteria(Source.class).
			add(Restrictions.eq("type", new Integer(type))).
			addOrder(Order.asc("name")).list();
		
		SimpleTableCell[][] table = new SimpleTableCell[sourcesDb.size()][];
		
		int rowIndex = 0;
		for (Iterator sourceIt = sourcesDb.iterator(); sourceIt.hasNext();)
		{
			Source source = (Source) sourceIt.next();
			
			table[rowIndex++] = new SimpleTableCell[] {
					new SimpleTableCell(source.getName(), "source-name"),
					new SimpleTableCell(source.getId(), "source-id")};
			
		}
		
		return null;
		
	}
	
	private SimpleTableCell[][] loadDocumentarySources(Session sess)
	{
		
		//long start = System.currentTimeMillis();
		
		//Pattern sourceRegEx = Pattern.compile("^[^\\(\\)]+\\(([^\\,]+),\\s*([^\\,]+),\\s*([^\\,]+)\\)");
		Pattern sourceRegEx = Pattern.compile("[^\\(\\)]+\\(([^\\,]+),\\s*([^\\,]+)\\).+");
		
		Map countriesToCities = new TreeMap();
		
		List sourcesDb = sess.
			createCriteria(Source.class).
			add(Restrictions.eq("type", new Integer(Source.TYPE_DOCUMENTARY_SOURCE))).
			addOrder(Order.asc("name")).list();
		
		int tableRowsCount = 0;
		
		for (Iterator sourceIt = sourcesDb.iterator(); sourceIt.hasNext();)
		{
			Source source = (Source) sourceIt.next();
			
			Matcher matcher = sourceRegEx.matcher(source.getName());
			
			String country;
			String city;
			if (matcher.matches())
			{
				city = matcher.group(1);
				country = matcher.group(2);
			}
			else
			{
				country = "uncategorized";
				city = "uncategorized";
			}

			Map citiesToSources = (Map) countriesToCities.get(country);
			if (citiesToSources == null)
			{
				citiesToSources = new TreeMap();
				countriesToCities.put(country, citiesToSources);
				tableRowsCount++;
			}
			
			List sourcesInCity = (List) citiesToSources.get(city);
			if (sourcesInCity == null)
			{
				sourcesInCity = new LinkedList();
				citiesToSources.put(city, sourcesInCity);
				tableRowsCount++;
			}
			
			sourcesInCity.add(source);
			tableRowsCount++;

		}
		
		SimpleTableCell[][] table = new SimpleTableCell[tableRowsCount][];
		
		int rowIndex = 0;
		for (Iterator countriesIt = countriesToCities.keySet().iterator(); countriesIt.hasNext();)
		{
			String country = (String) countriesIt.next();
			Map citiesToSources = (Map) countriesToCities.get(country);
			
			table[rowIndex++] = new SimpleTableCell[] {
					new SimpleTableCell(country, null, "sources-country", 1, 2)};
			
			for (Iterator citiesIt = citiesToSources.keySet().iterator(); citiesIt.hasNext();)
			{
				String city = (String) citiesIt.next();
				List sources = (List) citiesToSources.get(city);
				
				table[rowIndex++] = new SimpleTableCell[] {
						new SimpleTableCell(city, null, "sources-city", 1, 2)};
				
				for (Iterator sourceIt = sources.iterator(); sourceIt.hasNext();)
				{
					Source source = (Source) sourceIt.next();
					
					table[rowIndex++] = new SimpleTableCell[] {
							new SimpleTableCell(source.getName(), "source-name"),
							new SimpleTableCell(source.getId(), "source-id")};
					
				}
				
			}
			
		}
		
		//long stop = System.currentTimeMillis();
		//System.out.println((stop - start) + " ms");
		
		return table;
		
	}

	public SimpleTableCell[][] getSources()
	{

		Session sess = HibernateUtil.getSession();
		Transaction trans = sess.beginTransaction();
		
		SimpleTableCell[][] table;
		
		int typeInt = getTypeAsInt(); 
		if (typeInt == Source.TYPE_DOCUMENTARY_SOURCE)
			table = loadDocumentarySources(sess);
		else
			table = loadSimpleSources(sess, typeInt);

		trans.commit();
		sess.close();
		
		return table;
	
	}
	
	private int getTypeAsInt()
	{
		if ("newspapers".equals(type))
			return Source.TYPE_NEWSPAPER;
		else if ("published".equals(type))
			return Source.TYPE_PUBLISHED_SOURCE;
		else if ("unpublished".equals(type))
			return Source.TYPE_UNPUBLISHED_SECONDARY_SOURCE;
		else if ("private".equals(type))
			return Source.TYPE_PRIVATE_NOTE_OR_COLLECTION;
		else
			return Source.TYPE_DOCUMENTARY_SOURCE;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

}
