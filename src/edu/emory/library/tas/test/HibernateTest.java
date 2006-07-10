package edu.emory.library.tas.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;

//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartUtilities;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.data.category.CategoryDataset;
//import org.jfree.data.category.DefaultCategoryDataset;
//import org.jfree.data.general.DatasetGroup;

import edu.emory.library.tas.Configuration;
import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.Slave;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.VoyageIndex;
import edu.emory.library.tas.attrGroups.CompoundAttribute;
import edu.emory.library.tas.attrGroups.Group;
import edu.emory.library.tas.attrGroups.ObjectType;
import edu.emory.library.tas.dicts.PortLocation;
import edu.emory.library.tas.dicts.SecondDemPort;
import edu.emory.library.tas.dicts.Temp;
import edu.emory.library.tas.util.HibernateConnector;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.DirectValue;
import edu.emory.library.tas.util.query.QueryValue;

public class HibernateTest {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String command = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		
		HibernateConnector connector = HibernateConnector.getConnector();

		System.out.print("command:>");
		while ((command = reader.readLine()) != null
				&& !command.equalsIgnoreCase("exit")) {

			if (command.equals("store")) {
				Voyage v = Voyage.createNew(new Long(System.currentTimeMillis()));
				v.setShipname("Ship: " + v.getVoyageId().longValue());
				v.setVoyageId(v.getVoyageId());
				
				Slave slave1 = Slave.createNew(new Long(System.currentTimeMillis()));
				Slave slave2 = Slave.createNew(new Long(System.currentTimeMillis()));
				slave1.setName("Slave 1");
				slave2.setName("Slave 2");
				HashSet slaves = new HashSet();
				slaves.add(slave1);
				slaves.add(slave2);
				v.setSlaves(slaves);
				
				v.save();
			} else if (command.equals("list")) {
				try {
//					Voyage v = new Voyage();
//					v.setVoyageId(new Long(30001));
					
					long t1 = System.currentTimeMillis();
//					VoyageIndex[] list = connector.getVoyagesIndexSet(0, 100, HibernateConnector.APPROVED_AND_NOT_APPROVED & HibernateConnector.WITHOUT_HISTORY);
//					Voyage v = Voyage.loadMostRecent(new Long(2314));
					Voyage[] list = Voyage.loadAllMostRecent(0, 100);
					
					
					long t2 = System.currentTimeMillis();
					
					System.out.println("Returned: " + list.length + " time=" + (t2-t1));
					
//					if (v != null) {
//						
////						System.out.println("Event1: " + theVoyage);
//						System.out.println("Printing!");
//						System.out.println(((Slave)v.getSlaves().iterator().next()).getName());
//						System.out.println(v.getShipname() + "---" + v.getPortdep());
//					}
//					for (int i = 0; i < list.length; i++) {
//						Voyage v = list[i].getVoyage();
//						System.out.println("Printing!");
//						System.out.println(v.getShipname() + "---" + v.getPortdep());
//					}

				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			} else if (command.startsWith("modify")) {
				Long id = new Long(command.split(" ")[1]);
				Voyage v = Voyage.loadMostRecent(id);
				if (v != null) {
					v.setShipname(v.getShipname() + "-");
					v.setModified(Voyage.UPDATED);
					v.save();
				} else {
					System.out.println("Empty result for given id - cannot update");
				}
			} else if (command.equals("dictionary")) {
				Dictionary[] dicts = Dictionary.loadDictionary("Temp");
				for (int i = 0; i < dicts.length; i++) {
					System.out.println("Dict: " + dicts[i]);
				}
			} else if (command.equals("testSaving")) {
				Temp dic = new Temp();
				dic.setRemoteId(new Integer(10));
				dic.setName(System.currentTimeMillis() + "");
				dic.save();
				Voyage v = Voyage.loadMostRecent(new Long(1));
				
				if (v == null) {
					System.out.println("No object!");
				} else {
					System.out.println("After loading: " + v);
					v.setTemp(dic);
					System.out.println("Has: " + v.getAdpsale1());
					v.save();
				}
				
				Voyage v_new = Voyage.loadMostRecent(new Long(1));
				System.out.println("After loading: " + v_new);
			} else if (command.equals("limit")) {
				try {
					
					Voyage v[] = Voyage.loadMostRecent(new Long(30001), 10);

					for (int i = 0; i < v.length; i++) {
						Voyage theVoyage = v[i];
						System.out.println(theVoyage.getVoyageId() + ": " + theVoyage);
					}

				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			} else if (command.equals("query")) {
//				Conditions cMain = new Conditions(Conditions.JOIN_AND);
//				cMain.addCondition("first", "fsfs", Conditions.OP_EQUALS);
//				cMain.addCondition("second", "ddd", Conditions.OP_LIKE);
//				cMain.addCondition("third", new Integer(11), Conditions.OP_NOT_EQUALS);
//				Conditions c1 = new Conditions(Conditions.JOIN_OR);
//				c1.addCondition("first1", "fsfs", Conditions.OP_EQUALS);
//				c1.addCondition("second1", new Integer(11), Conditions.OP_NOT_EQUALS);
//				Conditions c11 = new Conditions(Conditions.JOIN_OR);
//				c11.addCondition("first11", "fsfs", Conditions.OP_EQUALS);
//				c11.addCondition("second11", new Integer(11), Conditions.OP_NOT_EQUALS);
//				c1.addCondition(c11);
//				Conditions c2 = new Conditions(Conditions.JOIN_NOT);
//				c11.addCondition("first2", "fsfs", Conditions.OP_EQUALS);
//				Conditions c21 = new Conditions(Conditions.JOIN_AND);
//				c21.addCondition("final", new Float(2.55), Conditions.OP_SMALLER);
//				c2.addCondition(c21);
				
//				cMain.addCondition(c1);
//				cMain.addCondition(c2);
				
				
				Conditions cMain = new Conditions(Conditions.JOIN_AND);
				cMain.addCondition("vi.voyageId", new Long(104), Conditions.OP_SMALLER_OR_EQUAL);
				cMain.addCondition(VoyageIndex.getRecent());
//				cMain.addCondition("v.voyage.shipname", "Pa%", Conditions.OP_LIKE);
//				Conditions cL1 = new Conditions(Conditions.JOIN_OR);
//				cMain.addCondition(cL1);
//				cL1.addCondition("v.voyage.portdep.name", "Lizbon", Conditions.OP_EQUALS);
//				cL1.addCondition("v.voyage.captaina", "Cunha%", Conditions.OP_LIKE);
				
				cMain.addCondition("vi.remoteVoyageId", new DirectValue("v.id"), Conditions.OP_EQUALS);
				QueryValue qValue = new QueryValue("VoyageIndex as vi, Voyage v", cMain);
				String [] attrs = Voyage.getAllAttrNames();
//				for (int i = 10; i < 115; i++) {
//					qValue.addPopulatedAttribute("v.voyage." + attrs[i]);
//				}
				//qValue.addPopulatedAttribute("vi.remoteVoyageId", false);
				qValue.addPopulatedAttribute("sum(v.voyage)", false);
				qValue.setGroupBy(new String[] {"v.datedep"});
//				qValue.addPopulatedAttribute("voyage.shipname");
//				qValue.addPopulatedAttribute("v.voyage.shipname", false);
//				qValue.addPopulatedAttribute("v.voyage.ownere", false);
//				qValue.addPopulatedAttribute("v.voyage.arrport", true);
				
				long t1 = System.currentTimeMillis();
				Object[] res = HibernateConnector.getConnector().loadObjects(qValue);
				long t2 = System.currentTimeMillis();
				
				System.out.println("Returned: " + res.length + " time=" + (t2-t1));
				for (int i = 0; i < res.length && i < 20; i++) {
					System.out.println(" -> " + (res[i]));
				}
			} else if (command.equals("chart")) {
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
			} else if ("tsave".equalsIgnoreCase(command)) {
				
				Conditions c1 = new Conditions();
				c1.addCondition("typeName", "Voyage", Conditions.OP_EQUALS);
				QueryValue qValue = new QueryValue("ObjectType", c1);
				ObjectType type = (ObjectType)qValue.executeQuery()[0];
				
				Group set = new Group();
				set.setName("new groupset " + System.currentTimeMillis());
				set.setObjectType(type);
				CompoundAttribute group = new CompoundAttribute();
				group.setName("new group " + System.currentTimeMillis());
				group.setObjectType(type);
				
				Conditions c = new Conditions(Conditions.JOIN_AND);
				c.addCondition("name", "captain%", Conditions.OP_LIKE);
				qValue = new QueryValue("Attribute", c);  
				Object [] aobjs = qValue.executeQuery();
				c = new Conditions(Conditions.JOIN_AND);
				c.addCondition("name", "ownera", Conditions.OP_EQUALS);
				qValue = new QueryValue("Attribute", c);
				Object[] aobjs1 = qValue.executeQuery();
				
				for (int i = 0; i < aobjs.length; i++) {
					group.getAttributes().add(aobjs[i]);
				}
				set.getAttributes().add(aobjs1[0]);
				set.getCompoundAttributes().add(group);
				HibernateConnector.getConnector().saveObject(group);
				HibernateConnector.getConnector().saveObject(set);
				
			} else if ("tload".equalsIgnoreCase(command)) {
				Conditions c = new Conditions(Conditions.JOIN_AND);
				QueryValue qValue = new QueryValue("Group", c);
				Object [] ret = qValue.executeQuery();
				for (int i = 0; i < ret.length; i++) {
					System.out.println("Has: " + ret[i]);
				}
			} else if ("cload".equalsIgnoreCase(command)) {
				Conditions c = new Conditions();
				QueryValue qValue = new QueryValue("Configuration", c);
				Object[] ret = qValue.executeQuery();
				for (int i = 0; i < ret.length; i++) {
					Object object = ret[i];
					System.out.println(object);
				}
			} else if ("csave".equalsIgnoreCase(command)) {
				Configuration conf = new Configuration();
				conf.addEntry("my1", "firstEntry_" + System.currentTimeMillis());
				conf.addEntry("my2", "secondEntry_" + System.currentTimeMillis());
				conf.save();
			}
			
			System.out.print("command:>");
		}
	}

}
