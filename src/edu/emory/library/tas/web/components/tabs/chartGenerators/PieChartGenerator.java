package edu.emory.library.tas.web.components.tabs.chartGenerators;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class PieChartGenerator extends AbstractChartGenerator {

	DefaultPieDataset pieDataset = new DefaultPieDataset();
	
	public JFreeChart getChart(String xAxis) {
		JFreeChart chart = ChartFactory.createPieChart(null, 
				pieDataset, false, true, false);
		return chart;
	}

	public void addRowToDataSet(Object[] data, Object[] series) {
		for (int i = 0; i < data.length; i++) {
			Object [] row = (Object[])data[i];
			pieDataset.setValue(row[0].toString(), (Number)row[1]);
		}
		
	}

	public String getXAxisSelectOperator(String xAxisAttribute) {
		return xAxisAttribute;
	}

}