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
	
	private class ArchiveDocument
	{
		
		private String id;
		private String name;
		
		public ArchiveDocument(String id, String name)
		{
			this.id = id;
			this.name = name;
		}

		public String getId()
		{
			return id;
		}
		
		public String getName()
		{
			return name;
		}
	}

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
					new SimpleTableCell("<span>" + String.valueOf(letter) + "</span>",
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
		
		long start = System.currentTimeMillis();
		
		//Pattern sourceRegEx = Pattern.compile("^[^\\(\\)]+\\(([^\\,]+),\\s*([^\\,]+),\\s*([^\\,]+)\\)");
		Pattern sourceRegEx = Pattern.compile("(<i>[^\\(\\)]+</i>)\\s*\\(([^\\,\\(\\)]+(?:,\\s*[^\\,\\(\\)]+)?),\\s*([^\\,\\(\\)]+)\\)\\s*(.*)");
		
		Map countriesToCities = new TreeMap();
		
		List sourcesDb = sess.
			createCriteria(Source.class).
			add(Restrictions.eq("type", new Integer(Source.TYPE_DOCUMENTARY_SOURCE))).
			addOrder(Order.asc(idColumnFirst ? "id" : "name")).
			addOrder(Order.asc(idColumnFirst ? "name" : "id")).
			list();
		
		int tableRowsCount = 0;
		
		for (Iterator sourceIt = sourcesDb.iterator(); sourceIt.hasNext();)
		{
			Source source = (Source) sourceIt.next();
			
			Matcher matcher = sourceRegEx.matcher(source.getName());
			
			String country;
			String city;
			String archive;
			String document;
			if (matcher.matches())
			{
				archive = matcher.group(1);
				city = matcher.group(2);
				country = matcher.group(3);
				document = matcher.group(4);
			}
			else
			{
				country = "uncategorized";
				city = "uncategorized";
				archive = source.getName();
				document = "";
			}
			
			boolean isDocument = !StringUtils.isNullOrEmpty(document);

			Map citiesToArchives = (Map) countriesToCities.get(country);
			if (citiesToArchives == null)
			{
				citiesToArchives = new TreeMap();
				countriesToCities.put(country, citiesToArchives);
				tableRowsCount++;
			}
			
			Map archivesToDocuments = (Map) citiesToArchives.get(city);
			if (archivesToDocuments == null)
			{
				archivesToDocuments = new TreeMap();
				citiesToArchives.put(city, archivesToDocuments);
			}
			
			LinkedList documents = (LinkedList) archivesToDocuments.get(archive);
			if (documents == null)
			{
				documents = new LinkedList();
				archivesToDocuments.put(archive, documents);
			}
			
			if (isDocument)
				documents.addLast(new ArchiveDocument(source.getId(), document));
			else
				documents.addFirst(new ArchiveDocument(source.getId(), archive));
			
			tableRowsCount++;

		}
		
		SimpleTableCell[][] table = new SimpleTableCell[tableRowsCount][];
		
		int rowIndex = 0;
		int countryIdx = 0;
		for (Iterator countriesIt = countriesToCities.keySet().iterator(); countriesIt.hasNext();)
		{
			String country = (String) countriesIt.next();
			Map citiesToArchives = (Map) countriesToCities.get(country);
			
			String countryCssClassSuffix = countryIdx == 0 ? "-first" : "";
			
			table[rowIndex++] = new SimpleTableCell[] {
					new SimpleTableCell(country, null,
							"sources-country" + countryCssClassSuffix, 1, 3)};

			int cityIdx = 0;
			int sourceIdxInCountry = 0;
			for (Iterator cityIt = citiesToArchives.keySet().iterator(); cityIt.hasNext();)
			{
				String city = (String) cityIt.next();
				Map archives = (Map) citiesToArchives.get(city);
				
				int documentsInCity = 0;
				for (Iterator archivesIt = archives.values().iterator(); archivesIt.hasNext();)
					documentsInCity += ((List)archivesIt.next()).size(); 

				String cityCssClassSuffix = cityIdx == 0 ? "-first" : "";

				table[rowIndex] = new SimpleTableCell[3];
				table[rowIndex][0] = new SimpleTableCell(
						city, null,
						"sources-city" + cityCssClassSuffix,
						documentsInCity, 1);
				
				int sourceIdxInCity = 0;
				for (Iterator archiveIt = archives.keySet().iterator(); archiveIt.hasNext();)
				{
					String archive = (String) archiveIt.next();
					List documents = (List) archives.get(archive);
					
					for (Iterator documentIt = documents.iterator(); documentIt.hasNext();)
					{
						ArchiveDocument document = (ArchiveDocument) documentIt.next();
						
						String cssClassSuffix =
							sourceIdxInCountry == 0 ? "first" :
								sourceIdxInCountry % 2 == 0 ? "even" :
									"odd";
						
						SimpleTableCell cellId = new SimpleTableCell(
								StringUtils.unNull(document.getId()),
								"source-id-" + cssClassSuffix);
						
						SimpleTableCell cellName = new SimpleTableCell(
								document.getName(),
								"source-name-" + cssClassSuffix);
						
						if (sourceIdxInCity > 0)
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
						
						sourceIdxInCountry++;
						sourceIdxInCity++;
						
					}
					
				}
				
				cityIdx++;
				
			}
			
			countryIdx++;
			
		}
		
		long stop = System.currentTimeMillis();
		System.out.println((stop - start) + " ms");
		
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