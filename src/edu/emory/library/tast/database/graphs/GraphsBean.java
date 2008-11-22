package edu.emory.library.tast.database.graphs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.jfree.chart.JFreeChart;

import edu.emory.library.tast.database.query.SearchBean;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;

public class GraphsBean
{

	private static final int XY_GRAPH_TYPE_INDEX = 0;
	private static final int BAR_GRAPH_TYPE_INDEX = 1;
	private static final int PIE_GRAPH_TYPE_INDEX = 2;
	
	public static final String GRAPHS_SERVLET = "../servlet/graph";
	public static final String SESSION_KEY_GRAPH = "graph";
	private static final int DEFAULT_CHART_HEIGHT = 480;
	private static final int DEFAULT_CHART_WIDTH = 640;
	
	private SearchBean searchBean = null;
	private TastDbConditions conditions = null;
	
	private boolean needRefresh = false;
	
	private int nextUrlSeq = 0;

	private static final IndependentVariable[] independentVariablesXY = new IndependentVariable[] {
		IndependentVariable.createForYear("yearam", "Year arrived with slaves*", "yearam"),
		IndependentVariable.createForInteger("voy1imp", "Voyage length, home port to slaves landing (days)*", "voy1imp"),
		IndependentVariable.createForInteger("voy2imp", "Middle passage (days)*", "voy2imp"),
		IndependentVariable.createForInteger("crew1", "Crew at voyage outset", "crew1"),
		IndependentVariable.createForInteger("crew3", "Crew at first landing of slaves", "crew3"),
		IndependentVariable.createForInteger("slaximp", "Slaves embarked", "slaximp"),
		IndependentVariable.createForInteger("slamimp", "Slaves disembarked", "slamimp"),
	};

	private static final DependentVariable[] dependentVariablesXY = new DependentVariable[] {
		DependentVariable.createCount("iid", "Number of voyages", "iid"),
		DependentVariable.createAvg("voy1imp", "Average voyage length, home port to slaves landing (days)*", "voy1imp"),
		DependentVariable.createAvg("voy2imp", "Average middle passage (days)*", "voy2imp"),
		DependentVariable.createAvg("tonmod", "Standardized tonnage*", "tonmod"),
		DependentVariable.createAvg("crew1-avg", "Average crew at voyage outset", "crew1"),
		DependentVariable.createAvg("crew3-avg", "Average crew at first landing of slaves", "crew3"),
		DependentVariable.createSum("crew1-sum", "Total crew at voyage outset", "crew1"),
		DependentVariable.createSum("crew3-sum", "Total crew at first landing of slaves", "crew3"),
		DependentVariable.createAvg("slaximpAvg", "Average number of slaves embarked", "slaximp"),
		DependentVariable.createAvg("slamimpAvg", "Average number of slaves disembarked", "slamimp"),
		DependentVariable.createSum("slaximpSum", "Total number of slaves embarked", "slaximp"),
		DependentVariable.createSum("slamimpSum", "Total number of slaves disembarked", "slamimp"),
		DependentVariable.createPercentage("menrat7", "Percentage men*", "menrat7"),
		DependentVariable.createPercentage("womrat7", "Percentage women*", "womrat7"),
		DependentVariable.createPercentage("boyrat7", "Percentage boys*", "boyrat7"),
		DependentVariable.createPercentage("girlrat7", "Percentage girls*", "girlrat7"),
		DependentVariable.createPercentage("chilrat7", "Percentage children*", "chilrat7"),
		DependentVariable.createPercentage("malrat7", "Percentage male*", "malrat7"),
		DependentVariable.createAvg("jamcaspr", "Sterling cash price in Jamaica*", "jamcaspr"),
		DependentVariable.createCustomAvg("resistance-rate", "Rate of resistance", new FunctionAttribute("coalesce_to_0_100", new Attribute[] {Voyage.getAttribute("resistance")})),
		DependentVariable.createPercentage("vymrtrat", "Percentage of slaves embarked who died during voyage*", "vymrtrat"),
	};

