package edu.emory.library.tast.ui.images.site;

public class ImageInfo {
	private String name;
	private String value;
	public ImageInfo(String name, String val) {
		this.name = name;
		this.value = val;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
