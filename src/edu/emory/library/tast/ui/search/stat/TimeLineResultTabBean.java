package edu.emory.library.tast.ui.search.stat;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.jfree.chart.JFreeChart;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.ui.EventLineEvent;
import edu.emory.library.tast.ui.EventLineGraph;
import edu.emory.library.tast.ui.EventLineVerticalLabels;
import edu.emory.library.tast.ui.EventLineZoomLevel;
import edu.emory.library.tast.ui.search.query.SearchBean;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttribute;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.util.MathUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * Bean for time line statistics.
 * @author Pawel Jurczyk
 *
 */
public class TimeLineResultTabBean {

	public static final String IMAGE_FEEDED_SERVLET = "servlet/ImageFeederServlet";

	private static final String[] aggregates = { "avg", "min", "max", "sum", "count" };

	private static final String[] aggregatesUL = { "Average", "Minimum", "Maximum", "Sum", "Count" };

	private static final String DEFAULT_CHART_HEIGHT = "480";

	private static final String DEFAULT_CHART_WIDTH = "640";

	/**
	 * List of voyage attributes.
	 */
	private List voyageAttributes;

	/**
	 * List of available aggregates.
	 */
	private List aggregateFunctions;

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

	/**
	 * Chart presented to user.
	 */
	private JFreeChart chart;

	/**
	 * Current chart height.
	 */
	private String chartHeight = DEFAULT_CHART_HEIGHT;

	/**
	 * Current chart width.
	 */
	private String chartWidth = DEFAULT_CHART_WIDTH;
	
	//private EventLineGraph graphImp;
	private EventLineGraph graphExp;
	private double viewportHeight;
	int expYears[] = {1500, 1600, 1700};
	double expValues[] = {45, 50, 30};

	/**
	 * Avaialable voyage attributes.
	 */
	//private VisibleAttributeInterface[] attributes = null;//VisibleAttribute.getAllAttributes();

	private EventLineVerticalLabels verticalLabels;

	private ArrayList availableStats;

	private class StatOption {
		public VisibleAttributeInterface attr;
		public String aggregate;
		public String userLabel;
		public StatOption(VisibleAttributeInterface attr, String agregate, String userLabel) {
			this.attr = attr;
			this.aggregate = agregate;
			this.userLabel = userLabel;
		}
	}
	
	/**
	 * Default constructor.
	 *
	 */
	public TimeLineResultTabBean() {
		
		
		this.availableStats = new ArrayList();
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("voyageid"), "count", "Number of voyages per year"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("tonnage"), "sum", VisibleAttribute.getAttribute("tonnage").getUserLabelOrName() + " - sum"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("tonnage"), "avg", VisibleAttribute.getAttribute("tonnage").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("tonmod"), "sum", VisibleAttribute.getAttribute("tonmod").getUserLabelOrName() + " - sum"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("tonmod"), "avg", VisibleAttribute.getAttribute("tonmod").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("guns"), "avg", VisibleAttribute.getAttribute("guns").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("voy1imp"), "avg", VisibleAttribute.getAttribute("voy1imp").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("voy2imp"), "avg", VisibleAttribute.getAttribute("voy2imp").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("crew1"), "avg", VisibleAttribute.getAttribute("crew1").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("crew3"), "avg", VisibleAttribute.getAttribute("crew3").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("crewdied"), "sum", VisibleAttribute.getAttribute("crewdied").getUserLabelOrName() + " - sum"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("crewdied"), "avg", VisibleAttribute.getAttribute("crewdied").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("slintend"), "sum", VisibleAttribute.getAttribute("slintend").getUserLabelOrName() + " - sum"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("slintend"), "avg", VisibleAttribute.getAttribute("slintend").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("slaximp"), "sum", VisibleAttribute.getAttribute("slaximp").getUserLabelOrName() + " - sum"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("slaximp"), "avg", VisibleAttribute.getAttribute("slaximp").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("slamimp"), "sum", VisibleAttribute.getAttribute("slamimp").getUserLabelOrName() + " - sum"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("slaximp"), "avg", VisibleAttribute.getAttribute("slaximp").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("vymrtimp"), "sum", VisibleAttribute.getAttribute("vymrtimp").getUserLabelOrName() + " - sum"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("vymrtimp"), "avg", VisibleAttribute.getAttribute("vymrtimp").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("boyrat7"), "avg", VisibleAttribute.getAttribute("boyrat7").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("girlrat7"), "avg", VisibleAttribute.getAttribute("girlrat7").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("menrat7"), "avg", VisibleAttribute.getAttribute("menrat7").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("womrat7"), "avg", VisibleAttribute.getAttribute("womrat7").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("chilrat7"), "avg", VisibleAttribute.getAttribute("chilrat7").getUserLabelOrName() + " - average"));
		this.availableStats.add(new StatOption(VisibleAttribute.getAttribute("malrat7"), "avg", VisibleAttribute.getAttribute("malrat7").getUserLabelOrName() + " - average"));
		
		
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

			
			//Prepare query
			Conditions localCondition = (Conditions)this.searchBean.getSearchParameters().getConditions().clone();
			localCondition.addCondition(Voyage.getAttribute("datedep"), null, Conditions.OP_IS_NOT);
