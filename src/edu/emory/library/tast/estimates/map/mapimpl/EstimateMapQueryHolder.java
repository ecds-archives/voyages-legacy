package edu.emory.library.tast.estimates.map.mapimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.ui.maps.AbstractTransformerQueryHolder;
import edu.emory.library.tast.ui.maps.AttributesMap;
import edu.emory.library.tast.ui.maps.AttributesRange;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttrEstimate;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class EstimateMapQueryHolder extends AbstractTransformerQueryHolder {

	public QueryValue[] estimateMapQuerys = null;

	public EstimateMapQueryHolder(Conditions conditions) {
		QueryValue qValue1 = new QueryValue(new String[] { "Estimate" },
				new String[] { "e" }, conditions);
		qValue1
				.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {
						Estimate.getAttribute("expRegion"),
						Region.getAttribute("id") }));
		qValue1.addPopulatedAttribute(new FunctionAttribute("sum",
				new Attribute[] { Estimate.getAttribute("slavExported") }));
		qValue1.addPopulatedAttribute(new DirectValueAttribute("2"));
		qValue1.setGroupBy(new Attribute[] { new SequenceAttribute(
				new Attribute[] { Estimate.getAttribute("expRegion"),
						Region.getAttribute("id") }) });

		QueryValue qValue2 = new QueryValue(new String[] { "Estimate" },
				new String[] { "e" }, conditions);
		qValue2
				.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {
						Estimate.getAttribute("impRegion"),
						Region.getAttribute("id") }));
		qValue2.addPopulatedAttribute(new FunctionAttribute("sum",
				new Attribute[] { Estimate.getAttribute("slavImported") }));
		qValue2.addPopulatedAttribute(new DirectValueAttribute("3"));
		qValue2.setGroupBy(new Attribute[] { new SequenceAttribute(
				new Attribute[] { Estimate.getAttribute("impRegion"),
						Region.getAttribute("id") }) });

		this.estimateMapQuerys = new QueryValue[] { qValue1, qValue2 };
		this.addQuery("", this.estimateMapQuerys);
	}

	protected void performExecuteQuery(QueryValue[] querySet) {
		List allResults = new ArrayList();
		AttributesMap attributes = new AttributesMap();
		List list0 = new ArrayList();
		List list1 = new ArrayList();
		for (int i = 0; i < querySet.length; i++) {
			int shift = allResults.size();
			Object[] results = querySet[i].executeQuery();
			allResults.addAll(Arrays.asList(results));
//			List list = new ArrayList();
//			list.add(new AttributesRange(Estimate.getAttribute("expRegion"), shift,
//					shift + results.length - 1));
//			attributes.addColumn(list);
//			list = new ArrayList();
//			list.add(new AttributesRange(Estimate.getAttribute("impRegion"), shift,
//					shift + results.length - 1));
//			attributes.addColumn(list);
			
			list0.add(new AttributesRange(Estimate.getAttribute(i == 0 ? "expRegion" : "impRegion"),
					shift, shift + results.length - 1));
			
			list1.add(new AttributesRange(Estimate.getAttribute(i == 0 ? "slavExported" : "slavImported"),
					shift, shift + results.length - 1));
		}
		attributes.addColumn(list0);
		attributes.addColumn(list1);
		this.setAttributesMap(attributes);
		this.setRawQueryResponse(allResults.toArray());
	}

}
