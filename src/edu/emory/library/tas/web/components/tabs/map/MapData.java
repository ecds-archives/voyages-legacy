package edu.emory.library.tas.web.components.tabs.map;

import edu.emory.library.tas.web.maps.PointOfInterest;

/**
 * Data of map.
 * The class keeps map items shown on the map.
 * It also keeps legend of map and tooltips.
 *  
 * @author Pawel Jurczyk
 *
 */
public class MapData {
	
	/**
	 * Items on map.
	 */
	private AbstractMapItem[] items = new AbstractMapItem[] {};
	
	/**
	 * Tooltips.
	 */
	private PointOfInterest[] points = new PointOfInterest[] {};
	
	/**
	 * Legend - keept as legend groups.
	 */
	private LegendItemsGroup[] legendItems = new LegendItemsGroup[] {};
	
	/**
	 * Constructs empty map data object.
	 *
	 */
	public MapData() {
	}
	
	/**
	 * Sets map data.
	 * This function performs translation from DB response to map items by calling appropriate data
	 * transformer.
	 * @param queryResult DB result
	 * @param min minimal value in data
	 * @param max max value in data
	 * @param transformer data  trasformer implementation that should be used
	 */
	public void setMapData(Object[] queryResult, double min, double max, AbstractDataTransformer transformer) {
		
		//Transform data
		TransformerResponse response = transformer.transformData(queryResult, min, max);
		
		//Set items
		this.items = response.getItems();
		
		//Set legend
		this.legendItems = response.getLegendItems();
		
		//Prepare tooltips
		this.points = new PointOfInterest[this.items.length];
		for (int i = 0; i < this.items.length; i++) {
			this.points[i] = this.items[i].getTooltipText();
		}
		
	}

	/**
	 * Gets items that will appear on map.
	 * @return
	 */
	public AbstractMapItem[] getItems() {
		return items;
	}
	
	/**
	 * Gets tooltips that will appear on map.
	 * @return
	 */
	public PointOfInterest[] getToolTip() {
		return points;
	}
	
	/**
	 * Gets legend of map.
	 * @return
	 */
	public LegendItemsGroup[] getLegend() {
		return this.legendItems;
	}
	
}
