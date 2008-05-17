package edu.emory.library.tast.database.statistic;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.SimpleTableCell;
import edu.emory.library.tast.database.query.SearchBean;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.util.CSVUtils;
import edu.emory.library.tast.util.HibernateUtil;


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
	private TastDbConditions prevConditions = null;
	
	//search bean reference
	private SearchBean searchBean = null;
	
	//statistical elements - represent rows in statistical table
	private StatisticElement[] elements = new StatisticElement[] {};
	
	private SimpleTableCell[][] cells;
	
	MessageFormat valuesFormat = new MessageFormat("{0,number,#,###,###.0}");
	
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
			
			TastDbConditions conditions = (TastDbConditions)prevConditions.clone();
			
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
		cells = new SimpleTableCell[elements.length + 1][];
		
		cells[0] = new SimpleTableCell[5];
		cells[0][0] = new SimpleTableCell("", "search-simple-stat-h_c1", null);
		cells[0][1] = new SimpleTableCell(TastResource.getText("components_statistictab_total_slaves"), "search-simple-stat-h_c2", null);
		cells[0][2] = new SimpleTableCell(TastResource.getText("components_statistictab_total_voyages"), "search-simple-stat-h_c3", null);
		cells[0][3] = new SimpleTableCell(TastResource.getText("components_statistictab_average"), "search-simple-stat-h_c4", null);
		cells[0][4] = new SimpleTableCell(TastResource.getText("components_statistictab_deviation"), "search-simple-stat-h_c4", null);
		for (int i = 1; i < cells.length; i++) {
			cells[i] = new SimpleTableCell[5];
			cells[i][0] = new SimpleTableCell(elements[i-1].getName(), "search-simple-stat-c_c1", null);
			cells[i][1] = new SimpleTableCell(elements[i-1].getTotal(), "search-simple-stat-c_c2", null);
			cells[i][2] = new SimpleTableCell(elements[i-1].getSampleTotal(), "search-simple-stat-c_c3", null);
			cells[i][3] = new SimpleTableCell(elements[i-1].getAvrg(), "search-simple-stat-c_c4", null);
			cells[i][4] = new SimpleTableCell(elements[i-1].getDev(), "search-simple-stat-c_c4", null);
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
	private void prepareEstimate(int i, String attribute, TastDbConditions conditions, boolean showTotal, boolean percent) {
		TastDbConditions cond2 = (TastDbConditions)conditions.clone();
		cond2.addCondition(Voyage.getAttribute(attribute), null, TastDbConditions.OP_IS_NOT);
		
		NumberFormat format = DecimalFormat.getInstance();
		TastDbQuery query = new TastDbQuery(new String[] {"Voyage"}, 
				new String[] {"v"}, 
				cond2);
		query.addPopulatedAttribute(new FunctionAttribute("sum", new Attribute[] {Voyage.getAttribute(attribute)}));
		query.addPopulatedAttribute(new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("iid")}));
		query.addPopulatedAttribute(new FunctionAttribute("stddev", new Attribute[] {Voyage.getAttribute(attribute)}));
		Object[] results = query.executeQuery();
		if (results.length > 0) {
			Object[] row = (Object[])results[0];
			if (row[0] == null || row[1] == null) {
				elements[i] = new StatisticElement(statNames[i], TastResource.getText("components_statistictab_notavailable"), TastResource.getText("components_statistictab_notavailable"), TastResource.getText("components_statistictab_notavailable"), TastResource.getText("components_statistictab_notavailable"));
			} else {
				String stat = "";
				String dev = "";
				if (percent) {
					stat = valuesFormat.format(new Object[] {new Double(Math.round(((Number)row[0]).doubleValue()/((Number)row[1]).doubleValue() * 10000) / (double)100)}) + "%";
					dev = valuesFormat.format(new Object[] {new Double(Math.round(((Number)row[2]).doubleValue() * 10000) / (double)100)}) + "%";
				} else {
					stat = valuesFormat.format(new Object[] {new Double((((Number)row[0]).doubleValue()/((Number)row[1]).doubleValue() * 100) / (double) 100)});
					dev = valuesFormat.format(new Object[] {new Double((((Number)row[2]).doubleValue() * 100) / (double)100)});
				}
				if (showTotal) {	
					elements[i] = new StatisticElement(statNames[i], 
							format.format((Number)row[0]), 
							format.format((Number)row[1]),
							stat, dev);
				} else {
					elements[i] = new StatisticElement(statNames[i], 
							"",
							format.format((Number)row[1]),
							stat, dev);
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
	
	public String getFileAllData() {	
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		String[][] data = new String[this.cells.length][this.cells[0].length];		
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				data[i][j] = this.cells[i][j].getText();
			}
		}
		CSVUtils.writeResponse(session, data);
		
		t.commit();
		session.close();
		return null;
	}
	
}
