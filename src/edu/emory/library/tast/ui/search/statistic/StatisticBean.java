package edu.emory.library.tast.ui.search.statistic;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
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
	
	public StatisticElement[] getStatisticElements() {
		
		if (prevConditions == null || 
				!prevConditions.equals(searchBean.getSearchParameters().getConditions())) {
			prevConditions = searchBean.getSearchParameters().getConditions();
			
			Conditions conditions = (Conditions)prevConditions.clone();
			conditions.addCondition(VoyageIndex.getRecent());
			conditions.addCondition(VoyageIndex.getAttribute("remoteVoyageId"), new DirectValue(Voyage.getAttribute("iid")), Conditions.OP_EQUALS);
			
			elements = new StatisticElement[8];
			
			this.prepareEstimate(0, "slaximp", conditions);
			this.prepareEstimate(1, "slamimp", conditions);
			this.prepareEstimate(2, "vymrtrat", conditions);
			this.prepareEstimate(3, "voy2imp", conditions);
			this.prepareEstimate(4, "malrat7", conditions);
			this.prepareEstimate(5, "chilrat7", conditions);
			this.prepareEstimate(6, "tonmod", conditions);
			
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
		
		return elements;
	}

	private void prepareEstimate(int i, String attribute, Conditions conditions) {
		Conditions cond2 = (Conditions)conditions.clone();
		cond2.addCondition(Voyage.getAttribute(attribute), null, Conditions.OP_IS_NOT);
		
		NumberFormat format = DecimalFormat.getInstance();
		QueryValue query = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, 
				new String[] {"vi", "v"}, 
				cond2);
		query.addPopulatedAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute(attribute)}));
		query.addPopulatedAttribute(new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("voyageId")}));
		Object[] results = query.executeQuery();
		if (results.length > 0) {
			Object[] row = (Object[])results[0];
			elements[i] = new StatisticElement(statNames[i], 
					format.format((Number)row[0]), 
					format.format((Number)row[1]),
					String.valueOf(Math.round(((Number)row[0]).doubleValue()/((Number)row[1]).doubleValue() * 1000) / (double)1000));
		}
	}
	
	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}
	
}
