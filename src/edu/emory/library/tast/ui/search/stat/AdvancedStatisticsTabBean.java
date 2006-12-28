package edu.emory.library.tast.ui.search.stat;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.ui.search.query.SearchBean;
import edu.emory.library.tast.ui.search.stat.charts.AbstractChartGenerator;
import edu.emory.library.tast.ui.search.table.ClickEvent;
import edu.emory.library.tast.ui.search.tabscommon.MemorizedAction;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttribute;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.DirectValue;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * Bean used for generating graphs.
 * 
 * @author Pawel Jurczyk
 * 
 */
public class AdvancedStatisticsTabBean {

	private static final String STAT_OBJECT_NAME = "__stat__object";

	public static final String IMAGE_FEEDED_SERVLET = "servlet/ImageFeederServlet";

	private static final String[] orders = { "Ignore", "Asc", "Desc" };

	private static final String[] aggregates = { "avg", "min", "max", "sum", "count" };

	private static final String[] aggregatesUL = { "Avg", "Min", "Max", "Sum", "Count" };

	private static final String[] availableChartsLabels = { "XY chart", "Bar chart", "Pie chart" };

	private static final String[] chartGenerators = { "XYChartGenerator", "BarChartGenerator", "PieChartGenerator" };

	private static final String DEFAULT_CHART_HEIGHT = "480";

	private static final String DEFAULT_CHART_WIDTH = "640";

	private static final int MAX_RESULTS_PER_GRAPH = 1000;
	
	/**
	 * Series of the chart. The class keeps information about attribute and
	 * aggregate function.
	 * 
	 * @author Pawel Jurczyk
	 * 
	 */
	public class SeriesItem {

		/**
		 * Attribute of series.
		 */
		public VisibleAttributeInterface attribute;

		/**
		 * Used aggregate.
		 */
		public String aggregate;

		/**
		 * Default constructor.
		 * 
		 * @param attribute
		 *            attribute of voyage
		 * @param aggregate
		 *            aggregate function
		 */
		public SeriesItem(VisibleAttributeInterface attribute, String aggregate) {
			this.attribute = attribute;
			this.aggregate = aggregate;

		}

		/**
		 * Implementation of equals. It checks if two objects have the same
		 * attribute and aggregate function.
		 */
		public boolean equals(Object o) {
			if (!(o instanceof SeriesItem)) {
				return false;
			}
			SeriesItem that = (SeriesItem) o;
			return ((this.aggregate == null && that.aggregate == null) || this.aggregate.equals(that.aggregate))
					&& this.attribute.equals(that.attribute);
		}

		/**
		 * toString method. Returns string that can be shown to user.
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			if (this.aggregate != null) {
				return buffer.append(this.aggregate).append("(").append(this.attribute.getUserLabelOrName())
						.append(")").toString();
			} else {
				return buffer.append(this.attribute).toString();
			}
		}
	}

	/**
	 * Indicatation of query need. If false - DB will not be queried.
	 */
	private boolean neededQuery = false;

	/**
	 * Current series of chart. List keeps SeriesItem objects.
	 */
	private List series = new ArrayList();

	/**
	 * Indication of aggregate functions enabling. If aggregates are enabled -
	 * true.
	 */
	private Boolean aggregate = new Boolean(true);

	/**
	 * Currently used order (asc, desc, ignore). See QueryValue.ORDER_*
	 * constants (string representation).
	 */
	private String order = "1";

	/**
	 * Current attribute of x axis.
	 */
	private String xaxis;

	/**
	 * Current attribute on y axis (when adding to series list).
	 */
	private VisibleAttributeInterface yaxis;

	/**
	 * Currently selected aggregate function (when adding new series).
	 */
	private String selectedAggregate;

	/**
	 * Available voyage attributes.
	 */
	private List voyageAttributes;

	/**
	 * Avaialable aggregate functions.
	 */
	private List aggregateFunctions;

	/**
	 * Available orders.
	 */
	private List availableOrders;

	/**
	 * List of series that should be removed from series list.
	 */
	private List toRemove;

	/**
	 * Current chart height.
	 */
	private String chartHeight = DEFAULT_CHART_HEIGHT;

	/**
	 * Current chart width.
	 */
	private String chartWidth = DEFAULT_CHART_WIDTH;

	/**
	 * Indicates if any chart is ready to show.
	 */
	private Boolean statReady = new Boolean(false);

	/**
	 * Selected chart type.
	 */
	private String selectedChart = "0";

