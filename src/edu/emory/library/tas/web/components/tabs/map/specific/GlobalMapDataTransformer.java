package edu.emory.library.tas.web.components.tabs.map.specific;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.emory.library.tas.GISPortLocation;
import edu.emory.library.tas.util.query.QueryValue;
import edu.emory.library.tas.web.components.tabs.map.AbstractDataTransformer;
import edu.emory.library.tas.web.components.tabs.map.AbstractMapItem;
import edu.emory.library.tas.web.components.tabs.map.AttributesMap;
import edu.emory.library.tas.web.components.tabs.map.Element;
import edu.emory.library.tas.web.components.tabs.map.LegendItem;
import edu.emory.library.tas.web.components.tabs.map.LegendItemsGroup;
import edu.emory.library.tas.web.components.tabs.map.MapItemElement;
import edu.emory.library.tas.web.components.tabs.map.TransformerResponse;

/**
 * Expects data row: [Dictionary - place] [Number - value] [Integer - color]
 * 
 * @author Pawe Jurczyk
 * 
 */
public class GlobalMapDataTransformer extends AbstractDataTransformer {

	private static final int CIRCLE_RANGES = 5;

	private static int DOUBLE_COLOR = 5;
	
	public GlobalMapDataTransformer(AttributesMap map) {
		super(map);
	}
	
	public TransformerResponse transformData(Object[] data, double min, double max) {

		double[] rangeBoundries = new double[CIRCLE_RANGES + 1];
		double step = (max - min) / CIRCLE_RANGES;
		for (int i = 0; i < CIRCLE_RANGES; i++) {
			rangeBoundries[i + 1] = i * step + step + min;
		}
		rangeBoundries[0] = min;

		return this.getItems(data, rangeBoundries);
	}

	private TransformerResponse getItems(Object[] data, double[] ranges) {

		List items = new ArrayList();

		for (int i = 0; i < data.length; i++) {
			GISPortLocation gisPort = GISPortLocation.getGISPortLocation((String) ((Object[]) data[i])[0]);
			int color = ((Integer) ((Object[]) data[i])[2]).intValue();
			Number value = (Number) ((Object[]) data[i])[1];
			if (gisPort != null) {
				GlobalMapDataItem testItem = new GlobalMapDataItem(gisPort.getX(), gisPort.getY(), gisPort
						.getPortName(), color);
				int index;

				if ((index = items.indexOf(testItem)) != -1) {
					GlobalMapDataItem item = (GlobalMapDataItem) items.get(index);
					item.getMapItemElements()[0].addElement(new Element(getAttribute(i, 1), new Double(value
							.doubleValue())));
					item.setSymbolColor(DOUBLE_COLOR);
				} else {
					MapItemElement itemElement = new MapItemElement(getAttribute(i, 0));
					itemElement.addElement(new Element(getAttribute(i, 1), new Double(value.doubleValue())));
					testItem.addMapItemElement(itemElement);
					items.add(testItem);
				}
			}

		}

		for (Iterator iter = items.iterator(); iter.hasNext();) {
			GlobalMapDataItem dataItem = (GlobalMapDataItem) iter.next();

			double size = ((Double) dataItem.getMapItemElements()[0].getMaxElement().getValue()).doubleValue();
			int index = CIRCLE_RANGES - 1;
			for (int j = 0; j < CIRCLE_RANGES; j++) {
				if (size >= ranges[j] && size < ranges[j + 1]) {
					index = j;
				}
			}
			dataItem.setSymbolSize(index);
		}

		List legend = new ArrayList();
		LegendItemsGroup legendSizes = new LegendItemsGroup("Number of slaves");
		LegendItemsGroup legendColors = new LegendItemsGroup("Colors");
		for (int i = 0; i < CIRCLE_RANGES - 1; i++) {
			LegendItem item = new LegendItem("symbols/circle-1-" + (i + 1), "" + ranges[i] + " - " + ranges[i+1]);
			legendSizes.addItemToGroup(item);
		}
		
		LegendItem emb = new LegendItem("circle-" + 1 + "-4", "Embarkation");
		LegendItem disemb = new LegendItem("circle-" + 2 + "-4", "Disembarkation");
		LegendItem both = new LegendItem("circle-" + 5 + "-4", "Both");
		legendColors.addItemToGroup(emb);
		legendColors.addItemToGroup(disemb);
		legendColors.addItemToGroup(both);
		
		return new TransformerResponse((AbstractMapItem[]) items.toArray(new AbstractMapItem[] {}), 
										new LegendItemsGroup[] {legendSizes, legendColors});
	}

}
