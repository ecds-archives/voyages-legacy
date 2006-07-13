package edu.emory.library.tas.web.components.tabs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.jfree.chart.JFreeChart;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.DirectValue;
import edu.emory.library.tas.util.query.QueryValue;
import edu.emory.library.tas.web.SearchParameters;
import edu.emory.library.tas.web.components.tabs.chartGenerators.AbstractChartGenerator;
import edu.emory.library.tas.web.components.tabs.chartGenerators.XYChartGenerator;

/**
 * Bean for time line statistics.
 * @author Pawel Jurczyk
 *
 */
public class TimeLineResultTabBean {

	public static final String IMAGE_FEEDED_SERVLET = "servlet/ImageFeederServlet";

	private static final String[] aggregates = { "avg", "min", "max", "sum", "count" };

	private static final String[] aggregatesUL = { "Avg", "Min", "Max", "Sum", "Count" };

	private static final String DEFAULT_CHART_HEIGHT = "480";

	private static final String DEFAULT_CHART_WIDTH = "640";

	/**
	 * List of voyage attributes.
	 */
	private List voyageAttributes;

	/**
	 * List of available aggregates.
	 */
	private List aggregateFunctions;

	/**
	 * Chosen aggregate.
	 */
	private String chosenAggregate = "sum";

	/**
	 * Chosen attribute name.
	 */
	private String chosenAttribute = "sla32imp";

	/**
	 * Current set of attributes.
	 */
	private Conditions conditions = null;

	/**
	 * Need of query indication.
	 */
	private boolean needQuery = false;

	/**
	 * Attributes changed indication.
	 */
	private boolean attributesChanged = false;

	/**
	 * Chart presented to user.
	 */
	private JFreeChart chart;

	/**
	 * Visibility of component.
	 */
	private Boolean componentVisible = new Boolean(false);

	/**
	 * Current chart height.
	 */
	private String chartHeight = DEFAULT_CHART_HEIGHT;

	/**
	 * Current chart width.
	 */
	private String chartWidth = DEFAULT_CHART_WIDTH;

	/**
	 * Current category of attributes (basic or general).
	 */
	private int category;

	/**
	 * Avaialable voyage attributes.
	 */
	private Attribute[] attributes = Voyage.getAttributes();

	/**
	 * Default constructor.
	 *
	 */
	public TimeLineResultTabBean() {
	}

	/**
	 * Gets numeric attributes of voyage.
	 * @return
	 */
	public List getVoyageNumericAttributes() {

		//Build list of numeric attributes.
		this.voyageAttributes = new ArrayList();
		for (int i = 0; i < attributes.length; i++) {
			Attribute attr = attributes[i];
			if (attr.isVisibleByCategory(this.category)
					&& (attr.getType().intValue() == Attribute.TYPE_FLOAT
							|| attr.getType().intValue() == Attribute.TYPE_INTEGER || attr.getType().intValue() == Attribute.TYPE_LONG)) {
				String outString = attr.toString();
				voyageAttributes.add(new ComparableSelectItem(attr.getName(), outString));

			}
		}
		Collections.sort(voyageAttributes);

		return this.voyageAttributes;
	}

	/**
	 * Gets avaialable aggregates.
	 * @return
	 */
	public List getAggregateFunctions() {
		if (this.aggregateFunctions == null) {
			this.aggregateFunctions = new ArrayList();
			for (int i = 0; i < aggregates.length; i++) {
				this.aggregateFunctions.add(new SelectItem(aggregates[i], aggregatesUL[i]));
			}
		}
		return this.aggregateFunctions;
	}