//			localCondition.addCondition(VoyageIndex.getAttribute("remoteVoyageId"), new DirectValue(Voyage.getAttribute("iid")), Conditions.OP_EQUALS);

			QueryValue qValue = new QueryValue(new String[] {"Voyage"}, new String[] {"v"}, localCondition);
			qValue.setGroupBy(new Attribute[] { new FunctionAttribute("date_trunc", new Attribute[] {new DirectValueAttribute("year"), Voyage.getAttribute("datedep")})});
			qValue.addPopulatedAttribute(new FunctionAttribute("date_trunc", new Attribute[] {new DirectValueAttribute("year"), Voyage.getAttribute("datedep")}));
			qValue.addPopulatedAttribute(new FunctionAttribute(this.chosenOption.aggregate, this.chosenOption.attr.getAttributes()));
			qValue.setOrderBy(new Attribute[] {new FunctionAttribute("date_trunc", new Attribute[] {new DirectValueAttribute("year"), Voyage.getAttribute("datedep")})});
			qValue.setOrder(QueryValue.ORDER_ASC);
			Object[] ret = qValue.executeQuery();
			
			Calendar cal = Calendar.getInstance();
			
			this.expValues = new double[ret.length];
			this.expYears = new int[ret.length];
			for (int i = 0; i < ret.length; i++) {
				Object[] row = (Object[])ret[i];
				cal.setTime((Timestamp)row[0]);
				this.expYears[i] = cal.get(Calendar.YEAR);
				if (row[1] != null) {
					this.expValues[i] = ((Number)row[1]).intValue();
				} else {
					this.expValues[i] = 0;
				}
			}
			
			
			graphExp = new EventLineGraph();
			graphExp.setName(chosenOption.userLabel);
			graphExp.setX(expYears);
			graphExp.setY(expValues);
			graphExp.setBaseCssClass("timeline-color");
			//graphExp.setEventColor("#AAAAAA");

			
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
		createVerticalLabels();
		return new Double(this.viewportHeight);
	}
	public EventLineGraph[] getGraphs() {
		
		this.showTimeLine();
		createVerticalLabels();
		
		return new EventLineGraph[] {graphExp};
	}
	
	private void createVerticalLabels()
	{

//		int maxValue = (int) Math.max(
//				graphExp.getMaxValue(),
//				graphImp.getMaxValue());
		double maxValue = graphExp.getMaxValue();
		
		if (maxValue > 0)
		{

			double majorSpacing;
			double minorSpacing;

			double nextPow10 = MathUtils.firstGreaterOrEqualPow10(maxValue);
			if (maxValue / (nextPow10/10) >= 5)
			{
				majorSpacing = nextPow10 / 2;
				minorSpacing = majorSpacing / 5;
			}
			else
			{
				majorSpacing = nextPow10 / 10;
				minorSpacing = majorSpacing / 2;
			}

			viewportHeight = (maxValue / minorSpacing + 1) * minorSpacing;
			verticalLabels = new EventLineVerticalLabels(majorSpacing, minorSpacing);

		}
		else
		{
			viewportHeight = 100;
			verticalLabels = new EventLineVerticalLabels(50, 10);
		}

	}
	
	public EventLineEvent[] getEvents() {
		showTimeLine();
		return new EventLineEvent[] {
//				new EventLineEvent(1530, "Event A"),
//				new EventLineEvent(1606, "Event B"),
//				new EventLineEvent(1723, "Event C"),
//				new EventLineEvent(1786, "Event D"),
//				new EventLineEvent(1807, "Event E"),
		};
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
	public EventLineVerticalLabels getVerticalLabels() {
		showTimeLine();
		return verticalLabels;
	}
}
