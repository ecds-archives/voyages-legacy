package edu.emory.library.tas.web.components.tabs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.DirectValue;
import edu.emory.library.tas.util.query.QueryValue;
import edu.emory.library.tas.web.components.tabs.chartGenerators.AbstractChartGenerator;

public class AdvancedStatisticsTabBean {

	private static final String STAT_OBJECT_NAME = "__stat__object";

	public static final String IMAGE_FEEDED_SERVLET = "servlet/ImageFeederServlet";

	private static final String[] orders = {"Ignore", "Asc", "Desc" };

	private static final String[] aggregates = { "avg", "min", "max", "sum",
			"count" };

	private static final String[] aggregatesUL = { "Avg", "Min", "Max", "Sum",
			"Count" };

	private static final String[] availableChartsLabels = {
			"XY chart", "Bar chart", "Pie chart" };

	private static final String[] chartGenerators = {
			"XYChartGenerator", "BarChartGenerator",
			"PieChartGenerator" };

	private static final String DEFAULT_CHART_HEIGHT = "480";

	private static final String DEFAULT_CHART_WIDTH = "640";

	private static final int MAX_RESULTS_PER_GRAPH = 1000;

	public class SeriesItem {

		public String attribute;

		public String aggregate;

		public SeriesItem(String attribute, String aggregate) {
			this.attribute = attribute;
			this.aggregate = aggregate;
		}
		
		public boolean equals(Object o) {
			if (!(o instanceof SeriesItem)) {
				return false;
			}
			SeriesItem that = (SeriesItem)o;
			return ((this.aggregate == null && that.aggregate == null) 
						|| this.aggregate.equals(that.aggregate)) 
					&& this.attribute.equals(that.attribute);
		}

		public String toString() {
			StringBuffer buffer = new StringBuffer();
			if (this.aggregate != null) {
				return buffer.append(this.aggregate).append("(").append(
						this.attribute).append(")").toString();
			} else {
				return buffer.append(this.attribute).toString();
			}
		}
	}

	private Conditions conditions;

	private boolean neededQuery = false;

	private List series = new ArrayList();

	private Boolean aggregate = new Boolean(false);

	private String order = "0";

	private String xaxis;

	private String yaxis;

	private String selectedAggregate;

	private List voyageAttributes;

	private List aggregateFunctions;

	private List availableOrders;

	private List toRemove;

	private String chartHeight = DEFAULT_CHART_HEIGHT;

	private String chartWidth = DEFAULT_CHART_WIDTH;

	private Boolean statReady = new Boolean(false);

	private String selectedChart = "0";

	private List availableCharts;

	private Attribute[] attributes = prepareAttributes();

	
	private List rollbackActions = new ArrayList();
	private List fixErrorActions = new ArrayList();

	private Boolean errorPresent = new Boolean(false);
	private String errorMessage = null;
	private String fixButton = null;
	
	private Boolean warningPresent = new Boolean(false);
	private String warningMessage = null;

	private boolean showedGraph = false;
	
	private Attribute[] prepareAttributes() {
		return Voyage.getAttributes();
	}

	private QueryValue prepareQueryValue(AbstractChartGenerator generator) {
		Conditions localCondition = this.conditions.addAttributesPrefix("v.");
		localCondition.addCondition("v." + this.xaxis, null, Conditions.OP_IS_NOT);
		localCondition.addCondition("vi.remoteVoyageId",
				new DirectValue("v.id"), Conditions.OP_EQUALS);

		QueryValue qValue = new QueryValue("VoyageIndex as vi, Voyage v",
				localCondition);
		
		String xAxis = generator.getXAxisSelectOperator("v." + this.xaxis);
		if (this.aggregate.booleanValue()) {
			qValue.setGroupBy(xAxis);
		}
		qValue.addPopulatedAttribute(xAxis, false);
		
		if (this.order.equals("1")) {
			qValue.setOrderBy(xAxis);
			qValue.setOrder(QueryValue.ORDER_ASC);
		} else if (this.order.equals("2")) {
			qValue.setOrderBy(xAxis);
			qValue.setOrder(QueryValue.ORDER_DESC);
		}
		
		for (Iterator iter = this.series.iterator(); iter.hasNext();) {
			AdvancedStatisticsTabBean.SeriesItem element = 
				(AdvancedStatisticsTabBean.SeriesItem) iter.next();
			String out = null;
			if (element.aggregate != null) {
				out = element.aggregate + "(v." + element.attribute + ")";
			} else {
				out = "v." + element.attribute;
			}
			qValue.addPopulatedAttribute(out, false);			
		}
		
		System.out.println(qValue.toStringWithParams().conditionString);
		
		return qValue;
	}

