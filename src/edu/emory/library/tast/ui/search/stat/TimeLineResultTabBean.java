package edu.emory.library.tast.ui.search.stat;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tas.util.HibernateUtil;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.ui.EventLineEvent;
import edu.emory.library.tast.ui.EventLineGraph;
import edu.emory.library.tast.ui.EventLineLabel;
import edu.emory.library.tast.ui.EventLineZoomLevel;
import edu.emory.library.tast.ui.search.query.SearchBean;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * Bean for time line statistics.
 * @author Pawel Jurczyk
 *
 */
public class TimeLineResultTabBean {

	public static final String IMAGE_FEEDED_SERVLET = "servlet/ImageFeederServlet";

//	private static final String[] aggregates = { "avg", "min", "max", "sum", "count" };

//	private static final String[] aggregatesUL = { "Average", "Minimum", "Maximum", "Sum", "Count" };

	private static final String DEFAULT_CHART_HEIGHT = "480";

	private static final String DEFAULT_CHART_WIDTH = "640";

	/**
	 * List of voyage attributes.
	 */
	private List voyageAttributes;

//	/**
//	 * List of available aggregates.
//	 */
//	private List aggregateFunctions;
//
//	/**
//	 * Chosen aggregate.
//	 */
//	private String chosenAggregate = "sum";

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

//	/**
//	 * Chart presented to user.
//	 */
//	private JFreeChart chart;

	/**
	 * Current chart height.
	 */
	private String chartHeight = DEFAULT_CHART_HEIGHT;

	/**
	 * Current chart width.
	 */
	private String chartWidth = DEFAULT_CHART_WIDTH;
	
	//private EventLineGraph graphImp;
	private EventLineGraph graph;
	private double viewportHeight;

	/**
	 * Avaialable voyage attributes.
	 */
	//private VisibleAttributeInterface[] attributes = null;//VisibleAttribute.getAllAttributes();

	private EventLineLabel[] verticalLabels;

