package edu.emory.library.tast.ui.search.map.mapimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tas.util.HibernateConnector;
import edu.emory.library.tas.util.HibernateUtil;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
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
	
	QueryValue[] querySetPorts;
	QueryValue[] querySetRegions;
	QueryValue[] querySetPortsAdj;
	QueryValue[] querySetRegionsAdj;
	
	public GlobalMapQueryHolder(Conditions conditions) {
		
		Conditions localCondition = (Conditions)conditions.clone();
		localCondition.addCondition(VoyageIndex.getRecent());

		// We will need join condition (to join VoyageIndex and Voyage).
		localCondition.addCondition(VoyageIndex.getAttribute("remoteVoyageId"), new DirectValue(Voyage.getAttribute("iid")), Conditions.OP_EQUALS);

		
		querySetPorts = new QueryValue[2];
		QueryValue query1 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuypt"), Port.getAttribute("id")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute("2"));
		query1.setGroupBy(new Attribute[] { new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuypt"), Port.getAttribute("id")}) });
		query1.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")}))});
		query1.setOrder(QueryValue.ORDER_ASC);
		querySetPorts[0] = query1;
				
		QueryValue query2 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselpt"), Port.getAttribute("id")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute("3"));
		query2.setGroupBy(new Attribute[] { new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselpt"), Port.getAttribute("id")}) });
		query2.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")}))});
		query2.setOrder(QueryValue.ORDER_ASC);
		querySetPorts[1] = query2;
		
	
		querySetRegions = new QueryValue[2];
		query1 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuyrg"),  Region.getAttribute("id")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute("2"));
		query1.setGroupBy(new Attribute[] { new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuyrg"),  Region.getAttribute("id")}) });
		query1.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")}))});
		query1.setOrder(QueryValue.ORDER_ASC);
		querySetRegions[0] = query1;
				
		query2 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselrg"),  Region.getAttribute("id")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute("3"));
		query2.setGroupBy(new Attribute[] { new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselrg"),  Region.getAttribute("id")}) });
		query2.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")}))});
		query2.setOrder(QueryValue.ORDER_ASC);
		querySetRegions[1] = query2;
		
		
		
		querySetPortsAdj = new QueryValue[2];
		query1 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuypt"), Port.getAttribute("id")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("s_slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute("2"));
		query1.setGroupBy(new Attribute[] { new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuypt"), Port.getAttribute("id")}) });
		query1.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("s_slaximp")}))});
		query1.setOrder(QueryValue.ORDER_ASC);
		querySetPortsAdj[0] = query1;
				
		query2 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselpt"), Port.getAttribute("id")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("s_slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute("3"));
		query2.setGroupBy(new Attribute[] {new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselpt"), Port.getAttribute("id")}) });
		query2.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("s_slamimp")}))});
		query2.setOrder(QueryValue.ORDER_ASC);
		querySetPortsAdj[1] = query2;
		
		
		
		querySetRegionsAdj = new QueryValue[2];
		query1 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuyrg"), Region.getAttribute("id")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("s_slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute("2"));
		query1.setGroupBy(new Attribute[] {new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbuyrg"), Region.getAttribute("id")}) });
		query1.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("s_slaximp")}))});
		query1.setOrder(QueryValue.ORDER_ASC);
		querySetRegionsAdj[0] = query1;
				
		query2 = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselrg"), Region.getAttribute("id")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("s_slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute("3"));
		query2.setGroupBy(new Attribute[] {new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majselrg"), Region.getAttribute("id")})});
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
		
		boolean ports = false;
		if (queries == querySetPorts || queries == querySetPortsAdj) {
			ports = true;
		}
		
		for (int i = 0; i < queries.length; i++) {
		
			executeMapQuery(response, queries[i], ports);
			if (i == 0) {
				col1.add(new AttributesRange(Voyage.getAttribute("majbuypt"), beginSize, beginSize + response.size() - 1));
				col2.add(new AttributesRange(Voyage.getAttribute("slamimp"), beginSize, beginSize + response.size() - 1));
			} else {
				col1.add(new AttributesRange(Voyage.getAttribute("majselpt"), beginSize, beginSize + response.size() - 1));
				col2.add(new AttributesRange(Voyage.getAttribute("slaximp"), beginSize, beginSize + response.size() - 1));
			}

			map.addColumn(col1);
			map.addColumn(col2);
			beginSize += response.size();
		}
		
		this.setAttributesMap(map);
		this.setRawQueryResponse(response.toArray());
	}
	
	private void executeMapQuery(List response, QueryValue query, boolean ports) {

		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		Object[] voyages = query.executeQuery(session);
		if (!ports) {
			for (int i = 0; i < voyages.length; i++) {
				if (((Object[])voyages[i])[0] != null) {
					((Object[])voyages[i])[0] = Region.loadById(session, ((Long)((Object[])voyages[i])[0]).longValue());
				}
			}
		} else {
			for (int i = 0; i < voyages.length; i++) {
				if (((Object[])voyages[i])[0] != null) {
					//System.out.println("next");
					//Port port = Port.loadById(((Long)((Object[])voyages[i])[0]).longValue());
					((Object[])voyages[i])[0] = Port.loadById(session, ((Long)((Object[])voyages[i])[0]).longValue());
				}
			}
		}
		t.commit();
		session.close();
		response.addAll(Arrays.asList(voyages));
	}

	public static String[] getAvailableQuerySets() {
		return availableQuerySets;
	}

}
