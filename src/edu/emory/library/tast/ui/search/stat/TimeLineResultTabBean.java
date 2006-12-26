package edu.emory.library.tast.ui.search.stat;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.jfree.chart.JFreeChart;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.ui.EventLineEvent;
import edu.emory.library.tast.ui.EventLineGraph;
import edu.emory.library.tast.ui.EventLineVerticalLabels;
import edu.emory.library.tast.ui.EventLineZoomLevel;
import edu.emory.library.tast.ui.search.query.SearchBean;
import edu.emory.library.tast.ui.search.query.SearchParameters;
import edu.emory.library.tast.ui.search.stat.charts.AbstractChartGenerator;
import edu.emory.library.tast.ui.search.stat.charts.XYChartGenerator;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttribute;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.util.MathUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.DirectValue;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * Bean for time line statistics.
 * @author Pawel Jurczyk
 *
 */
public class TimeLineResultTabBean {

	public static final String IMAGE_FEEDED_SERVLET = "servlet/ImageFeederServlet";

	private static final String[] aggregates = { "avg", "min", "max", "sum", "count" };

	private static final String[] aggregatesUL = { "Avg", "Min", "Max", "Sum", "Count" };

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

	/**
	 * Chosen aggregate.
	 */
	private String chosenAggregate = "sum";

	/**
	 * Chosen attribute name.
	 */
	private VisibleAttributeInterface chosenAttribute = VisibleAttribute.getAttribute("slaximp");

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
	private int viewportHeight;
	int expYears[] = {1500, 1600, 1700};
	double expValues[] = {45, 50, 30};

	/**
	 * Avaialable voyage attributes.
	 */
	private VisibleAttributeInterface[] attributes = VisibleAttribute.getAllAttributes();

	private EventLineVerticalLabels verticalLabels;

	/**
	 * Default constructor.
	 *
	 */
	public TimeLineResultTabBean() {
	}

	/**
	 * Gets numeric attributes of voyage.
	 * @return
	 */
	public List getVoyageNumericAttributes() {

		//Build list of numeric attributes.
		this.voyageAttributes = new ArrayList();
		for (int i = 0; i < attributes.length; i++) {
			VisibleAttributeInterface attr = attributes[i];
			if ((attr.getType().equals(VisibleAttributeInterface.NUMERIC_ATTRIBUTE)) &&
					attr.getAttributes().length == 1) {
				String outString = attr.toString();
				voyageAttributes.add(new ComparableSelectItem(attr.getName(), outString));

			}
		}
		Collections.sort(voyageAttributes);

		return this.voyageAttributes;
	}

	/**
	 * Gets avaialable aggregates.
	 * @return
	 */
	public List getAggregateFunctions() {
		if (this.aggregateFunctions == null) {
			this.aggregateFunctions = new ArrayList();
			for (int i = 0; i < aggregates.length; i++) {
				this.aggregateFunctions.add(new SelectItem(aggregates[i], aggregatesUL[i]));
			}
		}
		return this.aggregateFunctions;
	}

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
			qValue.addPopulatedAttribute(new FunctionAttribute(this.chosenAggregate, this.chosenAttribute.getAttributes()));
			qValue.setOrderBy(new Attribute[] {new FunctionAttribute("date_trunc", new Attribute[] {new DirectValueAttribute("year"), Voyage.getAttribute("datedep")})});
			qValue.setOrder(QueryValue.ORDER_ASC);
			Object[] ret = qValue.executeQuery();

			DateFormat format = new SimpleDateFormat("yyyy");
			this.expValues = new double[ret.length];
			this.expYears = new int[ret.length];
			for (int i = 0; i < ret.length; i++) {
				Object[] row = (Object[])ret[i];
				this.expYears[i] = Integer.parseInt(format.format((Timestamp)row[0]));
				if (row[1] != null) {
					this.expValues[i] = Math.round(((Number)row[1]).doubleValue());
				} else {
					this.expValues[i] = 0;
				}
			}
			
//			//Prepare chart generator.
//			AbstractChartGenerator generator = new XYChartGenerator(Voyage.getAttribute("datedep"));
//			generator.correctAndCompleteData(ret);
//			generator.addRowToDataSet(ret, new String[] { this.chosenAggregate + "("
//					+ this.chosenAttribute.getUserLabelOrName() + ")" });
//			chart = generator.getChart("Time line graph", false);
//
//			//Put chart into session.
//			ExternalContext servletContext = FacesContext.getCurrentInstance().getExternalContext();
//			((HttpSession) servletContext.getSession(true)).setAttribute("__chart__object", chart);

			this.needQuery = false;
			this.attributesChanged = false;
		}

		return null;
	}

	/**
	 * Gets currently chosen aggregate.
	 * @return
	 */
	public String getChosenAggregate() {
		return chosenAggregate;
	}

	/**
	 * Sets currently chosen aggregate.
	 * @param chosenAggregate
	 */
	public void setChosenAggregate(String chosenAggregate) {
		if (chosenAggregate != null && !chosenAggregate.equals(this.chosenAttribute)) {
			this.chosenAggregate = chosenAggregate;
			this.attributesChanged = true;
		}
	}

	/**
	 * Gets currently chosen attribute.
	 * @return
	 */
	public String getChosenAttribute() {
		return chosenAttribute.getName();
	}

	/**
	 * Sets currently chosen attribute.
	 * @param chosenAttribute
	 */
	public void setChosenAttribute(String chosenAttribute) {
		if (chosenAttribute != null && !chosenAttribute.equals(this.chosenAttribute)) {
			this.chosenAttribute = VisibleAttribute.getAttribute(chosenAttribute);
			this.attributesChanged = true;
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
	
	
	
	/* -New implementation- */
	public Integer getViewportHeight() {
		showTimeLine();
		return this.viewportHeight;
	}
	public EventLineGraph[] getGraphs() {
		
		this.showTimeLine();
//		int impYears[] = {1500, 1600, 1700};
//		double impValues[] = {50, 44, 35};
		
		
		
		graphExp = new EventLineGraph();
		graphExp.setName("Exported");
		graphExp.setX(expYears);
		graphExp.setY(expValues);
		graphExp.setBaseColor("#EEEEEE");
		graphExp.setEventColor("#AAAAAA");
		
		graphExp.setBaseColor("#F1E7C8");
		graphExp.setEventColor("#AAAAAA");

//		// graph for imported
//		graphImp = new EventLineGraph();
//		graphImp.setName("Imported");
//		graphImp.setX(impYears);
//		graphImp.setY(impValues);
//		graphImp.setBaseColor("#CCCCCC");
//		graphImp.setEventColor("#666666");
//		
//		graphImp.setBaseColor("#E7D59C");
//		graphImp.setEventColor("#666666");
		
		createVerticalLabels();
		
		return new EventLineGraph[] {graphExp};
	}
	
	private void createVerticalLabels()
	{

//		int maxValue = (int) Math.max(
//				graphExp.getMaxValue(),
//				graphImp.getMaxValue());
		int maxValue = (int)graphExp.getMaxValue();
		
		if (maxValue > 0)
		{

			int majorSpacing;
			int minorSpacing;

			int nextPow10 = MathUtils.firstGreaterOrEqualPow10(maxValue);
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
				new EventLineEvent(1530, "Event A"),
				new EventLineEvent(1606, "Event B"),
				new EventLineEvent(1723, "Event C"),
				new EventLineEvent(1786, "Event D"),
				new EventLineEvent(1807, "Event E"),
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
