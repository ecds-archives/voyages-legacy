/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.database.map.mapimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.database.tabscommon.VisibleAttribute;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
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

/**
 * Query for map. Prepares query that will be executed for maps purposes.
 * There are three groups of queries. First group (first two queries) is for embarkation and disembarkation ports.
 * Second group is for embarkation/disembarkation region. The last group is for major regions (also emb/disemb).
 */
public class GlobalMapQueryHolder extends AbstractTransformerQueryHolder {

	public static String[] availableQuerySets = new String[] {"Ports", "Regions", "Adjusted ports", "Adjusted regions"};
	
	TastDbQuery[] querySetPorts;
	TastDbQuery[] querySetRegions;
	TastDbQuery[] querySetAreas;
	
	TastDbQuery[][] queriesAll = null;
	
	public GlobalMapQueryHolder(TastDbConditions conditions) {
		
		// We will need join condition (to join VoyageIndex and Voyage).
		//localCondition.addCondition(VoyageIndex.getAttribute("remoteVoyageId"), new DirectValue(Voyage.getAttribute("iid")), Conditions.OP_EQUALS);
		TastDbConditions c = new TastDbConditions();
		if (!conditions.isEmpty()) {
			c.addCondition(conditions);
		}
		
		//Query for ports
		querySetPorts = new TastDbQuery[2];
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("showOnMainMap")}), new Boolean(true), TastDbConditions.OP_EQUALS);
		TastDbQuery query1 = new TastDbQuery(new String[] {"Voyage"}, new String[] {"v"}, c);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("id")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute(new Integer(2)));
		query1.addPopulatedAttribute(new DirectValueAttribute(new Integer(2)));
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("showAtZoom")}));
		query1.setGroupBy(
				new Attribute[] {
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("showAtZoom")}),
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjbyptimp"), Port.getAttribute("id")})
				});
		query1.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")}))});
		query1.setOrder(TastDbQuery.ORDER_ASC);
		querySetPorts[0] = query1;
				
		c = new TastDbConditions();
		if (!conditions.isEmpty()) {
			c.addCondition(conditions);
		}
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("showOnMainMap")}), new Boolean(true), TastDbConditions.OP_EQUALS);
		TastDbQuery query2 = new TastDbQuery(new String[] {"Voyage"}, new String[] {"v"}, c);
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("id")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute(new Integer(3)));
		query2.addPopulatedAttribute(new DirectValueAttribute(new Integer(2)));
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("showAtZoom")}));
		query2.setGroupBy(
				new Attribute[] {
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("showAtZoom")}),
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjslptimp"), Port.getAttribute("id")})
				});
		query2.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")}))});
		query2.setOrder(TastDbQuery.ORDER_ASC);
		querySetPorts[1] = query2;
		
		//Query for regions
		querySetRegions = new TastDbQuery[2];
		c = new TastDbConditions();
		if (!conditions.isEmpty()) {
			c.addCondition(conditions);
		}
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("showOnMap")}), new Boolean(true), TastDbConditions.OP_EQUALS);
		query1 = new TastDbQuery(new String[] {"Voyage"}, new String[] {"v"}, c);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("id")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute(new Integer(2)));
		query1.addPopulatedAttribute(new DirectValueAttribute(new Integer(1)));
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("showAtZoom")}));
		query1.setGroupBy(
				new Attribute[] {
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("showAtZoom")}),
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("id")})
				});
		query1.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")}))});
		query1.setOrder(TastDbQuery.ORDER_ASC);
		querySetRegions[0] = query1;
			
		c = new TastDbConditions();
		if (!conditions.isEmpty()) {
			c.addCondition(conditions);
		}
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("showOnMap")}), new Boolean(true), TastDbConditions.OP_EQUALS);
		query2 = new TastDbQuery(new String[] {"Voyage"}, new String[] {"v"}, c);
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("id")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute(new Integer(3)));
		query2.addPopulatedAttribute(new DirectValueAttribute(new Integer(1)));
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("showAtZoom")}));
		query2.setGroupBy(
				new Attribute[] {
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("showAtZoom")}),
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("id")})
				});
		query2.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")}))});
		query2.setOrder(TastDbQuery.ORDER_ASC);
		querySetRegions[1] = query2;
		
		//Query for broad regions
		querySetAreas = new TastDbQuery[2];
		c = new TastDbConditions();
		if (!conditions.isEmpty()) {
			c.addCondition(conditions);
		}
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("area"), Area.getAttribute("showOnMap")}), new Boolean(true), TastDbConditions.OP_EQUALS);
		query1 = new TastDbQuery(new String[] {"Voyage"}, new String[] {"v"}, c);
		query1.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("area"), Area.getAttribute("id")}));
		query1.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")})));
		query1.addPopulatedAttribute(new DirectValueAttribute(new Integer(2)));
		query1.addPopulatedAttribute(new DirectValueAttribute(new Integer(0)));
		query1.setGroupBy(
				new Attribute[] {
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("majbyimp"), Region.getAttribute("area"), Area.getAttribute("id")})
				});
		query1.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")}))});
		query1.setOrder(TastDbQuery.ORDER_ASC);
		querySetAreas[0] = query1;
			
		c = new TastDbConditions();
		if (!conditions.isEmpty()) {
			c.addCondition(conditions);
		}
		c.addCondition(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("area"), Area.getAttribute("showOnMap")}), new Boolean(true), TastDbConditions.OP_EQUALS);
		query2 = new TastDbQuery(new String[] {"Voyage"}, new String[] {"v"}, c);
		query2.addPopulatedAttribute(new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("area"), Area.getAttribute("id")}));
		query2.addPopulatedAttribute(new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")})));
		query2.addPopulatedAttribute(new DirectValueAttribute(new Integer(3)));
		query2.addPopulatedAttribute(new DirectValueAttribute(new Integer(0)));
		query2.setGroupBy(
				new Attribute[] {
						new SequenceAttribute(new Attribute[] {Voyage.getAttribute("mjselimp"), Region.getAttribute("area"), Area.getAttribute("id")})
				});
		query2.setOrderBy(new Attribute[] {new CaseNullToZeroAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")}))});
		query2.setOrder(TastDbQuery.ORDER_ASC);
		querySetAreas[1] = query2;
			
		
		this.addQuery(availableQuerySets[0], querySetAreas);
		this.addQuery(availableQuerySets[1], querySetRegions);
		this.addQuery(availableQuerySets[2], querySetPorts);
		
		queriesAll = new TastDbQuery[][] {querySetAreas, querySetRegions, querySetPorts};

	}
	
	/**
	 * Executes query
	 * Type is indication of place type (embarkation/disembarkation or both).
	 */
	protected void performExecuteQuery(Session session, TastDbQuery[] queries, int type) {

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
	private void executeMapQuery(Session session, List response, TastDbQuery query, int what)
	{
		
		boolean useSQL = AppConfig.getConfiguration().getBoolean(AppConfig.DATABASE_USE_SQL);
		Object[] voyages = query.executeQuery(session, useSQL);

		if (what == 1) {
			for (int i = 0; i < voyages.length; i++) {
				if (((Object[])voyages[i])[0] != null) {
					((Object[])voyages[i])[0] = Region.loadById(session, ((Number)((Object[])voyages[i])[0]).longValue());
				}
			}
		} else if (what == 0) {
			for (int i = 0; i < voyages.length; i++) {
				if (((Object[])voyages[i])[0] != null) {
					((Object[])voyages[i])[0] = Port.loadById(session, ((Number)((Object[])voyages[i])[0]).longValue());
				}
			}
		} else {
			for (int i = 0; i < voyages.length; i++) {
				if (((Object[])voyages[i])[0] != null) {
					((Object[])voyages[i])[0] = Area.loadById(session, ((Number)((Object[])voyages[i])[0]).longValue());
				}
			}
		}
		response.addAll(Arrays.asList(voyages));
	}

	public static String[] getAvailableQuerySets() {
		return availableQuerySets;
	}

}
