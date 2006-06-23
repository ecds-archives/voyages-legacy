package edu.emory.library.tas.web.components.tabs.chartGenerators;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import edu.emory.library.tas.attrGroups.Attribute;

public class XYChartGenerator extends AbstractChartGenerator {

	private Dataset dataset;

	public XYChartGenerator(Attribute xAxis) {
		super(xAxis);
		dataset = new TimeSeriesCollection();
	}

	public JFreeChart getChart() {
		if (this.getXAxisAttribute().getType().intValue() == Attribute.TYPE_DATE) {
			return this.prepareDateChart();
		} else {
			return this.prepareXYChart();
		}
	}

	private JFreeChart prepareXYChart() {
		JFreeChart chart = ChartFactory.createLineChart(null,
				getXAxis(), "Value", (DefaultCategoryDataset)dataset, PlotOrientation.VERTICAL,
				 true, true, false);
			
		return chart;
	}

	private JFreeChart prepareDateChart() {

		chart = ChartFactory.createTimeSeriesChart(null, getXAxis(), "Value",
				(TimeSeriesCollection)dataset, true, true, false);

		XYPlot xyplot = (XYPlot) chart.getPlot();
		// xyplot.setBackgroundPaint(Color.LIGHT_GRAY);
		// xyplot.setDomainGridlinePaint(Color.WHITE);
		// xyplot.setRangeGridlinePaint(Color.WHITE);
		// xyplot.setAxisOffset(new RectangleInsets(5, 5, 5, 5));
		// xyplot.setDomainCrosshairVisible(true);
		// xyplot.setRangeCrosshairVisible(true);
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		dateaxis.setDateFormatOverride(new SimpleDateFormat("yyyy"));

		return chart;
	}

	public void addRowToDataSet(Object[] data, Object[] series) {
		if (this.getXAxisAttribute().getType().intValue() == Attribute.TYPE_DATE) {
			addDateRowToDataSet(data, series);
		} else {
			addSimpleDataRowToDataSet(data, series);
		}
		
	}

	private void addSimpleDataRowToDataSet(Object[] data, Object[] series) {
		this.dataset = new DefaultCategoryDataset();
		for (int i = 0; i < series.length; i++) {			
			for (int j = 0; j < data.length; j++) {
				Object[] row = (Object[])data[j];
				((DefaultCategoryDataset)dataset).addValue((Number)row[i + 1], 
						series[i].toString(), row[0].toString());
			}
		}
	}
	
	private void addDateRowToDataSet(Object[] data, Object[] series) {
		this.dataset = new TimeSeriesCollection();
		for (int i = 0; i < series.length; i++) {
			TimeSeries timeseries = new TimeSeries(series[i].toString());
			for (int j = 0; j < data.length; j++) {
				Object[] row = (Object[]) data[j];
				timeseries.addOrUpdate(new Day((Date) row[0]),
						(Number) row[i + 1]);
			}
			((TimeSeriesCollection)this.dataset).addSeries(timeseries);
		}
	}

//	public String getXAxisSelectOperator(String xAxisAttribute) {
//		if (this.getType() != Attribute.TYPE_DATE) {
//			return xAxisAttribute;
//		} else {
//			return "date_trunc('year', " + xAxisAttribute + ")";
//		}
//	}

}