	private AbstractChartGenerator getChartGenerator() {
		int sel = Integer.parseInt(this.selectedChart);
		AbstractChartGenerator generator = null;
		String className = "edu.emory.library.tas.web.components.tabs.chartGenerators."
				+ chartGenerators[sel];
		Attribute attr = Voyage.getAttribute(this.xaxis);
		try {
			Class clazz = Class.forName(className);
			generator = (AbstractChartGenerator) clazz.getConstructor(new Class[] {Attribute.class})
						.newInstance(new Object[] {attr});
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

	private void validateSelectedChart() {
		if (this.selectedChart != null && this.selectedChart.equals("2") && this.series.size() > 1) {
			this.warningMessage = "Pie chart should have only one series. All series besides first will be ignored.";
			this.warningPresent = new Boolean(true);
		} else {
			this.warningPresent = new Boolean(false);
		}
	}
	
	public String addSeries() {
		String series = this.yaxis;
		String aggregate = this.selectedAggregate;
		SeriesItem tmpSeries;
		if (this.aggregate.booleanValue()) {
			tmpSeries = new SeriesItem(series, aggregate);
		} else {
			tmpSeries = new SeriesItem(series, null);
		}
		if (this.series.contains(tmpSeries)) {
			this.warningMessage = "Series has already been added.";
			this.warningPresent = new Boolean(true);
		} else {
			this.series.add(tmpSeries);
		}
		this.neededQuery = true;
		this.validateSelectedChart();
		return null;
	}

	public String removeSeries() {
		if (this.toRemove != null) {
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
		return null;
	}

	public String showGraph() {
		
		if (this.conditions != null && this.neededQuery) {
			this.statReady = new Boolean(true);
			
			AbstractChartGenerator generator = this.getChartGenerator();
			if (this.getNumberOfResults(generator) > MAX_RESULTS_PER_GRAPH) {
				this.warningMessage = "Current query returns more than " + MAX_RESULTS_PER_GRAPH 
								+ " voyages. Graph shows only first " + MAX_RESULTS_PER_GRAPH + " results.";
				this.warningPresent = new Boolean(true);
			} else {
				this.warningPresent = new Boolean(false);
			}
			
			QueryValue qValue = this.prepareQueryValue(generator);
			qValue.setLimit(MAX_RESULTS_PER_GRAPH);
			Object[] objs = qValue.executeQuery();
			generator.correctAndCompleteData(objs);
			generator.addRowToDataSet(objs, this.series.toArray());

			ExternalContext servletContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			((HttpSession) servletContext.getSession(true)).setAttribute(
					STAT_OBJECT_NAME, generator.getChart());

			this.neededQuery = false;
		}
		//this.validateSelectedChart();
		this.showedGraph = true;
		return null;
	}
	
	private int getNumberOfResults(AbstractChartGenerator generator) {
		
		Conditions localCondition = this.conditions.addAttributesPrefix("v.");
		localCondition.addCondition("v." + this.xaxis, null, Conditions.OP_IS_NOT);
		localCondition.addCondition("vi.remoteVoyageId",
				new DirectValue("v.id"), Conditions.OP_EQUALS);

		QueryValue qValue = new QueryValue("VoyageIndex as vi, Voyage v",
				localCondition);
		
		String xAxis = generator.getXAxisSelectOperator("v." + this.xaxis);
		if (this.aggregate.booleanValue()) {
			qValue.setGroupBy(xAxis);
		}
		qValue.addPopulatedAttribute(xAxis, false);
		qValue.setLimit(MAX_RESULTS_PER_GRAPH + 1);
		Object [] ret = qValue.executeQuery();

		return ret.length;
	}
	
	public String setNewView() {
		return null;
	}	
	
	public String fixError() {
		for (Iterator iter = this.fixErrorActions.iterator(); iter.hasNext();) {
			MemorizedAction element = (MemorizedAction) iter.next();
			element.performAction();
			
		}
		this.errorPresent = new Boolean(false);
		this.validateSelectedChart();
		return null;
	}
	
	public String rollback() {
		for (Iterator iter = this.rollbackActions.iterator(); iter.hasNext();) {
			MemorizedAction element = (MemorizedAction) iter.next();
			element.performAction();
			
		}
		this.errorPresent = new Boolean(false);
		this.validateSelectedChart();
		return null;
	}

	/**
	 * ///////////////////////////////// GETTERS / SETTERS
	 * //////////////////////////////////// *
	 */

	public Boolean getAggregate() {
		return aggregate;
	}

	public Boolean getNotAggregate() {
		return new Boolean(!aggregate.booleanValue());
	}

	public void setAggregate(Boolean aggregate) {
		if (this.series.size() > 0 
				&& !aggregate.equals(this.aggregate) 
				&& !this.errorPresent.booleanValue()) {
			this.errorPresent = new Boolean(true);
			if (this.aggregate.booleanValue()) {
				this.errorMessage = "Series with aggregate functions present! Remove it?";
			} else {
				this.errorMessage = "Series with not aggregate functions present! Remove it?";
			}
			this.fixButton = "Remove series";
		}
		if (this.errorPresent.booleanValue() && !aggregate.equals(this.aggregate)) {
			MemorizedAction action = new MemorizedAction(new Object[] {this.aggregate}) {
				public void performAction() {
					AdvancedStatisticsTabBean.this.aggregate = (Boolean)this.params[0];
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

	public Conditions getConditions() {
		return conditions;
	}

	public void setConditions(Conditions conditions) {
		if (conditions != null && !conditions.equals(this.conditions)) {
			this.conditions = conditions;
			this.neededQuery = true;
			if (this.showedGraph ) {
				this.showGraph();
			}
		}

	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String orderby) {
		if (orderby != null && !orderby.equals(this.order)) {
			this.neededQuery = true;
		}
		this.order = orderby;
	}

	public List getSeries() {
		List list = new ArrayList();
		if (series != null) {
			for (Iterator iter = series.iterator(); iter.hasNext();) {
				SeriesItem element = (SeriesItem) iter.next();
				String output = element.toString();
				list.add(new ComparableSelectItem(element.hashCode() + "", output));
			}
			Collections.sort(series);
		}
		return list;
	}

	public void setSeries(List series) {
	}

	public String getXaxis() {
		return xaxis;
	}

	public void setXaxis(String xaxis) {
		if (xaxis != null && !xaxis.equals(this.xaxis)) {
			this.neededQuery = true;
		}
		this.xaxis = xaxis;
	}

	public List getVoyageSelectedAttributes() {

		ArrayList list = new ArrayList();
		for (int i = 0; i < attributes.length; i++) {
			boolean ok = false;
			if (this.selectedChart.equals("0")) {
				ok = true;
			} else {
				if (attributes[i].getType().intValue() == Attribute.TYPE_INTEGER
						|| attributes[i].getType().intValue() == Attribute.TYPE_LONG
						|| attributes[i].getType().intValue() == Attribute.TYPE_FLOAT
						|| attributes[i].getType().intValue() == Attribute.TYPE_DICT) {
					ok = true;
				}
			}
			if (ok) {
				String outString = null;
				if (attributes[i].getUserLabel() == null
						|| attributes[i].getUserLabel().equals("")) {
					outString = attributes[i].getName();
				} else {
					outString = attributes[i].getUserLabel();
				}
				list.add(new ComparableSelectItem(attributes[i].getName(), outString));
			}
		}
		
		return list;
	}

	public List getVoyageNumericAttributes() {

		if (voyageAttributes == null) {
			String[] attributes = Voyage.getAllAttrNames();
			Arrays.sort(attributes);
			this.voyageAttributes = new ArrayList();
			for (int i = 0; i < attributes.length; i++) {
				Attribute attr = Voyage.getAttribute(attributes[i]);
				if (attr.getType().intValue() == Attribute.TYPE_FLOAT
						|| attr.getType().intValue() == Attribute.TYPE_INTEGER
						|| attr.getType().intValue() == Attribute.TYPE_LONG) {
					String outString = null;
					if (attr.getUserLabel() == null
							|| attr.getUserLabel().equals("")) {
						outString = attributes[i];
					} else {
						outString = attr.getUserLabel();
					}

					voyageAttributes.add(new ComparableSelectItem(attributes[i],
							outString));

				}
			}
			Collections.sort(voyageAttributes);
		}
		return this.voyageAttributes;

	}

	public List getAggregateFunctions() {
		if (this.aggregateFunctions == null) {
			this.aggregateFunctions = new ArrayList();
			for (int i = 0; i < aggregates.length; i++) {
				this.aggregateFunctions.add(new ComparableSelectItem(aggregates[i],
						aggregatesUL[i]));
			}
		}
		return this.aggregateFunctions;
	}

	public List getAvailableOrders() {
		if (this.availableOrders == null) {
			this.availableOrders = new ArrayList();
			for (int i = 0; i < orders.length; i++) {
				this.availableOrders.add(new ComparableSelectItem("" + i, orders[i]));
			}
		}
		return this.availableOrders;
	}

	public Boolean getSeriesAdded() {
		return new Boolean(this.series.size() > 0);
	}

	public String getSelectedAggregate() {
		return selectedAggregate;
	}

	public void setSelectedAggregate(String selectedAggregate) {
		this.selectedAggregate = selectedAggregate;
	}

	public String getYaxis() {
		return yaxis;
	}

	public void setYaxis(String yaxis) {
		this.yaxis = yaxis;
	}

	public List getToRemove() {
		return toRemove;
	}

	public void setToRemove(List toRemove) {
		this.toRemove = toRemove;
	}

	public String getChartHeight() {
		return chartHeight;
	}

	public void setChartHeight(String chartHeight) {
		this.chartHeight = chartHeight;
	}

	public String getChartWidth() {
		return chartWidth;
	}

	public void setChartWidth(String chartWidth) {
		this.chartWidth = chartWidth;
	}

	public String getChartPath() {
		return IMAGE_FEEDED_SERVLET + "?path=" + STAT_OBJECT_NAME + "&&height="
				+ this.chartHeight + "&width=" + this.chartWidth;
	}

	public Boolean getStatReady() {
		return statReady;
	}

	public String getSelectedChart() {
		return selectedChart;
	}

	public void setSelectedChart(String chartType) {
		if (chartType != null && !chartType.equals(this.selectedChart)) {
			this.neededQuery = true;
		}
		this.selectedChart = chartType;
		validateSelectedChart();
	}

	public List getAvailableCharts() {
		if (this.availableCharts == null) {
			this.availableCharts = new ArrayList();
			for (int i = 0; i < availableChartsLabels.length; i++) {
				this.availableCharts.add(new ComparableSelectItem(i + "",
						availableChartsLabels[i]));
			}
		}
		return availableCharts;
	}

	public void setAvailableCharts(List availableCharts) {
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Boolean getErrorPresent() {
		return errorPresent;
	}

	public void setErrorPresent(Boolean errorPresent) {
		this.errorPresent = errorPresent;
	}

	public String getFixButton() {
		return fixButton;
	}

	public void setFixButton(String fixButton) {
		this.fixButton = fixButton;
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warning) {
		this.warningMessage = warning;
	}

	public Boolean getWarningPresent() {
		return warningPresent;
	}

	public void setWarningPresent(Boolean warningPresent) {
		this.warningPresent = warningPresent;
	}

}
