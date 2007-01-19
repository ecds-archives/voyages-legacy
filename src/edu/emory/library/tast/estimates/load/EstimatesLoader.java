package edu.emory.library.tast.estimates.load;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tas.util.HibernateUtil;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.EstimatesExportRegion;
import edu.emory.library.tast.dm.EstimatesImportRegion;
import edu.emory.library.tast.dm.EstimatesNation;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;


/**
 * Class generates estimate rows basing on the Final.xls file.
 * The estimate row has the following columns: nation, year, from, to, exported, imported.
 * @author juri
 *
 */
public class EstimatesLoader {
	
	private class EstimateResponse {
		QueryValue qValue;
		int wiggleRoom;
	}
	
	//Current Final.xls file
	private File file;

	//Estimates that will be generated
	private List estimates = new ArrayList();

	public EstimatesLoader(String fileName) {
		this.file = new File(fileName);
	}

	/**
	 * Function returns minimal number of voyages that is expected to get in expanding algorithm.
	 * Numbers according to information from David.
	 * #of slaves <= 200 -> 1 voyage
	 * #of slaves <= 1000 -> 3 voyages
	 * #of slaves <= 10000 -> 20 voyage
	 * #of slaves > 10000 -> 100 voyage
	 * @param slaves
	 * @return
	 */
	public int getMinNumberOfVoyages(double slaves) {
		if (slaves <= 200) {
			return 1;
		} else if (slaves <= 1000) {
			return 3;
		} else if (slaves <= 10000) {
			return 20;
		} else {
			return 100;
		}
	}
	
	/**
	 * Functionality of loading estimates. Firstly, the Final.xls file is read, 
	 * then, for each row in file, estimates are generated.
	 * Each row from Final.xls has values for exported and imported number of slaves.
	 * Also, each of values above can be either direct number (such as 100, 1600 etc.) or
	 * formula (such as 3*slaximp, 0.44*slamimp).
	 * 
	 * The load function for each row from Final.xls file runs algorithm twice (once for
	 * exported number of slaves, once for imported).   
	 * @throws IOException
	 */
	public void load() throws IOException {
		//load rows from Final.xls
		EstimatesPosition[] positions = EstimatesParser.parse(this.file);
		
		//Calculate estimates for each of estimates
		for (int i = 0; i < positions.length; i++) {
			System.out.println(positions[i]);
			int wiggleRoom = -1;
			
			//Run algorithm for export number
			if (positions[i].isExportSimpleValue()) {
				//value in Final.xls was number
				wiggleRoom = handleSimpleValue(positions[i], "slaximp", positions[i]
						.getExportFormula(), false, wiggleRoom);
			} else {
				//value in Final.xls was formula
				wiggleRoom = handleConditionValue(positions[i], positions[i]
						.getExportFormula().toLowerCase().trim(), false, wiggleRoom);
			}

			//Run algorithm for import number
			if (positions[i].isImportSimpleValue()) {
				//value in Final.xls was number
				wiggleRoom = handleSimpleValue(positions[i], "slamimp", positions[i]
						.getImportFormula(), true, wiggleRoom);
			} else {
				//value in Final.xls was formula
				wiggleRoom = handleConditionValue(positions[i], positions[i]
						.getImportFormula().toLowerCase().trim(), true, wiggleRoom);
			}

		}
	}

