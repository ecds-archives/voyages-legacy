package edu.emory.library.tast.estimates.load;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tas.util.HibernateUtil;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.EstimatesExportRegion;
import edu.emory.library.tast.dm.EstimatesImportRegion;
import edu.emory.library.tast.dm.EstimatesNation;
import edu.emory.library.tast.dm.Location;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.EstimatesExportRegionAttribute;
import edu.emory.library.tast.dm.attributes.specific.CaseNullToZeroAttribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.DirectValue;
import edu.emory.library.tast.util.query.QueryValue;

public class EstimatesLoader {
	
	private class EstimateResponse {
		QueryValue qValue;
		int wiggleRoom;
	}
	
	private File file;

	private List estimates = new ArrayList();

	public EstimatesLoader(String fileName) {
		this.file = new File(fileName);
	}

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
	
	public void load() throws IOException {
		EstimatesPosition[] positions = EstimatesParser.parse(this.file);
		for (int i = 0; i < positions.length; i++) {
			System.out.println(positions[i]);
			int wiggleRoom = -1;
			
			if (positions[i].isExportSimpleValue()) {
				wiggleRoom = handleSimpleValue(positions[i], "slaximp", positions[i]
						.getExportFormula(), false, wiggleRoom);
			} else {
				wiggleRoom = handleConditionValue(positions[i], positions[i]
						.getExportFormula().toLowerCase().trim(), false, wiggleRoom);
			}

			if (positions[i].isImportSimpleValue()) {
				wiggleRoom = handleSimpleValue(positions[i], "slamimp", positions[i]
						.getImportFormula(), true, wiggleRoom);
			} else {
				wiggleRoom = handleConditionValue(positions[i], positions[i]
						.getImportFormula().toLowerCase().trim(), true, wiggleRoom);
			}

		}
	}

	private int handleConditionValue(EstimatesPosition position,
			String positionFormula, boolean imported, int prevWiggleRoom) {
		EstimateResponse response = this.getFunctValueQuery(position, positionFormula,
				imported, true, prevWiggleRoom, 1);

		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		Object[] voyages = response.qValue.executeQuery(session);
		System.out.println("exp direct: " + voyages.length + "; wiggle room = " + response.wiggleRoom);
		double sum = 0.0;

		EstimateResponse response1 = this.getFunctValueQuery(position, positionFormula,
				imported, false, prevWiggleRoom, 1);
		Object[] voyagesToSum = response1.qValue.executeQuery(session);
		for (int i = 0; i < voyagesToSum.length; i++) {
			Object[] row = (Object[]) voyagesToSum[i];
			if (row[2] != null) {
				sum += ((Number) row[2]).doubleValue();
			}
		}
	

		if (voyages.length > 0) {
			EstimateWeight[] weights = this.getEstimateWeights(voyages);

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

	private int handleSimpleValue(EstimatesPosition position,
			String voyageAttribute, String positionCondition, boolean imported, int prevWiggleRoom) {
		EstimateResponse response = this.getFixedValueQuery(position, voyageAttribute,
				imported, prevWiggleRoom, Double.parseDouble(position.getExportFormula()));
		
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		Object[] voyages = response.qValue.executeQuery(session);
		System.out.println("exp simple: " + voyages.length + "; wiggle room = " + response.wiggleRoom);
		// distribution of slaves...
		if (voyages.length != 0) {
			EstimateWeight[] weights = this.getEstimateWeights(voyages);
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

	private EstimateWeight[] getEstimateWeights(Object[] voyages) {
		EstimateWeight[] weights = new EstimateWeight[voyages.length];
		double total = 0;
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
		if (total != 0) {
			for (int i = 0; i < voyages.length; i++) {
				weights[i].weight = weights[i].number / total;
			}
		}
		return weights;
	}

	private EstimateResponse getFixedValueQuery(EstimatesPosition position,
			String field, boolean imported, int prevWiggleRoom, double slaves) {
		// get wiggle room for year
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
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
				new Attribute[] { Voyage.getAttribute(field) }));

		t.commit();
		session.close();
		
		EstimateResponse response = new EstimateResponse();
		response.qValue = qValue;
		response.wiggleRoom = yearWiggleRoom;
		return response;
	}

	private EstimateResponse getFunctValueQuery(EstimatesPosition position,
			String field, boolean imported, boolean useWiggleRoom, int prevWiggleRoom, double slaves) {
		// prepare conditions
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		int yearWiggleRoom = 0;
		if (useWiggleRoom) {
			if (prevWiggleRoom != -1) {
				yearWiggleRoom = prevWiggleRoom;
			}
			else {
				yearWiggleRoom = this.getYearWiggleRoom(position, imported, slaves);
			}
		}
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
		// conditions.addCondition(Voyage.getAttribute("yearam"), new Integer(
		// position.getYear()), Conditions.OP_EQUALS);
		conditions.addCondition(new SequenceAttribute(new Attribute[] {
				Voyage.getAttribute("e_natinimp"),
				EstimatesNation.getAttribute("id") }), new Long(position
				.getNatimp()), Conditions.OP_EQUALS);

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
		
		EstimateResponse response = new EstimateResponse();
		response.qValue = qValue;
		response.wiggleRoom = yearWiggleRoom;
		return response;
	}

	private int getYearWiggleRoom(EstimatesPosition position, boolean imported, double slaves) {
		int wiggleRoomExpand = 1;
		int currentWiggleRoom = -1;
		int currentResults = 0;

		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		int minVoyages = this.getMinNumberOfVoyages(slaves);

		System.out.println("Minimal number of viyages in sample: " + minVoyages);
		
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

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println("Usage: EstimatesLoader [file with estimates]");
		}

		EstimatesLoader loader = new EstimatesLoader(args[0]);
		loader.load();
		System.out.println("ESTIMATES:");
		for (int i = 0; i < loader.estimates.size(); i++) {
			System.out.println("Final estimate: " + loader.estimates.get(i));
			((Estimate) loader.estimates.get(i)).save();
		}
	}
}
