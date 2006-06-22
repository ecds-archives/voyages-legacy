package edu.emory.library.tas.web.components.tabs.chartGenerators;

import org.jfree.chart.JFreeChart;

public abstract class AbstractChartGenerator {
	protected JFreeChart chart = null;
	
	public abstract JFreeChart getChart();

	public abstract void addRowToDataSet(Object[] data, Object[] series);
	
	public abstract String getXAxisSelectOperator(String xAxisAttribute);
}
