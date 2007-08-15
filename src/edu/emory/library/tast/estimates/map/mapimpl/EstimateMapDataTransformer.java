package edu.emory.library.tast.estimates.map.mapimpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import org.hibernate.Session;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.dm.EstimatesExportRegion;
import edu.emory.library.tast.dm.EstimatesImportArea;
import edu.emory.library.tast.dm.EstimatesImportRegion;
import edu.emory.library.tast.dm.Location;
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
 * Class providing parsing functionality of query response for 
 * mad data in estimates.
 *
 */
public class EstimateMapDataTransformer extends AbstractDataTransformer {

	private static int DOUBLE_COLOR = 5;

	private static final int CIRCLE_RANGES = 5;

	MessageFormat valuesFormat = new MessageFormat("{0,number,#,###,###}");

	public EstimateMapDataTransformer(AttributesMap map) {
		super(map);
	}

	/**
	 * Parses data from given query response
	 */
	public TransformerResponse transformData(Session session,
			AbstractTransformerQueryHolder holder) {
		Object[] data = holder.getRawQueryResponse();
		List mapDataItems = new ArrayList();
		
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();

		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		//parse row by row
		for (int i = 0; i < data.length; i++) {
			Object[] row = (Object[]) data[i];
			Long expTmp = (Long) row[0];
			Location exp = null;
			int showAtZoom = ((Integer)row[3]).intValue();
			if (expTmp != null) {
				exp = EstimatesExportRegion.loadById(session, expTmp.longValue());
				if (exp == null) {
					exp = EstimatesImportRegion.loadById(session, expTmp.longValue());
				}
				if (exp == null) {
					exp = EstimatesImportArea.loadById(session, expTmp.longValue());
				}
			}
			if (exp != null) {
				double numberExp = Math
						.round(((Number) row[1]).doubleValue() * 100)
						/ (double) 100;
				int color = Integer.parseInt(row[2].toString());
				EstimateMapDataItem expDataItem = new EstimateMapDataItem(exp
						.getX(), exp.getY(), exp.getName());
				Element element = new Element(this.getAttribute(i, 1),
						new Double(numberExp));
				element.setColor(color);
				element.setShowAtZoom(showAtZoom);

				if (min > numberExp) {
					min = numberExp;
				}
				if (max < numberExp) {
					max = numberExp;
				}

				int index;
				if ((index = mapDataItems.indexOf(expDataItem)) != -1) {
					EstimateMapDataItem item = (EstimateMapDataItem) mapDataItems.get(index);
					item.getMapItemElements()[0].addElement(element);
					item.setColor(DOUBLE_COLOR);
				} else {
					MapItemElement expElement = new MapItemElement(this.getAttribute(i, 1));
					expElement.addElement(element);
					expDataItem.addMapItemElement(expElement);
					expDataItem.setColor(color);
					mapDataItems.add(expDataItem);
				}
			}
		}

		//prepare ranges for dots
		double[] ranges = new double[CIRCLE_RANGES + 1];
		double step = (max - min) / CIRCLE_RANGES;
		for (int i = 0; i < CIRCLE_RANGES; i++) {
			ranges[i + 1] = i * step + step + min;
		}
		ranges[0] = min;

		//correct number ranges
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

		//prepare legend
		LegendItemsGroup legendSizes = new LegendItemsGroup(TastResource.getText("estimates_map_leg_slavnum"));
		LegendItemsGroup legendColors = new LegendItemsGroup(TastResource.getText("estimates_map_leg_places"));

		// Prepare legend about size of dots
		for (int i = CIRCLE_RANGES - 1; i >= 0; i--) {
			long to = Math.round(ranges[i + 1]);
			long from = Math.round(ranges[i]);
			from++;
			LegendItem item = new LegendItem("circle.*-" + (i + 1) + "$",
					contextPath + "/map-assets/symbols/circle-1-" + (i + 1) + ".png", ""
							+ valuesFormat
									.format(new Object[] { new Long(from) })
							+ " - "
							+ valuesFormat
									.format(new Object[] { new Long(to) }));
			legendSizes.addItemToGroup(item);
		}

		// /Prepare legend about colors
		LegendItem emb = new LegendItem("circle-2-\\d", contextPath + "/map-assets/symbols/circle-" + 2
				+ "-4.png", TastResource.getText("estimates_map_leg_embplace"));
		LegendItem disemb = new LegendItem("circle-3-\\d", contextPath + "/map-assets/symbols/circle-"
				+ 3 + "-4.png", TastResource.getText("estimates_map_leg_disembplace"));
		LegendItem both = new LegendItem("circle-5-\\d", contextPath + "/map-assets/symbols/circle-" + 5
				+ "-4.png", TastResource.getText("estimates_map_leg_bothplaces"));
		legendColors.addItemToGroup(emb);
		legendColors.addItemToGroup(disemb);
		//legendColors.addItemToGroup(both);

		int i = 0;
		for (Iterator iter = mapDataItems.iterator(); iter.hasNext(); i++) {
			EstimateMapDataItem element = (EstimateMapDataItem) iter.next();
			element
					.addLegendItem(legendSizes.getItems()[legendSizes.getItems().length - element.getSize()]);
			if (element.getColor() == 2) {
				element.addLegendItem(emb);
			} else if (element.getColor() == 3) {
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
