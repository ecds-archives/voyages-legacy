package edu.emory.library.tast.db;

import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;

/**
 * Class used to provide to Conditions direct value. 
 * The value of given attribute will be set directly to set String value (w/o using :param notation).
 * @author Pawel Jurczyk
 *
 */
public class DirectValue {
	
	/**
	 * Direct value.
	 */
	private Attribute value;
	
	/**
	 * Constructor.
	 * @param str direct value
	 */
	public DirectValue(Attribute str) {
		this.value = str;
	}

	/**
	 * Getter for value.
	 * @return
	 */
	public Attribute getValue() {
		return value;
	}

	/**
	 * Setter for value.
	 * @param value
	 */
	public void setValue(Attribute value) {
		this.value = value;
	}
	
	public String toString(Map bindings) {
		return this.value.getHQLSelectPath(bindings);
	}
}
