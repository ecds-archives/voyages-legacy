package edu.emory.library.tast.database.map.mapimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.database.tabscommon.VisibleAttribute;
import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.CaseNullToZeroAttribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.maps.AbstractTransformerQueryHolder;
import edu.emory.library.tast.maps.AttributesMap;
import edu.emory.library.tast.maps.AttributesRange;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * Query for map. Prepares query that will be executed for maps purposes.
 * There are three groups of queries. First group (first two queries) is for embarkation and disembarkation ports.
 * Second group is for embarkation/disembarkation region. The last group is for major regions (also emb/disemb).
 */
public class GlobalMapQueryHolder extends AbstractTransformerQueryHolder {

	public static String[] availableQuerySets = new String[] {"Ports", "Regions", "Adjusted ports", "Adjusted regions"};
	
	QueryValue[] querySetPorts;
	QueryValue[] querySetRegions;
	QueryValue[] querySetAreas;
	
	QueryValue[][] queriesAll = null;
	
	public GlobalMapQueryHolder(Conditions conditions) {
		
		// We will need join condition (to join VoyageIndex and Voyage).
		//localCondition.addCondition(VoyageIndex.getAttribute("remoteVoyageId"), new DirectValue(Voyage.getAttribute("iid")), Conditions.OP_EQUALS);
		Conditions c = new Conditions();
		if (!conditions.isEmpty()) {
			c.addCondition(conditions);
		}
		
		//Query for ports
		querySetPorts = new QueryValue[2];
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("latitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("longitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("showOnMainMap")}), new Boolean(true), Conditions.OP_EQUALS);
		QueryValue query1 = new QueryValue(new String[] {"Voyage"}, new String[] {"v"}, c);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("id")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute("2"));
		query1.addPopulatedAttribute(new DirectValueAttribute("2"));
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("showAtZoom")}));
		query1.setGroupBy(
				new Attribute[] {
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("showAtZoom")}),
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("id")})
				});
		query1.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")}))});
		query1.setOrder(QueryValue.ORDER_ASC);
		querySetPorts[0] = query1;
				
		c = new Conditions();
		if (!conditions.isEmpty()) {
			c.addCondition(conditions);
		}
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("latitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("longitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("showOnMainMap")}), new Boolean(true), Conditions.OP_EQUALS);
		QueryValue query2 = new QueryValue(new String[] {"Voyage"}, new String[] {"v"}, c);
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("id")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute("3"));
		query2.addPopulatedAttribute(new DirectValueAttribute("2"));
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("showAtZoom")}));
		query2.setGroupBy(
				new Attribute[] {
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("showAtZoom")}),
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("id")})
				});
		query2.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")}))});
		query2.setOrder(QueryValue.ORDER_ASC);
		querySetPorts[1] = query2;
		
		//Query for regions
		querySetRegions = new QueryValue[2];
		c = new Conditions();
		if (!conditions.isEmpty()) {
			c.addCondition(conditions);
		}
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("latitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("longitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		query1 = new QueryValue(new String[] {"Voyage"}, new String[] {"v"}, c);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("id")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute("2"));
		query1.addPopulatedAttribute(new DirectValueAttribute("1"));
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("showAtZoom")}));
		query1.setGroupBy(
				new Attribute[] {
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("showAtZoom")}),
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("id")})
				});
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
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("id")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute("3"));
		query2.addPopulatedAttribute(new DirectValueAttribute("1"));
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("showAtZoom")}));
		query2.setGroupBy(
				new Attribute[] {
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("showAtZoom")}),
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("id")})
				});
		query2.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")}))});
		query2.setOrder(QueryValue.ORDER_ASC);
		querySetRegions[1] = query2;
		
		//Query for broad regions
		querySetAreas = new QueryValue[2];
		c = new Conditions();
		if (!conditions.isEmpty()) {
			c.addCondition(conditions);
		}
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("area"), Area.getAttribute("latitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("area"), Area.getAttribute("longitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		query1 = new QueryValue(new String[] {"Voyage"}, new String[] {"v"}, c);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("area"), Area.getAttribute("id")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute("2"));
		query1.addPopulatedAttribute(new DirectValueAttribute("0"));
		query1.setGroupBy(
				new Attribute[] {
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("area"), Area.getAttribute("id")})
				});
		query1.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")}))});
		query1.setOrder(QueryValue.ORDER_ASC);
		querySetAreas[0] = query1;
			
		c = new Conditions();
		if (!conditions.isEmpty()) {
			c.addCondition(conditions);
		}
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("area"), Area.getAttribute("latitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("area"), Area.getAttribute("longitude")}), new Double(0), Conditions.OP_NOT_EQUALS);
		query2 = new QueryValue(new String[] {"Voyage"}, new String[] {"v"}, c);
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("area"), Area.getAttribute("id")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute("3"));
		query2.addPopulatedAttribute(new DirectValueAttribute("0"));
		query2.setGroupBy(
				new Attribute[] {
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("area"), Area.getAttribute("id")})
				});
		query2.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")}))});
		query2.setOrder(QueryValue.ORDER_ASC);
		querySetAreas[1] = query2;
			
		
		this.addQuery(availableQuerySets[0], querySetAreas);
		this.addQuery(availableQuerySets[1], querySetRegions);
		this.addQuery(availableQuerySets[2], querySetPorts);
		
		queriesAll = new QueryValue[][] {querySetAreas, querySetRegions, querySetPorts};

	}
	
	/**
	 * Executes query
	 * Type is indication of place type (embarkation/disembarkation or both).
	 */
	protected void performExecuteQuery(Session session, QueryValue[] queries, int type) {

		AttributesMap map = new AttributesMap();
		List col1 = new ArrayList();
		List col2 = new ArrayList();
		ArrayList response = new ArrayList();
		int beginSize = response.size();
		
		int id = -1;
		if (queries == querySetPorts) {
			id = 0;
		} else if (queries == querySetRegions) {
			id = 1;
		} else {
			id = 2;
		}
		
		for (int i = 0; i < queries.length; i++) {
			if (type != -1 && type != i) {
				continue;
			}
			executeMapQuery(session, response, queries[i], id);
			if (i % 2 == 0) {
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
	
	/**
	 * Internal function. It loads results with ports/regions/major regions as basic query does not return
	 * those objects, but ids.
	 * @param session
	 * @param response
	 * @param query
	 * @param what
	 */
	private void executeMapQuery(Session session, List response, QueryValue query, int what) {

		Object[] voyages = query.executeQuery(session);
		if (what == 1) {
			for (int i = 0; i < voyages.length; i++) {
				if (((Object[])voyages[i])[0] != null) {
					((Object[])voyages[i])[0] = Region.loadById(session, ((Long)((Object[])voyages[i])[0]).longValue());
				}
			}
		} else if (what == 0) {
			for (int i = 0; i < voyages.length; i++) {
				if (((Object[])voyages[i])[0] != null) {
					((Object[])voyages[i])[0] = Port.loadById(session, ((Long)((Object[])voyages[i])[0]).longValue());
				}
			}
		} else {
			for (int i = 0; i < voyages.length; i++) {
				if (((Object[])voyages[i])[0] != null) {
					((Object[])voyages[i])[0] = Area.loadById(session, ((Long)((Object[])voyages[i])[0]).longValue());
				}
			}
		}
		response.addAll(Arrays.asList(voyages));
	}

	public static String[] getAvailableQuerySets() {
		return availableQuerySets;
	}

}
