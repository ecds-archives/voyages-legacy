package edu.emory.library.tast.ui.search.stat.charts;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import edu.emory.library.tast.dm.attributes.Attribute;

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
		chart.setBackgroundPaint(Color.white);
		return prepareChart(chart);
	}

	/**
	 * @inheritDoc
	 */
	public void addRowToDataSet(Object[] data, Object[] series) {
		if (series.length == 0) {
			return;
		}
		for (int i = 0; i < data.length; i++) {
			Object [] row = (Object[])data[i];
			pieDataset.setValue(row[0].toString(), (Number)row[1]);
		}
		
	}

}
