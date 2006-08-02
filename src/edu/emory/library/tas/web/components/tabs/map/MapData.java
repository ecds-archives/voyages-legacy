package edu.emory.library.tas.web.components.tabs.map;

import edu.emory.library.tas.web.maps.PointOfInterest;

public class MapData {
	
	private AbstractMapItem[] items = new AbstractMapItem[] {};
	private PointOfInterest[] points = new PointOfInterest[] {};
	private LegendItemsGroup[] legendItems = new LegendItemsGroup[] {};
	
	public MapData() {
	}
	
	public void setMapData(Object[] queryResult, double min, double max, AbstractDataTransformer transformer) {
		TransformerResponse response = transformer.transformData(queryResult, min, max); 
		this.items = response.getItems();
		this.legendItems = response.getLegendItems();
		this.points = new PointOfInterest[this.items.length];
		for (int i = 0; i < this.items.length; i++) {
			this.points[i] = this.items[i].getTooltipText();
		}
		
		for (int i = 0; i < legendItems.length; i++) {
			System.out.println(legendItems[i]);
		}
	}

	public AbstractMapItem[] getItems() {
		return items;
	}
	
	public PointOfInterest[] getToolTip() {
		return points;
	}
	
	public LegendItemsGroup[] getLegend() {
		return this.legendItems;
	}
	
}
