package edu.emory.library.tas.web.components.tabs.chartGenerators;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import edu.emory.library.tas.attrGroups.Attribute;

public class PieChartGenerator extends AbstractChartGenerator {

	DefaultPieDataset pieDataset = new DefaultPieDataset();
	
	public PieChartGenerator(Attribute xAxis) {
		super(xAxis);
	}
	
	public JFreeChart getChart() {
		return this.getChart(null, false);
	}
	
	public JFreeChart getChart(String title, boolean showLegend) {
		JFreeChart chart = ChartFactory.createPieChart(title, 
				pieDataset, showLegend, true, false);
		chart.setBackgroundPaint(new Color(241, 227, 101));
		return chart;
	}

	public void addRowToDataSet(Object[] data, Object[] series) {
		for (int i = 0; i < data.length; i++) {
			Object [] row = (Object[])data[i];
			pieDataset.setValue(row[0].toString(), (Number)row[1]);
		}
		
	}

//	public String getXAxisSelectOperator(String xAxisAttribute) {
//		return xAxisAttribute;
//	}

}
