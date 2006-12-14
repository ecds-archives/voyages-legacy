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
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.PortAttribute;
import edu.emory.library.tast.dm.attributes.RegionAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class Searchables
{
	
	private static final String SEARCHABLE_ATTRIBUTES_XML = "/searchable-attributes.xml";
	
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
			if (xmlSearchableAttr.getNodeType() != Node.COMMENT_NODE) {
			SearchableAttribute searchableAttribute = null;
			String id = xmlSearchableAttr.getAttributes().getNamedItem("id").getNodeValue();
			String type = xmlSearchableAttr.getAttributes().getNamedItem("type").getNodeValue();
			String userLabel = xmlSearchableAttr.getAttributes().getNamedItem("userLabel").getNodeValue();
			
			// check id uniqueness
			if (searchableAttributesByIds.containsKey(id))
				throw new RuntimeException("duplicate attribute id '" + id + "'");
			
			// read categories
			NodeList xmlUserCats = xmlSearchableAttr.getChildNodes().item(0).getChildNodes();
			UserCategories userCats = new UserCategories();
			for (int j = 0; j < xmlUserCats.getLength(); j++)
			{
				Node xmlUserCat = xmlUserCats.item(j);
				UserCategory category = UserCategory.parse(xmlUserCat.getAttributes().getNamedItem("name").getNodeValue());
				if (category != null) userCats.addTo(category);
			}

			// simple attribute -> read list of db attriutes
			if ("simple".equals(type))
			{
				NodeList xmlAttrs = xmlSearchableAttr.getChildNodes().item(1).getChildNodes();
				Attribute[] attrs = new Attribute[xmlAttrs.getLength()];
				
				// read the db attributes
				Attribute firstAttr = null;
				for (int j = 0; j < xmlAttrs.getLength(); j++)
				{
					Node xmlAttr = xmlAttrs.item(j);
					String name = xmlAttr.getAttributes().getNamedItem("name").getNodeValue();
					Attribute attr = Voyage.getAttribute(name);
					if (attr == null)
					{
						throw new RuntimeException("searchable attribute '" + name + "' not found");
					}
					else if (j == 0)
					{
						firstAttr = attr;
					}
					else
					{
						if (firstAttr.getClass() != attr.getClass())
							throw new RuntimeException("searchable attribute '" + id + "' contains invalid attributes");
					}
					attrs[j] = attr;
				}

				// create the corresponding searchable attribute
				if (firstAttr instanceof DictionaryAttribute)
				{
					searchableAttribute =
						new SearchableAttributeSimpleDictionary(
							id, userLabel, userCats, attrs);
				}
				else if (firstAttr instanceof StringAttribute) 
				{
					searchableAttribute =
						new SearchableAttributeSimpleText(
							id, userLabel, userCats, attrs);
				}
				else if (firstAttr instanceof NumericAttribute) 
				{
					searchableAttribute =
						new SearchableAttributeSimpleNumeric(
							id, userLabel, userCats, attrs);
				}
				else if (firstAttr instanceof DateAttribute)
				{		
					searchableAttribute =
						new SearchableAttributeSimpleDate(
							id, userLabel, userCats, attrs);
				}
				else if (firstAttr instanceof BooleanAttribute)
				{		
					searchableAttribute =
						new SearchableAttributeSimpleBoolean(
							id, userLabel, userCats, attrs);
				}
				else
				{
					throw new RuntimeException("unsupported type for searchable attribute '" + id + "'");					
				}

			}
			
			// location -> read list of locations
			else if ("port".equals(type))
			{
				NodeList xmlLocs = xmlSearchableAttr.getChildNodes().item(1).getChildNodes();
				Location[] locs = new Location[xmlLocs.getLength()];
				for (int j = 0; j < xmlLocs.getLength(); j++)
				{
					Node xmlLoc = xmlLocs.item(j);

					String port = xmlLoc.getAttributes().getNamedItem("port").getNodeValue();
					Attribute attrPort = Voyage.getAttribute(port);
					if (!(attrPort instanceof PortAttribute))
						throw new RuntimeException("searchable attribute '" + id + "' invalid port attribute");

					Node xmlRegion = xmlLoc.getAttributes().getNamedItem("region");
					Attribute attrRegion = null;
					if (xmlRegion != null)
					{
						String region = xmlRegion.getNodeValue();
						attrRegion = Voyage.getAttribute(region);
						if (!(attrRegion instanceof RegionAttribute))
							throw new RuntimeException("searchable attribute '" + id + "' invalid region attribute");
					}
					
					locs[j] = new Location(attrPort, attrRegion);

				}
				searchableAttribute = new SearchableAttributeLocation(id, userLabel, userCats, locs);
			}
			
			// add it to our collection
			if (searchableAttribute != null)
			{
				searchableAttributes[i] = searchableAttribute;
				searchableAttributesByIds.put(id, searchableAttribute);
			}
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