package edu.emory.library.tas.web.components.tabs.map;

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
	
	public LegendItem(String imageString, String legendString) {
		this.imagePath = imageString;
		this.legendString = legendString;
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
}
