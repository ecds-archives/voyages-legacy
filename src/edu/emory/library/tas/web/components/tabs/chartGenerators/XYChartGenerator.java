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

public class XYChartGenerator extends AbstractChartGenerator {

	private Dataset dataset;

	public XYChartGenerator() {
		dataset = new TimeSeriesCollection();
	}

	public JFreeChart getChart(String xAxis) {
		if (this.isDate()) {
			return this.prepareDateChart(xAxis);
		} else {
			return this.prepareXYChart(xAxis);
		}
	}

	private JFreeChart prepareXYChart(String xAxis) {
		JFreeChart chart = ChartFactory.createLineChart(null,
				 xAxis, "Value", (DefaultCategoryDataset)dataset, PlotOrientation.VERTICAL,
				 true, true, false);
			
		return chart;
	}

	private JFreeChart prepareDateChart(String xAxis) {

		chart = ChartFactory.createTimeSeriesChart(null, xAxis, "Value",
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
		if (this.isDate()) {
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

	public String getXAxisSelectOperator(String xAxisAttribute) {
		if (!isDate()) {
			return xAxisAttribute;
		} else {
			return "date_trunc('year', " + xAxisAttribute + ")";
		}
	}

}
