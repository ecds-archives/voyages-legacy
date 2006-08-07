package edu.emory.library.tast.misc.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;

import javax.imageio.ImageIO;

import com.sun.imageio.plugins.png.PNGImageWriterSpi;

import edu.emory.library.tast.dm.Configuration;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Slave;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.dm.attributes.CompoundAttribute;
import edu.emory.library.tast.dm.attributes.Group;
import edu.emory.library.tast.dm.attributes.ObjectType;
import edu.emory.library.tast.dm.dictionaries.Temp;
import edu.emory.library.tast.util.HibernateConnector;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.DirectValue;
import edu.emory.library.tast.util.query.QueryValue;

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
				
				Conditions conditions = new Conditions();
				conditions.addCondition("v.voyageId", new Long(1), Conditions.OP_EQUALS);
				conditions.addCondition(VoyageIndex.getApproved());
				QueryValue qValue = new QueryValue("VoyageIndex as v", conditions);
				qValue.addPopulatedAttribute("v.voyage.arrport", true);
				qValue.addPopulatedAttribute("v.voyage.shipname", false);
				
				Object[] res = qValue.executeQuery();
				
				System.out.println("Returned: " + res.length);
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
				System.out.println("Query: " + qValue.toStringWithParams().conditionString);
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
			} else if ("gis".equalsIgnoreCase(command)) {
				Conditions c = new Conditions();
				QueryValue qValue = new QueryValue("GISPortLocation", c);
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
