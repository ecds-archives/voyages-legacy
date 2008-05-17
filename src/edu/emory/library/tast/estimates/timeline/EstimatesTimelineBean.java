package edu.emory.library.tast.estimates.timeline;

import java.text.MessageFormat;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.EventLineEvent;
import edu.emory.library.tast.common.EventLineGraph;
import edu.emory.library.tast.common.EventLineLabel;
import edu.emory.library.tast.common.EventLineZoomLevel;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
import edu.emory.library.tast.util.CSVUtils;
import edu.emory.library.tast.util.HibernateUtil;

public class EstimatesTimelineBean
{
	
	private EstimatesSelectionBean selectionBean;
	private TastDbConditions conditions;
	private EventLineGraph graphImp;
	private EventLineGraph graphExp;
	private EventLineLabel[] verticalLabels;
	private double viewportHeight;
	private EventLineEvent[] events;
	
	private void regenerateIfNecessary()
	{
		
		// conditions from the left column (i.e. from select bean),
		// and check we we have to regenerate the graphs
		TastDbConditions newConditions = selectionBean.getConditions();
		if (newConditions.equals(conditions)) return;

		// yes, we have to
		conditions = newConditions;
		generateGraphs();
		presetEvents();

	}
	
	private void generateGraphs()
	{
		
		// start query
		TastDbQuery query = new TastDbQuery(
				new String[] {"Estimate"},
				new String[] {"estimate"},
				conditions);
		
		// group by years
		query.setGroupBy(new Attribute[] {
				Estimate.getAttribute("year")});
		
		// group by years
		query.setOrder(TastDbQuery.ORDER_ASC);
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
		String expStrings[] = new String[result.length];
		String impStrings[] = new String[result.length];
		MessageFormat fmt = new MessageFormat("{0,number,#,###,###}");
		
		// move db data to it
		for (int i = 0; i < result.length; i++)
		{
			Object[] row = (Object[]) result[i];
			impYears[i] = expYears[i] = ((Integer) row[0]).intValue();
			expValues[i] = Math.round(((Double) row[1]).doubleValue());
			impValues[i] = Math.round(((Double) row[2]).doubleValue());
			expStrings[i] = fmt.format(new Object[] {row[1]}); 
			impStrings[i] = fmt.format(new Object[] {row[2]}); 
		}
		
		// graph for exported
		graphExp = new EventLineGraph();
		graphExp.setName(TastResource.getText("estimates_timeline_exported"));
		graphExp.setX(expYears);
		graphExp.setY(expValues);
		graphExp.setLabels(expStrings);
		graphExp.setBaseCssClass("color-timeline-exported");
		graphExp.setEventCssClass("color-timeline-exported-event");

		// graph for imported
		graphImp = new EventLineGraph();
		graphImp.setName(TastResource.getText("estimates_timeline_imported"));
		graphImp.setX(impYears);
		graphImp.setY(impValues);
		graphImp.setLabels(impStrings);
		graphImp.setBaseCssClass("color-timeline-imported");
		graphImp.setEventCssClass("color-timeline-imported-event");
		
		// vertical labels and viewport height
		int maxValue = (int) Math.max(graphExp.getMaxValue(), graphImp.getMaxValue());
		if (maxValue > 0)
		{
			verticalLabels = EventLineLabel.createStandardLabels(maxValue, fmt);
			viewportHeight = verticalLabels[verticalLabels.length - 1].getValue();
		}
		else
		{
			verticalLabels = new EventLineLabel[] {new EventLineLabel(0.0, "0", true)};
			viewportHeight = 100;
		}

	}
	
	private void presetEvents()
	{
		
		Set impRegions = selectionBean.getSelectedImpRegionIds();
		Set expRegions = selectionBean.getSelectedExpRegionIds();
		
		if (selectionBean.isAllExpRegionsSelected() && selectionBean.isAllImpRegionsSelected() && selectionBean.isAllNationsSelected())
		{
		
			events = new EventLineEvent[] {
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
				new EventLineEvent(1867, "Last transatlantic slave voyage arrives in Americas")};
			
		}
		else if (expRegions.size() == 1 && expRegions.contains(new Long(5)))
		{

			events = new EventLineEvent[] {
				new EventLineEvent(1663, "English create trading post at Allada (Ardrah)"),
				new EventLineEvent(1671, "French trading post established at Whydah"),
				new EventLineEvent(1678, "Tobacco Roll imports from Bahia begin"),
				new EventLineEvent(1692, "Offra/Jaquin destroyed King of Little Popo"),
				new EventLineEvent(1727, "Dahomey conquest of Whydah"),
				new EventLineEvent(1785, "Oyo Empire enters long decline"),
				new EventLineEvent(1851, "Brazilian slave trade ends/British conquest of Whydah")};
		
		}
		else if (impRegions.size() == 1 && impRegions.contains(new Long(203)))
		{

			events = new EventLineEvent[] {
				new EventLineEvent(1699, "Rice exports begin"),
				new EventLineEvent(1739, "Stono Rebellion"),
				new EventLineEvent(1774, "Non-importation agreement"),
				new EventLineEvent(1803, "South Carolina re-opens ports to slave trade from Africa"),
				new EventLineEvent(1808, "US abolishes African slave trade")};
			
		}
		else
		{
			
			events = null;
	
		}

	}

	public EventLineEvent[] getEvents()
	{
		return events;
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

	public EventLineLabel[] getVerticalLabels()
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
	
	public String getFileAllData() {	
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		String[][] data = new String[this.graphImp.getX().length + 1][3];
		data[0][0] = "year";
		data[0][1] = "disembarked";
		data[0][2] = "embarked";
		for (int i = 0; i < data.length - 1; i++) {
			data[i+1][0] = String.valueOf(this.graphImp.getX()[i]);
			data[i+1][1] = String.valueOf(this.graphImp.getY()[i]);
			data[i+1][2] = String.valueOf(this.graphExp.getY()[i]);
		}
		CSVUtils.writeResponse(session, data);
		
		t.commit();
		session.close();
		return null;
	}

}