package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.util.query.Conditions;


public class SearchBean
{
	
	private History history = new History();
	private Query workingQuery = new Query();
	private Conditions currentConditions = null;
	private String selectedAtttibute;
	private boolean tableVisible = true;
	private boolean timeLineVisible = false;
	
	public void addQueryCondition()
	{
		workingQuery.addConditionOn(selectedAtttibute);
	}
	
	public void search()
	{
		
		// convert to condition
		Conditions conditions = new Conditions();
		boolean errors = false;
		for (Iterator iterQueryCondition = workingQuery.getConditions().iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			if (!queryCondition.addToConditions(conditions)) errors = true;
		}
		if (errors) return;

		// all ok -> set our property
		currentConditions = conditions;
		
		// and add to history list
		if (!workingQuery.equals(history.getLatestQuery()))
			history.addQuery((Query) workingQuery.clone());	
	}
	
	public void historyItemDelete(HistoryItemDeleteEvent event)
	{
		history.deleteItem(event.getHistoryId());
	}
	
	public void historyItemRestore(HistoryItemRestoreEvent event)
	{
		HistoryItem historyItem = history.getHistoryItem(event.getHistoryId());
		workingQuery = (Query) historyItem.getQuery().clone();
	}
	
	public void moduleTabChanged(TabChangeEvent event)
	{
		tableVisible = "table".equals(event.getTabId());
		timeLineVisible = "timeline".equals(event.getTabId());
	}
	
	public String getSelectedAtttibute()
	{
		return selectedAtttibute;
	}

	public void setSelectedAtttibute(String selectedAtttibute)
	{
		this.selectedAtttibute = selectedAtttibute;
	}

	public Conditions getCurrentConditions()
	{
		return currentConditions;
	}

	public void setCurrentConditions(Conditions currentQuery)
	{
		this.currentConditions = currentQuery;
	}

	public Query getWorkingQuery()
	{
		return workingQuery;
	}

	public void setWorkingQuery(Query newWorkingQuery)
	{
		this.workingQuery = newWorkingQuery;
	}

	public History getHistory()
	{
		return history;
	}

	public void setHistory(History history)
	{
		this.history = history;
	}

	public List getVoyageAttributes()
	{
		List options = new ArrayList();
		String[] dbNames = Voyage.getAllAttrNames();
		for (int i = 0; i < dbNames.length; i++)
		{
			SchemaColumn col = Voyage.getSchemaColumn(dbNames[i]);
			SelectItem selectItem = new SelectItem();
			selectItem.setValue(col.getName());
			selectItem.setLabel(col.getName());
			options.add(selectItem);
		}
		return options;
	}

	public boolean isTableVisible()
	{
		return tableVisible;
	}

	public void setTableVisible(boolean tableVisible)
	{
		this.tableVisible = tableVisible;
	}

	public boolean isTimeLineVisible()
	{
		return timeLineVisible;
	}

	public void setTimeLineVisible(boolean timeLineVisible)
	{
		this.timeLineVisible = timeLineVisible;
	}

}
