package edu.emory.library.tast.ui.search.stat.charts;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

import edu.emory.library.tast.dm.attributes.Attribute;

/**
 * Generator for XY charts.
 */
public class XYChartGenerator extends AbstractChartGenerator {

	/**
	 * Dataset.
	 */
	private Dataset dataset;

	/**
	 * @inheritDoc
	 */
	public XYChartGenerator(Attribute xAxis) {
		super(xAxis);
		dataset = new DefaultCategoryDataset();
	}

	/**
	 * @inheritDoc
	 */
	public JFreeChart getChart(String title, boolean showLegend) {
		if (this.getXAxisAttribute().getType().intValue() == Attribute.TYPE_DATE) {
			return prepareChart(this.prepareDateChart(title, showLegend));
		} else {
			return prepareChart(this.prepareXYChart(title, showLegend));
		}
	}

	/**
	 * @inheritDoc
	 */
	public JFreeChart getChart() {
		return getChart(null, true);
	}

	/**
	 * Prepares XY chart with other than Date data
	 * @param title title of chart
	 * @param showLegend true if legend is desired
	 * @return
	 */
	private JFreeChart prepareXYChart(String title, boolean showLegend) {
		JFreeChart chart = ChartFactory.createLineChart(title, getXAxis(), "Value", (DefaultCategoryDataset) dataset,
				PlotOrientation.VERTICAL, showLegend, true, false);

		CategoryPlot xyplot = (CategoryPlot) chart.getPlot();
		CategoryAxis axis = xyplot.getDomainAxis();
		
		SkippableCategoryAxis newAxis = new SkippableCategoryAxis(axis);
		newAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
		xyplot.setDomainAxis(newAxis);
		chart.setBackgroundPaint(new Color(241, 227, 101));
		return chart;
	}

	/**
	 * Prepares chart with Date data
	 */
	private JFreeChart prepareDateChart(String title, boolean showLegend) {

		JFreeChart chart = ChartFactory.createLineChart(title, getXAxis(), "Value", (DefaultCategoryDataset) dataset,
				PlotOrientation.VERTICAL, showLegend, true, false);

		CategoryPlot xyplot = (CategoryPlot) chart.getPlot();
		CategoryAxis axis = xyplot.getDomainAxis();
		
		SkippableCategoryAxis newAxis = new SkippableCategoryAxis(axis);
		newAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
		xyplot.setDomainAxis(newAxis);
		chart.setBackgroundPaint(new Color(241, 227, 101));
		return chart;
	}

	/**
	 * @inheritDoc
	 */
	public void addRowToDataSet(Object[] data, Object[] series) {
		if (this.getXAxisAttribute().getType().intValue() == Attribute.TYPE_DATE) {
			addDateRowToDataSet(data, series);
		} else {
			addSimpleDataRowToDataSet(data, series);
		}

	}

	/**
	 * Adds other type than date data to chart.
	 * @param data Rows of data
	 * @param series series of chart
	 */
	private void addSimpleDataRowToDataSet(Object[] data, Object[] series) {
		for (int i = 0; i < series.length; i++) {
			for (int j = 0; j < data.length; j++) {
				Object[] row = (Object[]) data[j];
				((DefaultCategoryDataset) dataset).addValue((Number) row[i + 1], series[i].toString(), row[0]
						.toString());
			}
		}
	}

	/**
	 * Adds date data to chart.
	 * @param data Rows of data
	 * @param series series of chart
	 */
	private void addDateRowToDataSet(Object[] data, Object[] series) {
		for (int i = 0; i < series.length; i++) {
			for (int j = 0; j < data.length; j++) {
				Object[] row = (Object[]) data[j];
				Date date = (Date) row[0];
				Calendar cal = new GregorianCalendar();
				cal.setTime(date);
				String str = cal.get(Calendar.YEAR) + "";
				((DefaultCategoryDataset) dataset).addValue((Number) row[i + 1], series[i].toString(), str);
			}
		}
	}

}
