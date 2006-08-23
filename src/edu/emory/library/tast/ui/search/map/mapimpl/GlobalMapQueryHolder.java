package edu.emory.library.tast.ui.search.map.mapimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.specific.CaseNullToZeroAttribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.ui.maps.AbstractTransformerQueryHolder;
import edu.emory.library.tast.ui.maps.AttributesMap;
import edu.emory.library.tast.ui.maps.AttributesRange;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.DirectValue;
import edu.emory.library.tast.util.query.QueryValue;

public class GlobalMapQueryHolder extends AbstractTransformerQueryHolder {

	public static String[] availableQuerySets = new String[] {"Ports", "Regions", "Adjusted ports", "Adjusted regions"};
	
	public GlobalMapQueryHolder(Conditions conditions) {
		
		Conditions localCondition = (Conditions)conditions.clone();
		localCondition.addCondition(VoyageIndex.getRecent());

		// We will need join condition (to join VoyageIndex and Voyage).
		localCondition.addCondition(VoyageIndex.getAttribute("remoteVoyageId"), new DirectValue(Voyage.getAttribute("iid")), Conditions.OP_EQUALS);

		
		QueryValue[] querySetPorts = new QueryValue[2];
		QueryValue query1 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuypt"), Dictionary.getAttribute("name")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute("1"));
		query1.setGroupBy(new Attribute[] { new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuypt"), Dictionary.getAttribute("name")}) });
		query1.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")}))});
		query1.setOrder(QueryValue.ORDER_ASC);
		querySetPorts[0] = query1;
				
		QueryValue query2 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselpt"), Dictionary.getAttribute("name")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute("2"));
		query2.setGroupBy(new Attribute[] { new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselpt"), Dictionary.getAttribute("name")}) });
		query2.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")}))});
		query2.setOrder(QueryValue.ORDER_ASC);
		querySetPorts[1] = query2;
		
	
		QueryValue[] querySetRegions = new QueryValue[2];
		query1 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuyrg"),  Dictionary.getAttribute("name")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute("1"));
		query1.setGroupBy(new Attribute[] { new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuyrg"),  Dictionary.getAttribute("name")}) });
		query1.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")}))});
		query1.setOrder(QueryValue.ORDER_ASC);
		querySetRegions[0] = query1;
				
		query2 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselrg"),  Dictionary.getAttribute("name")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute("2"));
		query2.setGroupBy(new Attribute[] { new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselrg"),  Dictionary.getAttribute("name")}) });
		query2.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")}))});
		query2.setOrder(QueryValue.ORDER_ASC);
		querySetRegions[1] = query2;
		
		
		
		QueryValue[] querySetPortsAdj = new QueryValue[2];
		query1 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuypt"), Dictionary.getAttribute("name")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("s_slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute("1"));
		query1.setGroupBy(new Attribute[] { new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuypt"), Dictionary.getAttribute("name")}) });
		query1.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("s_slaximp")}))});
		query1.setOrder(QueryValue.ORDER_ASC);
		querySetPortsAdj[0] = query1;
				
		query2 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselpt"), Dictionary.getAttribute("name")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("s_slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute("2"));
		query2.setGroupBy(new Attribute[] {new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselpt"), Dictionary.getAttribute("name")}) });
		query2.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("s_slamimp")}))});
		query2.setOrder(QueryValue.ORDER_ASC);
		querySetPortsAdj[1] = query2;
		
		
		
		QueryValue[] querySetRegionsAdj = new QueryValue[2];
		query1 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuyrg"), Dictionary.getAttribute("name")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("s_slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute("1"));
		query1.setGroupBy(new Attribute[] {new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuyrg"), Dictionary.getAttribute("name")}) });
		query1.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("s_slaximp")}))});
		query1.setOrder(QueryValue.ORDER_ASC);
		querySetRegionsAdj[0] = query1;
				
		query2 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselrg"), Dictionary.getAttribute("name")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("s_slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute("2"));
		query2.setGroupBy(new Attribute[] {new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselrg"), Dictionary.getAttribute("name")})});
		query2.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("s_slamimp")}))});
		query2.setOrder(QueryValue.ORDER_ASC);
		querySetRegionsAdj[1] = query2;
		
		
		this.addQuery(availableQuerySets[0], querySetPorts);
		this.addQuery(availableQuerySets[1], querySetRegions);
		this.addQuery(availableQuerySets[2], querySetPortsAdj);
		this.addQuery(availableQuerySets[3], querySetRegionsAdj);
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
