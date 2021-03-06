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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.Location;
import edu.emory.library.tast.dm.LocationWithImages;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.maps.AbstractDataTransformer;
import edu.emory.library.tast.maps.AbstractMapItem;
import edu.emory.library.tast.maps.AbstractTransformerQueryHolder;
import edu.emory.library.tast.maps.AttributesMap;
import edu.emory.library.tast.maps.Element;
import edu.emory.library.tast.maps.LegendItem;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapItemElement;
import edu.emory.library.tast.maps.TransformerResponse;

/**
 * Data transformer that transforms DB respnse for global map.
 * Expects data rows: [Dictionary - place] [Number - value] [Integer - color]
 * 
 * @author Pawe Jurczyk
 * 
 */
public class GlobalMapDataTransformer extends AbstractDataTransformer {

	/**
	 * Alowable max circle size.
	 */
	private static final int CIRCLE_RANGES = 5;

	/**
	 * Color of items having more information.
	 */
	private static int DOUBLE_COLOR = 5;
	
	MessageFormat valuesFormat = new MessageFormat("{0,number,#,###,###}");
	
	/**
	 * Constructs transformer.
	 * @param map Attributes mapping in data passed to transformData.
	 */
	public GlobalMapDataTransformer(AttributesMap map) {
		super(map);
	}
	
	/**
	 * Transforms data.
	 */
	public TransformerResponse transformData(Session session, AbstractTransformerQueryHolder data) {

		//Get items
		return this.getItems(session, data);
	}

