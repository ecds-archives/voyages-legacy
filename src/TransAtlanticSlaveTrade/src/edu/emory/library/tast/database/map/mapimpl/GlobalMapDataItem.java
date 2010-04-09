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
package edu.emory.library.tast.database.map.mapimpl;

import java.text.MessageFormat;

import javax.faces.context.FacesContext;

import edu.emory.library.tast.images.ThumbnailServlet;
import edu.emory.library.tast.maps.AbstractMapItem;
import edu.emory.library.tast.maps.Element;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.component.PointOfInterest;

/**
 * Item that is shown on global map. This type of map items use circles of
 * different size as symbols on map.
 * 
 * @author Pawel Jurczyk
 * 
 */
public class GlobalMapDataItem extends AbstractMapItem {

	private static final int THUMB_WIDTH = 120;
	private static final int THUMB_HEIGHT = 120;
	private static final int THUMB_MAX_COUNT = 3;

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

	private int i;

	MessageFormat valuesFormat = new MessageFormat("{0,number,#,###,###}");

	/**
	 * Data item constructor.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param mainLabel
	 *            label
	 * @param color
	 *            color
	 */
	public GlobalMapDataItem(double x, double y, String mainLabel, int color, int i, String[] imageUrls, String imagesQuery)
	{
		super(x, y, mainLabel);
		super.setImageUrls(imageUrls);
		super.setImagesQuery(imagesQuery);
		this.color = color;
		this.i = i;
	}

	public int getI() {
		return i;
	}

	/**
	 * Sets symbol color.
	 * 
	 * @param color
	 */
	public void setSymbolColor(int color) {
		this.color = color;
	}

	/**
	 * Gets symbol name.
	 */
	public String[] getSymbolNames() {
		return new String[] { symbolName };
	}

	/**
	 * Gets symbol names visible in legend.
	 */
	public String[] getLegendSymbolNames() {
		return new String[] { symbolName };
	}

	/**
	 * Sets symbol size.
	 * 
	 * @param size
	 */
	public void setSymbolSize(int size) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(SYMBOL_NAME_PREFIX).append(this.color);
		buffer.append("-").append(size + 1);
		// System.out.println("Symbol: " + buffer);
		this.symbolName = buffer.toString();
		this.size = size;
	}

	/**
	 * Gets tooltip text of map item.
	 */
	public PointOfInterest getTooltipText(LegendItemsGroup[] legend)
	{
		
		LegendItemsGroup types = legend[1];
		LegendItemsGroup sizes = legend[0];
		
		PointOfInterest point = new PointOfInterest(this.getProjectedY(), this .getProjectedX());
		
		point.setText(this.buildToolTipInfo());
		Element[] elements = this.getMapItemElements()[0].getElements();
		
		if (elements.length > 1) {
			if (!types.getItems()[0].isEnabled()
					|| !types.getItems()[1].isEnabled()) {
				if (types.getItems()[0].isEnabled()) {
					if (!sizes.getItems()[5 - elements[0].getSize()]
							.isEnabled()) {
						return null;
					}
					point.setSymbols(new String[] { SYMBOL_NAME_PREFIX
							+ elements[0].getColor() + "-"
							+ elements[0].getSize() });
				} else if (types.getItems()[1].isEnabled()) {
					if (!sizes.getItems()[5 - elements[1].getSize()]
							.isEnabled()) {
						return null;
					}
					point.setSymbols(new String[] { SYMBOL_NAME_PREFIX
							+ elements[1].getColor() + "-"
							+ elements[1].getSize() });
				} else {
					return null;
				}
			} else {
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
			}
		} else {
			point.setSymbols(new String[] { SYMBOL_NAME_PREFIX + elements[0].getColor() + "-" + elements[0].getSize() });
			point.setShowAtZoom(elements[0].getShowAtZoom());
		}
		
		point.setLabel(this.getMainLabel());

		return point;
	}

	/**
	 * Builds tooltip location. It builds HTML that includes descriptions, images, links etc.
	 */
	private String buildToolTipInfo() {
		
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		
		StringBuffer buffer = new StringBuffer();

		buffer.append("<div style=\"white-space: nowrap\">");

		buffer.append("<div><b>");
		buffer.append(this.getMainLabel());
		buffer.append("</b></div>");

		Element[] elements = this.getMapItemElements()[0].getElements();
		for (int i = 0; i < elements.length; i++) {
			Element element = elements[i];
			buffer.append(element.getAttribute().getUserLabelOrName()).append(": ");
			buffer.append(
					valuesFormat
							.format(new Object[] { new Long(Math
									.round(((Number) element.getValue())
											.doubleValue())) }))
					.append("<br/>");
		}
		
		//handle images
		String[] imageUrl = getImageUrls();
		if (imageUrl != null && imageUrl.length != 0)
		{
			buffer.append("<div class=\"map-bubble-images\">");
			buffer.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr>");
			int thumbsCount = Math.min(THUMB_MAX_COUNT, imageUrl.length);
			boolean[] used = new boolean[thumbsCount];
			for (int i = 0; i < used.length; i++) {
				used[i] = false;
			}
			for (int i = 0; i < thumbsCount; i++)
			{
				int index = i;
				if (thumbsCount != imageUrl.length) {
					while (true) {
						double rand = Math.random();
						if (rand == 1) continue;
						int nindex = (int)(rand * (double)thumbsCount);
						if (!used[nindex]) {
							used[nindex] = true;
							index = nindex;
							break;
						}
					}
				}
				buffer.append("<td ");
				buffer.append("class=\"").append(i < thumbsCount - 1 ? "map-bubble-image" : "map-bubble-image-last").append("\">");
				buffer.append("<img src=\"");
				ThumbnailServlet.appendThumbnailUrl(buffer, contextPath, imageUrl[index], THUMB_WIDTH, THUMB_HEIGHT);
				buffer.append("\" ");
				buffer.append("width=\"").append(THUMB_WIDTH).append("\" ");
				buffer.append("height=\"").append(THUMB_HEIGHT).append("\" ");
				buffer.append("border=\"0\">");
				buffer.append("</td>");
			}
			buffer.append("</tr></table>");
			//see all link
			buffer.append("<a href=\"/tast/resources/images-query.faces?" + this.getImagesQuery() + "\"/>See all images ></a>");
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