	/**
	 * Available charts.
	 */
	private List availableCharts;

	/**
	 * Avaialble attributes of Voyage.
	 */
	private VisibleAttributeInterface[] attributes = prepareAttributes();

	/**
	 * Actions that will be performed when rollback is required by user.
	 */
	private List rollbackActions = new ArrayList();

	/**
	 * Actions that will be performed to fix error.
	 */
	private List fixErrorActions = new ArrayList();

	/**
	 * Indication of error presence.
	 */
	private Boolean errorPresent = new Boolean(false);

	/**
	 * Error message that will be presented to user.
	 */
	private String errorMessage = null;

	/**
	 * Fix button string.
	 */
	private String fixButton = null;

	/**
	 * Indication of warning presence.
	 */
	private Boolean warningPresent = new Boolean(false);

	/**
	 * Message that will be shown to user.
	 */
	private String warningMessage = null;

	/**
	 * Reference to Search bean.
	 */
	private SearchBean searchBean = null;
	
	/**
	 * Conditions used in query.
	 */
	private Conditions conditions = null;
	
	private int firstResult = 0;

	/**
	 * Gets list of attributes of Voyage.
	 * 
	 * @return array of Attribute objects.
	 */
	private VisibleAttributeInterface[] prepareAttributes() {
		return VisibleAttribute.getAllAttributes();
	}

	/**
	 * Prepares query that will be performed.
	 * 
	 * @param generator
	 *            AbstractChartGenerator object
	 * @return QueryValue that will return result for chart.
	 */
	private QueryValue prepareQueryValue(AbstractChartGenerator generator) {

		/**
		 * TODO - I should add getMostRecent!
		 */
		
		// We will use "v" prefix for Voyage object.
		Conditions localCondition = (Conditions)this.conditions.clone();
		localCondition.addCondition(Voyage.getAttribute(this.xaxis), null, Conditions.OP_IS_NOT);

		// We will need join condition (to join VoyageIndex and Voyage).
		//localCondition.addCondition(VoyageIndex.getAttribute("remoteVoyageId"), new DirectValue(Voyage.getAttribute("iid")), Conditions.OP_EQUALS);

		QueryValue qValue = new QueryValue(new String[] {"Voyage"}, new String[] {"v"}, localCondition);

		// Get xAxis - consider attribute type (Dictionary, Number...)
		Attribute xAxis = generator.getXAxisSelectOperator(Voyage.getAttribute(this.xaxis));

		// Set group by only if aggregates are enabled
		if (this.aggregate.booleanValue()) {
			qValue.setGroupBy(new Attribute[] { xAxis });
		}
		qValue.addPopulatedAttribute(xAxis);

		// Setup order
		if (this.order.equals("1")) {
			qValue.setOrderBy(new Attribute[] { xAxis });
			qValue.setOrder(QueryValue.ORDER_ASC);
			qValue.setGroupBy(new Attribute[] { xAxis });
		} else if (this.order.equals("2")) {
			qValue.setOrderBy(new Attribute[] { xAxis });
			qValue.setOrder(QueryValue.ORDER_DESC);
			qValue.setGroupBy(new Attribute[] { xAxis });
		}

		// Add series of chart to query
		for (Iterator iter = this.series.iterator(); iter.hasNext();) {
			AdvancedStatisticsTabBean.SeriesItem element = (AdvancedStatisticsTabBean.SeriesItem) iter.next();
			Attribute out = null;
			if (element.aggregate != null) {
				out = new FunctionAttribute(element.aggregate, element.attribute.getAttributes());
			} else {
				out = element.attribute.getAttributes()[0];
			}
			qValue.addPopulatedAttribute(out);
		}

		return qValue;
	}

