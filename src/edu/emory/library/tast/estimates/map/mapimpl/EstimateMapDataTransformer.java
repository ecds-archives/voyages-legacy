package edu.emory.library.tast.estimates.map.mapimpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.ui.maps.AbstractDataTransformer;
import edu.emory.library.tast.ui.maps.AbstractMapItem;
import edu.emory.library.tast.ui.maps.AbstractTransformerQueryHolder;
import edu.emory.library.tast.ui.maps.AttributesMap;
import edu.emory.library.tast.ui.maps.Element;
import edu.emory.library.tast.ui.maps.LegendItem;
import edu.emory.library.tast.ui.maps.LegendItemsGroup;
import edu.emory.library.tast.ui.maps.MapItemElement;
import edu.emory.library.tast.ui.maps.TransformerResponse;
import edu.emory.library.tast.ui.search.map.mapimpl.GlobalMapDataItem;

public class EstimateMapDataTransformer extends AbstractDataTransformer {

	private static int DOUBLE_COLOR = 5;

	private static final int CIRCLE_RANGES = 5;

	MessageFormat valuesFormat = new MessageFormat("{0,number,#,###,###}");

	public EstimateMapDataTransformer(AttributesMap map) {
		super(map);
	}

	public TransformerResponse transformData(
			AbstractTransformerQueryHolder holder) {
		Object[] data = holder.getRawQueryResponse();
		List mapDataItems = new ArrayList();

		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		for (int i = 0; i < data.length; i++) {
			Object[] row = (Object[]) data[i];
			Long expTmp = (Long) row[0];
			Region exp = null;
			if (expTmp != null) {
				exp = Region.loadById(expTmp.longValue());
			}
			if (exp != null) {
				System.out.println(i);
				double numberExp = Math
						.round(((Number) row[1]).doubleValue() * 100)
						/ (double) 100;
				int color = Integer.parseInt(row[2].toString());
				EstimateMapDataItem expDataItem = new EstimateMapDataItem(exp
						.getX(), exp.getY(), exp.getName());
				Element element = new Element(this.getAttribute(i, 1),
						new Double(numberExp));
				element.setColor(color);

				if (min > numberExp) {
					min = numberExp;
				}
				if (max < numberExp) {
					max = numberExp;
				}
				// if (expTmp.intValue() == 4) {
				// System.out.println("Spain!");
				// }

				int index;
				if ((index = mapDataItems.indexOf(expDataItem)) != -1) {
					EstimateMapDataItem item = (EstimateMapDataItem) mapDataItems
							.get(index);
					item.getMapItemElements()[0].addElement(element);
					item.setColor(DOUBLE_COLOR);
				} else {
					MapItemElement expElement = new MapItemElement(this
							.getAttribute(i, 1));
					expElement.addElement(element);
					expDataItem.addMapItemElement(expElement);
					expDataItem.setColor(color);
					mapDataItems.add(expDataItem);
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

		// Set size of each of items created above
		for (Iterator iter = mapDataItems.iterator(); iter.hasNext();) {
			int maxSize = -1;
			EstimateMapDataItem dataItem = (EstimateMapDataItem) iter.next();

			// double size = ((Double) dataItem.getMapItemElements()[0]
			// .getMaxElement().getValue()).doubleValue();
			MapItemElement element = dataItem.getMapItemElements()[0];
			Element[] subs = element.getElements();
			for (int k = 0; k < subs.length; k++) {
				Element felement = subs[k];
				double size = ((Double) felement.getValue()).doubleValue();
				int index = CIRCLE_RANGES - 1;
				for (int j = 0; j < CIRCLE_RANGES; j++) {
					if (size >= ranges[j] && size < ranges[j + 1]) {
						index = j;
					}
				}
				felement.setSize(index + 1);
				if (index + 1 > maxSize) {
					maxSize = index + 1; 
				}
			}
			dataItem.setSize(maxSize);
		}

		LegendItemsGroup legendSizes = new LegendItemsGroup("Number of slaves");
		LegendItemsGroup legendColors = new LegendItemsGroup("Places");

		// Prepare legend about size of dots
		for (int i = CIRCLE_RANGES - 1; i >= 0; i--) {
			long to = Math.round(ranges[i + 1]);
			long from = Math.round(ranges[i]);
			from++;
			LegendItem item = new LegendItem("circle.*-" + (i + 1) + "$",
					"symbols/circle-1-" + (i + 1) + ".png", ""
							+ valuesFormat
									.format(new Object[] { new Long(from) })
							+ " - "
							+ valuesFormat
									.format(new Object[] { new Long(to) }));
			legendSizes.addItemToGroup(item);
		}

		// /Prepare legend about colors
		LegendItem emb = new LegendItem("circle-1-\\d", "symbols/circle-" + 2
				+ "-4.png", "Place of embarkation");
		LegendItem disemb = new LegendItem("circle-2-\\d", "symbols/circle-"
				+ 3 + "-4.png", "Place of disembarkation");
		LegendItem both = new LegendItem("circle-5-\\d", "symbols/circle-" + 5
				+ "-4.png", "Place of embarkatrion / disembarkation");
		legendColors.addItemToGroup(emb);
		legendColors.addItemToGroup(disemb);
		legendColors.addItemToGroup(both);

		int i = 0;
		for (Iterator iter = mapDataItems.iterator(); iter.hasNext(); i++) {
			EstimateMapDataItem element = (EstimateMapDataItem) iter.next();
			element
					.addLegendItem(legendSizes.getItems()[element.getSize() - 1]);
			if (element.getColor() == 1) {
				element.addLegendItem(emb);
			} else if (element.getColor() == 2) {
				element.addLegendItem(disemb);
			} else {
				element.addLegendItem(both);
			}
		}

		if (data.length == 0) {
			return new TransformerResponse(new AbstractMapItem[] {},
					new LegendItemsGroup[] {});
		}

		TransformerResponse response = new TransformerResponse(
				(AbstractMapItem[]) mapDataItems
						.toArray(new AbstractMapItem[] {}),
				new LegendItemsGroup[] { legendSizes, legendColors });

		return response;
	}

}
