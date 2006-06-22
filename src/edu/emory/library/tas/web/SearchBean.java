package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.attrGroups.CompoundAttribute;
import edu.emory.library.tas.attrGroups.Group;
import edu.emory.library.tas.util.query.Conditions;


public class SearchBean
{
	
	private static final String SIMPLE_ATTRIBUTE_PREFIX = "simple_";
	private static final String COMPOUND_ATTRIBUTE_PREFIX = "compound_";
	
	private String selectedAtttibuteId;
	private String selectedGroupId;
	private String activeGroupId;

	private History history = new History();
	private Query workingQuery = new Query();
	private Conditions currentConditions = null;
	private boolean tableVisible = true;
	private boolean timeLineVisible = false;
	private boolean statisticsVisible = false;
	
	public void listAttributes()
	{
		activeGroupId = selectedGroupId;
	}

	public void addQueryCondition()
	{
		AbstractAttribute attribute = null;
		if (selectedAtttibuteId.startsWith(SIMPLE_ATTRIBUTE_PREFIX))
		{
			Long id = new Long(selectedAtttibuteId.substring(SIMPLE_ATTRIBUTE_PREFIX.length()));
			attribute = Attribute.loadById(id);
		}
		else if (selectedAtttibuteId.startsWith(COMPOUND_ATTRIBUTE_PREFIX))
		{
			Long id = new Long(selectedAtttibuteId.substring(COMPOUND_ATTRIBUTE_PREFIX.length()));
			attribute = CompoundAttribute.loadById(id);
		}
		workingQuery.addConditionOn(attribute);
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
		statisticsVisible = "statistics".equals(event.getTabId());
	}
	
	public String getSelectedAtttibuteId()
	{
		return selectedAtttibuteId;
	}

	public void setSelectedAtttibuteId(String selectedAtttibute)
	{
		this.selectedAtttibuteId = selectedAtttibute;
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

//	public List getVoyageAttributes()
//	{
//		List options = new ArrayList();
//		String[] dbNames = Voyage.getAllAttrNames();
//		for (int i = 0; i < dbNames.length; i++)
//		{
//			SchemaColumn col = Voyage.getSchemaColumn(dbNames[i]);
//			SelectItem selectItem = new SelectItem();
//			selectItem.setValue(col.getName());
//			selectItem.setLabel(col.getName());
//			options.add(selectItem);
//		}
//		return options;
//	}
	
	public List getVoyageAttributeGroups()
	{
		Group[] groups = Voyage.getGroups();
		List options = new ArrayList();
		for (int i = 0; i < groups.length; i++)
		{
			SelectItem option = new SelectItem();
			option.setLabel(groups[i].getName());
			option.setValue(groups[i].getId().toString());
			options.add(option);
		}
		return options;
	}
	
	private void ensureActiveGroupSetId()
	{
		
		if (activeGroupId != null)
			return;
		
		Group[] groups = Voyage.getGroups();
		if (groups == null || groups.length == 0)
			return;
		
		activeGroupId = groups[0].getId().toString();

	}

	public List getVoyageAttributes()
	{
		
		ensureActiveGroupSetId();
		
		if (activeGroupId == null)
			return new ArrayList();
		
		Group group = Group.loadById(new Long(activeGroupId));
		List options = new ArrayList();
		
		for (Iterator iterCompAttr = group.getCompoundAttributes().iterator(); iterCompAttr.hasNext();)
		{
			CompoundAttribute a = (CompoundAttribute) iterCompAttr.next();
			SelectItem option = new SelectItem();
			option.setLabel(a.getName());
			option.setValue(COMPOUND_ATTRIBUTE_PREFIX + a.getId().toString());
			options.add(option);
		}
		
		for (Iterator iterAttr = group.getAttributes().iterator(); iterAttr.hasNext();)
		{
			Attribute a = (Attribute) iterAttr.next();
			SelectItem option = new SelectItem();
			option.setLabel(a.getName());
			option.setValue(SIMPLE_ATTRIBUTE_PREFIX + a.getId().toString());
			options.add(option);
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

	public String getSelectedGroupId()
	{
		return selectedGroupId;
	}

	public void setSelectedGroupId(String selectedGroupId)
	{
		this.selectedGroupId = selectedGroupId;
	}

	public boolean isStatisticsVisible() {
		return statisticsVisible;
	}

	public void setStatisticsVisible(boolean statisticsVisible) {
		this.statisticsVisible = statisticsVisible;
	}

}