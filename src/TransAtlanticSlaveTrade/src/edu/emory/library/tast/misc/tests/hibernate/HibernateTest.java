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
package edu.emory.library.tast.misc.tests.hibernate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.emory.library.tast.database.tabscommon.VisibleAttribute;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.dm.attributes.Group;

public class HibernateTest {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String command = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		
		//HibernateConnector connector = HibernateConnector.getConnector();
		
		System.out.println(Voyage.getAttributes().length);
		System.out.println(VisibleAttribute.loadVisibleAttributes().length);
		System.out.println(Group.getGroups().length);
		
		System.out.print("command:>");
		while ((command = reader.readLine()) != null
				&& !command.equalsIgnoreCase("exit")) {

			if (command.equals("store")) {
				//Voyage v = Voyage.createNew(new Long(System.currentTimeMillis()));
				//v.setShipname("Ship: " + v.getVoyageId().longValue());
				//v.setVoyageId(v.getVoyageId());
				
				//Slave slave1 = Slave.createNew(new Long(System.currentTimeMillis()));
				//Slave slave2 = Slave.createNew(new Long(System.currentTimeMillis()));
				//slave1.setName("Slave 1");
				//slave2.setName("Slave 2");
				//HashSet slaves = new HashSet();
				//slaves.add(slave1);
				//slaves.add(slave2);
				//v.setSlaves(slaves);
				
				//v.save();
			} else if (command.equals("list")) {
				TastDbConditions conditions = new TastDbConditions();
				conditions.addCondition(Voyage.getAttribute("voyageId"), new Long(1), TastDbConditions.OP_EQUALS);
				//TastDbQuery val = new TastDbQuery("Voyage", conditions);
				//Object[] ret = val.executeQuery();
				//Voyage v = (Voyage)ret[0];
			} else if (command.startsWith("modify")) {
//				Long id = new Long(command.split(" ")[1]);
//				Voyage v = Voyage.loadMostRecent(id);
//				if (v != null) {
//					v.setShipname(v.getShipname() + "-");
//					v.save();
//				} else {
//					System.out.println("Empty result for given id - cannot update");
//				}
			} else if (command.equals("dictionary")) {
			} else if (command.equals("testSaving")) {
			} else if (command.equals("limit")) {
//				try {
//					
//					Voyage v[] = Voyage.loadMostRecent(new Long(30001), 10);
//
//					for (int i = 0; i < v.length; i++) {
//						Voyage theVoyage = v[i];
//						System.out.println(theVoyage.getVoyageid() + ": " + theVoyage);
//					}
//
//				} catch (NullPointerException e) {
//					e.printStackTrace();
//				}
			} else if (command.equals("query")) {
				
				TastDbConditions conditions = new TastDbConditions();
				conditions.addCondition(Voyage.getAttribute("voyageId"), new Long(1), TastDbConditions.OP_EQUALS);
				conditions.addCondition(VoyageIndex.getApproved());
				TastDbQuery qValue = new TastDbQuery(new String [] {"VoyageIndex", "Voyage"},
						new String[] {"v", "vi"}, conditions);
				qValue.addPopulatedAttribute(Voyage.getAttribute("arrport"));
				qValue.addPopulatedAttribute(Voyage.getAttribute("shipname"));
				
				Object[] res = qValue.executeQuery();
				
				System.out.println("Returned: " + res.length);
				for (int i = 0; i < res.length && i < 20; i++) {
					System.out.println(" -> " + (res[i]));
				}
			} else if (command.equals("hql")) {
				
//				Session session = HibernateUtil.getSession();
//				Query query = session.createQuery("from Image as i left outer join i.people as p order by p.lastName");
//				System.out.println(query.list());
				
				
				Session session = HibernateConn.getSession();
				Query query = session.createQuery("from Image as i left outer join i.people where 455129 in i.people.id");
				System.out.println(query.list());
				
				
//				Conditions cMain = new Conditions(Conditions.JOIN_AND);
//				Conditions cNot = new Conditions(Conditions.JOIN_NOT);
//				Conditions cNNull = new Conditions(Conditions.JOIN_AND);
//				cNNull.addCondition("voyage.slamimp", null, Conditions.OP_EQUALS);
//				cNot.addCondition(cNNull);
//				cMain.addCondition(cNot);
//				cMain.addCondition("voyage.voyageId", new Integer (100), Conditions.OP_SMALLER_OR_EQUAL);
//				
//				QueryValue qValue = new QueryValue("VoyageIndex", cMain);
//				qValue.addPopulatedAttribute("voyage.voyageId");
//				qValue.addPopulatedAttribute("voyage.slamimp");
//				
//				Object[] objs =  HibernateConnector.getConnector().loadObjects(qValue);
//				
//				DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
//				
//				for (int i = 0; i < objs.length; i++) {
//					
//					categoryDataset.addValue(((Integer)((Object[])objs[i])[1]).intValue(), ((Object[])objs[i])[0].toString(), "");
//				
//				}
//				
//				JFreeChart chart = ChartFactory.createBarChart("Sample Category Chart", // Title
//				                      "Voyages",              // X-Axis label
//				                      "# of slaves",                 // Y-Axis label
//				                      categoryDataset,         // Dataset
//				                      PlotOrientation.VERTICAL,
//				                      true, true, true                     // Show legend
//				                     );
//				
//				
//				
//				ChartUtilities.saveChartAsPNG(new File("chart.png"), chart, 4000, 2000);
			} else if ("tload".equalsIgnoreCase(command)) {
				TastDbConditions c = new TastDbConditions(TastDbConditions.AND);
				TastDbQuery qValue = new TastDbQuery("Group", c);
				Object [] ret = qValue.executeQuery();
				for (int i = 0; i < ret.length; i++) {
					System.out.println("Has: " + ret[i]);
				}
			} else if ("cload".equalsIgnoreCase(command)) {
				TastDbConditions c = new TastDbConditions();
				TastDbQuery qValue = new TastDbQuery("Configuration", c);
				System.out.println("Query: " + qValue.toStringWithParams().conditionString);
				Object[] ret = qValue.executeQuery();
				for (int i = 0; i < ret.length; i++) {
					Object object = ret[i];
					System.out.println(object);
				}
			} else if ("csave".equalsIgnoreCase(command)) {
				//Configuration conf = new Configuration();
				//conf.addEntry("my1", "firstEntry_" + System.currentTimeMillis());
				//conf.addEntry("my2", "secondEntry_" + System.currentTimeMillis());
				//conf.save();
			} else if ("gis".equalsIgnoreCase(command)) {
				TastDbConditions c = new TastDbConditions();
				TastDbQuery qValue = new TastDbQuery("GISPortLocation", c);
				Object[] ret = qValue.executeQuery();
				for (int i = 0; i < ret.length && i < 20; i++) {
					Object object = ret[i];
					System.out.println(object);
				}
			}
			
			System.out.print("command:>");
		}
	}

}
