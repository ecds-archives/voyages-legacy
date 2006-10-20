package edu.emory.library.tast.estimates.load;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Location;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.CaseNullToZeroAttribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.dm.dictionaries.ImputedNation;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.DirectValue;
import edu.emory.library.tast.util.query.QueryValue;

public class EstimatesLoader {

	private File file;

	private List estimates = new ArrayList();

	public EstimatesLoader(String fileName) {
		this.file = new File(fileName);
	}

	public void load() throws IOException {
		EstimatesPosition[] positions = EstimatesParser.parse(this.file);
		for (int i = 0; i < positions.length; i++) {
			System.out.println(positions[i]);

			if (positions[i].isExportSimpleValue()) {
				handleSimpleValue(positions[i], "slaximp", positions[i]
						.getExportFormula(), false);
			} else {
				handleConditionValue(positions[i], positions[i]
						.getExportFormula().toLowerCase().trim(), false);
			}

			if (positions[i].isImportSimpleValue()) {
				handleSimpleValue(positions[i], "slamimp", positions[i]
						.getImportFormula(), true);
			} else {
				handleConditionValue(positions[i], positions[i]
						.getImportFormula().toLowerCase().trim(), true);
			}

		}
	}

	private void handleConditionValue(EstimatesPosition position,
			String positionFormula, boolean imported) {
		QueryValue qValue = this.getFunctValueQuery(position, positionFormula,
				imported, true);
		// System.out.println("condition: " +
		// qValue.toStringWithParams().conditionString);
		// System.out.println("params: " +
		// qValue.toStringWithParams().properties);
		Object[] voyages = qValue.executeQuery();
		System.out.println("exp direct: " + voyages.length);
		double sum = 0.0;

		QueryValue qValueTmp = this.getFunctValueQuery(position, positionFormula,
				imported, false);
		Object[] voyagesToSum = qValueTmp.executeQuery();
		for (int i = 0; i < voyagesToSum.length; i++) {
			Object[] row = (Object[]) voyagesToSum[i];
			if (row[2] != null) {
				sum += ((Number) row[2]).doubleValue();
			}
		}
	

		if (voyages.length > 0) {
			EstimateWeight[] weights = this.getEstimateWeights(voyages);

			// for (int j = 0; j < voyages.length; j++) {
			//
			// Object[] row = (Object[]) voyages[j];
			// Estimate estim = new Estimate();
			// estim.setNation((Nation) Nation.loadById(position.getNatimp()));
			// estim.setYear(new Integer(position.getYear()));
			// estim
			// .setExpRegion(Region.loadById(((Long) row[0])
			// .longValue()));
			// estim
			// .setImpRegion(Region.loadById(((Long) row[1])
			// .longValue()));
			// int index;
			// if ((index = estimates.indexOf(estim)) != -1) {
			// Estimate estimate = (Estimate) estimates.get(index);
			// if (row[2] != null) {
			// if (imported) {
			// estimate.setSlavImported(estimate.getSlavImported()
			// + ((Number) row[2]).doubleValue());
			// } else {
			// estimate.setSlavExported(estimate.getSlavExported()
			// + ((Number) row[2]).doubleValue());
			// }
			// }
			// } else {
			// if (row[2] != null) {
			// if (imported) {
			// estim.setSlavImported(((Number) row[2])
			// .doubleValue());
			// } else {
			// estim.setSlavExported(((Number) row[2])
			// .doubleValue());
			// }
			// }
			// estimates.add(estim);
			// }
			// System.out.println(" Added: " + estim);

			for (int j = 0; j < weights.length; j++) {
				Estimate estim = new Estimate();
				estim.setNation(Nation.loadById(position.getNatimp()));
				estim.setYear(new Integer(position.getYear()));
				if (weights[j].expRegion != null) {
					estim.setExpRegion(Region.loadById(weights[j].expRegion
						.longValue()));
				}
				if (weights[j].impRegion != null) {
					estim.setImpRegion(Region.loadById(weights[j].impRegion
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

		}
	}

	private void handleSimpleValue(EstimatesPosition position,
			String voyageAttribute, String positionCondition, boolean imported) {
		QueryValue qValue = this.getFixedValueQuery(position, voyageAttribute,
				imported);
		Object[] voyages = qValue.executeQuery();
		System.out.println("exp simple: " + voyages.length);
		// distribution of slaves...
		if (voyages.length != 0) {
			EstimateWeight[] weights = this.getEstimateWeights(voyages);
			for (int j = 0; j < weights.length; j++) {
				Estimate estim = new Estimate();
				estim.setNation(Nation.loadById(position.getNatimp()));
				estim.setYear(new Integer(position.getYear()));
				if (weights[j].expRegion != null) {
					estim.setExpRegion(Region.loadById(weights[j].expRegion
						.longValue()));
				} 
				if (weights[j].impRegion != null) {
					estim.setImpRegion(Region.loadById(weights[j].impRegion
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

	private QueryValue getFixedValueQuery(EstimatesPosition position,
			String field, boolean imported) {
		// get wiggle room for year
		int yearWiggleRoom = this.getYearWiggleRoom(position, imported);

		// prepare conditions
		Conditions conditions = new Conditions();
		if (position.getPort() != null) {
			int[] portIds = position.getPort();
			Port[] ports = new Port[portIds.length];
			for (int j = 0; j < ports.length; j++) {
				ports[j] = Port.loadById(portIds[j]);
			}
			conditions.addCondition(Voyage.getAttribute("portdep"), ports,
					Conditions.OP_IN);
		} else if (position.getMajselimp() != null) {
			int[] portIds = position.getMajselimp();
			Region[] ports = new Region[portIds.length];
			for (int j = 0; j < ports.length; j++) {
				ports[j] = Region.loadById(portIds[j]);
			}
			conditions.addCondition(Voyage.getAttribute("mjselimp"), ports,
					Conditions.OP_IN);
		}
		conditions.addCondition(Voyage.getAttribute("yearam"), new Integer(
				position.getYear() + yearWiggleRoom),
				Conditions.OP_SMALLER_OR_EQUAL);
		conditions.addCondition(Voyage.getAttribute("yearam"), new Integer(
				position.getYear() - yearWiggleRoom),
				Conditions.OP_GREATER_OR_EQUAL);
		conditions.addCondition(new SequenceAttribute(new Attribute[] {
				Voyage.getAttribute("natinimp"),
				Dictionary.getAttribute("remoteId") }), new Integer(position
				.getNatimp()), Conditions.OP_EQUALS);

		QueryValue qValue = new QueryValue(new String[] { "Voyage" },
				new String[] { "v" }, conditions);
		qValue.setGroupBy(new Attribute[] {
				new SequenceAttribute(new Attribute[] {
						Voyage.getAttribute("majbyimp"),
						Dictionary.getAttribute("id") }),
				new SequenceAttribute(new Attribute[] {
						Voyage.getAttribute("mjselimp"),
						Dictionary.getAttribute("id") }) });

		qValue.addPopulatedAttribute(new SequenceAttribute(
				new Attribute[] { Voyage.getAttribute("majbyimp"),
						Dictionary.getAttribute("id") }));
		qValue.addPopulatedAttribute(new SequenceAttribute(
				new Attribute[] { Voyage.getAttribute("mjselimp"),
						Dictionary.getAttribute("id") }));
		qValue.addPopulatedAttribute(new FunctionAttribute("sum",
				new Attribute[] { Voyage.getAttribute(field) }));

		return qValue;
	}

	private QueryValue getFunctValueQuery(EstimatesPosition position,
			String field, boolean imported, boolean useWiggleRoom) {
		// prepare conditions
		int yearWiggleRoom = 0;
		if (useWiggleRoom) {
			yearWiggleRoom = this.getYearWiggleRoom(position, imported);
		}
		Conditions conditions = new Conditions();
		if (position.getPort() != null) {
			int[] portIds = position.getPort();
			Port[] ports = new Port[portIds.length];
			for (int j = 0; j < ports.length; j++) {
				ports[j] = Port.loadById(portIds[j]);
			}
			conditions.addCondition(Voyage.getAttribute("portdep"), ports,
					Conditions.OP_IN);
		} else if (position.getMajselimp() != null) {
			int[] portIds = position.getMajselimp();
			Region[] ports = new Region[portIds.length];
			for (int j = 0; j < ports.length; j++) {
				ports[j] = Region.loadById(portIds[j]);
			}
			conditions.addCondition(Voyage.getAttribute("mjselimp"), ports,
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
				Voyage.getAttribute("natinimp"),
				Dictionary.getAttribute("remoteId") }), new Integer(position
				.getNatimp()), Conditions.OP_EQUALS);

		QueryValue qValue = new QueryValue(new String[] { "Voyage" },
				new String[] { "v" }, conditions);
		qValue.setGroupBy(new Attribute[] {
				new SequenceAttribute(new Attribute[] {
						Voyage.getAttribute("majbyimp"),
						Dictionary.getAttribute("id") }),
				new SequenceAttribute(new Attribute[] {
						Voyage.getAttribute("mjselimp"),
						Dictionary.getAttribute("id") }) });

		qValue.addPopulatedAttribute(new SequenceAttribute(
				new Attribute[] { Voyage.getAttribute("majbyimp"),
						Dictionary.getAttribute("id") }));
		qValue.addPopulatedAttribute(new SequenceAttribute(
				new Attribute[] { Voyage.getAttribute("mjselimp"),
						Dictionary.getAttribute("id") }));
		qValue.addPopulatedAttribute(new FunctionAttribute("sum",
				new Attribute[] { new FunctionAttribute("estimate",
						new Attribute[] { Voyage.getAttribute("iid"),
								new DirectValueAttribute(field) }) }));

		return qValue;
	}

	private int getYearWiggleRoom(EstimatesPosition position, boolean imported) {
		int wiggleRoomExpand = 1;
		int currentWiggleRoom = -1;
		int currentResults = 0;
		// if (position.getMajselimp() == null
		// || position.getMajselimp().length == 0) {
		// return 0;
		// } else {
		// if (position.getMajselimp().length > 1) {
		// int[] portIds = position.getMajselimp();
		// Region[] ports = new Region[portIds.length];
		// for (int j = 0; j < ports.length; j++) {
		// ports[j] = Region.loadById(portIds[j]);
		// }
		Object inP = null;
		Object inR = null;
		if (position.getPort() != null) {
			int[] portIds = position.getPort();
			Port[] ports = new Port[portIds.length];
			for (int j = 0; j < ports.length; j++) {
				ports[j] = Port.loadById(portIds[j]);
			}
			inP = ports;
		} else if (position.getMajselimp() != null) {
			int[] portIds = position.getMajselimp();
			Region[] ports = new Region[portIds.length];
			for (int j = 0; j < ports.length; j++) {
				ports[j] = Region.loadById(portIds[j]);
			}
			inR = ports;
		}

		while (currentWiggleRoom < 20 && currentResults < 20) {
			currentWiggleRoom += wiggleRoomExpand;
			Conditions conditions = new Conditions();
			if (inP != null) {
				conditions.addCondition(Voyage.getAttribute("portdep"), inP,
						Conditions.OP_IN);
			}
			if (inR != null) {
				conditions.addCondition(Voyage.getAttribute("mjselimp"), inR,
						Conditions.OP_IN);
			}

			conditions.addCondition(Voyage.getAttribute("yearam"), new Integer(
					position.getYear() + currentWiggleRoom),
					Conditions.OP_SMALLER_OR_EQUAL);
			conditions.addCondition(Voyage.getAttribute("yearam"), new Integer(
					position.getYear() - currentWiggleRoom),
					Conditions.OP_GREATER_OR_EQUAL);
			conditions.addCondition(new SequenceAttribute(new Attribute[] {
					Voyage.getAttribute("natinimp"),
					Dictionary.getAttribute("remoteId") }), new Integer(
					position.getNatimp()), Conditions.OP_EQUALS);

			QueryValue qValue = new QueryValue(new String[] { "Voyage" },
					new String[] { "v" }, conditions);
			qValue.addPopulatedAttribute(new FunctionAttribute("count",
					new Attribute[] { Voyage.getAttribute("iid") }));
			Object[] ret = qValue.executeQuery();
			currentResults = ((Integer) ret[0]).intValue();
		}
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
