package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;


public class SearchBean
{
	
	private History history = new History();
	private Query workingQuery = new Query();
	private Conditions currentConditions = null;
	private String selectedAtttibute;
	
	public SearchBean()
	{
//		workingQuery.addConditionOn("shipname");
//		workingQuery.addConditionOn("captaina");
//		workingQuery.addConditionOn("sla32imp");
	}
	
	public void addQueryCondition()
	{
		workingQuery.addConditionOn(selectedAtttibute);
	}
	
	public void search()
	{
		
		// convert to condition
		Conditions conditions = new Conditions();
		try
		{
			for (Iterator iterQueryCondition = workingQuery.getConditions().iterator(); iterQueryCondition.hasNext();)
			{
				QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
				queryCondition.addToConditions(conditions);
			}
		}
		catch (QueryInvalidValueException qive)
		{
			// to user: correct your query,
			// you have a problem in qive.getAttributeName()
			return;
		}

		//System.out.println(conditions.getConditionHQL().conditionString);
		
		// all ok -> set our property
		currentConditions = conditions;
		
		// and add to history list
		HistoryItem historyItem = new HistoryItem();
		historyItem.setQuery(workingQuery);
		history.addItem(historyItem);
	
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

	public void setWorkingQuery(Query currentWorkingQuery)
	{
		this.workingQuery = currentWorkingQuery;
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

}
