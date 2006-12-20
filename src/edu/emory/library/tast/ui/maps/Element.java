package edu.emory.library.tast.ui.maps;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface;

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
	private VisibleAttributeInterface attribute;
	
	/**
	 * Value that will appear on map.
	 */
	private Comparable value;

	//size of dot
	private int size;

	private int color;
	
	/**
	 * Constructor.
	 * @param attribute Attribute that will appear on map
	 * @param value value associated to attribute. If null then only attribute name will be shown
	 */
	public Element(VisibleAttributeInterface attribute, Comparable value) {
		this.attribute = attribute;
		this.value = value;
	}

	/**
	 * Gets attribute of element.
	 * @return
	 */
	public VisibleAttributeInterface getAttribute() {
		return attribute;
	}

	/**
	 * Sets attribute of element.
	 * @param attribute
	 */
	public void setAttribute(VisibleAttributeInterface attribute) {
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

	public void setSize(int i) {
		this.size = i;
	}

	public int getSize() {
		return size;
	}

	public int getColor() {
		return this.color;
	}
	
	public void setColor(int i) {
		this.color = i;
	}
	
}
