package edu.emory.library.tast.dm;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.HibernateConnector;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * Class that is used to store any configuration of any component.
 * This class is managed by Hibernate.
 * @author Pawel Jurczyk 
 *
 */
public class Configuration {
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new StringAttribute("id", "Configuration"));
		attributes.put("creationDate", new NumericAttribute("creationDate", "Configuration"));
		attributes.put("entries", new NumericAttribute("map_entries", "Configuration"));
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
	public void addEntry(String entry, Object value) {
		this.entries.put(entry, value);
	}
	
	/**
	 * Gets value stored under given key.
	 * @param entry
	 * @return
	 */
	public Object getEntry(String entry) {
		return this.entries.get(entry);
	}
	
	/**
	 * Gets all entries.
	 * @return serializable object (HashMap)
	 */
	public Serializable getEntries() {
		return this.entries;
	}
	
	/**
	 * Sets serializable object entries.
	 * @param entries HahMap instance
	 */
	public void setEntries(Serializable entries) {
		this.entries = (HashMap)entries; 
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
