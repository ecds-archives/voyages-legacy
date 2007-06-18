package edu.emory.library.tast.dm;

import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tools.ant.util.ReaderInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.HibernateConnector;
import edu.emory.library.tast.util.XMLUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * Class that is used to store any configuration of any component.
 * This class is managed by Hibernate.
 * @author Pawel Jurczyk 
 *
 */
public class Configuration {
	
	public static final String XML_PREFIX = XMLUtils.XML_PREFIX;
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new StringAttribute("id", "Configuration"));
		attributes.put("creationDate", new DateAttribute("creationDate", "Configuration"));
		attributes.put("entries", new NumericAttribute("map_entries", "Configuration", NumericAttribute.TYPE_INTEGER));
	}
	
	/**
	 * ID of configuration.
	 */
	private Long id;
	
	/**
	 * Date when the configuration has been created.
	 */
	private Date creationDate;
	
	/**
	 * Any entries in the configuration.
	 */
	private HashMap entries = new HashMap();
	
	/**
	 * Adds any value representing configuration to the map of stored entries.
	 * @param entry entry key
	 * @param value	entry value
	 */
	public void addEntry(String entry, XMLExportable value) {
		this.entries.put(entry, value);
	}
	
	/**
	 * Gets value stored under given key.
	 * @param entry
	 * @return
	 */
	public XMLExportable getEntry(String entry) {
		return (XMLExportable) this.entries.get(entry);
	}
	
	public String getEntries() {
		System.out.println("get entries");
		StringBuffer outXml = new StringBuffer();
		outXml.append(XML_PREFIX).append("\n").append("<entries>\n");
		Iterator entries = this.entries.keySet().iterator();
		while (entries.hasNext()) {
			String key = (String) entries.next();
			XMLExportable entry = (XMLExportable) this.entries.get(key);
			outXml.append("<entry name=\"").append(key).append("\" class=\"");
			outXml.append(entry.getClass().getName()).append("\">\n");
			outXml.append(entry.toXML());
			outXml.append("</entry>\n");
		}
		outXml.append("</entries>");
		return outXml.toString();
	}
	
	public void setEntries(String entries) {
		System.out.println("set entries");
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			System.out.println("XML is: " + entries);
			Document document = builder.parse(new ReaderInputStream(new StringReader(entries)));
			NodeList nodes = document.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("entries")) {
					this.parseEntries(node);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void parseEntries(Node node) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node entry = list.item(i);
			if (entry.getNodeType() == Node.ELEMENT_NODE && entry.getNodeName().equals("entry")) {
				String className = entry.getAttributes().getNamedItem("class").getNodeValue();
				String entryName = entry.getAttributes().getNamedItem("name").getNodeValue();
				Class clazz = Class.forName(className);
				XMLExportable export = (XMLExportable) clazz.newInstance();
				export.restoreFromXML(entry);
				this.entries.put(entryName , export);
			}
		}
		
	}

	/**
	 * Saves config to DB.
	 *
	 */
	public void save() {
		HibernateConnector.getConnector().saveObject(this);
	}
	
	/**
	 * Saves or updates config in DB.
	 *
	 */
	public void saveOrUpdate() {
		HibernateConnector.getConnector().saveOrUpdateObject(this);
	}
	
	/**
	 * Deletes config.
	 *
	 */
	public void delete() {
		HibernateConnector.getConnector().deleteObject(this);
	}
	
	/**
	 * Loads configuration with given ID.
	 * @param id ID of configuration to load
	 * @return
	 */
	public static Configuration loadConfiguration(Long id) {
		Conditions c = new Conditions();
		c.addCondition(Configuration.getAttribute("id"), id, Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("Configuration", c);
		Object[] ret = qValue.executeQuery();
		if (ret.length != 0) {
			return (Configuration)ret[0];
		}
		return null;
	}
	
	public static Configuration loadConfiguration(String idStr) {
		Long id = null;
		try
		{
			id = new Long(idStr);
		}
		catch (NumberFormatException nfe)
		{
			return null;
		}
		return Configuration.loadConfiguration(id);
	}
	
	public String toString() {
		return "Configuration with id=" + this.id + " " + this.entries;
	}

	/**
	 * Gets ID of config.
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets ID of config.
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}
}
