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
	 * Modified flag.
	 */
	public static final int UPDATED = 1;
	
	/**
	 * Unmodified flag.
	 */
	public static final int NOT_UPDATED = 0;

	/**
	 * Field informing whether object was modified/unmodified.
	 */
	protected int modified = NOT_UPDATED;

	/**
	 * Current attribute values.
	 */
	protected Map values = new HashMap();
	
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
		if ((p_attrValue == null && this.values.get(p_attrName) != null) 
				|| (p_attrValue != null && !p_attrValue.equals(this.values.get(p_attrName)))) {
				this.modified = UPDATED;
			}
		values.put(p_attrName, p_attrValue);
	}

	/**
	 * Gets values of all attributes.
	 * @return Map of values
	 */
	public Map getAllAttrValues() {
		return values;
	}
}
