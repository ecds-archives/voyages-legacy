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

import java.util.ArrayList;
import java.util.List;

import edu.emory.library.tast.TastResource;

/**
 * A group that appear in legend of map.
 * Single legend can have several groups. Each LegendItemsGroup can have
 * few LegendItems inside.
 * 
 * @author Pawel Jurczyk
 *
 */
public class LegendItemsGroup {
	
	/**
	 * Title of legend group
	 */
	private String title;
	
	/**
	 * Child legend items.
	 */
	private List items = new ArrayList();
	
	/**
	 * Constructs legend group with no child legend items.
	 * @param title titile of legend. If no title should be used - pass ""
	 */
	public LegendItemsGroup(String title) {
		this.title = title;
	}
	
	/**
	 * Adds child legend item.
	 * @param item
	 */
	public void addItemToGroup(LegendItem item) {
		this.items.add(item);
	}
	
	/**
	 * Gets list of child legend items.
	 * @return
	 */
	public LegendItem[] getItems() {
		return (LegendItem[])this.items.toArray(new LegendItem[] {});
	}

	/**
	 * Gets legend group title.
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets title of legend.
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Gets string representation of legend group object.
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(TastResource.getText("components_map_legendgroup") ).append(this.title).append("\n");
		LegendItem[] items = this.getItems();
		for (int i = 0; i < items.length; i++) {
			buffer.append("   ").append(items[i]).append("\n");
		}
		return buffer.toString();
	}
}