	private static final IndependentVariable[] independentVariablesBar = new IndependentVariable[] {
		
		IndependentVariable.createForPort("natinimp", "Flag*", "natinimp"),
		IndependentVariable.createForVesselRig("rig", "Rig", "rig"),

		IndependentVariable.createForFate("fate", "Particular outcome of the voyage", "fate"),
		IndependentVariable.createForFateSlaves("fate2", "Outcome for slaves*", "fate2"),
		IndependentVariable.createForFateOwner("fate4", "Outcome for owner*", "fate4"),
		IndependentVariable.createForFateVessel("fate3", "Outcome if ship captured*", "fate3"),
		
		IndependentVariable.createForResistance("resistance", "African resistance", "resistance"),
		
		IndependentVariable.createForPort("ptdepimp", "Place where voyage began*", "ptdepimp"),
		IndependentVariable.createForRegion("ptdepimp-region", "Region where voyage began*", "ptdepimp"),

		IndependentVariable.createForPort("mjbyptimp", "Principal place of slave purchase*", "mjbyptimp"),
		IndependentVariable.createForRegion("mjbyptimp-region", "Principal region of slave purchase*", "mjbyptimp"),
		
		IndependentVariable.createForPort("mjslptimp", "Principal place of slave landing*", "mjslptimp"),
		IndependentVariable.createForRegion("mjslptimp-region", "Principal region of slave landing*", "mjslptimp"),
		IndependentVariable.createForArea("mjslptimp-area", "Broad region of slave landing*", "mjslptimp"),

		IndependentVariable.createForPort("portret", "Place where voyage ended", "portret"),
		IndependentVariable.createForRegion("portret-region", "Region where voyage ended", "portret"),
		
		IndependentVariable.createForMonth("datedep-month", "Month voyage began in Africa", "datedep"),
		IndependentVariable.createForMonth("datebuy-month", "Month trade began in Africa", "datebuy"),
		IndependentVariable.createForMonth("dateleftafr-month", "Month vessel departed Africa", "dateleftafr"),
		IndependentVariable.createForMonth("dateland1-month", "Month vessel arrived with slaves", "dateland1"),
		IndependentVariable.createForMonth("datedepam-month", "Month vessel departed for home port", "datedepam"),
		IndependentVariable.createForMonth("dateend-month", "Month voyage completed", "dateend"),

		IndependentVariable.createForYearPeriod("yearam-5", "Year arrived with slaves (5 year periods)", "yearam", 5),
		IndependentVariable.createForYearPeriod("yearam-10", "Year arrived with slaves (10 year periods)", "yearam", 10),
		IndependentVariable.createForYearPeriod("yearam-25", "Year arrived with slaves (25 year periods)", "yearam", 25),
	};

	private static final DependentVariable[] dependentVariablesBar = new DependentVariable[] {
		DependentVariable.createCount("iid", "Number of voyages", "iid"),
		DependentVariable.createAvg("slaximpAvg", "Average number of slaves embarked", "slaximp"),
		DependentVariable.createAvg("slamimpAvg", "Average number of slaves disembarked", "slamimp"),
		DependentVariable.createSum("slaximpSum", "Total slaves embarked", "slaximp"),
		DependentVariable.createSum("slamimpSum", "Total slaves disembarked", "slamimp"),
		DependentVariable.createPercentage("menrat7", "Percentage men*", "menrat7"),
		DependentVariable.createPercentage("womrat7", "Percentage women*", "womrat7"),
		DependentVariable.createPercentage("boyrat7", "Percentage boys*", "boyrat7"),
		DependentVariable.createPercentage("girlrat7", "Percentage girls*", "girlrat7"),
		DependentVariable.createPercentage("chilrat7", "Percentage children*", "chilrat7"),
		DependentVariable.createPercentage("malrat7", "Percentage male*", "chilrat7"),
		DependentVariable.createAvg("jamcaspr", "Sterling cash price in Jamaica*", "jamcaspr"),
		//DependentVariable.createCustomAvg("resistance-rate", "Rate of resistance", new FunctionAttribute("coalesce_to_0_100", Voyage.getAttribute("resistance"))),
		DependentVariable.createPercentage("vymrtrat", "Percentage of slaves embarked who died during voyage*", "vymrtrat")
	};
	
