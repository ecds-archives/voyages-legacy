package edu.emory.library.tast.ui.search.map.mapimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.ui.maps.AbstractTransformerQueryHolder;
import edu.emory.library.tast.ui.maps.AttributesMap;
import edu.emory.library.tast.ui.maps.AttributesRange;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.DirectValue;
import edu.emory.library.tast.util.query.QueryValue;

public class GlobalMapQueryHolder extends AbstractTransformerQueryHolder {

	public static String[] availableQuerySets = new String[] {"Ports", "Regions"};
	
	public GlobalMapQueryHolder(Conditions conditions) {
		
		QueryValue[] querySetPorts = new QueryValue[2];
		Conditions localCondition = conditions.addAttributesPrefix("v.");
		localCondition.addCondition(VoyageIndex.getRecent().addAttributesPrefix("vi."));

		// We will need join condition (to join VoyageIndex and Voyage).
		localCondition.addCondition("vi.remoteVoyageId", new DirectValue("v.id"), Conditions.OP_EQUALS);

		QueryValue query1 = new QueryValue("VoyageIndex vi, Voyage v", localCondition);
		query1.addPopulatedAttribute("v.majbuypt.name", false);
		query1.addPopulatedAttribute("case when sum(v.slaximp) is null then 0 else sum(v.slaximp) end", false);
		query1.addPopulatedAttribute("1", false);
		query1.setGroupBy(new String[] { "v.majbuypt.name" });
		query1.setOrderBy(new String[] {"case when sum(v.slaximp) is null then 0 else sum(v.slaximp) end"});
		query1.setOrder(QueryValue.ORDER_ASC);
		querySetPorts[0] = query1;
				
		QueryValue query2 = new QueryValue("VoyageIndex vi, Voyage v", localCondition);
		query2.addPopulatedAttribute("v.majselpt.name", false);
		query2.addPopulatedAttribute("case when sum(v.slamimp) is null then 0 else sum(v.slamimp) end", false);
		query2.addPopulatedAttribute("2", false);
		query2.setGroupBy(new String[] { "v.majselpt.name" });
		query2.setOrderBy(new String[] {"case when sum(v.slamimp) is null then 0 else sum(v.slamimp) end"});
		query2.setOrder(QueryValue.ORDER_ASC);
		querySetPorts[1] = query2;
		
		
		QueryValue[] querySetRegions = new QueryValue[2];

		query1 = new QueryValue("VoyageIndex vi, Voyage v", localCondition);
		query1.addPopulatedAttribute("v.majbuyrg.name", false);
		query1.addPopulatedAttribute("case when sum(v.slaximp) is null then 0 else sum(v.slaximp) end", false);
		query1.addPopulatedAttribute("1", false);
		query1.setGroupBy(new String[] { "v.majbuyrg.name" });
		query1.setOrderBy(new String[] {"case when sum(v.slaximp) is null then 0 else sum(v.slaximp) end"});
		query1.setOrder(QueryValue.ORDER_ASC);
		querySetRegions[0] = query1;
				
		query2 = new QueryValue("VoyageIndex vi, Voyage v", localCondition);
		query2.addPopulatedAttribute("v.majselrg.name", false);
		query2.addPopulatedAttribute("case when sum(v.slamimp) is null then 0 else sum(v.slamimp) end", false);
		query2.addPopulatedAttribute("2", false);
		query2.setGroupBy(new String[] { "v.majselrg.name" });
		query2.setOrderBy(new String[] {"case when sum(v.slamimp) is null then 0 else sum(v.slamimp) end"});
		query2.setOrder(QueryValue.ORDER_ASC);
		querySetRegions[1] = query2;
		
		
		this.addQuery(availableQuerySets[0], querySetPorts);
		this.addQuery(availableQuerySets[1], querySetRegions);
	}
	
	protected void performExecuteQuery(QueryValue[] queries) {
		
		AttributesMap map = new AttributesMap();
		List col1 = new ArrayList();
		List col2 = new ArrayList();
		ArrayList response = new ArrayList();
		int beginSize = response.size();
		
		for (int i = 0; i < queries.length; i++) {
		
			executeMapQuery(response, queries[i]);
			col1.add(new AttributesRange(Voyage.getAttribute("majselpt"), beginSize, beginSize + response.size() - 1));
			col2.add(new AttributesRange(Voyage.getAttribute("slamimp"), beginSize, beginSize + response.size() - 1));

			map.addColumn(col1);
			map.addColumn(col2);
			beginSize += response.size();
		}
		
		this.setAttributesMap(map);
		this.setRawQueryResponse(response.toArray());
	}
	
	private void executeMapQuery(List response, QueryValue query) {

		Object[] voyages = query.executeQuery();
		response.addAll(Arrays.asList(voyages));
	}

	public static String[] getAvailableQuerySets() {
		return availableQuerySets;
	}

}
