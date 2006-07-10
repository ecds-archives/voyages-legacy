package edu.emory.library.tas.web.components.tabs.chartGenerators;

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

public class BarChartGenerator extends AbstractChartGenerator {

	private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	
	public BarChartGenerator(Attribute xAxis) {
		super(xAxis);
	}
	
	public JFreeChart getChart(String title, boolean showLegend) {
		
		JFreeChart chart = ChartFactory.createBarChart(title,
				getXAxis(), "Value", dataset, PlotOrientation.VERTICAL,
			 showLegend, true, false);
		
		
		CategoryPlot xyplot = (CategoryPlot) chart.getPlot();
		CategoryAxis axis = xyplot.getDomainAxis();
		SkippableCategoryAxis newAxis = new SkippableCategoryAxis(axis);
		newAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
		xyplot.setDomainAxis(newAxis);
		
		return chart;
	}

	public void addRowToDataSet(Object[] data, Object[] series) {
		if (this.getXAxisAttribute().getType().intValue() == Attribute.TYPE_DATE) {
			addDateRowToDataSet(data, series);
		} else {
			addSimpleDataRowToDataSet(data, series);
		}

	}

	private void addSimpleDataRowToDataSet(Object[] data, Object[] series) {
		for (int i = 0; i < series.length; i++) {
			for (int j = 0; j < data.length; j++) {
				Object[] row = (Object[]) data[j];
				((DefaultCategoryDataset) dataset).addValue((Number) row[i + 1], series[i].toString(), row[0]
						.toString());
			}
		}
	}

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
