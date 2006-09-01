package edu.emory.library.tast.ui.search.query.searchables;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;

public class Searchables
{
	
	private static final String SEARCHABLE_ATTRIBUTES_XML = "/searchable-attributes.xml";
	
	private static final String PORT_DICTIONARY = "TBD:locationport";
	private static final String REGION_DICTIONARY = "TBD:locationregiondict";

	private SearchableAttribute[] searchableAttributes = null;
	private Map searchableAttributesByIds = null;
	
	private static Searchables instance = null;
	
	private Searchables()
	{
	}
	
	public static Searchables getCurrent()
	{
		if (instance == null)
		{
			Searchables newInstance = new Searchables();
			newInstance.loadConfiguration();
			instance = newInstance;
		}
		return instance;
	}
	
	private void loadAttributes() throws ParserConfigurationException, SAXException, IOException
	{

		// we have a DTD and we want to get rid of whitespace
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setIgnoringElementContentWhitespace(true);

		// load main document
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream str = this.getClass().getResourceAsStream(SEARCHABLE_ATTRIBUTES_XML);
		Document document = builder.parse(str);
		
		// all <searchable-attribute>'s
		NodeList xmlSearchableAttrs = document.getDocumentElement().getChildNodes();
		searchableAttributes = new SearchableAttribute[xmlSearchableAttrs.getLength()];
		searchableAttributesByIds = new HashMap();
		
		// main loop over <searchable-attribute>
		for (int i = 0; i < xmlSearchableAttrs.getLength(); i++)
		{
			Node xmlSearchableAttr = xmlSearchableAttrs.item(i);
			
			// main properties
			SearchableAttribute searchableAttribute = null;
			String id = xmlSearchableAttr.getAttributes().getNamedItem("id").getNodeValue();
			String type = xmlSearchableAttr.getAttributes().getNamedItem("type").getNodeValue();
			String userLabel = xmlSearchableAttr.getAttributes().getNamedItem("userLabel").getNodeValue();
			UserCategory userCategory = UserCategory.parse(xmlSearchableAttr.getAttributes().getNamedItem("userCategory").getNodeValue());
			
			// check id uniqueness
			if (searchableAttributesByIds.containsKey(id))
				throw new RuntimeException("duplicate attribute id '" + id + "'");

			// simple attribute -> read list of db attriutes
			if ("simple".equals(type))
			{
				NodeList xmlAttrs = xmlSearchableAttr.getFirstChild().getChildNodes();
				Attribute[] attrs = new Attribute[xmlAttrs.getLength()];
				
				// read the db attributes
				String attrType = null;
				for (int j = 0; j < xmlAttrs.getLength(); j++)
				{
					Node xmlAttr = xmlAttrs.item(j);
					String name = xmlAttr.getAttributes().getNamedItem("name").getNodeValue();
					Attribute attr = Voyage.getAttribute(name);
					if (attr == null)
					{
						throw new RuntimeException("searchable attribute '" + id + "' not found");
					}
					else if (j == 0)
					{
						attrType = attr.decodeType();
					}
					else
					{
						if (!attrType.equals(attr.decodeType()))
							throw new RuntimeException("searchable attribute '" + id + "' contains invalid attributes");
					}
					attrs[j] = attr;
				}
				
				// create the corresponding searchable attribute
				if (attrType.equals(Attribute.DICTIONARY_ATTRIBUTE))
				{
					searchableAttribute = new SearchableAttributeSimpleDictionary(id, userLabel, userCategory, attrs);
				} else if (attrType.equals(Attribute.STRING_ATTRIBUTE)) 
				{
					searchableAttribute = new SearchableAttributeSimpleText(id, userLabel, userCategory, attrs);
				} else if (attrType.equals(Attribute.NUMERIC_ATTRIBUTE)) 
				{
					
					searchableAttribute = new SearchableAttributeSimpleNumeric(id, userLabel, userCategory, attrs);
				} else if (attrType.equals(Attribute.DATE_ATTRIBUTE)) {		
						searchableAttribute = new SearchableAttributeSimpleDate(id, userLabel, userCategory, attrs);
				}
				
			}

			// location -> read list of locations
			else if ("location".equals(type))
			{
				NodeList xmlLocs = xmlSearchableAttr.getFirstChild().getChildNodes();
				Location[] locs = new Location[xmlLocs.getLength()];
				for (int j = 0; j < xmlLocs.getLength(); j++)
				{
					Node xmlLoc = xmlLocs.item(j);

//					String port = xmlLoc.getAttributes().getNamedItem("port").getNodeValue();
//					String region = xmlLoc.getAttributes().getNamedItem("region").getNodeValue();
//					Attribute attrPort = Voyage.getAttribute(port);
//					Attribute attrRegion = Voyage.getAttribute(region);
//					
//					if (attrPort.getType().intValue() != Attribute.TYPE_DICT ||
//							attrRegion.getType().intValue() != Attribute.TYPE_DICT || 
//							!PORT_DICTIONARY.equals(attrPort.getDictionary()) ||
//							!REGION_DICTIONARY.equals(attrRegion.getDictionary()))
//						throw new RuntimeException("searchable attribute '" + id + "' invalid location");
//					
//					locs[j] = new Location(attrPort, attrRegion);
					locs[j] = new Location(null, null);
				}
				searchableAttribute = new SearchableAttributeLocation(id, userLabel, userCategory, locs);
			}

			// add it to our collection
			if (searchableAttribute != null)
			{
				searchableAttributes[i] = searchableAttribute;
				searchableAttributesByIds.put(id, searchableAttribute);
			}
		}
		
	}


	private void loadConfiguration()
	{
		try
		{
			loadAttributes();
		}
		catch (ParserConfigurationException e)
		{
			throw new RuntimeException(e);
		}
		catch (SAXException e)
		{
			throw new RuntimeException(e);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public SearchableAttribute getSearchableAttributeById(String attributeId)
	{
		return (SearchableAttribute) searchableAttributesByIds.get(attributeId);
	}
	
	public SearchableAttribute[] getSearchableAttributes()
	{
		return searchableAttributes;
	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
	{
		Searchables sa = new Searchables();
		sa.loadConfiguration();
	}
	
	public static SearchableAttribute getById(String attrId) {
		return getCurrent().getSearchableAttributeById(attrId);
	}

}