package edu.emory.library.tas.web.components.tabs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

import org.apache.myfaces.context.servlet.ServletExternalContextImpl;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.VoyageIndex;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.DirectValue;
import edu.emory.library.tas.util.query.QueryValue;

public class TimeLineResultTabBean {
	
	private Conditions conditions = new Conditions();
	
	private static final String[] aggregates = {"avg", "min", "max", "sum", "count"};
	private static final String[] aggregatesUL = {"Avg", "Min", "Max", "Sum", "Count"};
	
	private List voyageAttributes;
	private List aggregateFunctions;
	
	private String chosenAggregate;
	private String chosenAttribute;
	private String chartPath;
	private String chartPathLarge;
	private boolean largeViewMode = false;
	
	public TimeLineResultTabBean() {
		this.conditions.addCondition(VoyageIndex.getRecent());		
		this.conditions.addCondition("v.datedep", null, Conditions.OP_IS_NOT);
		this.conditions.addCondition("vi.remoteVoyageId", new DirectValue("v.id"), Conditions.OP_EQUALS);
	}
	
	public List getVoyageNumericAttributes() {
		if (voyageAttributes == null) {
			String [] attributes = Voyage.getAllAttrNames();
			Arrays.sort(attributes);
			this.voyageAttributes = new ArrayList();
			for (int i = 0; i < attributes.length; i++) {
				if (Voyage.getSchemaColumn(attributes[i]).getType() == SchemaColumn.TYPE_FLOAT || 
						Voyage.getSchemaColumn(attributes[i]).getType() == SchemaColumn.TYPE_INTEGER ||
						Voyage.getSchemaColumn(attributes[i]).getType() == SchemaColumn.TYPE_LONG) {
					String outString = null;
					if (Voyage.getSchemaColumn(attributes[i]).getUserLabel() == null
							|| Voyage.getSchemaColumn(attributes[i]).getUserLabel().equals("")) {
						outString = attributes[i];
					} else {
						outString = Voyage.getSchemaColumn(attributes[i]).getUserLabel();
					}
					
					voyageAttributes.add(new SelectItem(attributes[i], outString));
					
				}
			}
		}
		return this.voyageAttributes;
	}
	
	public List getAggregateFunctions() {
		if (this.aggregateFunctions == null) {
			this.aggregateFunctions = new ArrayList();
			for (int i = 0; i < aggregates.length; i++) {
				this.aggregateFunctions.add(new SelectItem(aggregates[i], aggregatesUL[i]));
			}
		}
		return this.aggregateFunctions;
	}
	
	public String showTimeLine() {
		QueryValue qValue = new QueryValue("VoyageIndex as vi, Voyage v", this.conditions);
		qValue.setGroupBy("date_trunc('year', v.datedep)");
		qValue.addPopulatedAttribute("date_trunc('year', v.datedep)", false);
		qValue.addPopulatedAttribute(this.chosenAggregate + "(v." + this.chosenAttribute + ")", false);
		Object [] ret = qValue.executeQuery();
		
		DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
		
		for (int i = 0; i < ret.length; i++) {
			Object [] row = (Object[])ret[i];
			String label = row[0] != null ? (row[0].toString()) : "null";
			categoryDataset.addValue((Number)row[1], label, "");
		
		}
		
		JFreeChart chart = ChartFactory.createBarChart("Sample Category Chart", // Title
		                      "Voyages",              // X-Axis label
		                      "# of slaves",                 // Y-Axis label
		                      categoryDataset,         // Dataset
		                      PlotOrientation.VERTICAL,
		                      false, true, true);

		try {
			ExternalContext servletContext = FacesContext.getCurrentInstance().getExternalContext();
			String path = ((ServletContext)servletContext.getContext()).getRealPath("/");
			ChartUtilities.saveChartAsPNG(new File(path + "chart.png"), chart, 640, 480);
			this.chartPath = "chart.png";
			ChartUtilities.saveChartAsPNG(new File(path + "chartBig.png"), chart, 1280, 1024);
			this.chartPathLarge = "chartBig.png";
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public String getChosenAggregate() {
		return chosenAggregate;
	}

	public void setChosenAggregate(String chosenAggregate) {
		this.chosenAggregate = chosenAggregate;
	}

	public String getChosenAttribute() {
		return chosenAttribute;
	}

	public void setChosenAttribute(String chosenAttribute) {
		this.chosenAttribute = chosenAttribute;
	}

	public String getChartPath() {
		return largeViewMode ? chartPathLarge : chartPath;
	}

	public void setChartPath(String chartPath) {
		this.chartPath = chartPath;
	}
	
	public boolean getNormalView() {
		return largeViewMode == false;
	}
	
	public boolean getLargeView() {
		return largeViewMode;
	}
	
	public void setLargeView() {
		this.largeViewMode = true;
	}
	
	public void setNormalView() {
		this.largeViewMode = false;
	}
	
	public boolean getChartReady() {
		return this.chartPath != null;
	}
	
	public void setConditionsOut(Conditions c) {
		System.out.println("set conditions out!");
	}
}
