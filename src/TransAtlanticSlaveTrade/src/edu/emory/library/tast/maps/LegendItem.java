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

/**
 * Single item in legend of map.
 * Legend item has path to image representing symbol on map and
 * string that will appear as the description of given symbol.
 * Used in LegendItemsGroup.
 * 
 * @author Pawel Jurczyk
 *
 */
public class LegendItem {
	
	/**
	 * Path to image.
	 * Used in html as img source. 
	 */
	private String imagePath;
	
	/**
	 * String that is description of image above.
	 */
	private String legendString;
	
	private String imageId;
	
	/**
	  *Information if elements of this legend item will appear in ma
	  */
	private boolean enabled;
	
	public LegendItem(String imageId, String imageString, String legendString) {
		this.imageId = imageId;
		this.imagePath = imageString;
		this.legendString = legendString;
		this.enabled = true;
	}

	/**
	 * Gets image path.
	 * @return
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * Sets image path.
	 * @param imagePath
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * Gets description.
	 */
	public String getLegendString() {
		return legendString;
	}

	/**
	 * Sets description.
	 * @param legendString
	 */
	public void setLegendString(String legendString) {
		this.legendString = legendString;
	}
	
	/**
	 * Returns String representation of legend.
	 */
	public String toString() {
		return this.legendString;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getImageId() {
		return imageId;
	}
}
