package edu.emory.library.tast.ui.search.map.mapimpl;

import edu.emory.library.tast.ui.maps.AbstractMapItem;
import edu.emory.library.tast.ui.maps.Element;
import edu.emory.library.tast.ui.maps.component.PointOfInterest;

/**
 * Item that is shown on global map.
 * This type of map items use circles of different size as symbols on map.
 * 
 * @author Pawel Jurczyk
 *
 */
public class GlobalMapDataItem extends AbstractMapItem {

	/**
	 * Used symbol prefix.
	 */
	public static final String SYMBOL_NAME_PREFIX = "circle-";
	
	/**
	 * Name of symbol.
	 */
	private String symbolName;
	
	/**
	 * Used color.
	 */
	private int color;

	private int size;
	
	/**
	 * Data item constructor.
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param mainLabel label 
	 * @param color color
	 */
	public GlobalMapDataItem(double x, double y, String mainLabel, int color) {
		super(x, y, mainLabel);
		this.color = color;
	}
	
	/**
	 * Sets symbol color.
	 * @param color
	 */
	public void setSymbolColor(int color) {
		this.color = color;
	}

	/**
	 * Gets symbol name.
	 */
	public String getSymbolName() {
		return symbolName;
	}
	
	/**
	 * Sets symbol size. 
	 * @param size
	 */
	public void setSymbolSize(int size) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(SYMBOL_NAME_PREFIX).append(this.color);
		buffer.append("-").append(size + 1);
		this.symbolName =  buffer.toString();
		this.size = size;
	}

	/**
	 * Gets tooltip text of map item.
	 */
	public PointOfInterest getTooltipText() {
		PointOfInterest point = new PointOfInterest(this.getProjectedX(), this.getProjectedY());
		point.setText(this.buildToolTipInfo());
		return point;
	}
	
	/**
	 * Builds tooltip location.
	 */
	private String buildToolTipInfo() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("<div style=\"white-space: nowrap\">");
		buffer.append("<b>");
		buffer.append("Location : ").append(this.getMainLabel());
		buffer.append("</b><br/>");

		Element[] elements = this.getMapItemElements()[0].getElements();
		for (int i = 0; i < elements.length; i++) {
			Element element = elements[i];
			buffer.append(element.getAttribute().getUserLabelOrName()).append(": ");
			buffer.append(element.getValue()).append("<br/>");
		}
		buffer.append("</div>");

		return buffer.toString();
	}

	public int getSymbolColor() {
		return this.color;
	}

	public int getSymbolSize() {
		return this.size; 
	}


}
