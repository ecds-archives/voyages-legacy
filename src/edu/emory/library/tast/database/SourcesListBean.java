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
import edu.emory.library.tast.util.StringUtils;

class SouceTypeDescriptor
{
	
	private int type;
	private String urlId;
	private String pageTitle;
	
	public SouceTypeDescriptor(int type, String urlId, String pageTitle)
	{
		this.type = type;
		this.urlId = urlId;
		this.pageTitle = pageTitle;
	}

	public int getType()
	{
		return type;
	}

	public String getUrlId()
	{
		return urlId;
	}

	public String getPageTitle()
	{
		return pageTitle;
	}
	
}

public class SourcesListBean
{

	private String type;
	private boolean idColumnFirst = true;
	
	private static SouceTypeDescriptor[] typeDescriptors = new SouceTypeDescriptor[] {
		
		new SouceTypeDescriptor(
				Source.TYPE_DOCUMENTARY_SOURCE,
				"documentary",
				"Documentary Sources"),
				
		new SouceTypeDescriptor(
				Source.TYPE_NEWSPAPER,
				"newspapers",
				"Newspapers"),
				
		new SouceTypeDescriptor(
				Source.TYPE_PUBLISHED_SOURCE,
				"published",
				"Published Sources"),
		
		new SouceTypeDescriptor(
				Source.TYPE_UNPUBLISHED_SECONDARY_SOURCE,
				"unpublished",
				"Unpublished Secondary Sources"),
		
		new SouceTypeDescriptor(
				Source.TYPE_PRIVATE_NOTE_OR_COLLECTION,
				"private",
				"Private Notes and Collections") };
	
	private SimpleTableCell[][] loadSimpleSources(Session sess, int type)
	{
		
		List sourcesDb = sess.
			createCriteria(Source.class).
			add(Restrictions.eq("type", new Integer(type))).
			addOrder(Order.asc(idColumnFirst ? "id" : "name")).list();
		
		SimpleTableCell[][] sources = new SimpleTableCell[sourcesDb.size()][];
		
		int rowIndex = 0;
		for (Iterator sourceIt = sourcesDb.iterator(); sourceIt.hasNext();)
		{
			Source source = (Source) sourceIt.next();
			String cssClassParity = rowIndex % 2 == 0 ? "even" : "odd";
			
			SimpleTableCell cellId = new SimpleTableCell(source.getId(), "source-id-" + cssClassParity);
			SimpleTableCell cellName = new SimpleTableCell(source.getName(), "source-name-" + cssClassParity);
			
			sources[rowIndex] = new SimpleTableCell[2];
			sources[rowIndex][0] = idColumnFirst ? cellId : cellName; 
			sources[rowIndex][1] = idColumnFirst ? cellName : cellId; 
			rowIndex++;
			
		}
		
		return sources;
		
	}
	
	private SimpleTableCell[][] loadPublishedSources(Session sess)
	{

		List sourcesDb = sess.
			createCriteria(Source.class).
			add(Restrictions.eq("type", new Integer(Source.TYPE_PUBLISHED_SOURCE))).
			addOrder(Order.asc(idColumnFirst ? "id" : "name")).list();
		
		Map soucesByLetters = new TreeMap();
		for (Iterator sourceIt = sourcesDb.iterator(); sourceIt.hasNext();)
		{
			Source source = (Source) sourceIt.next();
			Character firstLetter = new Character(Character.toUpperCase(
					StringUtils.getFirstLetter(
							idColumnFirst ? source.getId() : source.getName())));
			
			List sources = (List) soucesByLetters.get(firstLetter); 			
			if (sources == null)
			{
				sources = new LinkedList();
				soucesByLetters.put(firstLetter, sources);
			}
			
			sources.add(source);
			
		}
		
		SimpleTableCell[][] table = new SimpleTableCell[sourcesDb.size() + soucesByLetters.size()][];
		
		int rowIndex = 0;
		for (Iterator letterId = soucesByLetters.keySet().iterator(); letterId.hasNext();)
		{
			Character letter = (Character) letterId.next();
			List sources = (List) soucesByLetters.get(letter);
			
			table[rowIndex] = new SimpleTableCell[] {
					new SimpleTableCell(String.valueOf(letter),
							null, "sources-letter" + (rowIndex == 0 ? "-first" : ""),
							1, 2)};
			
			rowIndex++;
			
			int sourceIdx = 0;
			for (Iterator sourceIt = sources.iterator(); sourceIt.hasNext();)
			{
				Source source = (Source) sourceIt.next();
				
				String cssClassParity = sourceIdx % 2 == 0 ? "even" : "odd";
				sourceIdx++;

				SimpleTableCell cellId = new SimpleTableCell(source.getId(), "source-id-" + cssClassParity);
				SimpleTableCell cellName = new SimpleTableCell(source.getName(), "source-name-" + cssClassParity);
				
				table[rowIndex] = new SimpleTableCell[2];
				table[rowIndex][0] = idColumnFirst ? cellId : cellName; 
				table[rowIndex][1] = idColumnFirst ? cellName : cellId; 
				rowIndex++;
				
			}
			
		}
		
		return table;
		
	}
	