	/**
	 * Creates AbstractChartGenerator subclass.
	 * 
	 * @return AbstractChartGenerator object.
	 */
	private AbstractChartGenerator getChartGenerator() {

		// Get AbstractChartGenerator subclass name
		int sel = Integer.parseInt(this.selectedChart);
		AbstractChartGenerator generator = null;
		String className = "edu.emory.library.tast.ui.search.stat.charts." + chartGenerators[sel];
		Attribute attr = Voyage.getAttribute(this.xaxis);
		try {
			// Invoke new AbstractChartGenerator(Attribute xAxis)
			Class clazz = Class.forName(className);
			generator = (AbstractChartGenerator) clazz.getConstructor(new Class[] { Attribute.class }).newInstance(
					new Object[] { attr });
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return generator;
	}

	/**
	 * Checks if chart is ok. Decides if user should see warning message.
	 */
	private void validateSelectedChart() {
		if (this.selectedChart != null && this.selectedChart.equals("2") && this.series.size() > 1) {
			this.warningMessage = "Pie chart should have only one series. All series besides first will be ignored.";
			this.warningPresent = new Boolean(true);
		} else {
			this.warningPresent = new Boolean(false);
		}
	}

	/**
	 * Adds series to chart.
	 * 
	 * @return
	 */
	public String addSeries() {
		// Current series and aggregate to add
		String aggregate = this.selectedAggregate;
		SeriesItem tmpSeries;

		// Create appropriate series item
		if (this.aggregate.booleanValue()) {
			tmpSeries = new SeriesItem(this.yaxis, aggregate);
		} else {
			tmpSeries = new SeriesItem(this.yaxis, null);
		}

		// Checks whether series already present
		if (this.series.contains(tmpSeries)) {
			// If present - show warning
			this.warningMessage = "Series has already been added.";
			this.warningPresent = new Boolean(true);
		} else {
			// If not - we will add it
			this.series.add(tmpSeries);
		}
		this.neededQuery = true;
		this.validateSelectedChart();
		if (this.statReady.booleanValue()) {
			this.showGraph();
		}

		return null;
	}

	/**
	 * Removes selected series from chart.
	 * 
	 * @return
	 */
	public String removeSeries() {
		if (this.toRemove != null) {
			// Find and remove series
			for (Iterator iter = toRemove.iterator(); iter.hasNext();) {
				String element = (String) iter.next();
				int hash = Integer.parseInt(element);
				for (int i = 0; i < this.series.size(); i++) {
					if (this.series.get(i).hashCode() == hash) {
						this.series.remove(i);
						this.neededQuery = true;
						break;
					}
				}
			}
		}
		this.validateSelectedChart();
		if (this.statReady.booleanValue()) {
			this.showGraph();
		}
		return null;
	}

	/**
	 * Generates chart.
	 * 
	 * @return
	 */
	public String showGraph() {

		if (!this.searchBean.getSearchParameters().getConditions().equals(this.conditions)) {
			this.conditions = (Conditions)this.searchBean.getSearchParameters().getConditions().clone();
			neededQuery = true;
		}
		
		// Check whether we should query DB
		if (this.searchBean.getSearchParameters().getConditions() != null && this.neededQuery) {
			this.statReady = new Boolean(true);

			// Prepare current generator
			AbstractChartGenerator generator = this.getChartGenerator();

			// Prepare query
			QueryValue qValue = this.prepareQueryValue(generator);
			qValue.setLimit(MAX_RESULTS_PER_GRAPH + 1);
			qValue.setFirstResult(this.firstResult);

			// Query
			Object[] objs = qValue.executeQuery();
			if (objs.length == 0) {
				if (this.firstResult != 0) {
					this.firstResult -= MAX_RESULTS_PER_GRAPH;
				}
			} else {

				Object[] realobjs = objs;
				if (objs.length > MAX_RESULTS_PER_GRAPH) {
					realobjs = new Object[objs.length - 1];
					for (int i = 0; i < realobjs.length; i++) {
						realobjs[i] = objs[i];
						
					}
				}
				
				if (objs.length > MAX_RESULTS_PER_GRAPH || this.firstResult != 0) {
					this.warningMessage = "Current query returns more than " + MAX_RESULTS_PER_GRAPH
						+ " results. Graph shows chunks of data by " + MAX_RESULTS_PER_GRAPH + " results." +
								"Use arrow buttons below to navigate between visible chunks. " +
								"Currently visible results from " + (this.firstResult + 1) + " to " + 
								(this.firstResult + realobjs.length) + ".";
					this.warningPresent = new Boolean(true);
				}
				
				// Add results to chart
				generator.correctAndCompleteData(objs);
				generator.addRowToDataSet(objs, this.series.toArray());

				// Put chart in session
				ExternalContext servletContext = FacesContext.getCurrentInstance().getExternalContext();
				((HttpSession) servletContext.getSession(true)).setAttribute(STAT_OBJECT_NAME, generator.getChart());
			}
			this.neededQuery = false;
		}

		return null;
	}

//	/**
//	 * Gets current number of result by invoking specific query.
//	 * 
//	 * @param generator
//	 *            currently used generator
//	 * @return number of results
//	 */
//	private int getNumberOfResults(AbstractChartGenerator generator) {
//
//		// Add prefix (we will use "v" alias for Voyage in query)
//		Conditions localCondition = this.conditions.addAttributesPrefix("v.");
//		localCondition.addCondition("v." + this.xaxis, null, Conditions.OP_IS_NOT);
//		localCondition.addCondition("vi.remoteVoyageId", new DirectValue("v.id"), Conditions.OP_EQUALS);
//
//		// Build query
//		QueryValue qValue = new QueryValue("VoyageIndex as vi, Voyage v", localCondition);
//
//		// set populated attributes
//		String xAxis = generator.getXAxisSelectOperator("v." + this.xaxis);
//		if (this.aggregate.booleanValue()) {
//			qValue.setGroupBy(new String[] { xAxis });
//		}
//		qValue.addPopulatedAttribute(xAxis, false);
//		qValue.setLimit(MAX_RESULTS_PER_GRAPH + 1);
//
//		// Execute query
//		Object[] ret = qValue.executeQuery();
//
//		return ret.length;
//	}

	public String setNewView() {
		return null;
	}

	/**
	 * Action invoked when user pressed fix error.
	 * 
	 * @return
	 */
	public String fixError() {
		for (Iterator iter = this.fixErrorActions.iterator(); iter.hasNext();) {
			MemorizedAction element = (MemorizedAction) iter.next();
			element.performAction();

		}
		this.errorPresent = new Boolean(false);
		this.validateSelectedChart();
		this.fixErrorActions.clear();
		this.rollbackActions.clear();
		return null;
	}

	/**
	 * Action invoked when user clicks back button (on error).
	 * 
	 * @return
	 */
	public String rollback() {
		for (Iterator iter = this.rollbackActions.iterator(); iter.hasNext();) {
			MemorizedAction element = (MemorizedAction) iter.next();
			element.performAction();

		}
		this.errorPresent = new Boolean(false);
		this.validateSelectedChart();
		this.fixErrorActions.clear();
		this.rollbackActions.clear();
		return null;
	}
	
	public void prev(ClickEvent event) {
		if (firstResult > 0) {
			this.firstResult -= MAX_RESULTS_PER_GRAPH;
			if (this.firstResult < 0) {
				this.firstResult = 0;
			}
			this.neededQuery = true;
			this.showGraph();
		}
	}

	public void next(ClickEvent event) {
		
		this.firstResult += MAX_RESULTS_PER_GRAPH;
		this.neededQuery = true;
		this.showGraph();
		
	}
	
	/**
	 * Gets selection of aggregate functions.
	 * 
	 * @return
	 */
	public Boolean getAggregate() {
		return aggregate;
	}

	/**
	 * Gets selection of non-aggregate functions.
	 * 
	 * @return
	 */
	public Boolean getNotAggregate() {
		return new Boolean(!aggregate.booleanValue());
	}

	/**
	 * Sets aggregate selection (after clicking checkbox on UI).
	 * 
	 * @param aggregate
	 */
	public void setAggregate(Boolean aggregate) {

		if (aggregate == null) {
			return;
		}
		
		// If we have any series added - we need to remove it firstly.
		if (this.series.size() > 0 && !aggregate.equals(this.aggregate) && !this.errorPresent.booleanValue()) {
			this.errorPresent = new Boolean(true);
			if (this.aggregate.booleanValue()) {
				this.errorMessage = "Series with aggregate functions present! Remove it?";
			} else {
				this.errorMessage = "Series with not aggregate functions present! Remove it?";
			}
			this.fixButton = "Remove series";
		}

		// Prepare actions for fix/rollback
		if (this.errorPresent.booleanValue() && !aggregate.equals(this.aggregate)) {
			MemorizedAction action = new MemorizedAction(new Object[] { this.aggregate }) {
				public void performAction() {
					AdvancedStatisticsTabBean.this.aggregate = (Boolean) this.params[0];
				}
			};
			this.rollbackActions.add(action);

			action = new MemorizedAction(new Object[] {}) {
				public void performAction() {
					AdvancedStatisticsTabBean.this.series = new ArrayList();
				}
			};
			this.fixErrorActions.add(action);
		}
		this.aggregate = aggregate;
	}

	/**
	 * Gets chosen order.
	 * 
	 * @return
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * Sets chosen order from UI.
	 * 
	 * @param orderby
	 */
	public void setOrder(String orderby) {
		if (orderby != null && !orderby.equals(this.order)) {
			this.neededQuery = true;
		}
		this.order = orderby;
	}

	/**
	 * Gets list of current series.
	 * 
	 * @return
	 */
	public List getSeries() {
		List list = new ArrayList();
		if (series != null) {
			for (Iterator iter = series.iterator(); iter.hasNext();) {
				SeriesItem element = (SeriesItem) iter.next();
				String output = element.toString();
				list.add(new ComparableSelectItem(element.hashCode() + "", output));
			}
		}
		return list;
	}

	public void setSeries(List series) {
	}

	/**
	 * Gets currently selected x axis attribute.
	 * 
	 * @return
	 */
	public String getXaxis() {
		return xaxis;
	}

	/**
	 * Sets attribute that will be shown on x axis.
	 * 
	 * @param xaxis
	 */
	public void setXaxis(String xaxis) {
		if (xaxis != null && !xaxis.equals(this.xaxis)) {
			this.neededQuery = true;
		}
		this.xaxis = xaxis;
	}

	/**
	 * Gets attributes that may appear on chart series.
	 * 
	 * @return List of SelectItems
	 */
	public List getVoyageSelectedAttributes() {

		ArrayList list = new ArrayList();
		// Look through attributes
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i].getAttributes().length != 1) {
				continue;
			}
			boolean ok = false;
			// if (this.selectedChart != null && this.selectedChart.equals("0"))
			// {
			// ok = true;
			// } else {
			// if (attributes[i].getType().intValue() == Attribute.TYPE_INTEGER
			// || attributes[i].getType().intValue() == Attribute.TYPE_LONG
			// || attributes[i].getType().intValue() == Attribute.TYPE_FLOAT
			// || attributes[i].getType().intValue() == Attribute.TYPE_DICT) {
			// ok = true;
			// }
			// }
			ok = true;
			if (ok) {
				String outString = null;
				if (attributes[i].getUserLabel() == null || 
						attributes[i].getUserLabel().equals("")) {
					outString = attributes[i].getName();
				} else {
					outString = attributes[i].getUserLabel();
				}
				list.add(new ComparableSelectItem(attributes[i].getName(), outString));
			}
		}

