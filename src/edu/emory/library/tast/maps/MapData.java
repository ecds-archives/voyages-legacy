package edu.emory.library.tast.maps;

import java.util.ArrayList;
import java.util.List;

import edu.emory.library.tast.maps.component.PointOfInterest;

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
	public void setMapData(AbstractTransformerQueryHolder data, AbstractDataTransformer transformer) {
		
		//Transform data
		TransformerResponse response = transformer.transformData(data);
		
		//Set items
		this.items = response.getItems();
		
		//Set legend
		this.legendItems = response.getLegendItems();
		
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
		
		//Prepare tooltips
		List localPoints = new ArrayList();
		for (int i = 0; i < this.items.length; i++) {
			if (this.items[i].isPointEnabled()) {
				PointOfInterest pt = this.items[i].getTooltipText(legendItems);
				if (pt != null) {
					localPoints.add(pt);
				}
			}
		}			
		this.points = (PointOfInterest[])localPoints.toArray(new PointOfInterest[] {});
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
