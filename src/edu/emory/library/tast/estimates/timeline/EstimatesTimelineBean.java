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
	private int viewportHeight;
	
	private void generateGraphsIfNecessary()
	{
		
		// conditions from the left column (i.e. from select bean)
		Conditions newConditions = selectionBean.getConditions();

		// check if we have to
		if (newConditions.equals(conditions)) return;
		conditions = newConditions;
		
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
		graphExp.setColor("#EEEEEE");

		// graph for imported
		graphImp = new EventLineGraph();
		graphImp.setName("Imported");
		graphImp.setX(impYears);
		graphImp.setY(impValues);
		graphImp.setColor("#CCCCCC");

		// vertical labels
		createVerticalLabels();
		
	}

	private void createVerticalLabels()
	{

		int maxValue = (int) Math.max(
				graphExp.getMaxValue(),
				graphImp.getMaxValue());
		
		if (maxValue > 0)
		{

			int majorSpacing;
			int minorSpacing;

			int nextPow10 = MathUtils.firstGreaterOrEqualPow10(maxValue);
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
		generateGraphsIfNecessary();
		return new EventLineEvent[] {
				new EventLineEvent(1530, "Event A"),
				new EventLineEvent(1606, "Event B"),
				new EventLineEvent(1723, "Event C"),
				new EventLineEvent(1786, "Event D"),
				new EventLineEvent(1807, "Event E"),
		};
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
		generateGraphsIfNecessary();
		return new EventLineGraph[] {graphExp, graphImp};
	}

	public EventLineVerticalLabels getVerticalLabels()
	{
		generateGraphsIfNecessary();
		return verticalLabels;
	}

	public EstimatesSelectionBean getSelectionBean()
	{
		return selectionBean;
	}

	public void setSelectionBean(EstimatesSelectionBean selectionBean)
	{
		this.selectionBean = selectionBean;
	}

	public int getViewportHeight()
	{
		generateGraphsIfNecessary();
		return viewportHeight;
	}

}
