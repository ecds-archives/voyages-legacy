package edu.emory.library.tas.web.components.tabs.map;

import edu.emory.library.tas.attrGroups.Attribute;

/**
 * Single element in map.
 * Can be understood as Attribute that is shown in map.
 * 
 * @author Pawel Jurczyk
 *
 */
public class Element {
	
	/**
	 * Attribute of element
	 */
	private Attribute attribute;
	
	/**
	 * Value that will appear on map.
	 */
	private Comparable value;
	
	/**
	 * Constructor.
	 * @param attribute Attribute that will appear on map
	 * @param value value associated to attribute. If null then only attribute name will be shown
	 */
	public Element(Attribute attribute, Comparable value) {
		this.attribute = attribute;
		this.value = value;
	}

	/**
	 * Gets attribute of element.
	 * @return
	 */
	public Attribute getAttribute() {
		return attribute;
	}

	/**
	 * Sets attribute of element.
	 * @param attribute
	 */
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	/**
	 * Gets value of element.
	 * @return
	 */
	public Comparable getValue() {
		return value;
	}

	/**
	 * Sets value of element
	 * @param value
	 */
	public void setValue(Comparable value) {
		this.value = value;
	}
	
}