		// Sort output
		Collections.sort(list);

		return list;
	}

	/**
	 * Gets attributes that are numeric.
	 * 
	 * @return List of SelectItems
	 */
	public List getVoyageNumericAttributes() {

		voyageAttributes = new ArrayList();
		for (int i = 0; i < attributes.length; i++) {
			VisibleAttributeInterface attr = attributes[i];
			if (attr.getType().equals(VisibleAttributeInterface.NUMERIC_ATTRIBUTE) &&
					attr.getAttributes().length == 1) {
				String outString = attr.toString();
				// if (attr.getUserLabel() == null
				// || attr.getUserLabel().equals("")) {
				// outString = attributes[i];
				// } else {
				// outString = attr.getUserLabel();
				// }

				voyageAttributes.add(new ComparableSelectItem(attributes[i].getAttributes()[0].getName(), outString));

			}
		}
		Collections.sort(this.voyageAttributes);

		return this.voyageAttributes;

	}

	/**
	 * Gets currently available aggregates.
	 * 
	 * @return List of SelectItems
	 */
	public List getAggregateFunctions() {
		if (this.aggregateFunctions == null) {
			this.aggregateFunctions = new ArrayList();
			for (int i = 0; i < aggregates.length; i++) {
				this.aggregateFunctions.add(new ComparableSelectItem(aggregates[i], aggregatesUL[i]));
			}
		}
		return this.aggregateFunctions;
	}

	/**
	 * Gets currently available orders.
	 * 
	 * @return List of SelectItems
	 */
	public List getAvailableOrders() {
		if (this.availableOrders == null) {
			this.availableOrders = new ArrayList();
			for (int i = 0; i < orders.length; i++) {
				this.availableOrders.add(new ComparableSelectItem("" + i, orders[i]));
			}
		}
		return this.availableOrders;
	}

	/**
	 * Checks if there are any series added.
	 * 
	 * @return true if any series is added
	 */
	public Boolean getSeriesAdded() {
		return new Boolean(this.series.size() > 0);
	}

	/**
	 * Gets currently selected aggregate.
	 * 
	 * @return aggregate
	 */
	public String getSelectedAggregate() {
		return selectedAggregate;
	}

	/**
	 * Sets selected aggregate in UI.
	 * 
	 * @param selectedAggregate
	 *            aggregate
	 */
	public void setSelectedAggregate(String selectedAggregate) {
		this.selectedAggregate = selectedAggregate;
	}

	/**
	 * Gets y axis attribute
	 * 
	 * @return y axis attribute
	 */
	public String getYaxis() {
		if (yaxis == null) {
			return null;
		} else {
			return yaxis.getName();
		}
	}

	/**
	 * Sets y axis attribute
	 * 
	 * @param yaxis
	 *            y axis attribute
	 */
	public void setYaxis(String yaxis) {
		this.yaxis = VisibleAttribute.getAttribute(yaxis);
	}

	/**
	 * Gets List of series that can be removed.
	 * 
	 * @return
	 */
	public List getToRemove() {
		return toRemove;
	}

	/**
	 * Sets list of series that can be removed.
	 * 
	 * @param toRemove
	 */
	public void setToRemove(List toRemove) {
		this.toRemove = toRemove;
	}

	/**
	 * Gets current chart height.
	 * 
	 * @return
	 */
	public String getChartHeight() {
		return chartHeight;
	}

	/**
	 * Sets current chart height
	 * 
	 * @param chartHeight
	 */
	public void setChartHeight(String chartHeight) {
		this.chartHeight = chartHeight;
	}

	/**
	 * Gets current chart width.
	 * 
	 * @return
	 */
	public String getChartWidth() {
		return chartWidth;
	}

	/**
	 * Sets current chart width.
	 * 
	 * @param chartWidth
	 */
	public void setChartWidth(String chartWidth) {
		this.chartWidth = chartWidth;
	}

	/**
	 * Gets path of servlet providing chart.
	 * 
	 * @return
	 */
	public String getChartPath() {
		return IMAGE_FEEDED_SERVLET + "?path=" + STAT_OBJECT_NAME + "&&height=" + this.chartHeight + "&width="
				+ this.chartWidth;
	}

	/**
	 * Checks whether any chart is ready to show.
	 * 
	 * @return
	 */
	public Boolean getStatReady() {
		return statReady;
	}

	/**
	 * Gets currently selected chart type.
	 * 
	 * @return
	 */
	public String getSelectedChart() {
		return selectedChart;
	}

	/**
	 * Sets currently selected chart type in UI.
	 * 
	 * @param chartType
	 */
	public void setSelectedChart(String chartType) {
		if (chartType == null) return;
		if (!chartType.equals(this.selectedChart)) {
			this.neededQuery = true;
			validateSelectedChart();
		}
		this.selectedChart = chartType;
		if (this.statReady.booleanValue()) {
			this.showGraph();
		}
	}

	/**
	 * Gets list of available charts.
	 * 
	 * @return
	 */
	public List getAvailableCharts() {
		if (this.availableCharts == null) {
			this.availableCharts = new ArrayList();
			for (int i = 0; i < availableChartsLabels.length; i++) {
				this.availableCharts.add(new ComparableSelectItem(i + "", availableChartsLabels[i]));
			}
		}
		return availableCharts;
	}

	public void setAvailableCharts(List availableCharts) {
	}

	/**
	 * Gets error message.
	 * 
	 * @return
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Sets error message.
	 * 
	 * @param errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Checks whether error message is present.
	 * 
	 * @return
	 */
	public Boolean getErrorPresent() {
		return errorPresent;
	}

	/**
	 * TODO: Do i need it?
	 * 
	 * @param errorPresent
	 */
	public void setErrorPresent(Boolean errorPresent) {
		this.errorPresent = errorPresent;
	}

	/**
	 * Gets fix button user label.
	 * 
	 * @return
	 */
	public String getFixButton() {
		return fixButton;
	}

	/**
	 * TODO: do I need it?
	 * 
	 * @param fixButton
	 */
	public void setFixButton(String fixButton) {
		this.fixButton = fixButton;
	}

	/**
	 * Gets warning message
	 * 
	 * @return
	 */
	public String getWarningMessage() {
		return warningMessage;
	}

	/**
	 * TODO: do I need it?
	 * 
	 * @param warning
	 */
	public void setWarningMessage(String warning) {
		this.warningMessage = warning;
	}

	/**
	 * Checks whether warning message is present.
	 * 
	 * @return
	 */
	public Boolean getWarningPresent() {
		return warningPresent;
	}

	/**
	 * TODO: do I need it?
	 * 
	 * @param warningPresent
	 */
	public void setWarningPresent(Boolean warningPresent) {
		this.warningPresent = warningPresent;
	}

	public SearchBean getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}

}
