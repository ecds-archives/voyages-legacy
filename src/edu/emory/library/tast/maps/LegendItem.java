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
