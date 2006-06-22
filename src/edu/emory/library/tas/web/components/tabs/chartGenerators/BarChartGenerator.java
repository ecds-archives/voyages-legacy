package edu.emory.library.tas.web.components.tabs.chartGenerators;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChartGenerator extends AbstractChartGenerator {

	private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	
	public JFreeChart getChart(String xAxis) {
		
		JFreeChart chart = ChartFactory.createBarChart(null,
			 xAxis, "Value", dataset, PlotOrientation.VERTICAL,
			 true, true, false);
		
		return chart;
	}

	public void addRowToDataSet(Object[] data, Object[] series) {
		for (int i = 0; i < series.length; i++) {			
			for (int j = 0; j < data.length; j++) {
				Object[] row = (Object[])data[j];
				dataset.addValue((Number)row[i + 1], series[i].toString(), row[0].toString());
			}
		}
	}

	public String getXAxisSelectOperator(String xAxisAttribute) {
		return xAxisAttribute;
	}

}
