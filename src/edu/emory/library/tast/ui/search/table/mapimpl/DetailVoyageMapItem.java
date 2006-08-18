package edu.emory.library.tast.ui.search.table.mapimpl;

import edu.emory.library.tast.ui.maps.AbstractMapItem;
import edu.emory.library.tast.ui.maps.Element;
import edu.emory.library.tast.ui.maps.MapItemElement;
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
	 * Color of map item.
	 */
	private int color;
	
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
	public String[] getSymbolNames() {
		String[] symbols = new String[this.getMapItemElements().length];
		for (int i = 0; i < symbols.length; i++) {
			MapItemElement element = this.getMapItemElements()[i];
			StringBuffer buffer = new StringBuffer();
			buffer.append("number-");
			buffer.append(this.color);
			buffer.append("-");
			buffer.append(element.getAttributes()[0]);
			if (this.getMapItemElements().length > 1) {
				buffer.append("-").append(i + 1);
			}
			symbols[i] = buffer.toString();
		}
		return symbols;
	}
	
	public String[] getLegendSymbolNames() {
		String[] symbols = new String[this.getMapItemElements().length];
		for (int i = 0; i < symbols.length; i++) {
			MapItemElement element = this.getMapItemElements()[i];
			StringBuffer buffer = new StringBuffer();
			buffer.append("number-");
			buffer.append(this.color);
			buffer.append("-");
			buffer.append(element.getAttributes()[0]);
			symbols[i] = buffer.toString();
		}
		return symbols;
	}

	/**
	 * Gets tooltip for item.
	 */
	public PointOfInterest getTooltipText() {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<b>").append("Location: ");
		buffer.append(this.getMainLabel()).append("</b><br/>");
		MapItemElement[] mapItemElements = this.getMapItemElements();
		for (int i = 0; i < mapItemElements.length; i++) {
			MapItemElement mapItemElement = mapItemElements[i];
			buffer.append(mapItemElement.getAttribute());
			Element[] elements = mapItemElement.getElements();
			for (int j = 1; j < elements.length; j++) {
				buffer.append("<br/> &nbsp&nbsp ");
				buffer.append(elements[j].getAttribute());
				buffer.append(": ").append(elements[j].getValue()).append("<br/>");
			}
		}
		
		PointOfInterest point = new PointOfInterest(this.getProjectedX(), this.getProjectedY());
		point.setText(buffer.toString());
		return point;
	}

	/**
	 * Gets legend for this item.
	 * @return legend String
	 */
	public String[] getLegendTexts() {
		String[] responses = new String[this.getMapItemElements().length];
		for (int i = 0; i < responses.length; i++) {
			responses[i] = this.getMapItemElements()[i].getLegendText();
		}
		return responses;
	}

}
