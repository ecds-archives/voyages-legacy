package edu.emory.library.tas.web.components.tabs.chartGenerators;

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

import edu.emory.library.tas.attrGroups.Attribute;

/**
 * Generator for bar charts.
 * @author Pawel Jurczyk
 *
 */
public class BarChartGenerator extends AbstractChartGenerator {

	/**
	 * Dataset of chart.
	 */
	private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	
	/**
	 * Constructor.
	 */
	public BarChartGenerator(Attribute xAxis) {
		super(xAxis);
	}
	
	/**
	 * @inheritDoc
	 */
	public JFreeChart getChart(String title, boolean showLegend) {
		
		//Create chart
		JFreeChart chart = ChartFactory.createBarChart(title,
				getXAxis(), "Value", dataset, PlotOrientation.VERTICAL,
			 showLegend, true, false);
				
		//Update chart
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
			//Date data
			addDateRowToDataSet(data, series);
		} else {
			//Other type data
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
	 * @param data rows of data
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

//	public String getXAxisSelectOperator(String xAxisAttribute) {
//		
//		return xAxisAttribute + ".id";
//	}

}
