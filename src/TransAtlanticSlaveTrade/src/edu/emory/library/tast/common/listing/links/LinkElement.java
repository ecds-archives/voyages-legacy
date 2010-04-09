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
package edu.emory.library.tast.common.listing.links;

import java.io.Serializable;

/**
 * This class represents links available in pager.
 * Pager is component which is visible below the table
 * and allows switching between result sets.
 *
 */
public class LinkElement implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String lablel;
	private String classStyle;
	private boolean clickable;
	private int firstVisible;
	private boolean selectedNumber = false;
	
	public LinkElement(int id, String label, boolean clickable, int firstVisible, String classStyle) {
		this.id = id;
		this.lablel = label;
		this.clickable = clickable;
		this.firstVisible = firstVisible;
		this.classStyle = classStyle;
	}
	
	public LinkElement(int id, String label, boolean clickable, boolean selectedNumber, int firstVisible, String classStyle) {
		this.id = id;
		this.lablel = label;
		this.clickable = clickable;
		this.firstVisible = firstVisible;
		this.selectedNumber = selectedNumber;
		this.classStyle = classStyle;
	}
	
	public int getId() {
		return this.id;
	}

	/**
	 * Label of link (1, 2, next, prev)
	 * @return
	 */
	public String getLabel() {
		return this.lablel;
	}

	/**
	 * True if one can click on link.
	 * @return
	 */
	public boolean isClickable() {
		return this.clickable;
	}

	/**
	 * Number of first visible row after clicking this link 
	 * @return
	 */
	public int getFirstVisible() {
		return firstVisible;
	}
	
	/**
	 * Tells whether this link is selected
	 * @return
	 */
	public boolean isSelectedNumber() {
		return this.selectedNumber ;
	}

	/**
	 * Class (css style) of this link
	 * @return
	 */
	public String getClassStyle() {
		return classStyle;
	}
}
