package edu.emory.library.tast.estimates.map.mapimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.database.tabscommon.VisibleAttrEstimate;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.EstimatesExportArea;
import edu.emory.library.tast.dm.EstimatesExportRegion;
import edu.emory.library.tast.dm.EstimatesImportArea;
import edu.emory.library.tast.dm.EstimatesImportRegion;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.maps.AbstractTransformerQueryHolder;
import edu.emory.library.tast.maps.AttributesMap;
import edu.emory.library.tast.maps.AttributesRange;

public class EstimateMapQueryHolder extends AbstractTransformerQueryHolder
{

	public TastDbQuery[] estimateMapQueriesAreas = null;
	public TastDbQuery[] estimateMapQueriesRegions = null;

	public EstimateMapQueryHolder(TastDbConditions conditions)
	{

		// export regions

		TastDbConditions c = new TastDbConditions();
		c.addCondition(conditions);
		c.addCondition(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesExportRegion.getAttribute("showOnMap") }), new Boolean(true), TastDbConditions.OP_EQUALS);
		TastDbQuery qValue1 = new TastDbQuery(new String[] { "Estimate" }, new String[] { "e" }, c);
		qValue1.addPopulatedAttribute(new DirectValueAttribute(new Integer(2)));
		qValue1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportArea.getAttribute("id") }));
		qValue1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportArea.getAttribute("name") }));
		qValue1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportArea.getAttribute("longitude") }));
		qValue1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportArea.getAttribute("latitude") }));
		qValue1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportRegion.getAttribute("showAtZoom") }));
		qValue1.addPopulatedAttribute(new FunctionAttribute("sum", new Attribute[] { Estimate.getAttribute("slavExported") }));
		qValue1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesExportRegion.getAttribute("showAtZoom") }));
		qValue1.setGroupBy(new Attribute[] {
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportArea.getAttribute("id") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportArea.getAttribute("name") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportArea.getAttribute("longitude") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportArea.getAttribute("latitude") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportRegion.getAttribute("showAtZoom") })});
		
		// import regions

		c = new TastDbConditions();
		c.addCondition(conditions);
		c.addCondition(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportRegion.getAttribute("showOnMap") }), new Boolean(true), TastDbConditions.OP_EQUALS);
		TastDbQuery qValue2 = new TastDbQuery(new String[] { "Estimate" }, new String[] { "e" }, c);
		qValue2.addPopulatedAttribute(new DirectValueAttribute(new Integer(3)));
		qValue2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportArea.getAttribute("id") }));
		qValue2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportArea.getAttribute("name") }));
		qValue2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportArea.getAttribute("longitude") }));
		qValue2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportArea.getAttribute("latitude") }));
		qValue2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportRegion.getAttribute("showAtZoom") }));
		qValue2.addPopulatedAttribute(new FunctionAttribute("sum", new Attribute[] { Estimate.getAttribute("slavImported") }));
		qValue2.setGroupBy(new Attribute[] {
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportArea.getAttribute("id") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportArea.getAttribute("name") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportArea.getAttribute("longitude") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportArea.getAttribute("latitude") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportRegion.getAttribute("showAtZoom") })});
		
		this.estimateMapQueriesRegions = new TastDbQuery[] { qValue1, qValue2 };
		
		// export areas

		c = new TastDbConditions();
		c.addCondition(conditions);
		c.addCondition(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesExportRegion.getAttribute("area"), EstimatesExportArea.getAttribute("showOnMap") }), new Boolean(true), TastDbConditions.OP_EQUALS);
		qValue1 = new TastDbQuery(new String[] { "Estimate" }, new String[] { "e" }, c);
		qValue1.addPopulatedAttribute(new DirectValueAttribute(new Integer(2)));
		qValue1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("id") }));
		qValue1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("name") }));
		qValue1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("longitude") }));
		qValue1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("latitude") }));
		qValue1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportRegion.getAttribute("showAtZoom") }));
		qValue1.addPopulatedAttribute(new FunctionAttribute("sum", new Attribute[] { Estimate.getAttribute("slavExported") }));
		qValue1.setGroupBy(new Attribute[] {
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("id") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("name") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("longitude") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("latitude") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("expRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportRegion.getAttribute("showAtZoom") })});

		// import areas

		c = new TastDbConditions();
		c.addCondition(conditions);
		c.addCondition(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("showOnMap") }), new Boolean(true), TastDbConditions.OP_EQUALS);
		qValue2 = new TastDbQuery(new String[] { "Estimate" }, new String[] { "e" }, c);
		qValue2.addPopulatedAttribute(new DirectValueAttribute(new Integer(3)));
		qValue2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("id") }));
		qValue2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("name") }));
		qValue2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("longitude") }));
		qValue2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("latitude") }));
		qValue2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportRegion.getAttribute("showAtZoom") }));
		qValue2.addPopulatedAttribute(new FunctionAttribute("sum", new Attribute[] { Estimate.getAttribute("slavImported") }));
		qValue2.setGroupBy(new Attribute[] {
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("id") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("name") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("longitude") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportArea.getAttribute("latitude") }),
				new SequenceAttribute(new Attribute[] { Estimate.getAttribute("impRegion"), EstimatesImportRegion.getAttribute("area"), EstimatesImportRegion.getAttribute("showAtZoom") })});

		this.estimateMapQueriesAreas = new TastDbQuery[] { qValue1, qValue2 };

		this.addQuery("", this.estimateMapQueriesAreas);
		this.addQuery("", this.estimateMapQueriesRegions);

	}

	protected void performExecuteQuery(Session session, TastDbQuery[] querySet, int type)
	{
		List allResults = new ArrayList();
		AttributesMap attributes = new AttributesMap();
		List list0 = new ArrayList();
		List list1 = new ArrayList();

		for (int i = 0; i < querySet.length; i++)
		{
			if (type != -1 && type != i)
			{
				continue;
			}
			int shift = allResults.size();
			Object[] results = querySet[i].executeQuery(session);
			allResults.addAll(Arrays.asList(results));
			list0.add(new AttributesRange(VisibleAttrEstimate.getAttributeForTable(i == 0 ? "expRegion" : "impRegion"), shift, shift + results.length - 1));
			list1.add(new AttributesRange(VisibleAttrEstimate.getAttributeForTable(i == 0 ? "slavExported" : "slavImported"), shift, shift + results.length - 1));
		}
		attributes.addColumn(list0);
		attributes.addColumn(list1);
		this.setAttributesMap(attributes);
		this.setRawQueryResponse(allResults.toArray());
	}

}
