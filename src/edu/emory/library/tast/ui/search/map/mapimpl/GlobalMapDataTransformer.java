package edu.emory.library.tast.ui.search.map.mapimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.emory.library.tast.dm.GISPortLocation;
import edu.emory.library.tast.ui.maps.AbstractDataTransformer;
import edu.emory.library.tast.ui.maps.AbstractMapItem;
import edu.emory.library.tast.ui.maps.AttributesMap;
import edu.emory.library.tast.ui.maps.Element;
import edu.emory.library.tast.ui.maps.LegendItem;
import edu.emory.library.tast.ui.maps.LegendItemsGroup;
import edu.emory.library.tast.ui.maps.MapItemElement;
import edu.emory.library.tast.ui.maps.TransformerResponse;
import edu.emory.library.tast.util.query.QueryValue;

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
	public TransformerResponse transformData(Object[] data) {

//		//Prepare ranges of circles size
//		double[] rangeBoundries = new double[CIRCLE_RANGES + 1];
//		double step = (max - min) / CIRCLE_RANGES;
//		for (int i = 0; i < CIRCLE_RANGES; i++) {
//			rangeBoundries[i + 1] = i * step + step + min;
//		}
//		rangeBoundries[0] = min;

		//Get items
		return this.getItems(data);
	}

	/**
	 * Parses DB result.
	 * @param data data from DB
	 * @param ranges ranges of circles size.
	 * @return
	 */
	private TransformerResponse getItems(Object[] data) {

		List items = new ArrayList();

		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		
		//Parse each response row
		for (int i = 0; i < data.length; i++) {
			//Get port
			GISPortLocation gisPort = GISPortLocation.getGISPortLocation((String) ((Object[]) data[i])[0]);
			
			//Get color
			int color = ((Integer) ((Object[]) data[i])[2]).intValue();
			
			//Get valaue
			Number value = (Number) ((Object[]) data[i])[1];
			if (gisPort != null) {
				
				if (min > value.doubleValue()) {
					min = value.doubleValue();
				}
				if (max < value.doubleValue()) {
					max = value.doubleValue();
				}
				
				//Create test item
				GlobalMapDataItem testItem = new GlobalMapDataItem(gisPort.getX(), gisPort.getY(), gisPort
						.getPortName(), color);
				int index;

				//Check if test item is among map items that have already been added
				if ((index = items.indexOf(testItem)) != -1) {
					//If so - add Element to existing item
					GlobalMapDataItem item = (GlobalMapDataItem) items.get(index);
					item.getMapItemElements()[0].addElement(new Element(getAttribute(i, 1), new Double(value
							.doubleValue())));
					item.setSymbolColor(DOUBLE_COLOR);
				} else {
					//If no - add test item to map items
					MapItemElement itemElement = new MapItemElement(getAttribute(i, 0));
					itemElement.addElement(new Element(getAttribute(i, 1), new Double(value.doubleValue())));
					testItem.addMapItemElement(itemElement);
					double [] projXY = gisPort.getXYProjected();
					testItem.setProjXY(projXY[0], projXY[1]);
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
		
		
		//Set size of each of items created above
		for (Iterator iter = items.iterator(); iter.hasNext();) {
			GlobalMapDataItem dataItem = (GlobalMapDataItem) iter.next();

			System.out.println(dataItem.getMapItemElements()[0].getMaxElement().getValue());
			
			double size = ((Double) dataItem.getMapItemElements()[0].getMaxElement().getValue()).doubleValue();
			int index = CIRCLE_RANGES - 1;
			for (int j = 0; j < CIRCLE_RANGES; j++) {
				if (size >= ranges[j] && size < ranges[j + 1]) {
					index = j;
				}
			}
			dataItem.setSymbolSize(index);
		}

		//Prepare legend
		//Here legend has 2 groups - sizes and place colors
		LegendItemsGroup legendSizes = new LegendItemsGroup("Number of slaves");
		LegendItemsGroup legendColors = new LegendItemsGroup("Places");
		
		//Prepare legend about size of dots
		for (int i = 0; i < CIRCLE_RANGES; i++) {
			LegendItem item = new LegendItem("circle.*-" + (i + 1) + "$", "symbols/circle-1-" + (i + 1) + ".png", "" + Math.round(ranges[i]) + " - " + Math.round(ranges[i+1]));
			legendSizes.addItemToGroup(item);
		}
		
		///Prepare legend about colors
		LegendItem emb = new LegendItem("circle-1-\\d", "symbols/circle-" + 1 + "-4.png", "Place of embarkation");
		LegendItem disemb = new LegendItem("circle-2-\\d", "symbols/circle-" + 2 + "-4.png", "Place of disembarkation");
		LegendItem both = new LegendItem("circle-5-\\d", "symbols/circle-" + 5 + "-4.png", "Place of embarkatrion / disembarkation");
		legendColors.addItemToGroup(emb);
		legendColors.addItemToGroup(disemb);
		legendColors.addItemToGroup(both);
		
		int i = 0; 
		for (Iterator iter = items.iterator(); iter.hasNext(); i++) {
			GlobalMapDataItem element = (GlobalMapDataItem) iter.next();
			element.addLegendItem(legendSizes.getItems()[element.getSymbolSize()]);
			if (element.getSymbolColor() == 1) {
				element.addLegendItem(emb);
			} else if (element.getSymbolColor() == 2) {
				element.addLegendItem(disemb);
			} else {
				element.addLegendItem(both);
			}
		}
		
		//Return result response
		return new TransformerResponse((AbstractMapItem[]) items.toArray(new AbstractMapItem[] {}), 
										new LegendItemsGroup[] {legendSizes, legendColors});
	}

}
