package edu.emory.library.tas.web.components.tabs.map;

import edu.emory.library.tas.web.maps.PointOfInterest;

public class MapData {
	
	private AbstractMapItem[] items = new AbstractMapItem[] {};
	private PointOfInterest[] points = new PointOfInterest[] {};
	
	public MapData() {
	}
	
	public void setMapData(Object[] queryResult, double min, double max, AbstractDataTransformer transformer) {
		this.items = transformer.transformData(queryResult, min, max);
		this.points = new PointOfInterest[this.items.length];
		for (int i = 0; i < this.items.length; i++) {
			this.points[i] = this.items[i].getTooltipText();
		}
	}

	public AbstractMapItem[] getItems() {
		return items;
	}
	
	public PointOfInterest[] getToolTip() {
		return points;
	}
	
	public LegendItem[] getLegend() {
		return null;
	}
	
}
