package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class SourceInformation {
	private String id;
	private String information;
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new StringAttribute("id", null));
		attributes.put("information", new StringAttribute("information", null));
	}
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}
	
	public static SourceInformation loadById(Session session, String id) {
		Conditions c = new Conditions();
		c.addCondition(getAttribute("id"), id, Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("SourceInformation", c);
		Object[] response = qValue.executeQuery(session);
		if (response.length == 0) {
			return null;
		} else {
			return (SourceInformation) response[0];
		}
	}
	
	public String getInformation() {
		return information;
	}
	
	public void setInformation(String description) {
		this.information = description;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
}
