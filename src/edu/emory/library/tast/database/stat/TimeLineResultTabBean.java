package edu.emory.library.tast.database.stat;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.util.CSVUtils;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.EventLineEvent;
import edu.emory.library.tast.common.EventLineGraph;
import edu.emory.library.tast.common.EventLineLabel;
import edu.emory.library.tast.common.EventLineZoomLevel;
import edu.emory.library.tast.database.query.SearchBean;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * Bean for time line.
 * The bean has a reference to search bean which provides current conditions that should be
 * used while database is queried. It is used in database/search-tab-graph.jsp.
 * Basically, this bean runs different queries on database (depending on statistic type
 * that is chosen on web iunterface) and generates yearly distribution of this data.
 * 
 * The implementation of time-line does not use JFreeChart package. It provides
 * data that is directly used by TimeLineComponent. For more details, please refer to
 * documentation of TimeLine component.
 *
 */
public class TimeLineResultTabBean {

	/**
	 * List of voyage attributes.
	 */
	private List voyageAttributes;

	/**
	 * Chosen attribute name.
	 */
	private StatOption chosenOption = null;

	/**
	 * Current search bean reference.
	 */
	private SearchBean searchBean;
	
	/**
	 * Conditions used in query last time.
	 */
	private Conditions conditions = null;
	
	/**
	 * Need of query indication.
	 */
	private boolean needQuery = false;

	/**
	 * Attributes changed indication.
	 */
	private boolean attributesChanged = false;

	//Event line series
	private EventLineGraph graph;
	
	//viewport height
	private double viewportHeight;

	/**
	 * Avaialable voyage attributes.
	 */
	
	//Labels for certical axis 
	private EventLineLabel[] verticalLabels;

	//list of available statistics - filled in in constructor
	private ArrayList availableStats;

	/**
	 * Class that represents available statistics.
	 * Each statistic has a user label which represents string visible to user,
	 * sqlValue (sql formula that should be executed) and sqlWhere which defines rows for which 
	 * statistic can be computed. I also provide information about format that should be used by output values.
	 *
	 */
	private class StatOption
	{

		public String userLabel;
		public String sqlValue;
		public Object sqlWhere = null;
		public String formatString;
		public Attribute attributeValue;
		
		public StatOption(String sqlValue, Attribute valueAttribute, String userLabel, String formatString)
		{
			this.sqlValue = sqlValue;
			this.attributeValue = valueAttribute;
			this.userLabel = userLabel;
			this.formatString = formatString;
		}

	}
	
