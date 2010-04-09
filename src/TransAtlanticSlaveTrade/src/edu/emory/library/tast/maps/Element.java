/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.maps;

import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;

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

	private int showAtZoom;

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

	public int getShowAtZoom()
	{
		return showAtZoom;
	}

	public void setShowAtZoom(int showAtZoom)
	{
		this.showAtZoom = showAtZoom;
	}
	
}