	/**
	 * Shows time line chart.
	 * @return
	 */
	public String showTimeLine() {
		
		//Check if we can construct chart
		if (this.componentVisible.booleanValue() && (this.needQuery || this.attributesChanged)
				&& this.conditions != null) {
			//Prepare query
			Conditions localCondition = this.conditions.addAttributesPrefix("v.");
			localCondition.addCondition("v.datedep", null, Conditions.OP_IS_NOT);
			localCondition.addCondition("vi.remoteVoyageId", new DirectValue("v.id"), Conditions.OP_EQUALS);

			QueryValue qValue = new QueryValue("VoyageIndex as vi, Voyage v", localCondition);
			qValue.setGroupBy(new String[] { "date_trunc('year', v.datedep)" });
			qValue.addPopulatedAttribute("date_trunc('year', v.datedep)", false);
			qValue.addPopulatedAttribute(this.chosenAggregate + "(v." + this.chosenAttribute + ")", false);
			qValue.setOrderBy(new String[] { "date_trunc('year', v.datedep)" });
			qValue.setOrder(QueryValue.ORDER_ASC);
			Object[] ret = qValue.executeQuery();

			//Prepare chart generator.
			AbstractChartGenerator generator = new XYChartGenerator(Voyage.getAttribute("datedep"));
			generator.correctAndCompleteData(ret);
			generator.addRowToDataSet(ret, new String[] { this.chosenAggregate + "("
					+ Voyage.getAttribute(this.chosenAttribute) + ")" });
			chart = generator.getChart("Time line graph", false);

			//Put chart into session.
			ExternalContext servletContext = FacesContext.getCurrentInstance().getExternalContext();
			((HttpSession) servletContext.getSession(true)).setAttribute("__chart__object", chart);

			this.needQuery = false;
			this.attributesChanged = false;
		}

		return null;
	}

	/**
	 * Gets currently chosen aggregate.
	 * @return
	 */
	public String getChosenAggregate() {
		return chosenAggregate;
	}

	/**
	 * Sets currently chosen aggregate.
	 * @param chosenAggregate
	 */
	public void setChosenAggregate(String chosenAggregate) {
		if (!chosenAggregate.equals(this.chosenAttribute)) {
			this.chosenAggregate = chosenAggregate;
			this.attributesChanged = true;
		}
	}

	/**
	 * Gets currently chosen attribute.
	 * @return
	 */
	public String getChosenAttribute() {
		return chosenAttribute;
	}

	/**
	 * Sets currently chosen attribute.
	 * @param chosenAttribute
	 */
	public void setChosenAttribute(String chosenAttribute) {
		if (!chosenAttribute.equals(this.chosenAttribute)) {
			this.chosenAttribute = chosenAttribute;
			this.attributesChanged = true;
		}

	}

	/**
	 * Gets path to chart image.
	 * @return
	 */
	public String getChartPath() {
		return IMAGE_FEEDED_SERVLET + "?path=__chart__object&&height=" + this.chartHeight + "&width=" + this.chartWidth;
	}
	public void setChartPath(String path) {
	}
	public String setNewView() {
		return null;
	}

	/**
	 * Checks if any chart is ready to show.
	 */
	public boolean getChartReady() {
		return this.chart != null;
	}

	/**
	 * Sets current search parameters.
	 * @param params
	 */
	public void setConditions(SearchParameters params) {
		if (params == null) {
			return;
		}
		this.category = params.getCategory();
		Conditions c = params.getConditions();
		if (c == null || c.equals(conditions)) {
			return;
		} else {
			conditions = c;
			needQuery = true;
		}
		showTimeLine();
	}

	/**
	 * Checks if component is visible.
	 * @return
	 */
	public Boolean getComponentVisible() {
		return componentVisible;
	}

	/**
	 * Sets visibility of component.
	 * @param componentVisible
	 */
	public void setComponentVisible(Boolean componentVisible) {
		boolean shouldQuery = false;
		if (this.componentVisible.booleanValue() == false && componentVisible.booleanValue() == true) {
			shouldQuery = true;
		}
		this.componentVisible = componentVisible;
		if (shouldQuery) {
			this.showTimeLine();
		}
	}

	/**
	 * Gets chart height.
	 * @return
	 */
	public String getChartHeight() {
		return chartHeight;
	}

	/**
	 * Gets chart height.
	 * @param chartHeight
	 */
	public void setChartHeight(String chartHeight) {
		this.chartHeight = chartHeight;
	}

	/**
	 * Gets chart width.
	 * @param chartHeight
	 */
	public String getChartWidth() {
		return chartWidth;
	}

	/**
	 * Sets chart width.
	 * @param chartWidth
	 */
	public void setChartWidth(String chartWidth) {
		this.chartWidth = chartWidth;
	}
}
