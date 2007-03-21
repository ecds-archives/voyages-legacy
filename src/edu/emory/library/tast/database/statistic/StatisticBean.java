package edu.emory.library.tast.database.statistic;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.SimpleTableCell;
import edu.emory.library.tast.database.query.SearchBean;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;


/**
 * The bean is used to generate statistic table for voyages. It is used in
 * database/search-tab-basic-stats.jsp. The bean is connected to search bean
 * which provides current conditions that should be satisfied by voyages taken
 * into account when statistics are calculated.
 *
 */
public class StatisticBean {
	
	//names for statistics - user will see those labels in first column of statistical table
	public static final String[] statNames = {
		TastResource.getText("components_statistictab_slavemb"), 
		TastResource.getText("components_statistictab_slavdisemb"), 
		TastResource.getText("components_statistictab_slavdead"), 
		TastResource.getText("components_statistictab_lenmiddlepass"), 
		TastResource.getText("components_statistictab_percentmale"), 
		TastResource.getText("components_statistictab_percentchil"), 
		TastResource.getText("components_statistictab_tonnagevessel"), 
		TastResource.getText("components_statistictab_crewatoutset")};
	
	//previously used conditions
	private Conditions prevConditions = null;
	
	//search bean reference
	private SearchBean searchBean = null;
	
	//statistical elements - represent rows in statistical table
	private StatisticElement[] elements = new StatisticElement[] {};
	
	/**
	 * This method queries the database if required and calculates statistics
	 * defined for voyages.
	 * @return
	 */
	public SimpleTableCell[][] getStatisticElements() {
		
		//if required, prepare query for statistics and query the database
		if (prevConditions == null || 
				!prevConditions.equals(searchBean.getSearchParameters().getConditions())) {
			prevConditions = searchBean.getSearchParameters().getConditions();
			
			Conditions conditions = (Conditions)prevConditions.clone();
			
			elements = new StatisticElement[7];
			
			//fill in elements with appropriate statistical elements
			this.prepareEstimate(0, "slaximp", conditions, true, false);
			this.prepareEstimate(1, "slamimp", conditions, true, false);
			this.prepareEstimate(2, "vymrtrat", conditions, false, true);
			this.prepareEstimate(3, "voy2imp", conditions, false, false);
			this.prepareEstimate(4, "malrat7", conditions, false, true);
			this.prepareEstimate(5, "chilrat7", conditions, false, true);
			this.prepareEstimate(6, "tonnage", conditions, false, false);
			
		}
		
		//prepare cells in statistical table
		SimpleTableCell[][] cells = new SimpleTableCell[elements.length + 1][];
		
		cells[0] = new SimpleTableCell[4];
		cells[0][0] = new SimpleTableCell("", "search-simple-stat-h_c1", null);
		cells[0][1] = new SimpleTableCell(TastResource.getText("components_statistictab_total_slaves"), "search-simple-stat-h_c2", null);
		cells[0][2] = new SimpleTableCell(TastResource.getText("components_statistictab_total_voyages"), "search-simple-stat-h_c3", null);
		cells[0][3] = new SimpleTableCell(TastResource.getText("components_statistictab_average"), "search-simple-stat-h_c4", null);
		for (int i = 1; i < cells.length; i++) {
			cells[i] = new SimpleTableCell[4];
			cells[i][0] = new SimpleTableCell(elements[i-1].getName(), "search-simple-stat-c_c1", null);
			cells[i][1] = new SimpleTableCell(elements[i-1].getTotal(), "search-simple-stat-c_c2", null);
			cells[i][2] = new SimpleTableCell(elements[i-1].getSampleTotal(), "search-simple-stat-c_c3", null);
			cells[i][3] = new SimpleTableCell(elements[i-1].getAvrg(), "search-simple-stat-c_c4", null);
		}
		
		return cells;
	}

	/**
	 * The method fills in ith element of elements table.
	 * It perpares query for given attribute, queries the database
	 * and creates StatisticElement
	 * @param i
	 * @param attribute
	 * @param conditions
	 * @param showTotal
	 * @param percent
	 */
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
				elements[i] = new StatisticElement(statNames[i], TastResource.getText("components_statistictab_notavailable"), TastResource.getText("components_statistictab_notavailable"), TastResource.getText("components_statistictab_notavailable"));
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
	
	/**
	 * Called by JSF mechanisms - sets current instance of search bean.
	 * @param searchBean
	 */
	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}
	
}
