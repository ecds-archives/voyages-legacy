package edu.emory.library.tas;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.emory.library.tas.util.HibernateConnector;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;

public class Configuration {
	
	private Long id;
	private Date creationDate;
	private HashMap entries = new HashMap();
	
	public void addEntry(String entry, Object value) {
		this.entries.put(entry, value);
	}
	
	public Object getEntry(String entry) {
		return this.entries.get(entry);
	}
	
	public Serializable getEntries() {
		return this.entries;
	}
	
	public void setEntries(Serializable entries) {
		this.entries = (HashMap)entries; 
	}
	
	public void save() {
		HibernateConnector.getConnector().saveObject(this);
	}
	
	public void saveOrUpdate() {
		HibernateConnector.getConnector().saveOrUpdateObject(this);
	}
	
	public void delete() {
		HibernateConnector.getConnector().deleteObject(this);
	}
	
	public static Configuration loadConfiguration(Long id) {
		Conditions c = new Conditions();
		c.addCondition("id", id, Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("Configuration", c);
		Object[] ret = qValue.executeQuery();
		if (ret.length != 0) {
			return (Configuration)ret[0];
		}
		return null;
	}
	
	public String toString() {
		return "Configuration with id=" + this.id + " " + this.entries;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
