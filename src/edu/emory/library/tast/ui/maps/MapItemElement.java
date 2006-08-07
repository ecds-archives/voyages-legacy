package edu.emory.library.tast.ui.maps;

import java.util.ArrayList;
import java.util.List;

import edu.emory.library.tast.dm.attributes.Attribute;

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
	private Attribute attribute;
	
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
	public MapItemElement(Attribute attribute) {
		this.attribute = attribute;
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
	public Attribute getAttribute() {
		return attribute;
	}
	
}
