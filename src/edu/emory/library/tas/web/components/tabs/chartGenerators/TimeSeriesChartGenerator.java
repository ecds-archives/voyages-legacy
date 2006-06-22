package edu.emory.library.tas.web.components.tabs.chartGenerators;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class TimeSeriesChartGenerator extends AbstractChartGenerator {
	
	private TimeSeriesCollection dataset;
	
	public TimeSeriesChartGenerator() {
		dataset = new TimeSeriesCollection();
	}
	
	public JFreeChart getChart() {
		
		chart = ChartFactory.createTimeSeriesChart("Time statistics",
				"Year of voyages", " value ", dataset, false, true,
				false);

		
		XYPlot xyplot = (XYPlot) chart.getPlot();
		xyplot.setBackgroundPaint(Color.LIGHT_GRAY);
		xyplot.setDomainGridlinePaint(Color.WHITE);
		xyplot.setRangeGridlinePaint(Color.WHITE);
		xyplot.setAxisOffset(new RectangleInsets(5, 5, 5, 5));
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		dateaxis.setDateFormatOverride(new SimpleDateFormat("yyyy"));
		
		return chart;
	}

	public void addRowToDataSet(Object[] data, Object [] series) {
		for (int i = 0; i < series.length; i++) {
			TimeSeries timeseries = new TimeSeries(series[i].toString());
			for (int j = 0; j < data.length; j++) {
				Object[] row = (Object[]) data[j];
				timeseries.add(new Day((Date) row[0]), (Number) row[i + 1]);
			}
			this.dataset.addSeries(timeseries);
		}
	}

	public String getXAxisSelectOperator(String xAxisAttribute) {
		return "date_trunc('year', " + xAxisAttribute + ")";
	}

}
