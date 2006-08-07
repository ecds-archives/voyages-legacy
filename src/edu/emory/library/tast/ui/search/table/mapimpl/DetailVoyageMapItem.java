package edu.emory.library.tast.ui.search.table.mapimpl;

import edu.emory.library.tast.ui.maps.AbstractMapItem;
import edu.emory.library.tast.ui.maps.Element;
import edu.emory.library.tast.ui.maps.component.PointOfInterest;

/**
 * Map item that is shown on detail voyage map.
 * This type of map items use numbered circles as symbols on map.
 * 
 * @author Pawel Jurczyk
 */
public class DetailVoyageMapItem extends AbstractMapItem {

	/**
	 * Symbol prefix.
	 */
	public static final String SYMBOL_NAME_PREFIX = "number-";
	
	/**
	 * Symbol name that will be used in map file.
	 */
	private String symbolName;
	
	/**
	 * Color of map item.
	 */
	private int color;
	
	/**
	 * Number on map item.
	 */
	private int number;
	
	/**
	 * Creates new map item.
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param mainLabel label of map item
	 * @param color color of map item
	 */
	public DetailVoyageMapItem(double x, double y, String mainLabel, int color) {
		super(x, y, mainLabel);
		this.color = color;
	}

	/**
	 * Gets name of symbol that should be used by this map item.
	 */
	public String getSymbolName() {
		return symbolName;
	}

	/**
	 * Gets tooltip for item.
	 */
	public PointOfInterest getTooltipText() {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<b>").append("Location: ");
		buffer.append(this.getMainLabel()).append("</b><br/>");
		Element[] elements = this.getMapItemElements()[0].getElements();
		boolean first = true;
		for (int i = 0; i < elements.length; i++) {
			if (!first) {
				buffer.append(", ");
			}
			Element element = elements[i];
			buffer.append(element.getAttribute());
			first = false;
		}
		
		PointOfInterest point = new PointOfInterest(this.getProjectedX(), this.getProjectedY());
		point.setText(buffer.toString());
		return point;
	}
	
	/**
	 * Sets number that will appear on map symbol.
	 * @param number
	 */
	public void setNumber(int number) {
		
		//Set number
		this.number = number;
		
		//Prepare symbol name
		StringBuffer buffer = new StringBuffer();
		buffer.append(SYMBOL_NAME_PREFIX);
		buffer.append(color).append("-");
		buffer.append(this.number);
		this.symbolName = buffer.toString();
	}

	/**
	 * Gets legend for this item.
	 * @return legend String
	 */
	public String getLegendText() {
		StringBuffer buffer = new StringBuffer();
		Element[] elements = this.getMapItemElements()[0].getElements();
		boolean first = true;
		for (int i = 0; i < elements.length; i++) {
			if (!first) {
				buffer.append(", ");
			}
			Element element = elements[i];
			buffer.append(element.getAttribute());
			first = false;
		}
		return buffer.toString();
	}

}
