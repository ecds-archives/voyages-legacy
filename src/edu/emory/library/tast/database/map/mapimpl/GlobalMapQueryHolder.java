package edu.emory.library.tast.database.map.mapimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.maps.AbstractTransformerQueryHolder;
import edu.emory.library.tast.maps.AttributesMap;
import edu.emory.library.tast.maps.AttributesRange;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.database.tabscommon.VisibleAttribute;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.CaseNullToZeroAttribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * Query for map. Prepares query that will be executed for maps purposes.
 *
 */
public class GlobalMapQueryHolder extends AbstractTransformerQueryHolder {

	public static String[] availableQuerySets = new String[] {"Ports", "Regions", "Adjusted ports", "Adjusted regions"};
	
	QueryValue[] querySetPorts;
	QueryValue[] querySetRegions;
	QueryValue[] querySetPortsAdj;
	QueryValue[] querySetRegionsAdj;
	
	public GlobalMapQueryHolder(Conditions conditions) {
		
		// We will need join condition (to join VoyageIndex and Voyage).
		//localCondition.addCondition(VoyageIndex.getAttribute("remoteVoyageId"), new DirectValue(Voyage.getAttribute("iid")), Conditions.OP_EQUALS);
		Conditions c = new Conditions();
		if (!conditions.isEmpty()) {
			c.addCondition(conditions);
		}
		
		querySetPorts = new QueryValue[2];
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("latitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("longitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		QueryValue query1 = new QueryValue(new String[] {"Voyage"}, new String[] {"v"}, c);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("id")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute("2"));
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("showAtZoom")}));
		query1.setGroupBy(new Attribute[] {new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("showAtZoom")}), new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("id")}) });
		query1.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")}))});
		query1.setOrder(QueryValue.ORDER_ASC);
		querySetPorts[0] = query1;
				
		c = new Conditions();
		if (!conditions.isEmpty()) {
			c.addCondition(conditions);
		}
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("latitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("longitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		QueryValue query2 = new QueryValue(new String[] {"Voyage"}, new String[] {"v"}, c);
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("id")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute("3"));
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("showAtZoom")}));
		query2.setGroupBy(new Attribute[] {new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("showAtZoom")}), new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("id")}) });
		query2.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")}))});
		query2.setOrder(QueryValue.ORDER_ASC);
		querySetPorts[1] = query2;
		
	
		querySetRegions = new QueryValue[2];
		c = new Conditions();
		if (!conditions.isEmpty()) {
			c.addCondition(conditions);
		}
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("latitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("longitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		query1 = new QueryValue(new String[] {"Voyage"}, new String[] {"v"}, c);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"),  Region.getAttribute("id")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute("2"));
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("showAtZoom")}));
		query1.setGroupBy(new Attribute[] {new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("showAtZoom")}), new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"),  Region.getAttribute("id")}) });
		query1.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")}))});
		query1.setOrder(QueryValue.ORDER_ASC);
		querySetRegions[0] = query1;
			
		c = new Conditions();
		if (!conditions.isEmpty()) {
			c.addCondition(conditions);
		}
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("latitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("longitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		query2 = new QueryValue(new String[] {"Voyage"}, new String[] {"v"}, c);
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"),  Region.getAttribute("id")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute("3"));
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("showAtZoom")}));
		query2.setGroupBy(new Attribute[] {new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("showAtZoom")}), new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"),  Region.getAttribute("id")}) });
		query2.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")}))});
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
		
		boolean ports = false;
		if (queries == querySetPorts || queries == querySetPortsAdj) {
			ports = true;
		}
		
		for (int i = 0; i < queries.length; i++) {
		
			executeMapQuery(response, queries[i], ports);
			if (i == 0) {
				col1.add(new AttributesRange(VisibleAttribute.getAttribute("mjbyptimp"), beginSize, beginSize + response.size() - 1));
				col2.add(new AttributesRange(VisibleAttribute.getAttribute("slaximp"), beginSize, beginSize + response.size() - 1));
			} else {
				col1.add(new AttributesRange(VisibleAttribute.getAttribute("mjslptimp"), beginSize, beginSize + response.size() - 1));
				col2.add(new AttributesRange(VisibleAttribute.getAttribute("slamimp"), beginSize, beginSize + response.size() - 1));
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
