package edu.emory.library.tast.ui.search.statistic;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.ui.SimpleTableCell;
import edu.emory.library.tast.ui.search.query.SearchBean;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.DirectValue;
import edu.emory.library.tast.util.query.QueryValue;

public class StatisticBean {
	
	public static final String[] statNames = {"Slaves embarked", 
		"Slaves disembarked", "Share of slaves embarked who died during voyage", 
		"Length of Middle Passage (in days)", "Percentage male", 
		"Percentage children", "Tonnage of vessel", "Number of crew at outset"};
	
	private Conditions prevConditions = null;
	private SearchBean searchBean = null;
	private StatisticElement[] elements = new StatisticElement[] {};
	
	public SimpleTableCell[][] getStatisticElements() {
		
		if (prevConditions == null || 
				!prevConditions.equals(searchBean.getSearchParameters().getConditions())) {
			prevConditions = searchBean.getSearchParameters().getConditions();
			
			Conditions conditions = (Conditions)prevConditions.clone();
//			conditions.addCondition(VoyageIndex.getRecent());
//			conditions.addCondition(VoyageIndex.getAttribute("remoteVoyageId"), new DirectValue(Voyage.getAttribute("iid")), Conditions.OP_EQUALS);
			
			elements = new StatisticElement[7];
			
			this.prepareEstimate(0, "slaximp", conditions, true, false);
			this.prepareEstimate(1, "slamimp", conditions, true, false);
			this.prepareEstimate(2, "vymrtrat", conditions, false, true);
			this.prepareEstimate(3, "voy2imp", conditions, true, false);
			this.prepareEstimate(4, "malrat7", conditions, false, true);
			this.prepareEstimate(5, "chilrat7", conditions, false, true);
			this.prepareEstimate(6, "tonnage", conditions, true, false);
			
//			Object[] results = query.executeQuery();
//			
//			
//			
//			if (results.length > 0) {
//				results = (Object[])results[0];
//				for (int i = 0; i < results.length - 1; i++) {
//					elements[i] = new StatisticElement(statNames[i], 
//							format.format((Number)results[i]), 
//							format.format((Number)results[8]),
//							String.valueOf(Math.round(((Number)results[i]).doubleValue()/((Number)results[8]).doubleValue() * 1000) / (double)1000));
//				}
//			}
		}
		
		SimpleTableCell[][] cells = new SimpleTableCell[elements.length + 1][];
		
		cells[0] = new SimpleTableCell[4];
		cells[0][0] = new SimpleTableCell("", "search-simple-stat-h_c1", null);
		cells[0][1] = new SimpleTableCell("Total sum", "search-simple-stat-h_c2", null);
		cells[0][2] = new SimpleTableCell("Number of voyages", "search-simple-stat-h_c3", null);
		cells[0][3] = new SimpleTableCell("Average", "search-simple-stat-h_c4", null);
		for (int i = 1; i < cells.length; i++) {
			cells[i] = new SimpleTableCell[4];
			cells[i][0] = new SimpleTableCell(elements[i-1].getName(), "search-simple-stat-c_c1", null);
			cells[i][1] = new SimpleTableCell(elements[i-1].getTotal(), "search-simple-stat-c_c2", null);
			cells[i][2] = new SimpleTableCell(elements[i-1].getSampleTotal(), "search-simple-stat-c_c3", null);
			cells[i][3] = new SimpleTableCell(elements[i-1].getAvrg(), "search-simple-stat-c_c4", null);
		}
		
		return cells;
	}

	private void prepareEstimate(int i, String attribute, Conditions conditions, boolean showTotal, boolean percent) {
		Conditions cond2 = (Conditions)conditions.clone();
		cond2.addCondition(Voyage.getAttribute(attribute), null, Conditions.OP_IS_NOT);
		
		NumberFormat format = DecimalFormat.getInstance();
		QueryValue query = new QueryValue(new String[] {"Voyage"}, 
				new String[] {"v"}, 
				cond2);
		query.addPopulatedAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute(attribute)}));
		query.addPopulatedAttribute(new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("iid")}));
		Object[] results = query.executeQuery();
		if (results.length > 0) {
			Object[] row = (Object[])results[0];
			if (row[0] == null || row[1] == null) {
				elements[i] = new StatisticElement(statNames[i], "not available", "not available", "not available");
			} else {
				String stat = "";
				if (percent) {
					stat = String.valueOf(Math.round(((Number)row[0]).doubleValue()/((Number)row[1]).doubleValue())) + "%";
				} else {
					stat = String.valueOf(Math.round(((Number)row[0]).doubleValue()/((Number)row[1]).doubleValue()));						
				}
				if (showTotal) {	
					elements[i] = new StatisticElement(statNames[i], 
							format.format((Number)row[0]), 
							format.format((Number)row[1]),
							stat);
				} else {
					elements[i] = new StatisticElement(statNames[i], 
							"",
							format.format((Number)row[1]),
							stat);
				}
			}
		}
	}
	
	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}
	
}
