package edu.emory.library.tast.database.stat.charts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.DefaultCategoryItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;

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
				getXAxis(), TastResource.getText("components_charts_barvalue"), dataset, PlotOrientation.VERTICAL,
			 showLegend, true, false);
				
		//Update chart
		CategoryPlot xyplot = (CategoryPlot) chart.getPlot();
		CategoryAxis axis = xyplot.getDomainAxis();
		SkippableCategoryAxis newAxis = new SkippableCategoryAxis(axis);
		newAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
		xyplot.setDomainAxis(newAxis);
		chart.setBackgroundPaint(Color.white);
		
		return prepareChart(chart);
	}

	/**
	 * @inheritDoc
	 */
	public void addRowToDataSet(Object[] data, Object[] series) {
		if (this.getXAxisAttribute() instanceof DateAttribute) {
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


}