	private static final IndependentVariable[] independentVariablesPie = new IndependentVariable[] {
		
		IndependentVariable.createForPort("natinimp", "Flag*", "natinimp"),
		IndependentVariable.createForVesselRig("rig", "Rig", "rig"),
		
		IndependentVariable.createForFate("fate", "Particular outcome of the voyage", "fate"),
		IndependentVariable.createForFateSlaves("fate2", "Outcome for slaves*", "fate2"),
		IndependentVariable.createForFateOwner("fate4", "Outcome for owner*", "fate4"),
		IndependentVariable.createForFateVessel("fate3", "Outcome if ship captured*", "fate3"),
		
		IndependentVariable.createForResistance("resistance", "African resistance", "resistance"),
		
		IndependentVariable.createForPort("ptdepimp", "Place where voyage began*", "ptdepimp"),
		IndependentVariable.createForRegion("ptdepimp-region", "Region where voyage began*", "ptdepimp"),

		IndependentVariable.createForPort("mjbyptimp", "Principal place of slave purchase*", "mjbyptimp"),
		IndependentVariable.createForRegion("mjbyptimp-region", "Principal region of slave purchase*", "mjbyptimp"),
		
		IndependentVariable.createForPort("mjslptimp", "Principal place of slave landing*", "mjslptimp"),
		IndependentVariable.createForRegion("mjslptimp-region", "Principal region of slave landing*", "mjslptimp"),
		IndependentVariable.createForArea("mjslptimp-area", "Broad region of slave landing*", "mjslptimp"),
		
		IndependentVariable.createForPort("portret", "Place where voyage ended", "portret"),
		IndependentVariable.createForRegion("portret-region", "Region where voyage ended", "portret"),

	};
	
	private static final DependentVariable[] dependentVariablesPie = new DependentVariable[] {
		DependentVariable.createCount("iid", "Number of voyages", "iid"),
		DependentVariable.createSum("slaximpSum", "Total slaves embarked*", "slaximp"),
		DependentVariable.createSum("slamimpSum", "Total slaves disembarked*", "slamimp"),
	};
	
	private GraphType[] graphs = new GraphType[] {
		new GraphTypeXY("xy", independentVariablesXY, dependentVariablesXY),
		new GraphTypeBar("bar", independentVariablesBar, dependentVariablesBar),
		new GraphPie("pie", independentVariablesPie, dependentVariablesPie),
	};
	
	private GraphType selectedGraph;

	public GraphsBean()
	{
		resetToDefault();
	}
	
	public void resetToDefault()
	{
		
		selectedGraph = graphs[XY_GRAPH_TYPE_INDEX];
		selectedGraph.removeAllSeries();
		selectedGraph.setSelectedIndependentVariableId("voy2imp");
		selectedGraph.setSelectedDependentVariableId("vymrtrat");
		addSeries();
		
		selectedGraph = graphs[BAR_GRAPH_TYPE_INDEX];
		selectedGraph.removeAllSeries();
		selectedGraph.setSelectedIndependentVariableId("mjbyptimp-region");
		selectedGraph.setSelectedDependentVariableId("chilrat7");
		addSeries();
		
		selectedGraph = graphs[PIE_GRAPH_TYPE_INDEX];
		selectedGraph.removeAllSeries();
		selectedGraph.setSelectedIndependentVariableId("natinimp");
		selectedGraph.setSelectedDependentVariableId("iid");
		addSeries();
		
		selectedGraph = graphs[XY_GRAPH_TYPE_INDEX];

	}

	public String addSeries()
	{

		DependentVariable depVar = selectedGraph.getSelectedDependentVariable();
		
		if (!selectedGraph.canHaveMoreSeries())
			selectedGraph.removeAllSeries();
		
		DataSeries newSeries = new DataSeries(depVar);
		if (selectedGraph.getDataSeries().contains(newSeries))
			return null;
		
		selectedGraph.getDataSeries().add(newSeries);
		
		needRefresh = true;
		return null;

	}
	
	public String removeSelectedSeries()
	{
		List allDataSeries = selectedGraph.getDataSeries();
		for (int i = 0; i < allDataSeries.size(); i++)
		{
			DataSeries dataSeries = ((DataSeries)allDataSeries.get(i));
			if (dataSeries.isSelected())
			{
				allDataSeries.remove(i);
				i--;
				needRefresh = true;
			}
		}
		return null;
	}
	
