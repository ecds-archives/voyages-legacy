package edu.emory.library.tas.web.components.tabs.map;

public class LegendItem {
	private String imagePath;
	private String legendString;
	
	public LegendItem(String imageString, String legendString) {
		this.imagePath = imageString;
		this.legendString = legendString;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getLegendString() {
		return legendString;
	}

	public void setLegendString(String legendString) {
		this.legendString = legendString;
	}
	
	public String toString() {
		return this.legendString;
	}
}
