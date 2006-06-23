package edu.emory.library.tas.web.components.tabs.chartGenerators;

import org.jfree.chart.JFreeChart;

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;

public abstract class AbstractChartGenerator {
	protected JFreeChart chart = null;
	private Attribute xAxisAttribute;
	
	
	public abstract JFreeChart getChart();

	public abstract void addRowToDataSet(Object[] data, Object[] series);
	
	public AbstractChartGenerator(Attribute attribute) {
		this.setXAxisAttribute(attribute);
	}

	public String getXAxisSelectOperator(String string) {
		if (this.getXAxisAttribute().getType().intValue() == Attribute.TYPE_DATE) {
			return "date_trunc('year', " + string + ")";
		} else if (this.getXAxisAttribute().getType().intValue() == Attribute.TYPE_DICT) {
			return string + ".id";
		} else {
			return string;
		}
	}

	public void correctAndCompleteData(Object[] objs) {
		if (this.getXAxisAttribute().getType().intValue() == Attribute.TYPE_DICT) {
			for (int i = 0; i < objs.length; i++) {
				Object[] row = (Object[])objs[i];
				Long id = (Long)row[0];
				Conditions conditions = new Conditions();
				conditions.addCondition("id", id, Conditions.OP_EQUALS);
				QueryValue qValue = new QueryValue("Dictionary", conditions);
				Object[] dicts = qValue.executeQuery();
				if (dicts.length > 0) {
					((Object[])objs[i])[0] = ((Dictionary)dicts[0]).getName();
				}
			}
		}
	}

	public String getXAxis() {
		return this.getXAxisAttribute().getName();
	}

	public Attribute getXAxisAttribute() {
		return xAxisAttribute;
	}

	public void setXAxisAttribute(Attribute axisAttribute) {
		xAxisAttribute = axisAttribute;
	}
	
}
