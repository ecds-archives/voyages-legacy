package edu.emory.library.tas;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class that provides basic methods for storing properties in object specific classes.
 * @author Pawel Jurczyk
 *
 */
public abstract class AbstractDescriptiveObject {
	
	/**
	 * Current attribute values.
	 */
	protected Map values = new HashMap();
	
	/**
	 * Types of attributes.
	 */
	protected static Map types = new HashMap();
	
	/**
	 * User labels of attributes.
	 */
	protected static Map userLabels = new HashMap();
	
	/**
	 * Gets value of given attribute
	 * @param p_attrName attribut name
	 * @return Object representing current attribute value
	 */
	public Object getAttrValue(String p_attrName) {
		return values.get(p_attrName);
	}

	/**
	 * Sets value of given attribute.
	 * @param p_attrName	attribut name
	 * @param p_attrValue	new attribute value
	 */
	public void setAttrValue(String p_attrName, Object p_attrValue) {
		values.put(p_attrName, p_attrValue);
	}

	/**
	 * Gets type of given attribute
	 * @param p_attreName	attribute name
	 * @return	String representing type of attribute
	 */
	public static String getAttrType(String p_attreName) {
		return (String)types.get(p_attreName);
	}

	/**
	 * Gets user label of given attribute.
	 * @param p_attrName	attribute name
	 * @return	user label of attribute
	 */
	public static String getUserLabel(String p_attrName) {
		return (String)userLabels.get(p_attrName);
	}

	/**
	 * Gets a list of all valid attributes for object.
	 * @return String[] with attribute names
	 */
	public static String[] getAllAttrNames() {
		return (String[])types.keySet().toArray(new String[] {});
	}

	/**
	 * Gets values of all attributes.
	 * @return Map of values
	 */
	public Map getAllAttrValues() {
		return values;
	}
}
