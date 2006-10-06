package edu.emory.library.tast.estimates.load;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.Location;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
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

			// qValue.addPopulatedAttribute(new FunctionAttribute("sum", new
			// Attribute[] {Voyage.getAttribute("slaximp")}));
			// qValue.addPopulatedAttribute(new FunctionAttribute("sum", new
			// Attribute[] {Voyage.getAttribute("slamimp")}));

			if (positions[i].isExportSimpleValue()) {
				QueryValue qValue = this.getFixedValueQuery(positions[i],
						"slaximp");
				Object[] voyages = qValue.executeQuery();
				System.out.println("exp simple: " + voyages.length);
				// distribution of slaves...
				if (voyages.length != 0) {
					EstimateWeight[] weights = this.getEstimateWeights(voyages);
					for (int j = 0; j < weights.length; j++) {
						Estimate estim = new Estimate();
						estim.setNation((ImputedNation) Dictionary
								.loadDictionaryByRemoteId("ImputedNation",
										new Integer(positions[i].getNatimp()))[0]);
						estim.setYear(String.valueOf(positions[i].getYear()));
						estim.setExpRegion(weights[j].expRegion);
						estim.setImpRegion(weights[j].impRegion);
						int index;
						if ((index = estimates.indexOf(estim)) != -1) {
							Estimate estimate = (Estimate) estimates.get(index);
							estimate.setSlavExported(estimate.getSlavExported()
									+ Double.parseDouble(positions[i].getExportFormula())*weights[j].weight);
						} else {
							estim.setSlavExported(Double.parseDouble(positions[i].getExportFormula())*weights[j].weight);
							estimates.add(estim);
						}
						System.out.println("   Added: " + estim);
					}
				}
			} else {
				QueryValue qValue = this.getFunctValueQuery(positions[i],
						positions[i].getExportFormula().toLowerCase().trim());
				Object[] voyages = qValue.executeQuery();
				System.out.println("exp direct: " + voyages.length);
				for (int j = 0; j < voyages.length; j++) {
					Object[] row = (Object[]) voyages[j];
					Estimate estim = new Estimate();
					estim.setNation((ImputedNation) Dictionary
									.loadDictionaryByRemoteId("ImputedNation",
											new Integer(positions[i].getNatimp()))[0]);
					estim.setYear(String.valueOf(positions[i].getYear()));
					estim.setExpRegion((Long) row[0]);
					estim.setImpRegion((Long) row[1]);
					int index;
					if ((index = estimates.indexOf(estim)) != -1) {
						Estimate estimate = (Estimate) estimates.get(index);
						estimate.setSlavExported(estimate.getSlavExported()
								+ ((Number) row[2]).doubleValue());
					} else {
						estim.setSlavExported(((Number) row[2]).doubleValue());
						estimates.add(estim);
					}
					System.out.println("   Added: " + estim);
				}
			}

			/*
			 * QueryValue query = new QueryValue(new String[] {"Voyage"}, new
			 * String[] {"v"}, conditions); query.addPopulatedAttribute(new
			 * FunctionAttribute("estimate", new Attribute[]
			 * {Voyage.getAttribute("iid"), new
			 * DirectValueAttribute("slaximp/100")}));
			 * 
			 * Object[] ret = query.executeQuery();
			 * System.out.println(ret.length);
			 */
			/*
			 * 
			 * 
			 * QueryValue qValue = new QueryValue(new String[] {"Voyage"}, new
			 * String[] {"v"}, conditions); qValue.setGroupBy(new Attribute[] {
			 * new SequenceAttribute(new Attribute[]
			 * {Voyage.getAttribute("majbuyrg"),
			 * Dictionary.getAttribute("id")}), new SequenceAttribute(new
			 * Attribute[] {Voyage.getAttribute("mjselimp"),
			 * Dictionary.getAttribute("id")})});
			 * 
			 * qValue.addPopulatedAttribute(new SequenceAttribute( new
			 * Attribute[] {Voyage.getAttribute("majbuyrg"),
			 * Dictionary.getAttribute("id")}));
			 * qValue.addPopulatedAttribute(new SequenceAttribute( new
			 * Attribute[] {Voyage.getAttribute("mjselimp"),
			 * Dictionary.getAttribute("id")}));
			 * qValue.addPopulatedAttribute(new FunctionAttribute("sum", new
			 * Attribute[] {Voyage.getAttribute("slaximp")}));
			 * qValue.addPopulatedAttribute(new FunctionAttribute("sum", new
			 * Attribute[] {Voyage.getAttribute("slamimp")}));
			 * 
			 * Object[] voyages = qValue.executeQuery(); if
			 * (!positions[i].isExportSimpleValue()) {
			 *  } else {
			 *  }
			 * 
			 * if (!positions[i].isImportSimpleValue()) {
			 *  } else {
			 *  }
			 */

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
			String field) {
		// get wiggle room for year
		int yearWiggleRoom = this.getYearWiggleRoom(position);

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
			Port[] ports = new Port[portIds.length];
			for (int j = 0; j < ports.length; j++) {
				ports[j] = Port.loadById(portIds[j]);
			}
			conditions.addCondition(Voyage.getAttribute("majselimp"), ports,
					Conditions.OP_IN);
		}
		conditions.addCondition(Voyage.getAttribute("yeardep"), new Integer(
				position.getYear() + yearWiggleRoom),
				Conditions.OP_SMALLER_OR_EQUAL);
		conditions.addCondition(Voyage.getAttribute("yeardep"), new Integer(
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
						Voyage.getAttribute("majbuyrg"),
						Dictionary.getAttribute("id") }),
				new SequenceAttribute(new Attribute[] {
						Voyage.getAttribute("mjselimp"),
						Dictionary.getAttribute("id") }) });

		qValue.addPopulatedAttribute(new SequenceAttribute(
				new Attribute[] { Voyage.getAttribute("majbuyrg"),
						Dictionary.getAttribute("id") }));
		qValue.addPopulatedAttribute(new SequenceAttribute(
				new Attribute[] { Voyage.getAttribute("mjselimp"),
						Dictionary.getAttribute("id") }));
		qValue.addPopulatedAttribute(new FunctionAttribute("sum",
				new Attribute[] { Voyage.getAttribute(field) }));

		return qValue;
	}

	private QueryValue getFunctValueQuery(EstimatesPosition position,
			String field) {
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
			Port[] ports = new Port[portIds.length];
			for (int j = 0; j < ports.length; j++) {
				ports[j] = Port.loadById(portIds[j]);
			}
			conditions.addCondition(Voyage.getAttribute("majselimp"), ports,
					Conditions.OP_IN);
		}
		conditions.addCondition(Voyage.getAttribute("yeardep"), new Integer(
				position.getYear()), Conditions.OP_EQUALS);
		conditions.addCondition(new SequenceAttribute(new Attribute[] {
				Voyage.getAttribute("natinimp"),
				Dictionary.getAttribute("remoteId") }), new Integer(position
				.getNatimp()), Conditions.OP_EQUALS);

		QueryValue qValue = new QueryValue(new String[] { "Voyage" },
				new String[] { "v" }, conditions);
		qValue.setGroupBy(new Attribute[] {
				new SequenceAttribute(new Attribute[] {
						Voyage.getAttribute("majbuyrg"),
						Dictionary.getAttribute("id") }),
				new SequenceAttribute(new Attribute[] {
						Voyage.getAttribute("mjselimp"),
						Dictionary.getAttribute("id") }) });

		qValue.addPopulatedAttribute(new SequenceAttribute(
				new Attribute[] { Voyage.getAttribute("majbuyrg"),
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

	private int getYearWiggleRoom(EstimatesPosition position) {
		int voyagesPerPort = 20;
		int wiggleRoomExpand = 5;
		int basicYear = position.getYear();
		if (position.getPort().length == 0
				&& position.getMajselimp().length == 0) {
			return 0;
		} else {
			if (position.getPort().length != 0) {
				// TODO add it
			} else {
				// TODO add it
			}
		}
		return 0;
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
		}
	}
}
