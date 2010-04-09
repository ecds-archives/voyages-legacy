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

import edu.emory.library.tast.maps.component.PointOfInterest;

/**
 * Abstract item that will be shown in map. Specific object showed in maps should use this
 * class as superclass.
 * 
 * Class has two types of coordinates - x and y that are basic coordinates got from Db and
 * projx and projy that are coordinates after project to desired projection type.
 * 
 * Each map item has array of map item elements. Currently, only first map item element is used.
 * Implementation as array is left for future extension.
 * 
 * @author Pawel Jurczyk
 */
public abstract class AbstractMapItem {
	
	/**
	 * X coordinate of map item.
	 */
	private double x;
	
	/**
	 * Y coordinate of map item.
	 */
	private double y;
	
	/**
	 * Projected X coordinate of map item.
	 */
	private double projx;
	
	/**
	 * Projected Y coordinate of map item.
	 */
	private double projy;
	
	/**
	 * Label of map item.
	 */
	private String mainLabel;
	
	/**
	 * Images
	 */
	private String[] imageUrls;
	
	private String imagesQuery;
	
	/**
	 * Legend item corresponding to map item.
	 */
	private List legendItems = new ArrayList();
	
	private List elements = new ArrayList();
	
	/**
	 * Constructc map item.
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param mainLabel main label that will be shown on map
	 */
	public AbstractMapItem(double x, double y, String mainLabel) {
		this.x = x;
		this.y = y;
		this.mainLabel = mainLabel;
	}
	
	/**
	 * Sets projected coordinates.
	 * @param x projected x
	 * @param y projected y
	 */
	public void setProjXY(double x, double y) {
		this.projx = x;
		this.projy = y;
	}
	
	/**
	 * Gets map item elements. 
	 * @return array of MapItemElements 
	 */
	public MapItemElement[] getMapItemElements() {
		return (MapItemElement[])elements.toArray(new MapItemElement[] {});
	}
	
	/**
	 * Adds new map item element.
	 * @param element MapItemElement to add
	 */
	public void addMapItemElement(MapItemElement element) {
		this.elements.add(element);
	}
	
	/**
	 * Gets name of symbol that should represent given map item.
	 * This string will appear in map file, thus it should correspond
	 * to any SYMBOL name in map file. Otherwise, mapserver will throw
	 * exception.
	 * @return name of symbol
	 */
	public abstract String[] getSymbolNames();
	
	/**
	 * Gets list of symbols that will appear as pictures in legend of map.
	 * @return
	 */
	public abstract String[] getLegendSymbolNames();
	
	/**
	 * Gets tooltip of map item.
	 * @param legendItems 
	 * @return PointOfInterest that is used as tooltip by mapcomponent.
	 */
	public abstract PointOfInterest getTooltipText(LegendItemsGroup[] legendItems);

	/**
	 * Gets main label of item.
	 * @return main label
	 */
	public String getMainLabel() {
		return mainLabel;
	}

	/**
	 * Gets x coord.
	 * @return x coord
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets y coord.
	 * @return y coord
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Equals implementation.
	 * Compares coordinates of map item.
	 */
	public boolean equals(Object o) {
		if (!(o instanceof AbstractMapItem)) return false;
		AbstractMapItem that = (AbstractMapItem)o;
		boolean ret = this.x == that.x && this.y == that.y;
		return ret;
	}

	/**
	 * Gets projected x coord.
	 * @return projected x coord
	 */
	public double getProjectedX() {
		return projx;
	}

	/**
	 * Gets projected y coord.
	 * @return projected y coord
	 */
	public double getProjectedY() {
		return projy;
	}

	public boolean isPointEnabled() {
		boolean enabled = true;
		for (int i = 0; i < this.legendItems.size(); i++) {
			LegendItem item = (LegendItem)this.legendItems.get(i);
			enabled = enabled && item.isEnabled();
		}
		return enabled;
	}

	public void addLegendItem(LegendItem legendItem) {
		this.legendItems.add(legendItem);
	}

	public String[] getImageUrls()
	{
		return imageUrls;
	}

	public void setImageUrls(String[] imageUrls)
	{
		this.imageUrls = imageUrls;
	}

	public String getImagesQuery() {
		return imagesQuery;
	}

	public void setImagesQuery(String imagesQuery) {
		this.imagesQuery = imagesQuery;
	}
}