	/**
	 * Default constructor.
	 * This constructor fills in available statistics by putting into availableStats 
	 * different StatOptions.
	 */
	public TimeLineResultTabBean() {
		
		
		this.availableStats = new ArrayList();
		
		this.availableStats.add(new StatOption(
				"COUNT(voyageid)",
				new FunctionAttribute("COUNT", new Attribute[] {Voyage.getAttribute("voyageid")}),
				TastResource.getText("components_timeline_stat_numofvoyages"),
				"{0,number,#,###,###}"));
		
		this.availableStats.add(new StatOption(
				"AVG(tonnage)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("tonnage")}),
				TastResource.getText("components_timeline_stat_averagetonnage"),
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(tonmod)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("tonmod")}),
				TastResource.getText("components_timeline_stat_averagetonnagestand"),
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(guns)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("guns")}),
				TastResource.getText("components_timeline_stat_averageguns"),
				"{0,number,#,###,###.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(LEAST(COALESCE(resistance, 0), 1)) * 100",
				new FunctionAttribute("AVG", new Attribute[] {new FunctionAttribute("crop_to_0_100", new Attribute[] {Voyage.getAttribute("resistance")})}),
				TastResource.getText("components_timeline_stat_rateresistance"),
				"{0,number,#,###,##0.0}%"));

		this.availableStats.add(new StatOption(
				"AVG(voy1imp)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("voy1imp")}),
				TastResource.getText("components_timeline_stat_averagedurationfirst"),
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(voy2imp)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("voy2imp")}),
				TastResource.getText("components_timeline_stat_averagedurationmiddle"),
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(crew1)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("tonnage")}),
				TastResource.getText("components_timeline_stat_averagecrew"),
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(crew30)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("crew3")}),
				TastResource.getText("components_timeline_stat_averagecrewfirst"),
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"SUM(crewdied)",
				new FunctionAttribute("SUM", new Attribute[] {Voyage.getAttribute("crewdied")}),
				TastResource.getText("components_timeline_stat_crewdeaths"),
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(crewdied)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("crewdied")}),
				TastResource.getText("components_timeline_stat_averagecrewdeaths"),
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"SUM(slintend)",
				new FunctionAttribute("SUM", new Attribute[] {Voyage.getAttribute("slintend")}),
				TastResource.getText("components_timeline_stat_intnumpurchases"),
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(slintend)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("slintend")}),
				TastResource.getText("components_timeline_stat_averageintpurchases"),
				"{0,number,#,###,###.0}"));
		
		this.availableStats.add(new StatOption(
				"SUM(slaximp)",
				new FunctionAttribute("SUM", new Attribute[] {Voyage.getAttribute("slaximp")}),
				TastResource.getText("components_timeline_stat_totalnumcaptivesemb"),
				"{0,number,#,###,###}"));
		
		this.availableStats.add(new StatOption(
				"AVG(slaximp)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("slaximp")}),
				TastResource.getText("components_timeline_stat_averagenumcaptivesemb"),
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"SUM(slamimp)",
				new FunctionAttribute("SUM", new Attribute[] {Voyage.getAttribute("slamimp")}),
				TastResource.getText("components_timeline_stat_totalnumcaptivesdisemb"),
				"{0,number,#,###,###}"));
		
		this.availableStats.add(new StatOption(
				"AVG(slamimp)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("slamimp")}),
				TastResource.getText("components_timeline_stat_averagenumcaptivesdisemb"),
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(menrat7)",
				new FunctionAttribute("AVG", new Attribute[] {new FunctionAttribute("crop_to_0_100", new Attribute[] {Voyage.getAttribute("menrat7")})}),
				TastResource.getText("components_timeline_stat_percentmen"),
				"{0,number,#,###,##0.0}%"));

		this.availableStats.add(new StatOption(
				"AVG(womrat7)",
				new FunctionAttribute("AVG", new Attribute[] {new FunctionAttribute("crop_to_0_100", new Attribute[] {Voyage.getAttribute("womrat7")})}),
				TastResource.getText("components_timeline_stat_percentwomen"),
				"{0,number,#,###,##0.0}%"));

		this.availableStats.add(new StatOption(
				"AVG(boyrat7)",
				new FunctionAttribute("AVG", new Attribute[] {new FunctionAttribute("crop_to_0_100", new Attribute[] {Voyage.getAttribute("boyrat7")})}),
				TastResource.getText("components_timeline_stat_percentboys"),
				"{0,number,#,###,##0.0}%"));
		
		this.availableStats.add(new StatOption(
				"AVG(girlrat7)",
				new FunctionAttribute("AVG", new Attribute[] {new FunctionAttribute("crop_to_0_100", new Attribute[] {Voyage.getAttribute("girlrat7")})}),
				TastResource.getText("components_timeline_stat_percentgirls"),
				"{0,number,#,###,##0.0}%"));
		
		this.availableStats.add(new StatOption(
				"AVG(malrat7)",
				new FunctionAttribute("AVG", new Attribute[] {new FunctionAttribute("crop_to_0_100", new Attribute[] {Voyage.getAttribute("malrat7")})}),
				TastResource.getText("components_timeline_stat_percentmales"),
				"{0,number,#,###,##0.0}%"));

		this.availableStats.add(new StatOption(
				"AVG(chilrat7)",
				new FunctionAttribute("AVG", new Attribute[] {new FunctionAttribute("crop_to_0_100", new Attribute[] {Voyage.getAttribute("chilrat7")})}),
				TastResource.getText("components_timeline_stat_percentchildren"),
				"{0,number,#,###,##0.0}%"));
		
		this.availableStats.add(new StatOption(
				"AVG(jamcaspr)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("jamcaspr")}),
				TastResource.getText("components_timeline_stat_averageprice"),
				"{0,number,#,###,##0.0}"));

		this.availableStats.add(new StatOption(
				"SUM(vymrtimp)",
				new FunctionAttribute("SUM", new Attribute[] {Voyage.getAttribute("vymrtimp")}),
				TastResource.getText("components_timeline_stat_numdeaths"),
				"{0,number,#,###,###}"));
		
		this.availableStats.add(new StatOption(
				"AVG(vymrtimp)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("vymrtimp")}),
				TastResource.getText("components_timeline_stat_averagedeaths"),
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(vymrtrat)",
				new FunctionAttribute("AVG", new Attribute[] {new FunctionAttribute("crop_to_0_100", new Attribute[] {Voyage.getAttribute("vymrtrat")})}),
				TastResource.getText("components_timeline_stat_mortrate"),
				"{0,number,#,###,##0.0}%"));

		this.chosenOption = (StatOption)this.availableStats.get(0);
		
	}

	/**
	 * Gets available list of statistics for voyages.
	 * @return
	 */
	public List getVoyageNumericAttributes() {

		this.voyageAttributes = new ArrayList();
		Iterator iter = this.availableStats.iterator();
		while (iter.hasNext()) {
			StatOption option = (StatOption)iter.next();
			this.voyageAttributes.add(new SelectItem(String.valueOf(option.hashCode()), option.userLabel));
		}
		
		return this.voyageAttributes;
	}


	/**
	 * Shows time line chart.
	 * This function queries the database if necessary.
	 * @return
	 */
	public String showTimeLine() {
		
		if (!this.searchBean.getSearchParameters().getConditions().equals(this.conditions)) {
			this.conditions = (Conditions)this.searchBean.getSearchParameters().getConditions().clone();
			needQuery = true;
		}
		
		//Check if we can construct chart
		if ((this.needQuery || this.attributesChanged) && this.searchBean.getSearchParameters().getConditions() != null) {

			Session sess = HibernateUtil.getSession();
			Transaction tran = sess.beginTransaction();

			QueryValue qValue = new QueryValue(new String[] {"Voyage"}, new String[] {"v"}, this.searchBean.getSearchParameters().getConditions());
			qValue.setGroupBy(new Attribute[] {Voyage.getAttribute("yearam")});
			qValue.addPopulatedAttribute(Voyage.getAttribute("yearam"));
			qValue.addPopulatedAttribute(this.chosenOption.attributeValue);
			qValue.setOrderBy(new Attribute[] {Voyage.getAttribute("yearam")});
			qValue.setOrder(QueryValue.ORDER_ASC);
			
			System.out.println(qValue.toStringWithParams().conditionString);
			
			List ret = qValue.executeQueryList(sess);
			
			MessageFormat fmt = new MessageFormat(chosenOption.formatString);
			Object[] valueHolder = new Object[1];
			Double zeroDouble = new Double(0);

			double[] values = new double[ret.size()];
			String[] stringValues = new String[ret.size()];
			int[] years = new int[ret.size()];
			
			int i = 0;
			for (Iterator iter = ret.iterator(); iter.hasNext();)
			{
				Object[] row = (Object[]) iter.next();
				years[i] = ((Number)row[0]).intValue();
				if (row[1] != null)
				{
					valueHolder[0] = row[1];
					values[i] = ((Number)row[1]).doubleValue();
				}
				else
				{
					valueHolder[0] = zeroDouble;
					values[i] = 0;
				}
				stringValues[i] = fmt.format(valueHolder);
				i++;
			}
			
			graph = new EventLineGraph();
			graph.setName(chosenOption.userLabel);
			graph.setX(years);
			graph.setY(values);
			graph.setLabels(stringValues);
			graph.setBaseCssClass("timeline-color");
			
			tran.commit();
			sess.close();
			
			double maxValue = graph.getMaxValue();

			if (maxValue > 0)
			{
				verticalLabels = EventLineLabel.createStandardLabels(maxValue, fmt);
				viewportHeight = verticalLabels[verticalLabels.length - 1].getValue();
			}
			else
			{
				verticalLabels = new EventLineLabel[] {new EventLineLabel(0.0, "0", true)};
				viewportHeight = 100;
			}

			this.needQuery = false;
			this.attributesChanged = false;

		}

		return null;
		
	}

	/**
	 * Gets currently chosen attribute.
	 * @return
	 */
	public String getChosenAttribute() {
		return String.valueOf(chosenOption.hashCode());
	}

	/**
	 * Sets currently chosen attribute.
	 * @param chosenAttribute
	 */
	public void setChosenAttribute(String chosenAttribute) {
		this.attributesChanged = true;
		if (chosenAttribute != null) {
			Iterator iter = this.availableStats.iterator();
			while (iter.hasNext()) {
				StatOption option = (StatOption) iter.next();
				if (option.hashCode() == Integer.parseInt(chosenAttribute)) {
					this.chosenOption = option;
					break;
				}
			}
		}

	}


	public String setNewView() {
		return null;
	}

	public SearchBean getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}

	public Double getViewportHeight() {
		showTimeLine();
		return new Double(this.viewportHeight);
	}
	
	/**
	 * Gets available series in timeline - by default only one here.
	 * @return
	 */
	public EventLineGraph[] getGraphs() {
		
		this.showTimeLine();
		return new EventLineGraph[] {graph};
	}
	
	/**
	 * Gets current events associated with dates.
	 * @return
	 */
	public EventLineEvent[] getEvents() {
		showTimeLine();
		return new EventLineEvent[] {};
	}

	/**
	 * Gets avaialble zoom levels.
	 * @return
	 */
	public EventLineZoomLevel[] getZoomLevels() {
		showTimeLine();
		return new EventLineZoomLevel[] {
				new EventLineZoomLevel(2, 50, 400, 100),
				new EventLineZoomLevel(4, 25, 200, 50),
				new EventLineZoomLevel(8, 10, 100, 25),
				new EventLineZoomLevel(16, 5, 50, 10),
				new EventLineZoomLevel(32, 5, 25, 5)};
	}
	public EventLineLabel[] getVerticalLabels() {
		showTimeLine();
		return verticalLabels;
	}
	
	/**
	 * Prepares ZIP file with current data.
	 * @return
	 */
	public String getFileAllData() {	
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		String[][] data = new String[this.graph.getX().length + 1][2];
		data[0][0] = "year";
		data[0][1] = this.chosenOption.userLabel;
		for (int i = 0; i < data.length - 1; i++) {
			data[i + 1][0] = String.valueOf(this.graph.getX()[i]);
			data[i + 1][1] = String.valueOf(this.graph.getY()[i]);
		}
		CSVUtils.writeResponse(session, data);
		
		t.commit();
		session.close();
		return null;
	}
}
