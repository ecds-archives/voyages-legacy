/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.estimates.map.mapimpl;

import java.text.MessageFormat;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.maps.AbstractMapItem;
import edu.emory.library.tast.maps.Element;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.component.PointOfInterest;

public class EstimateMapDataItem extends AbstractMapItem {

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

	MessageFormat valuesFormat = new MessageFormat("{0,number,#,###,###}");

	public EstimateMapDataItem(double x, double y, String mainLabel) {
		super(x, y, mainLabel);
	}

	public String[] getLegendSymbolNames() {
		return new String[] { this.symbolName };
	}

	public String[] getSymbolNames() {
		return new String[] { this.symbolName };
	}

	/**
	 * Gets point of interest for this map data item.
	 */
	public PointOfInterest getTooltipText(LegendItemsGroup[] legend) {

		PointOfInterest point = new PointOfInterest(this.getY(), this.getX());
		point.setText(this.buildToolTipInfo());
		Element[] elements = this.getMapItemElements()[0].getElements();
		if (elements.length > 1) {
			if (elements[0].getSize() < elements[1].getSize()) {
				point.setSymbols(new String[] {
						SYMBOL_NAME_PREFIX + elements[1].getColor() + "-"
								+ elements[1].getSize(),
						SYMBOL_NAME_PREFIX + elements[0].getColor() + "-"
								+ elements[0].getSize() });
					point.setShowAtZoom(elements[1].getShowAtZoom());
			} else {
				point.setSymbols(new String[] {
						SYMBOL_NAME_PREFIX + elements[0].getColor() + "-"
								+ elements[0].getSize(),
						SYMBOL_NAME_PREFIX + elements[1].getColor() + "-"
								+ elements[1].getSize() });
				point.setShowAtZoom(elements[0].getShowAtZoom());

			}
		} else {
			point.setSymbols(new String[] { SYMBOL_NAME_PREFIX
					+ elements[0].getColor() + "-" + elements[0].getSize() });
			point.setShowAtZoom(elements[0].getShowAtZoom());

		}
		point.setLabel(this.getMainLabel());

		return point;

	}

	/**
	 * Gets contents of mouse-over box.
	 * @return
	 */
	private String buildToolTipInfo() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("<div style=\"white-space: nowrap\">");
		buffer.append("<b>");
		buffer.append(TastResource.getText("estimates_map_location") + " ").append(this.getMainLabel());
		buffer.append("</b><br/>");

		Element[] elements = this.getMapItemElements()[0].getElements();
		for (int i = 0; i < elements.length; i++) {
			Element element = elements[i];
			buffer.append(element.getAttribute().getUserLabelOrName()).append(
					": ");
			buffer.append(
					valuesFormat
							.format(new Object[] { new Long(Math
									.round(((Number) element.getValue())
											.doubleValue())) }))
					.append("<br/>");
		}
		buffer.append("</div>");

		return buffer.toString();

	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
		StringBuffer buf = new StringBuffer();
		buf.append(SYMBOL_NAME_PREFIX);
		buf.append(this.color);
		buf.append("-").append(this.size);
		this.symbolName = buf.toString();
	}

}
