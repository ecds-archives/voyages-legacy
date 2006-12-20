package edu.emory.library.tast.ui.maps;

import java.util.ArrayList;
import java.util.List;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface;

/**
 * Map item element class that represent one of elements of map item.
 * Map item elements can have several subelements - Element objects.
 * 
 * @author Pawel Jurczyk
 *
 */
public class MapItemElement {
	
	/**
	 * Main attribute of map element items.
	 */
	private VisibleAttributeInterface attribute;
	
	private Object[] attributes;
	
	/**
	 * Subelements.
	 */
	private List elements = new ArrayList();
	
	/**
	 * Element with maximal value.
	 */
	private Element max = null;
	
	/**
	 * Constructs map item element.
	 * @param attribute main attribute of map item element
	 */
	public MapItemElement(VisibleAttributeInterface attribute) {
		this(attribute, null);
	}
	
	/**
	 * Constructs map item element.
	 * @param attribute main attribute of map item element
	 */
	public MapItemElement(VisibleAttributeInterface attribute, Object[] attributes) {
		this.attribute = attribute;
		this.attributes = attributes;
	}
	
	/**
	 * Adds subelement.
	 * @param element Element to add
	 */
	public void addElement(Element element) {
		
		//Add element
		this.elements.add(element);
		
		//Update max if needed
		if (this.max == null || (this.max.getValue() != null && this.max.getValue().compareTo(element.getValue()) == -1)) {
			this.max = element;
		}
	}
	
	/**
	 * Gets subelements of map item element
	 * @return array of Elements
	 */
	public Element[] getElements() {
		return (Element[])this.elements.toArray(new Element[] {});
	}
	
	/**
	 * Gets maximal Element.
	 * @return
	 */
	public Element getMaxElement() {
		return max;
	}

	/**
	 * Gets main Attribute.
	 * @return
	 */
	public VisibleAttributeInterface getAttribute() {
		return attribute;
	}

	public Object[] getAttributes() {
		return attributes;
	}
	
	
	public String getLegendText() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getAttribute());
		return buffer.toString();
	}

}
