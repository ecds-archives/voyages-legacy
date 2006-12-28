package edu.emory.library.tast.estimates.timeline;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
import edu.emory.library.tast.ui.EventLineEvent;
import edu.emory.library.tast.ui.EventLineGraph;
import edu.emory.library.tast.ui.EventLineVerticalLabels;
import edu.emory.library.tast.ui.EventLineZoomLevel;
import edu.emory.library.tast.util.MathUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class EstimatesTimelineBean
{
	
	private EstimatesSelectionBean selectionBean;
	private Conditions conditions;
	private EventLineGraph graphImp;
	private EventLineGraph graphExp;
	private EventLineVerticalLabels verticalLabels;
	private double viewportHeight;
	
	private void regenerateIfNecessary()
	{
		
		// conditions from the left column (i.e. from select bean),
		// and check we we have to regenerate the graphs
		Conditions newConditions = selectionBean.getConditions();
		if (newConditions.equals(conditions)) return;

		// yes, we have to
		conditions = newConditions;
		generateGraphs();
		createVerticalLabels();

	}
	
	private void generateGraphs()
	{
		
		// start query
		QueryValue query = new QueryValue(
				new String[] {"Estimate"},
				new String[] {"estimate"},
				conditions);
		
		// group by years
		query.setGroupBy(new Attribute[] {
				Estimate.getAttribute("year")});
		
		// group by years
		query.setOrder(QueryValue.ORDER_ASC);
		query.setOrderBy(new Attribute[] {
				Estimate.getAttribute("year")});

		// list years
		query.addPopulatedAttribute(
				Estimate.getAttribute("year"));

		// sum the number of expoted slaves
		query.addPopulatedAttribute(
				new FunctionAttribute("sum",
						new Attribute[] {Estimate.getAttribute("slavExported")}));

		// sum the number of imported slaves
		query.addPopulatedAttribute(
				new FunctionAttribute("sum",
						new Attribute[] {Estimate.getAttribute("slavImported")}));

		// finally query the database 
		Object[] result = query.executeQuery();

		// data arrays
		int expYears[] = new int[result.length];
		int impYears[] = new int[result.length];
		double expValues[] = new double[result.length];
		double impValues[] = new double[result.length];
		
		// move db data to it
		for (int i = 0; i < result.length; i++)
		{
			Object[] row = (Object[]) result[i];
			impYears[i] = expYears[i] = ((Integer) row[0]).intValue();
			expValues[i] = Math.round(((Double) row[1]).doubleValue());
			impValues[i] = Math.round(((Double) row[2]).doubleValue());
		}
		
		// graph for exported
		graphExp = new EventLineGraph();
		graphExp.setName("Exported");
		graphExp.setX(expYears);
		graphExp.setY(expValues);
		graphExp.setBaseCssClass("color-timeline-exported");
		graphExp.setEventCssClass("color-timeline-exported-event");

		// graph for imported
		graphImp = new EventLineGraph();
		graphImp.setName("Imported");
		graphImp.setX(impYears);
		graphImp.setY(impValues);
		graphImp.setBaseCssClass("color-timeline-imported");
		graphImp.setEventCssClass("color-timeline-imported-event");
		
	}

	private void createVerticalLabels()
	{

		int maxValue = (int) Math.max(
				graphExp.getMaxValue(),
				graphImp.getMaxValue());
		
		if (maxValue > 0)
		{

			double majorSpacing;
			double minorSpacing;

			double nextPow10 = MathUtils.firstGreaterOrEqualPow10(maxValue);
			if (maxValue / (nextPow10/10) >= 5)
			{
				majorSpacing = nextPow10 / 2;
				minorSpacing = majorSpacing / 5;
			}
			else
			{
				majorSpacing = nextPow10 / 10;
				minorSpacing = majorSpacing / 2;
			}

			viewportHeight = (maxValue / minorSpacing + 1) * minorSpacing;
			verticalLabels = new EventLineVerticalLabels(majorSpacing, minorSpacing);

		}
		else
		{
			viewportHeight = 100;
			verticalLabels = new EventLineVerticalLabels(50, 10);
		}

	}
	
	public EventLineEvent[] getEvents()
	{
		return new EventLineEvent[] {
				new EventLineEvent(1525, "First slave voyage direct from Africa to the Americas"),
				new EventLineEvent(1560, "Continuous slave trade from Brazil begins"),
				new EventLineEvent(1641, "Sugar exports from Eastern Caribbean begin"),
				new EventLineEvent(1655, "English capture Jamaica"),
				new EventLineEvent(1695, "Gold discovered in Minas Gerais (Brazil)"),
				new EventLineEvent(1697, "French obtain St Domingue in Treaty of Rywsick"),
				new EventLineEvent(1756, "Seven years war begins"),
				new EventLineEvent(1776, "American Revolutionary War begins"),
				new EventLineEvent(1789, "Bourbon reforms open Spanish colonial ports to slaves"),
				new EventLineEvent(1791, "St Domingue revolution begins"),
				new EventLineEvent(1808, "Abolition of British and US slave trades takes effect"),
				new EventLineEvent(1830, "Anglo-Brazilian anti-slave trade treaty"),
				new EventLineEvent(1850, "Brazil suppresses slave trade"),
				new EventLineEvent(1867, "Last transatlantic slave voyage sails")};
	}
	
	public EventLineZoomLevel[] getZoomLevels()
	{
		return new EventLineZoomLevel[] {
				new EventLineZoomLevel(2, 50, 400, 100),
				new EventLineZoomLevel(4, 25, 200, 50),
				new EventLineZoomLevel(8, 10, 100, 25),
				new EventLineZoomLevel(16, 5, 50, 10),
				new EventLineZoomLevel(32, 5, 25, 5)};
	}

	public EventLineGraph[] getGraphs()
	{
		regenerateIfNecessary();
		return new EventLineGraph[] {graphExp, graphImp};
	}

	public EventLineVerticalLabels getVerticalLabels()
	{
		regenerateIfNecessary();
		return verticalLabels;
	}

	public double getViewportHeight()
	{
		regenerateIfNecessary();
		return viewportHeight;
	}

	public EstimatesSelectionBean getSelectionBean()
	{
		return selectionBean;
	}

	public void setSelectionBean(EstimatesSelectionBean selectionBean)
	{
		this.selectionBean = selectionBean;
	}

}