	/**
	 * Function computes estimated number of slaves (either exported or imported - depending on imported attributevalue) 
	 * for given estimate position (entry from Final.xls file).
	 * 
	 * @param position
	 * @param positionFormula
	 * @param imported
	 * @param prevWiggleRoom
	 * @return
	 */
	private int handleConditionValue(EstimatesPosition position,
			String positionFormula, boolean imported, int prevWiggleRoom) {
		
		//prepare query that will take into account desired wiggle room for year
		//(wiggle room will be computed based on David's suggestion)
		//Response also will contain computed wiggle room - just to save some time later
		//(not important from algorithm's point of view)
		EstimateResponse response = this.getFunctValueQuery(position, positionFormula,
				imported, true, prevWiggleRoom, 1);

		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		//Get voyages included in estimate position + wiggle room set
		Object[] voyages = response.qValue.executeQuery(session);
		System.out.println("exp direct: " + voyages.length + "; wiggle room = " + response.wiggleRoom);
		double sum = 0.0;

		//We need to know exact value of slaves (w/o wiggle room). Only this number of slaves needs
		//to be distributed among locations got in previous query (extended by wiggle room)
		EstimateResponse response1 = this.getFunctValueQuery(position, positionFormula,
				imported, false, prevWiggleRoom, 1);
		Object[] voyagesToSum = response1.qValue.executeQuery(session);
		for (int i = 0; i < voyagesToSum.length; i++) {
			Object[] row = (Object[]) voyagesToSum[i];
			if (row[2] != null) {
				sum += ((Number) row[2]).doubleValue();
			}
		}
	
		//now: sum contains number of slaves w/o wiggle room
		//now: voyages contains locations that will be taken into account
		// 		during the distribution of sum slaves
		
		if (voyages.length > 0) {
			//Compute weights for voyages.
			//Each weight contains of two regions: from and to; It also has number
			//in range 0..1 that represents percentage of slaves classified into this
			//pair of regions (sum of numbers in weights add up to 1).
			EstimateWeight[] weights = this.getEstimateWeights(voyages);

			//just create extimates and add them to list of estimates.
			for (int j = 0; j < weights.length; j++) {
				Estimate estim = new Estimate();
				estim.setNation(Nation.loadById(session, position.getNatimp()));
				estim.setYear(new Integer(position.getYear()));
				if (weights[j].expRegion != null) {
					estim.setExpRegion(EstimatesExportRegion.loadById(session, weights[j].expRegion
						.longValue()));
				}
				if (weights[j].impRegion != null) {
					estim.setImpRegion(EstimatesImportRegion.loadById(session, weights[j].impRegion
						.longValue()));
				}
				int index;
				if ((index = estimates.indexOf(estim)) != -1) {
					Estimate estimate = (Estimate) estimates.get(index);
					if (imported) {
						estimate.setSlavImported(estimate.getSlavImported()
								+ sum * weights[j].weight);
					} else {
						estimate.setSlavExported(estimate.getSlavExported()
								+ sum * weights[j].weight);
					}
				} else {
					if (imported) {
						estim.setSlavImported(sum * weights[j].weight);
					} else {
						estim.setSlavExported(sum * weights[j].weight);
					}

					estimates.add(estim);
				}
				System.out.println("   Added: " + estim);
			}
			t.commit();
			session.close();
		}
		return response.wiggleRoom;
	}

	/**
	 * Function creates estimates for estimate position with simple value.
	 * Information if given number is exported or imported number of slaves
	 * if passed as voyageAttribute attribute (can have either "slaximp" or "slamimp"). 
	 * 
	 * @param position
	 * @param voyageAttribute
	 * @param positionCondition
	 * @param imported
	 * @param prevWiggleRoom
	 * @return
	 */
	private int handleSimpleValue(EstimatesPosition position,
			String voyageAttribute, String positionCondition, boolean imported, int prevWiggleRoom) {
		
		//Need to find regions to distribute given number
		//This will prepare query and wiggle room
		EstimateResponse response = this.getFixedValueQuery(position, voyageAttribute,
				imported, prevWiggleRoom, Double.parseDouble(position.getExportFormula()));
		
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		//Execute query 
		Object[] voyages = response.qValue.executeQuery(session);
		System.out.println("exp simple: " + voyages.length + "; wiggle room = " + response.wiggleRoom);
		
		// distribution of slaves among regions got from previous query
		if (voyages.length != 0) {
			//Compute weights for voyages.
			//Each weight contains of two regions: from and to; It also has number
			//in range 0..1 that represents percentage of slaves classified into this
			//pair of regions (sum of numbers in weights add up to 1).
			EstimateWeight[] weights = this.getEstimateWeights(voyages);
			
			//Just create estimates
			for (int j = 0; j < weights.length; j++) {
				Estimate estim = new Estimate();
				estim.setNation(Nation.loadById(session, position.getNatimp()));
				estim.setYear(new Integer(position.getYear()));
				if (weights[j].expRegion != null) {
					estim.setExpRegion(EstimatesExportRegion.loadById(session, weights[j].expRegion
						.longValue()));
				} 
				if (weights[j].impRegion != null) {
					estim.setImpRegion(EstimatesImportRegion.loadById(session, weights[j].impRegion
						.longValue()));
				}
				int index;
				if ((index = estimates.indexOf(estim)) != -1) {
					Estimate estimate = (Estimate) estimates.get(index);
					if (imported) {
						estimate.setSlavImported(estimate.getSlavImported()
								+ Double.parseDouble(positionCondition)
								* weights[j].weight);
					} else {
						estimate.setSlavExported(estimate.getSlavExported()
								+ Double.parseDouble(positionCondition)
								* weights[j].weight);
					}
				} else {
					if (imported) {
						estim.setSlavImported(Double
								.parseDouble(positionCondition)
								* weights[j].weight);
					} else {
						estim.setSlavExported(Double
								.parseDouble(positionCondition)
								* weights[j].weight);
					}

					estimates.add(estim);
				}
				System.out.println("   Added: " + estim);
			}
		}
		t.commit();
		session.close();
		return response.wiggleRoom;
	}