	/**
	 * Parses DB result.
	 * @param data data from DB
	 * @param ranges ranges of circles size.
	 * @return
	 */
	private TransformerResponse getItems(Session session, AbstractTransformerQueryHolder holder) {

		Object[] data = holder.getRawQueryResponse();
		
		String mapAssetsBaseUrl = AppConfig.getConfiguration().getString(AppConfig.MAP_URL);
		
		List items = new ArrayList();

		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		
		//Parse each response row
		for (int i = 0; i < data.length; i++) {
			
			Object[] row = (Object[]) data[i];
			
			String[] imageUrls = new String[0];
			String queryImages = null;
			
			//Get port
			Location location = (Location) (row)[0];
			if (location instanceof LocationWithImages) {
					if (location instanceof Port) {
						queryImages = "port=";
					} else {
						queryImages = "region=";
					}
					queryImages += location.getId();
					
					// collect images (just the names so far)
					Set images =  ((LocationWithImages)location).getReadyToGoImages();
					imageUrls = new String[images.size()];
					int imageIndex = 0;
					for (Iterator iter = images.iterator(); iter.hasNext();)
					{
						Image image = (Image) iter.next();
						imageUrls[imageIndex++] = image.getFileName(); 
					}
				}
				// Get color
			int color = Integer.parseInt(row[2].toString());

			// Get zoom
			int showAtzoom = 0; //Integer.parseInt(((String) row[3]));

			// Get valaue
			Number value = (Number) ((Object[]) data[i])[1];
			if (location != null) {

				if (min > value.doubleValue()) {
					min = value.doubleValue();
				}
				if (max < value.doubleValue()) {
					max = value.doubleValue();
				}
				// System.out.println("Color---: " + color + gisPort.getX() + "
				// " + gisPort.getY());
				// Create test item
				GlobalMapDataItem testItem = new GlobalMapDataItem(location.getX(), location.getY(),
						location.getName(), color, i, imageUrls, queryImages);

				int index;

				// System.out.println("i=" + i + " Checking port: " + gisPort);

				// Check if test item is among map items that have already been
				// added
				if ((index = items.indexOf(testItem)) != -1) {
					// If so - add Element to existing item
					GlobalMapDataItem item = (GlobalMapDataItem) items.get(index);
					Element el = new Element(getAttribute(i, 1), new Double(value.doubleValue()));
					item.getMapItemElements()[0].addElement(el);
					el.setColor(color);
					el.setShowAtZoom(showAtzoom);
					item.setSymbolColor(DOUBLE_COLOR);
					// System.out.println("Equals to: " + item.getI());
				} else {
					// If no - add test item to map items
					MapItemElement itemElement = new MapItemElement(getAttribute(i, 0));
					Element el = new Element(getAttribute(i, 1), new Double(value.doubleValue()));
					el.setColor(color);
					el.setShowAtZoom(showAtzoom);
					itemElement.addElement(el);
					testItem.addMapItemElement(itemElement);
					// double [] projXY = gisPort.getXYProjected();
					testItem.setProjXY(location.getX(), location.getY());
					items.add(testItem);
				}
			}
		}

		double[] ranges = new double[CIRCLE_RANGES + 1];
		double step = (max - min) / CIRCLE_RANGES;
		for (int i = 0; i < CIRCLE_RANGES; i++) {
			ranges[i + 1] = i * step + step + min;
		}
		ranges[0] = min;
		
		this.round(ranges);
		
		//Set size of each of items created above
		for (Iterator iter = items.iterator(); iter.hasNext();) {
			GlobalMapDataItem dataItem = (GlobalMapDataItem) iter.next();

			//System.out.println(dataItem.getMapItemElements()[0].getMaxElement().getValue());
			
			//double size = ((Double) dataItem.getMapItemElements()[0].getMaxElement().getValue()).doubleValue();
			Element[] els = dataItem.getMapItemElements()[0].getElements();
			int maxindex = -1;
			for (int k = 0; k < els.length; k++) {
				Element el = els[k];
				double size = ((Double)el.getValue()).doubleValue();
				int index = CIRCLE_RANGES - 1; 
				for (int j = 0; j < CIRCLE_RANGES; j++) {
					if (size >= ranges[j] && size < ranges[j + 1]) {
						index = j;
					}
				}
				el.setSize(index + 1);
				if (index > maxindex) {
					maxindex = index;
				}
			}
			dataItem.setSymbolSize(maxindex);
		}

		//Prepare legend
		//Here legend has 2 groups - sizes and place colors
		LegendItemsGroup legendSizes = new LegendItemsGroup("Number of slaves");
		LegendItemsGroup legendColors = new LegendItemsGroup("Places");
		
		//Prepare legend about size of dots
		for (int i = CIRCLE_RANGES - 1; i >= 0; i--) {
			long to = Math.round(ranges[i+1]);
			long from = Math.round(ranges[i]);
			from++;
			LegendItem item = new LegendItem("circle.*-" + (i + 1) + "$", mapAssetsBaseUrl + "/symbols/circle-1-" + (i + 1) + ".png", 
					"" + valuesFormat.format(new Object[] {new Long(from)}) + " - " + valuesFormat.format(new Object[] {new Long(to)}));
			legendSizes.addItemToGroup(item);
		}
		
		///Prepare legend about colors
		LegendItem emb = new LegendItem("circle-2-\\d", mapAssetsBaseUrl + "/symbols/circle-" + 2 + "-4.png", TastResource.getText("estimates_map_leg_embplace"));
		LegendItem disemb = new LegendItem("circle-3-\\d", mapAssetsBaseUrl + "/symbols/circle-" + 3 + "-4.png", TastResource.getText("estimates_map_leg_disembplace"));
		LegendItem both = new LegendItem("circle-5-\\d", mapAssetsBaseUrl + "/symbols/circle-" + 5 + "-4.png", "Place of embarkatrion / disembarkation");
		legendColors.addItemToGroup(emb);
		legendColors.addItemToGroup(disemb);
		//legendColors.addItemToGroup(both);
		
		int i = 0; 
		for (Iterator iter = items.iterator(); iter.hasNext(); i++) {
			GlobalMapDataItem element = (GlobalMapDataItem) iter.next();
			element.addLegendItem(legendSizes.getItems()[legendSizes.getItems().length - element.getSymbolSize() - 1]);
			if (element.getSymbolColor() == 2) {
				element.addLegendItem(emb);
			} else if (element.getSymbolColor() == 3) {
				element.addLegendItem(disemb);
			} else {
				element.addLegendItem(both);
			}
		}
		
		if (data.length == 0) {
			return new TransformerResponse(new AbstractMapItem[] {}, 
					new LegendItemsGroup[] {});
		}
		
		//Return result response
		return new TransformerResponse((AbstractMapItem[]) items.toArray(new AbstractMapItem[] {}), 
										new LegendItemsGroup[] {legendSizes, legendColors});
	}


}
