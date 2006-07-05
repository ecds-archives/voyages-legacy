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
import edu.emory.library.tas.attrGroups.VisibleColumn;
import edu.emory.library.tas.util.query.Conditions;


public class SearchBean
{
	
	private static final String ATTRIBUTES_LIST_SEPARATOR_TEXT = "-----------------------";
	private static final String ATTRIBUTES_LIST_SEPARATOR_VALUE = "-";
	private static final String SIMPLE_ATTRIBUTE_PREFIX = "simple_";
	private static final String COMPOUND_ATTRIBUTE_PREFIX = "compound_";
	
	private String selectedAtttibuteId;
	private String selectedGroupId;

	private History history = new History();
	private Query workingQuery = new Query();
//	private Conditions currentConditions = null;
	private SearchParameters searchParameters = null;
	private boolean tableVisible = true;
	private boolean timeLineVisible = false;
	private boolean statisticsVisible = false;
	
	public void addQueryCondition()
	{
		AbstractAttribute attribute = null;
		if (selectedAtttibuteId.equals(ATTRIBUTES_LIST_SEPARATOR_VALUE))
		{
			return;
		}
		else if (selectedAtttibuteId.startsWith(SIMPLE_ATTRIBUTE_PREFIX))
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
		
		VisibleColumn[] columns = new VisibleColumn[workingQuery.getConditionCount()];
		Conditions conditions = new Conditions();

		int i = 0;
		boolean errors = false;
		for (Iterator iterQueryCondition = workingQuery.getConditions().iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			if (!queryCondition.addToConditions(conditions)) errors = true;
			columns[i++] = queryCondition.getAttribute();
		}
		if (errors) return;

		searchParameters = new SearchParameters();
		searchParameters.setConditions(conditions);
		searchParameters.setColumns(columns);

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
	
	public List getVoyageAttributeGroups()
	{
		Group[] groups = Voyage.getGroups();
		Group.sortByUserLabel(groups);
		List options = new ArrayList();
		for (int i = 0; i < groups.length; i++)
		{
			SelectItem option = new SelectItem();
			option.setLabel(groups[i].getUserLabelOrName());
			option.setValue(groups[i].getId().toString());
			options.add(option);
		}
		return options;
	}
	
	private void ensureSelectedGroupSetId()
	{
		
		if (selectedGroupId != null)
			return;
		
		Group[] groups = Voyage.getGroups();
		Group.sortByUserLabel(groups);
		if (groups == null || groups.length == 0)
			return;
		
		selectedGroupId = groups[0].getId().toString();

	}

	public List getVoyageAttributes()
	{
		
		ensureSelectedGroupSetId();
		
		if (selectedGroupId == null)
			return new ArrayList();
		
		Group group = Group.loadById(new Long(selectedGroupId));
		List options = new ArrayList();
		
		CompoundAttribute[] compoundAttributes = (CompoundAttribute[]) group.getCompoundAttributes().toArray(new CompoundAttribute[0]);
		Attribute[] attributes = (Attribute[]) group.getAttributes().toArray(new Attribute[0]);
		
		AbstractAttribute.sortByUserLabel(compoundAttributes);
		AbstractAttribute.sortByUserLabel(attributes);
		
		for (int i = 0; i < compoundAttributes.length; i++)
		{
			CompoundAttribute a = compoundAttributes[i];
			SelectItem option = new SelectItem();
			option.setLabel(a.getUserLabelOrName());
			option.setValue(COMPOUND_ATTRIBUTE_PREFIX + a.getId().toString());
			options.add(option);
		}
		
		SelectItem sep = new SelectItem();
		sep.setLabel(ATTRIBUTES_LIST_SEPARATOR_TEXT);
		sep.setValue(ATTRIBUTES_LIST_SEPARATOR_VALUE);
		options.add(sep);

		for (int i = 0; i < attributes.length; i++)
		{
			Attribute a = attributes[i];
			SelectItem option = new SelectItem();
			option.setLabel(a.getUserLabelOrName());
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

	public String getSelectedAtttibuteId()
	{
		return selectedAtttibuteId;
	}

	public void setSelectedAtttibuteId(String selectedAtttibute)
	{
		this.selectedAtttibuteId = selectedAtttibute;
	}

//	public Conditions getCurrentConditions()
//	{
//		return currentConditions;
//	}
//
//	public void setCurrentConditions(Conditions currentQuery)
//	{
//		this.currentConditions = currentQuery;
//	}

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

	public SearchParameters getSearchParameters()
	{
		return searchParameters;
	}

	public void setSearchParameters(SearchParameters searchParameters)
	{
		this.searchParameters = searchParameters;
	}

}