	/**
	 * Function calculates distribution of number of slaves among pairs of from and to regions.
	 * If, for instance we have 3 rows in voyages:
	 * 	- from A to B: 10
	 * 	- from A to C: 30
	 * 	- from D to C: 70
	 * Then retuered weights will have:
	 *  - from A to B: 0.1
	 * 	- from A to C: 0.3
	 * 	- from D to C: 0.7
	 * Basing on this, number of slaves can be distributed into 3 groups as defined above.  
	 * @param voyages
	 * @return
	 */
	private EstimateWeight[] getEstimateWeights(Object[] voyages) {
		EstimateWeight[] weights = new EstimateWeight[voyages.length];
		//Sum of slave number in all rows from voyages
		double total = 0;
		//Prepare weights
		for (int i = 0; i < voyages.length; i++) {
			Object[] row = (Object[]) voyages[i];
			weights[i] = new EstimateWeight();
			weights[i].expRegion = (Long) row[0];
			weights[i].impRegion = (Long) row[1];

			if (row[2] == null) {
				weights[i].number = 0;
			} else {
				weights[i].number = ((Number) row[2]).doubleValue();
			}
			total += weights[i].number;
		}
		//Normalize weights
		if (total != 0) {
			for (int i = 0; i < voyages.length; i++) {
				weights[i].weight = weights[i].number / total;
			}
		}
		return weights;
	}

	/**
	 * Function prepares query that returns rows containing pair of regions (from, to) 
	 * and number of slaves for this pair (either imported or exported depending on field).
	 * There isn't used estimate function (query for sum of "slaximp"/"slamimp").
	 * The function uses prevWiggleRoom or calculates new one.
	 * Result of this function is used to distribute fixed number of slaves among pairs of
	 * regions (from, to).
	 * 
	 * @param position
	 * @param field
	 * @param imported
	 * @param prevWiggleRoom
	 * @param slaves
	 * @return
	 */
	private EstimateResponse getFixedValueQuery(EstimatesPosition position,
			String field, boolean imported, int prevWiggleRoom, double slaves) {
		// get wiggle room for year
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		//Calculate wiggle room (or use previous one if possible)
		int yearWiggleRoom = 0;
		if (prevWiggleRoom == -1) {
			yearWiggleRoom = this.getYearWiggleRoom(position, imported, slaves);
		} else {
			yearWiggleRoom = prevWiggleRoom;
		}

		// prepare conditions
		Conditions conditions = new Conditions();
		if (position.getPort() != null) {
			int[] portIds = position.getPort();
			Port[] ports = new Port[portIds.length];
			for (int j = 0; j < ports.length; j++) {
				ports[j] = Port.loadById(session, portIds[j]);
			}
			conditions.addCondition(Voyage.getAttribute("portdep"), ports,
					Conditions.OP_IN);
		} else if (position.getMajselimp() != null) {
			int[] portIds = position.getMajselimp();
			EstimatesImportRegion[] ports = new EstimatesImportRegion[portIds.length];
			for (int j = 0; j < ports.length; j++) {
				ports[j] = EstimatesImportRegion.loadById(session, portIds[j]);
			}
			conditions.addCondition(Voyage.getAttribute("e_mjselimp"), ports,
					Conditions.OP_IN);
		}
		conditions.addCondition(Voyage.getAttribute("yearam"), new Integer(
				position.getYear() + yearWiggleRoom),
				Conditions.OP_SMALLER_OR_EQUAL);
		conditions.addCondition(Voyage.getAttribute("yearam"), new Integer(
				position.getYear() - yearWiggleRoom),
				Conditions.OP_GREATER_OR_EQUAL);
		conditions.addCondition(new SequenceAttribute(new Attribute[] {
				Voyage.getAttribute("e_natinimp"),
				EstimatesNation.getAttribute("id") }), new Long(position
				.getNatimp()), Conditions.OP_EQUALS);
		conditions.addCondition(Voyage.getAttribute("e_mjselimp"), null, Conditions.OP_IS_NOT);
		conditions.addCondition(Voyage.getAttribute("e_majbyimp"), null, Conditions.OP_IS_NOT);
		
		//Prepare full query
		QueryValue qValue = new QueryValue(new String[] { "Voyage" },
				new String[] { "v" }, conditions);
		qValue.setGroupBy(new Attribute[] {
				new SequenceAttribute(new Attribute[] {
						Voyage.getAttribute("e_majbyimp"),
						EstimatesExportRegion.getAttribute("id") }),
				new SequenceAttribute(new Attribute[] {
						Voyage.getAttribute("e_mjselimp"),
						EstimatesImportRegion.getAttribute("id") }) });

		//Populate pair of regions (from, to) and sum of field attribute for each of pairs
		qValue.addPopulatedAttribute(new SequenceAttribute(
				new Attribute[] { Voyage.getAttribute("e_majbyimp"),
						EstimatesExportRegion.getAttribute("id") }));
		qValue.addPopulatedAttribute(new SequenceAttribute(
				new Attribute[] { Voyage.getAttribute("e_mjselimp"),
						EstimatesImportRegion.getAttribute("id") }));
		qValue.addPopulatedAttribute(new FunctionAttribute("sum",
				new Attribute[] { Voyage.getAttribute(field) }));

		t.commit();
		session.close();
		
		//Prepare response
		EstimateResponse response = new EstimateResponse();
		response.qValue = qValue;
		response.wiggleRoom = yearWiggleRoom;
		return response;
	}

