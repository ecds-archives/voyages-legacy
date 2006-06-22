package edu.emory.library.tas.web.components.tabs.chartGenerators;

import org.jfree.chart.JFreeChart;

public abstract class AbstractChartGenerator {
	protected JFreeChart chart = null;
	private boolean date = false;
	
	public abstract JFreeChart getChart(String xAxis);

	public abstract void addRowToDataSet(Object[] data, Object[] series);
	
	public abstract String getXAxisSelectOperator(String xAxisAttribute);
	
	public void setDate(boolean date) {
		this.date = date;
	}
	
	public boolean isDate() {
		return this.date;
	}
}
