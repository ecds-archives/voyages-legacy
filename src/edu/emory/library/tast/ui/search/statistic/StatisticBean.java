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
			
			QueryValue query = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, 
					new String[] {"vi", "v"}, 
					conditions);
			
			query.addPopulatedAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slaximp")}));
			query.addPopulatedAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("slamimp")}));
			query.addPopulatedAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("vymrtrat")}));
			query.addPopulatedAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("voy2imp")}));
			query.addPopulatedAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("malrat7")}));
			query.addPopulatedAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("chilrat7")}));
			query.addPopulatedAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("tonmod")}));
			query.addPopulatedAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute("crew1")}));
			query.addPopulatedAttribute(new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("voyageId")}));
			
			Object[] results = query.executeQuery();
			
			elements = new StatisticElement[8];
			
			NumberFormat format = DecimalFormat.getInstance();
			if (results.length > 0) {
				results = (Object[])results[0];
				for (int i = 0; i < results.length - 1; i++) {
					elements[i] = new StatisticElement(statNames[i], 
							format.format((Number)results[i]), 
							format.format((Number)results[8]),
							String.valueOf(Math.round(((Number)results[i]).doubleValue()/((Number)results[8]).doubleValue() * 1000) / (double)1000));
				}
			}
		}
		
		return elements;
	}

	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}
	
}