	/**
	 * Function prepares query that returns rows containing pair of regions (from, to) 
	 * and number of slaves for this pair (either imported or exported depending on field).
	 * There IS used estimate function (e.g. query for sum of "0.2*slaximp"/"1.5*slamimp").
	 * The function uses prevWiggleRoom or calculates new one.
	 * Result of this function is used to distribute fixed number of slaves among pairs of
	 * regions (from, to).
	 * 
	 * @param position
	 * @param field
	 * @param imported
	 * @param useWiggleRoom
	 * @param prevWiggleRoom
	 * @param slaves
	 * @return
	 */
	private EstimateResponse getFunctValueQuery(EstimatesPosition position,
			String field, boolean imported, boolean useWiggleRoom, int prevWiggleRoom, double slaves) {
		// prepare conditions
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		//Get appropriate wiggle room (if should use one).
		int yearWiggleRoom = 0;
		if (useWiggleRoom) {
			if (prevWiggleRoom != -1) {
				yearWiggleRoom = prevWiggleRoom;
			}
			else {
				yearWiggleRoom = this.getYearWiggleRoom(position, imported, slaves);
			}
		}
		
		//Prepare conditions
		Conditions conditions = new Conditions();
		if (position.getPort() != null) {
			int[] portIds = position.getPort();
			Port[] ports = new Port[portIds.length];
			for (int j = 0; j < ports.length; j++) {
				ports[j] = Port.loadById(session, portIds[j]);
			}
			conditions.addCondition(Voyage.getAttribute("portdep"), ports,
					Conditions.OP_IN);
		} else if (position.getMajselimp() != null) {
			int[] portIds = position.getMajselimp();
			EstimatesImportRegion[] ports = new EstimatesImportRegion[portIds.length];
			for (int j = 0; j < ports.length; j++) {
				ports[j] = EstimatesImportRegion.loadById(session, portIds[j]);
			}
			conditions.addCondition(Voyage.getAttribute("e_mjselimp"), ports,
					Conditions.OP_IN);
		}
		conditions.addCondition(Voyage.getAttribute("yearam"), new Integer(
				position.getYear() + yearWiggleRoom),
				Conditions.OP_SMALLER_OR_EQUAL);
		conditions.addCondition(Voyage.getAttribute("yearam"), new Integer(
				position.getYear() - yearWiggleRoom),
				Conditions.OP_GREATER_OR_EQUAL);
		conditions.addCondition(new SequenceAttribute(new Attribute[] {
				Voyage.getAttribute("e_natinimp"),
				EstimatesNation.getAttribute("id") }), new Long(position.getNatimp()), Conditions.OP_EQUALS);
		if (useWiggleRoom) {
			conditions.addCondition(Voyage.getAttribute("e_mjselimp"), null, Conditions.OP_IS_NOT);
			conditions.addCondition(Voyage.getAttribute("e_majbyimp"), null, Conditions.OP_IS_NOT);
		}
		
		//Prepare query
		QueryValue qValue = new QueryValue(new String[] { "Voyage" },
				new String[] { "v" }, conditions);
		qValue.setGroupBy(new Attribute[] {
				new SequenceAttribute(new Attribute[] {
						Voyage.getAttribute("e_majbyimp"),
						EstimatesExportRegion.getAttribute("id") }),
				new SequenceAttribute(new Attribute[] {
						Voyage.getAttribute("e_mjselimp"),
						EstimatesImportRegion.getAttribute("id") }) });

		qValue.addPopulatedAttribute(new SequenceAttribute(
				new Attribute[] { Voyage.getAttribute("e_majbyimp"),
						EstimatesExportRegion.getAttribute("id") }));
		qValue.addPopulatedAttribute(new SequenceAttribute(
				new Attribute[] { Voyage.getAttribute("e_mjselimp"),
						EstimatesImportRegion.getAttribute("id") }));
		qValue.addPopulatedAttribute(new FunctionAttribute("sum",
				new Attribute[] { new FunctionAttribute("estimate",
						new Attribute[] { Voyage.getAttribute("iid"),
								new DirectValueAttribute(field) }) }));
		

		t.commit();
		session.close();
		
		//Prepare response
		EstimateResponse response = new EstimateResponse();
		response.qValue = qValue;
		response.wiggleRoom = yearWiggleRoom;
		return response;
	}