	public void refreshGraphIfNeeded()
	{
		
		TastDbConditions searchBeanConditions = this.searchBean.getSearchParameters().getConditions();
		if (searchBeanConditions.equals(this.conditions) && !this.needRefresh)
			return;
		
		IndependentVariable indepVar = selectedGraph.getSelectedIndependentVariable();
		if (indepVar == null) return;

		this.conditions = (TastDbConditions)searchBeanConditions.clone();
		this.needRefresh = false;

		TastDbConditions localConditions = (TastDbConditions)this.conditions.clone();
		
		localConditions.addCondition(indepVar.getSelectAttribute(), null, TastDbConditions.OP_IS_NOT);
		
		TastDbQuery qValue = new TastDbQuery(new String[] {"Voyage"}, new String[] {"v"}, localConditions);
		
		qValue.addPopulatedAttribute(indepVar.getSelectAttribute());
		
		for (Iterator iter = selectedGraph.getDataSeries().iterator(); iter.hasNext();)
		{
			DataSeries ser = (DataSeries) iter.next();
			qValue.addPopulatedAttribute(ser.getVariable().getSelectAttribute());
		}
		
		qValue.setGroupBy(indepVar.getGroupByAttributes());
		
		qValue.setOrderBy(new Attribute[] {indepVar.getOrderAttribute()});
		qValue.setOrder(TastDbQuery.ORDER_ASC);
		
		// query db
		Object[] data = qValue.executeQuery();

		// create graph
		JFreeChart chart = selectedGraph.createChart(data);

		// put the graph into the session
		ExternalContext servletContext = FacesContext.getCurrentInstance().getExternalContext();
		((HttpSession) servletContext.getSession(true)).setAttribute(SESSION_KEY_GRAPH, chart);

	}
	
	public String showGraph()
	{
		this.needRefresh = true;
		return null;
	}
	
	public SelectItem[] getIndependentVariables()
	{
		
		IndependentVariable[] vars = selectedGraph.getIndependentVariables();
		
		SelectItem[] items = new SelectItem[vars.length];
		for (int i = 0; i < vars.length; i++)
		{
			IndependentVariable var = vars[i];
			items[i] = new SelectItem(var.getId(), var.getLabel());
		}
		
		return items;
		
	}
	
	public SelectItem[] getDependentVariables()
	{
		
		DependentVariable[] vars = selectedGraph.getDependentVariables();
		
		List itemsList = new LinkedList();
		
		for (int i = 0; i < vars.length; i++)
		{
			DependentVariable var = vars[i];
			itemsList.add(new SelectItem(var.getId(), var.getLabel()));
		}
		
		SelectItem[] items = new SelectItem[itemsList.size()];
		itemsList.toArray(items);
		
		return items;
		
	}

	public List getSeries()
	{
		this.refreshGraphIfNeeded();
		return selectedGraph.getDataSeries();
	}
	
	public String getChartPath()
	{
		this.refreshGraphIfNeeded();
		nextUrlSeq++;
		return GRAPHS_SERVLET + "?" +
			"height=" + DEFAULT_CHART_HEIGHT + "&" +
			"width=" + DEFAULT_CHART_WIDTH + "&" +
			"seq=" + nextUrlSeq;
	}

	public void setSelectedDependentVariableId(String id)
	{
		selectedGraph.setSelectedDependentVariableId(id);
		needRefresh = true;
	}

	public void setGraphTypeId(String graphTypeId)
	{
		for (int i = 0; i < graphs.length; i++)
		{
			if (graphs[i].getId().equals(graphTypeId))
			{
				selectedGraph = graphs[i]; 
				needRefresh = true;
				return;
			}
		}
	}

	public String getSelectedIndependentVariableId()
	{
		return selectedGraph.getSelectedIndependentVariableId();
	}

	public String getSelectedDependentVariableId()
	{
		return selectedGraph.getSelectedDependentVariableId();
	}

	public void setSelectedIndependentVariableId(String id)
	{
		selectedGraph.setSelectedIndependentVariableId(id);
	}

	public boolean isGraphAvailable()
	{
		return selectedGraph.hasDataSeries();
	}
	
	public boolean isHaveSeries()
	{
		return selectedGraph.hasDataSeries();
	}

	public SearchBean getSearchBean()
	{
		return searchBean;
	}

	public void setSearchBean(SearchBean searchBean)
	{
		this.searchBean = searchBean;
	}

	public String getGraphTypeId()
	{
		return selectedGraph.getId();
	}

}
