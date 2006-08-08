package edu.emory.library.tast.ui.maps;

public class MapLayer {
	
	private String key;
	private String label;
	private boolean enabled;
	
	public MapLayer(String key, String label) {
		this.key = key;
		this.label = label;
		this.enabled = true;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getKey() {
		return key;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