	private ArrayList availableStats;

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
	 *
	 */
	public TimeLineResultTabBean() {
		
		
		this.availableStats = new ArrayList();
		
		this.availableStats.add(new StatOption(
				"COUNT(voyageid)",
				new FunctionAttribute("COUNT", new Attribute[] {Voyage.getAttribute("voyageid")}),
				"Number of voyages",
				"{0,number,#,###,###}"));
		
		this.availableStats.add(new StatOption(
				"AVG(tonnage)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("tonnage")}),
				"Average tonnage",
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(tonmod)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("tonmod")}),
				"Average tonnage (standardized)",
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(guns)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("guns")}),
				"Average number of guns",
				"{0,number,#,###,###.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(LEAST(COALESCE(resistance, 0), 1)) * 100",
				new FunctionAttribute("AVG", new Attribute[] {new FunctionAttribute("crop_to_0_100", new Attribute[] {Voyage.getAttribute("resistance")})}),
				"Rate of resistance",
				"{0,number,#,###,##0.0}%"));

		this.availableStats.add(new StatOption(
				"AVG(voy1imp)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("voy1imp")}),
				"Average duration of first leg of voyage (days)",
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(voy2imp)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("voy2imp")}),
				"Average duration of middle passage (days)",
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(crew1)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("tonnage")}),
				"Average crew at outset",
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(crew30)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("crew30")}),
				"Average crew at first landing of slaves",
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"SUM(crewdied)",
				new FunctionAttribute("SUM", new Attribute[] {Voyage.getAttribute("crewdied")}),
				"Number of crew deaths",
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(crewdied)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("crewdied")}),
				"Average crew deaths",
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"SUM(slintend)",
				new FunctionAttribute("SUM", new Attribute[] {Voyage.getAttribute("slintend")}),
				"Intended number of purchases",
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(slintend)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("slintend")}),
				"Average intended purchases",
				"{0,number,#,###,###.0}"));
		
		this.availableStats.add(new StatOption(
				"SUM(slaximp)",
				new FunctionAttribute("SUM", new Attribute[] {Voyage.getAttribute("slaximp")}),
				"Total number of captives embarked",
				"{0,number,#,###,###}"));
		
		this.availableStats.add(new StatOption(
				"AVG(slaximp)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("slaximp")}),
				"Average number of captives embarked",
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"SUM(slamimp)",
				new FunctionAttribute("SUM", new Attribute[] {Voyage.getAttribute("slamimp")}),
				"Total number of captives disembarked",
				"{0,number,#,###,###}"));
		
		this.availableStats.add(new StatOption(
				"AVG(slamimp)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("slamimp")}),
				"Average number of captives disembarked",
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(menrat7)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("menrat7")}),
				"Percent men (among captives)",
				"{0,number,#,###,##0.0}%"));

		this.availableStats.add(new StatOption(
				"AVG(womrat7)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("womrat7")}),
				"Percent women (among captives)",
				"{0,number,#,###,##0.0}%"));

		this.availableStats.add(new StatOption(
				"AVG(boyrat7)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("boyrat7")}),
				"Percent boys (among captives)",
				"{0,number,#,###,##0.0}%"));
		
		this.availableStats.add(new StatOption(
				"AVG(girlrat7)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("girlrat7")}),
				"Percent girls (among captives)",
				"{0,number,#,###,##0.0}%"));
		
		this.availableStats.add(new StatOption(
				"AVG(malrat7)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("malrat7")}),
				"Percent males (among captives)",
				"{0,number,#,###,##0.0}%"));

		this.availableStats.add(new StatOption(
				"AVG(chilrat7)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("chilrat7")}),
				"Percent children (among captives)",
				"{0,number,#,###,##0.0}%"));
		
		this.availableStats.add(new StatOption(
				"AVG(jamcaspr)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("jamcaspr")}),
				"Average price (standardized)",
				"{0,number,#,###,##0.0}"));

		this.availableStats.add(new StatOption(
				"SUM(vymrtimp)",
				new FunctionAttribute("SUM", new Attribute[] {Voyage.getAttribute("vymrtimp")}),
				"Number of slave deaths",
				"{0,number,#,###,###}"));
		
		this.availableStats.add(new StatOption(
				"AVG(vymrtimp)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("vymrtimp")}),
				"Average slave deaths",
				"{0,number,#,###,##0.0}"));
		
		this.availableStats.add(new StatOption(
				"AVG(vymrtrat)",
				new FunctionAttribute("AVG", new Attribute[] {Voyage.getAttribute("vymrtrat")}),
				"Slave mortality rate",
				"{0,number,#,###,##0.0}%"));

		this.chosenOption = (StatOption)this.availableStats.get(0);
		
		
//		ArrayList list = new ArrayList();
//		Group groups[] = Group.getGroups();
//		for (int i = 0; i < groups.length; i++) {
//			list.addAll(Arrays.asList(groups[i].getAllVisibleAttributes()));
//		}
//		attributes = (VisibleAttributeInterface[])list.toArray(new VisibleAttributeInterface[] {});
	}

	/**
	 * Gets numeric attributes of voyage.
	 * @return
	 */
	public List getVoyageNumericAttributes() {

		//Build list of numeric attributes.
		this.voyageAttributes = new ArrayList();
//		for (int i = 0; i < attributes.length; i++) {
//			VisibleAttributeInterface attr = attributes[i];
//			if (attr.getType().equals(VisibleAttributeInterface.NUMERIC_ATTRIBUTE) && (!attr.isDate() || attr.getName().equals("voyageid")) &&
//					attr.getAttributes().length == 1) {
//				String outString = attr.toString();
//				voyageAttributes.add(new ComparableSelectItem(attr.getName(), outString));
//
//			}
//		}
		//Collections.sort(voyageAttributes);

		Iterator iter = this.availableStats.iterator();
		while (iter.hasNext()) {
			StatOption option = (StatOption)iter.next();
			this.voyageAttributes.add(new SelectItem(String.valueOf(option.hashCode()), option.userLabel));
		}
		
		return this.voyageAttributes;
	}

