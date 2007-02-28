package edu.emory.library.tast.ui.search.stat.charts;

import org.jfree.chart.JFreeChart;
import org.jfree.ui.RectangleInsets;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

/**
 * Abstract class that is used to generate charts.
 * @author Pawel Jurczyk
 *
 */
public abstract class AbstractChartGenerator {
	
	/**
	 * Generated chart.
	 */
	protected JFreeChart chart = null;
	
	/**
	 * Attribute on x axis.
	 */
	private Attribute xAxisAttribute;
	
	/**
	 * Generates chart.
	 * @param title title of chart
	 * @param showLegend true if legend should be shown
	 * @return JFreeChart object
	 */
	public abstract JFreeChart getChart(String title, boolean showLegend);

	/**
	 * Generates chart without title and with legend.
	 * @return
	 */
	public JFreeChart getChart() {
		return this.getChart(null, true);
	}
	
	/**
	 * Adds rows to chart data.
	 * @param data rows (Expects Object[][])
	 * @param series series on chart (calls toString on each array entry)
	 */
	public abstract void addRowToDataSet(Object[] data, Object[] series);
	
	/**
	 * Constructor.
	 * @param attribute x axis attribute
	 */
	public AbstractChartGenerator(Attribute attribute) {
		this.setXAxisAttribute(attribute);
	}

	/**
	 * Gets expression that should be used for x axis data retrieve
	 * @param string
	 * @return
	 */
	public Attribute getXAxisSelectOperator(Attribute string) {
		if (this.getXAxisAttribute() instanceof DateAttribute) {
			return new FunctionAttribute("date_trunc", new Attribute[] {new DirectValueAttribute("year"), string});
		} else if (this.getXAxisAttribute() instanceof DictionaryAttribute) {
			return new SequenceAttribute(new Attribute[] {string, ((DictionaryAttribute)this.getXAxisAttribute()).getAttribute("name")});
		} else {
			return string;
		}
	}

	/**
	 * Performs any needed correction of response.
	 * Shoulb be overriden in subclasses as needed.
	 * @param objs
	 */
	public void correctAndCompleteData(Object[] objs) {
//		if (this.getXAxisAttribute().getType().intValue() == Attribute.TYPE_DICT) {
//			for (int i = 0; i < objs.length; i++) {
//				Object[] row = (Object[])objs[i];
//				Long id = (Long)row[0];
//				Conditions conditions = new Conditions();
//				conditions.addCondition("id", id, Conditions.OP_EQUALS);
//				QueryValue qValue = new QueryValue(this.getXAxisAttribute().getDictionary(), conditions);
//				Object[] dicts = qValue.executeQuery();
//				if (dicts.length > 0) {
//					((Object[])objs[i])[0] = ((Dictionary)dicts[0]).getName();
//				}
//			}
//		}
	}

	/**
	 * Gets attribute name on x axis.
	 * @return
	 */
	public String getXAxis() {
		return this.getXAxisAttribute().getName();
	}

	/**
	 * Gets Attribute on x axis.
	 */
	public Attribute getXAxisAttribute() {
		return xAxisAttribute;
	}

	/**
	 * Sets x axis attribute.
	 * @param axisAttribute
	 */
	public void setXAxisAttribute(Attribute axisAttribute) {
		xAxisAttribute = axisAttribute;
		
	}
	
	protected final JFreeChart prepareChart(JFreeChart chart) {
		chart.setPadding(new RectangleInsets(2, 2, 2, 24));
		return chart;
	}

}
