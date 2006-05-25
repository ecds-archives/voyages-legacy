package edu.emory.library.tas.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.Slave;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.VoyageIndex;
import edu.emory.library.tas.util.HibernateConnector;

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
					Voyage v = new Voyage();
					v.setVoyageId(new Long(1));
					
					VoyageIndex[] list = connector.getVoyageIndexByVoyage(null, HibernateConnector.APPROVED_AND_NOT_APPROVED & HibernateConnector.WITHOUT_HISTORY);

					for (int i = 0; i < list.length; i++) {
						Object theVoyage = list[i];
						System.out.println("Event1: " + theVoyage);
					}

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
				Dictionary[] dicts = Dictionary.loadDictionary("PortLocation");
				for (int i = 0; i < dicts.length; i++) {
					System.out.println("Dict: " + dicts[i]);
				}
			}
			
			System.out.print("command:>");
		}
	}

}
