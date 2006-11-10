package edu.emory.library.tast.ui.search.stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.jfree.chart.JFreeChart;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.ui.search.query.SearchBean;
import edu.emory.library.tast.ui.search.query.SearchParameters;
import edu.emory.library.tast.ui.search.stat.charts.AbstractChartGenerator;
import edu.emory.library.tast.ui.search.stat.charts.XYChartGenerator;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.DirectValue;
import edu.emory.library.tast.util.query.QueryValue;

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
	private String chosenAttribute = "slaximp";

	/**
	 * Current search bean reference.
	 */
	private SearchBean searchBean;
	
	/**
	 * Conditions used in query last time.
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
	 * Current chart height.
	 */
	private String chartHeight = DEFAULT_CHART_HEIGHT;

	/**
	 * Current chart width.
	 */
	private String chartWidth = DEFAULT_CHART_WIDTH;

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
			if ((attr instanceof NumericAttribute)) {
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
		
		if (!this.searchBean.getSearchParameters().getConditions().equals(this.conditions)) {
			this.conditions = (Conditions)this.searchBean.getSearchParameters().getConditions().clone();
			needQuery = true;
		}
		
		//Check if we can construct chart
		if ((this.needQuery || this.attributesChanged) && this.searchBean.getSearchParameters().getConditions() != null) {

			
			//Prepare query
			Conditions localCondition = (Conditions)this.searchBean.getSearchParameters().getConditions().clone();
			localCondition.addCondition(Voyage.getAttribute("datedep"), null, Conditions.OP_IS_NOT);
			localCondition.addCondition(VoyageIndex.getAttribute("remoteVoyageId"), new DirectValue(Voyage.getAttribute("iid")), Conditions.OP_EQUALS);

			QueryValue qValue = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCondition);
			qValue.setGroupBy(new Attribute[] { new FunctionAttribute("date_trunc", new Attribute[] {new DirectValueAttribute("year"), Voyage.getAttribute("datedep")})});
			qValue.addPopulatedAttribute(new FunctionAttribute("date_trunc", new Attribute[] {new DirectValueAttribute("year"), Voyage.getAttribute("datedep")}));
			qValue.addPopulatedAttribute(new FunctionAttribute(this.chosenAggregate, new Attribute[] {Voyage.getAttribute(this.chosenAttribute)}));
			qValue.setOrderBy(new Attribute[] {new FunctionAttribute("date_trunc", new Attribute[] {new DirectValueAttribute("year"), Voyage.getAttribute("datedep")})});
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
		if (chosenAggregate != null && !chosenAggregate.equals(this.chosenAttribute)) {
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
		if (chosenAttribute != null && !chosenAttribute.equals(this.chosenAttribute)) {
			this.chosenAttribute = chosenAttribute;
			this.attributesChanged = true;
		}

	}

	/**
	 * Gets path to chart image.
	 * @return
	 */
	public String getChartPath() {
		this.showTimeLine();
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
		//return this.chart != null;
		return true;
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
		if (chartHeight == null) return;
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
		if (chartWidth == null) return;
		this.chartWidth = chartWidth;
	}

	public SearchBean getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}
}
