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
import javax.servlet.http.HttpSession;

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

	public static final String IMAGE_FEEDED_SERVLET = "servlet/ImageFeederServlet";
	
	private Conditions conditions = new Conditions();

	private static final String[] aggregates = { "avg", "min", "max", "sum",
			"count" };

	private static final String[] aggregatesUL = { "Avg", "Min", "Max", "Sum",
			"Count" };

	private List voyageAttributes;

	private List aggregateFunctions;

	private String chosenAggregate;

	private String chosenAttribute;

//	private String chartPath;
//
//	private String chartPathLarge;

	private boolean largeViewMode = false;

	private boolean needQuery;

	private boolean attributesChanged = false;
	
	private JFreeChart chart;

	public TimeLineResultTabBean() {
	}

	public List getVoyageNumericAttributes() {
		if (voyageAttributes == null) {
			String[] attributes = Voyage.getAllAttrNames();
			Arrays.sort(attributes);
			this.voyageAttributes = new ArrayList();
			for (int i = 0; i < attributes.length; i++) {
				if (Voyage.getSchemaColumn(attributes[i]).getType() == SchemaColumn.TYPE_FLOAT
						|| Voyage.getSchemaColumn(attributes[i]).getType() == SchemaColumn.TYPE_INTEGER
						|| Voyage.getSchemaColumn(attributes[i]).getType() == SchemaColumn.TYPE_LONG) {
					String outString = null;
					if (Voyage.getSchemaColumn(attributes[i]).getUserLabel() == null
							|| Voyage.getSchemaColumn(attributes[i])
									.getUserLabel().equals("")) {
						outString = attributes[i];
					} else {
						outString = Voyage.getSchemaColumn(attributes[i])
								.getUserLabel();
					}

					voyageAttributes.add(new SelectItem(attributes[i],
							outString));

				}
			}
		}
		return this.voyageAttributes;
	}

	public List getAggregateFunctions() {
		if (this.aggregateFunctions == null) {
			this.aggregateFunctions = new ArrayList();
			for (int i = 0; i < aggregates.length; i++) {
				this.aggregateFunctions.add(new SelectItem(aggregates[i],
						aggregatesUL[i]));
			}
		}
		return this.aggregateFunctions;
	}

	public String showTimeLine() {
		if (this.needQuery || this.attributesChanged) {
			
			Conditions localCondition = this.conditions.addAttributesPrefix("v.");
			localCondition.addCondition("v.datedep", null, Conditions.OP_IS_NOT);
			localCondition.addCondition("vi.remoteVoyageId", new DirectValue(
					"v.id"), Conditions.OP_EQUALS);
			
			QueryValue qValue = new QueryValue("VoyageIndex as vi, Voyage v",
					localCondition);
			qValue.setGroupBy("date_trunc('year', v.datedep)");
			qValue
					.addPopulatedAttribute("date_trunc('year', v.datedep)",
							false);
			qValue.addPopulatedAttribute(this.chosenAggregate + "(v."
					+ this.chosenAttribute + ")", false);
			Object[] ret = qValue.executeQuery();

			DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();

			for (int i = 0; i < ret.length; i++) {
				Object[] row = (Object[]) ret[i];
				String label = row[0] != null ? (row[0].toString()) : "null";
				categoryDataset.addValue((Number) row[1], label, "");

			}

			chart = ChartFactory.createBarChart(
					"Sample Category Chart", // Title
					"Voyages", // X-Axis label
					"# of slaves", // Y-Axis label
					categoryDataset, // Dataset
					PlotOrientation.VERTICAL, false, true, true);

				ExternalContext servletContext = FacesContext
						.getCurrentInstance().getExternalContext();
				((HttpSession)servletContext.getSession(true)).setAttribute("__chart__object", chart);
			
			this.needQuery = false;
			this.attributesChanged = false;
		}

		return null;
	}

	public String getChosenAggregate() {
		return chosenAggregate;
	}

	public void setChosenAggregate(String chosenAggregate) {
		if (!chosenAggregate.equals(this.chosenAttribute)) {
			this.chosenAggregate = chosenAggregate;
			this.attributesChanged = true;
		}
	}

	public String getChosenAttribute() {
		return chosenAttribute;
	}

	public void setChosenAttribute(String chosenAttribute) {
		if (!chosenAttribute.equals(this.chosenAttribute)) {
			this.chosenAttribute = chosenAttribute;
			this.attributesChanged = true;
		}

	}

	public String getChartPath() {
		return IMAGE_FEEDED_SERVLET + "?path=__chart__object";
	}
	
	public void setChartPath(String path) {		
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
		return this.chart != null;
	}

	public void setConditions(Conditions c) {
		if (c == null) {
			needQuery = false;
		} else if (c.equals(conditions)) {
			needQuery = false;
		} else {
			conditions = c;
			needQuery = true;
		}
	}
}