	private SimpleTableCell[][] loadDocumentarySources(Session sess)
	{
		
		//long start = System.currentTimeMillis();
		
		//Pattern sourceRegEx = Pattern.compile("^[^\\(\\)]+\\(([^\\,]+),\\s*([^\\,]+),\\s*([^\\,]+)\\)");
		Pattern sourceRegEx = Pattern.compile("[^\\(\\)]+\\(([^\\,\\(\\)]+(?:,\\s*[^\\,\\(\\)]+)?),\\s*([^\\,\\(\\)]+)\\).*");
		
		Map countriesToCities = new TreeMap();
		
		List sourcesDb = sess.
			createCriteria(Source.class).
			add(Restrictions.eq("type", new Integer(Source.TYPE_DOCUMENTARY_SOURCE))).
			addOrder(Order.asc(idColumnFirst ? "id" : "name")).list();
		
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
				//tableRowsCount++;
			}
			
			sourcesInCity.add(source);
			tableRowsCount++;

		}
		
		SimpleTableCell[][] table = new SimpleTableCell[tableRowsCount][];
		
		int rowIndex = 0;
		int countryIdx = 0;
		for (Iterator countriesIt = countriesToCities.keySet().iterator(); countriesIt.hasNext();)
		{
			String country = (String) countriesIt.next();
			Map citiesToSources = (Map) countriesToCities.get(country);
			
			String countryCssClassSuffix = countryIdx == 0 ? "-first" : "";
			
			table[rowIndex++] = new SimpleTableCell[] {
					new SimpleTableCell(country, null, "sources-country" + countryCssClassSuffix, 1, 3)};

			int cityIdx = 0;
			int souceInCountryIdx = 0;
			for (Iterator citiesIt = citiesToSources.keySet().iterator(); citiesIt.hasNext();)
			{
				String city = (String) citiesIt.next();
				List sources = (List) citiesToSources.get(city);
				int sourcesCount = sources.size();
				
				String cityCssClassSuffix = cityIdx == 0 ? "-first" : "";
				
				table[rowIndex] = new SimpleTableCell[3];
				table[rowIndex][0] = new SimpleTableCell(city, null, "sources-city" + cityCssClassSuffix, sourcesCount, 1);
				
				int sourceIdx = 0;
				for (Iterator sourceIt = sources.iterator(); sourceIt.hasNext();)
				{
					Source source = (Source) sourceIt.next();
					
					String cssClassSuffix =
						souceInCountryIdx == 0 ? "first" :
							souceInCountryIdx % 2 == 0 ? "even" :
								"odd";
					
					SimpleTableCell cellId = new SimpleTableCell(source.getId(), "source-id-" + cssClassSuffix);
					SimpleTableCell cellName = new SimpleTableCell(source.getName(), "source-name-" + cssClassSuffix);
					
					if (sourceIdx > 0)
					{
						table[rowIndex] = new SimpleTableCell[2];
						table[rowIndex][0] = idColumnFirst ? cellId : cellName;
						table[rowIndex][1] = idColumnFirst ? cellName : cellId;
					}
					else
					{
						table[rowIndex][1] = idColumnFirst ? cellId : cellName;
						table[rowIndex][2] = idColumnFirst ? cellName : cellId;
					}
					rowIndex++;
					
					sourceIdx++;
					souceInCountryIdx++;
					
				}
				
				cityIdx++;
				
			}
			
			countryIdx++;
			
		}
		
		//long stop = System.currentTimeMillis();
		//System.out.println((stop - start) + " ms");
		
		return table;
		
	}

	public SimpleTableCell[][] getSources()
	{

		Session sess = HibernateUtil.getSession();
		Transaction trans = sess.beginTransaction();
		
		SimpleTableCell[][] sources;
		
		SouceTypeDescriptor typeDesc = getTypeDescriptor(type);
		switch (typeDesc.getType())
		{
			case Source.TYPE_DOCUMENTARY_SOURCE:
				sources = loadDocumentarySources(sess);
				break;
			case Source.TYPE_PUBLISHED_SOURCE:
				sources = loadPublishedSources(sess);
				break;
			default:
				sources = loadSimpleSources(sess, typeDesc.getType());
		}

		trans.commit();
		sess.close();
		
		return sources;
	
	}

	private SouceTypeDescriptor getTypeDescriptor(String urlId)
	{
		for (int i = 0; i < typeDescriptors.length; i++)
		{
			SouceTypeDescriptor desc = typeDescriptors[i];
			if (desc.getUrlId().equals(type)) return desc; 
		}
		return typeDescriptors[0];
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getPageTitle()
	{
		return getTypeDescriptor(type).getPageTitle();
	}

	public String getIdColumnFirst()
	{
		return idColumnFirst ? "true" : "false";
	}

	public void setIdColumnFirst(String idColumnFirst)
	{
		this.idColumnFirst = idColumnFirst == null || idColumnFirst.equals("true");
	}
	
	public String getSwitchLayoutLink()
	{
		return "sources.faces?" +
			"type=" + getTypeDescriptor(type).getUrlId() + "&" +
			"idColumnFirst=" + (idColumnFirst ? "false" : "true");
	}

}