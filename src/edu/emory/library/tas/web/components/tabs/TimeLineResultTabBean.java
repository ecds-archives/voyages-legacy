package edu.emory.library.tas.web.components.tabs;

import java.awt.Color;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.ui.RectangleInsets;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.DirectValue;
import edu.emory.library.tas.util.query.QueryValue;
import edu.emory.library.tas.web.components.tabs.chartGenerators.AbstractChartGenerator;
import edu.emory.library.tas.web.components.tabs.chartGenerators.XYChartGenerator;

public class TimeLineResultTabBean {

	public static final String IMAGE_FEEDED_SERVLET = "servlet/ImageFeederServlet";

	private static final String[] aggregates = { "avg", "min", "max", "sum",
			"count" };

	private static final String[] aggregatesUL = { "Avg", "Min", "Max", "Sum",
			"Count" };

	private static final String DEFAULT_CHART_HEIGHT = "480";

	private static final String DEFAULT_CHART_WIDTH = "640";

	private List voyageAttributes;

	private List aggregateFunctions;

	private String chosenAggregate = "sum";

	private String chosenAttribute = "sla32imp";

	private Conditions conditions = null;

	private boolean needQuery = false;

	private boolean attributesChanged = false;

	private JFreeChart chart;

	private Boolean componentVisible = new Boolean(false);

	private String chartHeight = DEFAULT_CHART_HEIGHT;

	private String chartWidth = DEFAULT_CHART_WIDTH;

	public TimeLineResultTabBean() {
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
				this.aggregateFunctions.add(new SelectItem(aggregates[i],
						aggregatesUL[i]));
			}
		}
		return this.aggregateFunctions;
	}

	public String showTimeLine() {
		if (this.componentVisible.booleanValue()
				&& (this.needQuery || this.attributesChanged)) {

			Conditions localCondition = this.conditions
					.addAttributesPrefix("v.");
			localCondition
					.addCondition("v.datedep", null, Conditions.OP_IS_NOT);
			localCondition.addCondition("vi.remoteVoyageId", new DirectValue(
					"v.id"), Conditions.OP_EQUALS);

			QueryValue qValue = new QueryValue("VoyageIndex as vi, Voyage v",
					localCondition);
			qValue.setGroupBy("date_trunc('year', v.datedep)");
			qValue
					.addPopulatedAttribute("date_trunc('year', v.datedep)",
							false);
			qValue.addPopulatedAttribute(this.chosenAggregate + "(v."
					+ this.chosenAttribute + ")", false);
			qValue.setOrderBy("date_trunc('year', v.datedep)");
			Object[] ret = qValue.executeQuery();

//			TimeSeriesCollection dataset = new TimeSeriesCollection();
//			TimeSeries timeseries = new TimeSeries("Years");
//			dataset.addSeries(timeseries);
//
//			for (int i = 0; i < ret.length; i++) {
//				Object[] row = (Object[]) ret[i];
//				timeseries.add(new ChangedDay((Date) row[0]), (Number) row[1]);
//
//			}
//
//			chart = ChartFactory.createTimeSeriesChart("Time statistics",
//					"Year of voyages", "" + this.chosenAggregate + "("
//							+ this.chosenAttribute + ")", dataset, false, true,
//					false);
//
//			
//			XYPlot xyplot = (XYPlot) chart.getPlot();
//			xyplot.setBackgroundPaint(Color.LIGHT_GRAY);
//			xyplot.setDomainGridlinePaint(Color.WHITE);
//			xyplot.setRangeGridlinePaint(Color.WHITE);
//			xyplot.setAxisOffset(new RectangleInsets(5, 5, 5, 5));
//			xyplot.setDomainCrosshairVisible(true);
//			xyplot.setRangeCrosshairVisible(true);
//			DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
//			dateaxis.setDateFormatOverride(new SimpleDateFormat("yyyy"));
			AbstractChartGenerator generator = new XYChartGenerator(Voyage.getAttribute("datedep"));
			generator.correctAndCompleteData(ret);
			generator.addRowToDataSet(ret, new String[] {this.chosenAggregate + "("
					+ Voyage.getAttribute(this.chosenAttribute) + ")"});
			chart = generator.getChart("Time line graph", false);
			
			ExternalContext servletContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			((HttpSession) servletContext.getSession(true)).setAttribute(
					"__chart__object", chart);

			this.needQuery = false;
			this.attributesChanged = false;
		}

		return null;
	}

	public String getChosenAggregate() {
		return chosenAggregate;
	}

	public void setChosenAggregate(String chosenAggregate) {
		if (!chosenAggregate.equals(this.chosenAttribute)) {
			this.chosenAggregate = chosenAggregate;
			this.attributesChanged = true;
		}
	}

	public String getChosenAttribute() {
		return chosenAttribute;
	}

	public void setChosenAttribute(String chosenAttribute) {
		if (!chosenAttribute.equals(this.chosenAttribute)) {
			this.chosenAttribute = chosenAttribute;
			this.attributesChanged = true;
		}

	}

	public String getChartPath() {
		return IMAGE_FEEDED_SERVLET 
				+ "?path=__chart__object&&height=" 
				+ this.chartHeight + "&width=" 
				+ this.chartWidth;
	}

	public void setChartPath(String path) {
	}
	
	public String setNewView() {
		return null;
	}

	public boolean getChartReady() {
		return this.chart != null;
	}

	public void setConditions(Conditions c) {
		if (c == null || c.equals(conditions)) {
			return;
		} else {
			conditions = c;
			needQuery = true;
		}
		showTimeLine();
	}

	public Boolean getComponentVisible() {
		return componentVisible;
	}

	public void setComponentVisible(Boolean componentVisible) {
		boolean shouldQuery = false;
		if (this.componentVisible.booleanValue() == false
				&& componentVisible.booleanValue() == true) {
			shouldQuery = true;
		}
		this.componentVisible = componentVisible;
		if (shouldQuery) {
			this.showTimeLine();
		}
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
}
