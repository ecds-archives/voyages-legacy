package edu.emory.library.tas.util.query;

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
	private String value;
	
	/**
	 * Constructor.
	 * @param str direct value
	 */
	public DirectValue(String str) {
		this.value = str;
	}

	/**
	 * Getter for value.
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Setter for value.
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return this.value;
	}
}
