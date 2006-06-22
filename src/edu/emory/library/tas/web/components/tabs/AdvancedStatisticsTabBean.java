package edu.emory.library.tas.web.components.tabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
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

	private static final String[] orders = { "Ignore", "Asc", "Desc" };

	private static final String[] aggregates = { "avg", "min", "max", "sum",
			"count" };

	private static final String[] aggregatesUL = { "Avg", "Min", "Max", "Sum",
			"Count" };

	private static final String[] availableChartsLabels = {
			"Time series chart", "Bar chart", "Pie chart" };

	private static final String[] chartGenerators = {
			"TimeSeriesChartGenerator", "BarChartGenerator",
			"PieChartGenerator" };

	private static final String DEFAULT_CHART_HEIGHT = "480";

	private static final String DEFAULT_CHART_WIDTH = "640";

	public class SeriesItem {

		public String attribute;

		public String aggregate;

		public SeriesItem(String attribute, String aggregate) {
			this.attribute = attribute;
			this.aggregate = aggregate;
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

	private String order;

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

	private Attribute[] prepareAttributes() {
		return Voyage.getAttributes();
	}

	private QueryValue prepareQueryValue(AbstractChartGenerator generator) {
		Conditions localCondition = this.conditions.addAttributesPrefix("v.");
		localCondition.addCondition("v.datedep", null, Conditions.OP_IS_NOT);
		localCondition.addCondition("vi.remoteVoyageId",
				new DirectValue("v.id"), Conditions.OP_EQUALS);

		QueryValue qValue = new QueryValue("VoyageIndex as vi, Voyage v",
				localCondition);
		
		String xAxis = generator.getXAxisSelectOperator(this.xaxis);
		if (this.aggregate.booleanValue()) {
			qValue.setGroupBy(xAxis);
		}
		qValue.addPopulatedAttribute(xAxis, false);

		for (Iterator iter = this.series.iterator(); iter.hasNext();) {
			AdvancedStatisticsTabBean.SeriesItem element = 
				(AdvancedStatisticsTabBean.SeriesItem) iter.next();
			qValue.addPopulatedAttribute(element.toString(), false);			
		}
		
		return qValue;
	}

	private AbstractChartGenerator getChartGenerator() {
		int sel = Integer.parseInt(this.selectedChart);
		AbstractChartGenerator generator = null;
		String className = "edu.emory.library.tas.web.components.tabs.chartGenerators"
				+ chartGenerators[sel];
		try {
			Class clazz = Class.forName(className);
			generator = (AbstractChartGenerator) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return generator;
	}

	public String addSeries() {
		String series = this.yaxis;
		String aggregate = this.selectedAggregate;
		if (this.aggregate.booleanValue()) {
			this.series.add(new SeriesItem(series, aggregate));
		} else {
			this.series.add(new SeriesItem(series, null));
		}
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
						break;
					}
				}
			}
		}
		return null;
	}

	public String showGraph() {
		this.statReady = new Boolean(true);
		if (this.neededQuery) {
			AbstractChartGenerator generator = this.getChartGenerator();
			QueryValue qValue = this.prepareQueryValue(generator);
			Object[] objs = qValue.executeQuery();
			generator.addRowToDataSet(objs, this.series.toArray());

			ExternalContext servletContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			((HttpSession) servletContext.getSession(true)).setAttribute(
					STAT_OBJECT_NAME, generator.getChart());

			this.neededQuery = false;
		}
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
		this.aggregate = aggregate;
	}

	public Conditions getConditions() {
		return conditions;
	}

	public void setConditions(Conditions conditions) {
		if (conditions != null && conditions.equals(this.conditions)) {
			this.conditions = conditions;
			this.neededQuery = true;
		}

	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String orderby) {
		this.order = orderby;
	}

	public List getSeries() {
		List list = new ArrayList();
		if (series != null) {
			for (Iterator iter = series.iterator(); iter.hasNext();) {
				SeriesItem element = (SeriesItem) iter.next();
				String output = element.toString();
				list.add(new SelectItem(element.hashCode() + "", output));
			}
		}
		return list;
	}

	public void setSeries(List series) {
	}

	public String getXaxis() {
		return xaxis;
	}

	public void setXaxis(String xaxis) {
		this.xaxis = xaxis;
	}

	public List getVoyageSelectedAttributes() {

		ArrayList list = new ArrayList();
		for (int i = 0; i < attributes.length; i++) {
			boolean ok = false;
			if (this.selectedChart.equals("0")) {
				if (attributes[i].getType().intValue() == Attribute.TYPE_DATE) {
					ok = true;
				}
			} else {
				if (attributes[i].getType().intValue() == Attribute.TYPE_INTEGER
						|| attributes[i].getType().intValue() == Attribute.TYPE_LONG
						|| attributes[i].getType().intValue() == Attribute.TYPE_FLOAT) {
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
				list.add(new SelectItem(attributes[i].getName(), outString));
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

					voyageAttributes.add(new SelectItem(attributes[i],
							outString));

				}
			}
		}
		return this.voyageAttributes;

	}

	public List getAggregateFunctions() {
		if (this.aggregateFunctions == null) {
			this.aggregateFunctions = new ArrayList();
			for (int i = 0; i < aggregates.length; i++) {
				this.aggregateFunctions.add(new SelectItem(aggregates[i],
						aggregatesUL[i]));
			}
		}
		return this.aggregateFunctions;
	}

	public List getAvailableOrders() {
		if (this.availableOrders == null) {
			this.availableOrders = new ArrayList();
			for (int i = 0; i < orders.length; i++) {
				this.availableOrders.add(new SelectItem(orders[i]));
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
		this.selectedChart = chartType;
	}

	public List getAvailableCharts() {
		if (this.availableCharts == null) {
			this.availableCharts = new ArrayList();
			for (int i = 0; i < availableChartsLabels.length; i++) {
				this.availableCharts.add(new SelectItem(i + "",
						availableChartsLabels[i]));
			}
		}
		return availableCharts;
	}

	public void setAvailableCharts(List availableCharts) {
	}

}
