package edu.emory.library.tas.web.components.tabs.chartGenerators;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import edu.emory.library.tas.attrGroups.Attribute;

/**
 * Generator that creates pie charts.
 * @author Pawel Jurczyk
 *
 */
public class PieChartGenerator extends AbstractChartGenerator {

	/**
	 * Dataset.
	 */
	DefaultPieDataset pieDataset = new DefaultPieDataset();
	
	/**
	 * Constructor.
	 * @param xAxis x axis attribute
	 */
	public PieChartGenerator(Attribute xAxis) {
		super(xAxis);
	}
	
	/**
	 * Creates chart with no title and legend.
	 */
	public JFreeChart getChart() {
		return this.getChart(null, false);
	}
	
	/**
	 * Creates chart.
	 * @inheritDoc
	 */
	public JFreeChart getChart(String title, boolean showLegend) {
		JFreeChart chart = ChartFactory.createPieChart(title, 
				pieDataset, showLegend, true, false);
		chart.setBackgroundPaint(new Color(241, 227, 101));
		return prepareChart(chart);
	}

	/**
	 * @inheritDoc
	 */
	public void addRowToDataSet(Object[] data, Object[] series) {
		for (int i = 0; i < data.length; i++) {
			Object [] row = (Object[])data[i];
			pieDataset.setValue(row[0].toString(), (Number)row[1]);
		}
		
	}

}