	/**
	 * Function calculates wiggle room for given estimate position.
	 * @param position
	 * @param imported
	 * @param slaves
	 * @return
	 */
	private int getYearWiggleRoom(EstimatesPosition position, boolean imported, double slaves) {
		int wiggleRoomExpand = 1;
		int currentWiggleRoom = -1;
		int currentResults = 0;

		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		//Get minimal number of voyages for current number of slaves. 
		int minVoyages = this.getMinNumberOfVoyages(slaves);

		System.out.println("Minimal number of viyages in sample: " + minVoyages);
		
		//Prepare regions/ports for condition
		Object inP = null;
		Object inR = null;
		if (position.getPort() != null) {
			int[] portIds = position.getPort();
			Port[] ports = new Port[portIds.length];
			for (int j = 0; j < ports.length; j++) {
				ports[j] = Port.loadById(session, portIds[j]);
			}
			inP = ports;
		} else if (position.getMajselimp() != null) {
			int[] portIds = position.getMajselimp();
			EstimatesImportRegion[] ports = new EstimatesImportRegion[portIds.length];
			for (int j = 0; j < ports.length; j++) {
				ports[j] = EstimatesImportRegion.loadById(session, portIds[j]);
			}
			inR = ports;
		}

		//Find wiggle room
		while (currentWiggleRoom < 200 && currentResults < minVoyages) {
			currentWiggleRoom += wiggleRoomExpand;
			Conditions conditions = new Conditions();
			if (inP != null) {
				conditions.addCondition(Voyage.getAttribute("portdep"), inP,
						Conditions.OP_IN);
			}
			if (inR != null) {
				conditions.addCondition(Voyage.getAttribute("e_mjselimp"), inR,
						Conditions.OP_IN);
			}

			conditions.addCondition(Voyage.getAttribute("yearam"), new Integer(
					position.getYear() + currentWiggleRoom),
					Conditions.OP_SMALLER_OR_EQUAL);
			conditions.addCondition(Voyage.getAttribute("yearam"), new Integer(
					position.getYear() - currentWiggleRoom),
					Conditions.OP_GREATER_OR_EQUAL);
			conditions.addCondition(new SequenceAttribute(new Attribute[] {
					Voyage.getAttribute("e_natinimp"),
					EstimatesNation.getAttribute("id") }), new Long(
					position.getNatimp()), Conditions.OP_EQUALS);
			conditions.addCondition(Voyage.getAttribute("e_mjselimp"), null, Conditions.OP_IS_NOT);
			conditions.addCondition(Voyage.getAttribute("e_majbyimp"), null, Conditions.OP_IS_NOT);

			QueryValue qValue = new QueryValue(new String[] { "Voyage" },
					new String[] { "v" }, conditions);
			qValue.addPopulatedAttribute(new FunctionAttribute("count",
					new Attribute[] { Voyage.getAttribute("iid") }));
			Object[] ret = qValue.executeQuery(session);
			currentResults = ((Long) ret[0]).intValue();
		}
		t.commit();
		session.close();
		
		System.out
				.println("   Computed year wiggle room: " + currentWiggleRoom);
		return currentWiggleRoom;

	}

	/**
	 * Just main function. Runs load() function of loader.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println("Usage: EstimatesLoader [file with estimates]");
		}

		EstimatesLoader loader = new EstimatesLoader(args[0]);
		loader.load();
		System.out.println("ESTIMATES:");
		for (int i = 0; i < loader.estimates.size(); i++) {
			System.out.println("Final estimate: " + loader.estimates.get(i));
			//UNCOMMENT BELOW TO SAVE ESTIMATES INTO DB
			//((Estimate) loader.estimates.get(i)).save();
		}
	}
}