//	/**
//	 * Gets avaialable aggregates.
//	 * @return
//	 */
//	public List getAggregateFunctions() {
//		if (this.aggregateFunctions == null) {
//			this.aggregateFunctions = new ArrayList();
//			for (int i = 0; i < aggregates.length; i++) {
//				this.aggregateFunctions.add(new SelectItem(aggregates[i], aggregatesUL[i]));
//			}
//		}
//		return this.aggregateFunctions;
//	}

	/**
	 * Shows time line chart.
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
			
//			StringBuffer sql = new StringBuffer();
//			sql.append("SELECT ");
//			sql.append("yearam AS year");
//			sql.append(", ");
//			sql.append(this.chosenOption.sqlValue).append(" AS value");
//			sql.append(" ");
//			sql.append("FROM voyages ");
//			sql.append("WHERE ");
//			sql.append("datedep IS NOT NULL ");
//			if (this.chosenOption.sqlWhere != null)
//			{
//				sql.append("AND ");
//				sql.append(this.chosenOption.sqlWhere);
//				sql.append(" ");
//			}
//			sql.append("GROUP BY year");
//			sql.append(" ");
//			sql.append("ORDER BY year");
//			
//			SQLQuery query = sess.createSQLQuery(sql.toString());
//			query.addScalar("year", Hibernate.DOUBLE);
//			query.addScalar("value", Hibernate.DOUBLE);
//			List ret = query.list();

			//Conditions localCondition = (Conditions)this.searchBean.getSearchParameters().getConditions().clone();
			//localCondition.addCondition(Voyage.getAttribute("datedep"), null, Conditions.OP_IS_NOT);
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

//	/**
//	 * Gets currently chosen aggregate.
//	 * @return
//	 */
//	public String getChosenAggregate() {
//		return chosenAggregate;
//	}

//	/**
//	 * Sets currently chosen aggregate.
//	 * @param chosenAggregate
//	 */
//	public void setChosenAggregate(String chosenAggregate) {
//		if (chosenAggregate != null && !chosenAggregate.equals(this.chosenAttribute)) {
//			this.chosenAggregate = chosenAggregate;
//			this.attributesChanged = true;
//		}
//	}

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

	/**
	 * Gets path to chart image.
	 * @return
	 */
	public String getChartPath() {
		this.showTimeLine();
		return IMAGE_FEEDED_SERVLET + "?path=__chart__object&&height=" + this.chartHeight + "&width=" + this.chartWidth;
	}
	public void setChartPath(String path) {
	}
	public String setNewView() {
		return null;
	}

	/**
	 * Checks if any chart is ready to show.
	 */
	public boolean getChartReady() {
		//return this.chart != null;
		return true;
	}
	

	/**
	 * Gets chart height.
	 * @return
	 */
	public String getChartHeight() {
		return chartHeight;
	}

	/**
	 * Gets chart height.
	 * @param chartHeight
	 */
	public void setChartHeight(String chartHeight) {
		if (chartHeight == null) return;
		this.chartHeight = chartHeight;
	}

	/**
	 * Gets chart width.
	 * @param chartHeight
	 */
	public String getChartWidth() {
		return chartWidth;
	}

	/**
	 * Sets chart width.
	 * @param chartWidth
	 */
	public void setChartWidth(String chartWidth) {
		if (chartWidth == null) return;
		this.chartWidth = chartWidth;
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
	public EventLineGraph[] getGraphs() {
		
		this.showTimeLine();
		return new EventLineGraph[] {graph};
	}
	
	public EventLineEvent[] getEvents() {
		showTimeLine();
		return new EventLineEvent[] {};
	}

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
}
