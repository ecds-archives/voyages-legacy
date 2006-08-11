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
import edu.emory.library.tast.dm.attributes.Group;

public class Searchables
{
	
	private static final String SEARCHABLE_ATTRIBUTES_XML = "/searchable-attributes.xml";
	private static final String ATTRIBUTE_GROUPS_XML = "/attribute-groups.xml";
	
	private static final String PORT_DICTIONARY = "TBD:locationport";
	private static final String REGION_DICTIONARY = "TBD:locationregiondict";

	private SearchableAttribute[] searchableAttributes = null;
	private Map searchableAttributesByIds = null;

	private Group[] groups = null;
	private Map groupsByIds = null;
	
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
				int attrType = 0;
				for (int j = 0; j < xmlAttrs.getLength(); j++)
				{
					Node xmlAttr = xmlAttrs.item(j);
					String name = xmlAttr.getAttributes().getNamedItem("name").getNodeValue();
					Attribute attr = Voyage.getAttribute(name);
					if (j == 0)
					{
						attrType = attr.getType().intValue();
					}
					else
					{
						if (attrType != attr.getType().intValue())
							throw new RuntimeException("searchable attribute '" + id + "' contains invalid attributes");
					}
					attrs[j] = attr;
				}
				
				// create the corresponding searchable attribute
				switch (attrType)
				{
					case Attribute.TYPE_DICT:
						searchableAttribute = new SearchableAttributeSimpleDictionary(id, userLabel, userCategory, attrs);
						break;
					
					case Attribute.TYPE_STRING:
						searchableAttribute = new SearchableAttributeSimpleText(id, userLabel, userCategory, attrs);
						break;
					
					case Attribute.TYPE_INTEGER:
					case Attribute.TYPE_FLOAT:
					case Attribute.TYPE_LONG:
						searchableAttribute = new SearchableAttributeSimpleNumeric(id, userLabel, userCategory, attrs);
						break;
						
					case Attribute.TYPE_DATE:
						searchableAttribute = new SearchableAttributeSimpleDate(id, userLabel, userCategory, attrs);
						break;
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

					String port = xmlLoc.getAttributes().getNamedItem("port").getNodeValue();
					String region = xmlLoc.getAttributes().getNamedItem("region").getNodeValue();
					Attribute attrPort = Voyage.getAttribute(port);
					Attribute attrRegion = Voyage.getAttribute(region);
					
					if (attrPort.getType().intValue() != Attribute.TYPE_DICT ||
							attrRegion.getType().intValue() != Attribute.TYPE_DICT || 
							!PORT_DICTIONARY.equals(attrPort.getDictionary()) ||
							!REGION_DICTIONARY.equals(attrRegion.getDictionary()))
						throw new RuntimeException("searchable attribute '" + id + "' invalid location");
					
					locs[j] = new Location(attrPort, attrRegion);
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

	private void loadGroups() throws ParserConfigurationException, SAXException, IOException
	{
		
		// we have a DTD and we want to get rid of whitespace
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setIgnoringElementContentWhitespace(true);

		// load main document
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream str = this.getClass().getResourceAsStream(ATTRIBUTE_GROUPS_XML);
		Document document = builder.parse(str);
		
		NodeList xmlGroups = document.getDocumentElement().getChildNodes();
		groups = new Group[xmlGroups.getLength()];
		groupsByIds = new HashMap();
		
		// main loop over <searchable-attribute>
		for (int i = 0; i < xmlGroups.getLength(); i++)
		{
			Node xmlGroup = xmlGroups.item(i);
			
			// main properties
			String id = xmlGroup.getAttributes().getNamedItem("id").getNodeValue();
			String userLabel = xmlGroup.getAttributes().getNamedItem("userLabel").getNodeValue();
			
			// check id uniqueness
			if (groupsByIds.containsKey(id))
				throw new RuntimeException("duplicate group id '" + id + "'");

			// locate <searchable-attributes>
			NodeList xmlAttrTypes = xmlGroup.getChildNodes();
			NodeList xmlAttrs = null;
			for (int k = 0; k < xmlAttrTypes.getLength(); k++)
			{
				Node xmlAttrType = xmlAttrTypes.item(k);
				if ("searchable-attributes".equals(xmlAttrType.getNodeName()))
				{
					xmlAttrs = xmlAttrType.getChildNodes(); 
					break;
				}
			}

			// if found ...
			if (xmlAttrs != null)
			{

				// real all <searchable-attribute>'s in it
				SearchableAttribute[] attrs = new SearchableAttribute[xmlAttrs.getLength()]; 
				for (int j = 0; j < xmlAttrs.getLength(); j++)
				{
					Node xmlAttr = xmlAttrs.item(j);
					String attrId = xmlAttr.getAttributes().getNamedItem("id").getNodeValue();
					SearchableAttribute attr = (SearchableAttribute) searchableAttributesByIds.get(attrId);
					if (attr == null) throw new RuntimeException("group '" + id + "' contains a nonexistent attribute '" + attrId + "'");
					attrs[j] = attr;
				}
				Group group = new Group(id, userLabel, attrs);

				// add it to our collection
				groups[i] = group;
				groupsByIds.put(id, group);
			
			}

		}
		
	}

	private void loadConfiguration()
	{
		try
		{
			loadAttributes();
			loadGroups();
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
	
	public Group[] getGroups()
	{
		return groups;
	}
	
	public SearchableAttribute[] getSearchableAttributes()
	{
		return searchableAttributes;
	}
	
	public Group getGroupById(String groupId)
	{
		return (Group) groupsByIds.get(groupId);
	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
	{
		Searchables sa = new Searchables();
		sa.loadConfiguration();
	}

}