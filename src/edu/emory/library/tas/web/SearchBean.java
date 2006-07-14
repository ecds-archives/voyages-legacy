package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import edu.emory.library.tas.Configuration;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.attrGroups.CompoundAttribute;
import edu.emory.library.tas.attrGroups.Group;
import edu.emory.library.tas.attrGroups.VisibleColumn;
import edu.emory.library.tas.util.StringUtils;
import edu.emory.library.tas.util.query.Conditions;


public class SearchBean
{
	
	private static final String ATTRIBUTES_LIST_SEPARATOR_TEXT = "-----------------------";
	private static final String ATTRIBUTES_LIST_SEPARATOR_VALUE = "-";
	private static final String SIMPLE_ATTRIBUTE_PREFIX = "simple_";
	private static final String COMPOUND_ATTRIBUTE_PREFIX = "compound_";
	
	private String selectedCategory = "beginner";
	private String selectedBeginnerGroupId;
	private String selectedBeginnerAtttibuteId;
	private String selectedGeneralGroupId;
	private String selectedGeneralAtttibuteId;

	private History history = new History();
	private Query workingQuery = new Query();
	private SearchParameters searchParameters = new SearchParameters();

	private boolean tableVisible = true;
	private boolean timeLineVisible = false;
	private boolean statisticsVisible = false;
	
	private MessageBarComponent messageBar;
	
	/**
	 * Called by {@link #addQueryConditionBeginner} or
	 * {@link #addQueryConditionGeneral()}. Inserts a new condition on the
	 * attribute with the given ID to the current query {@link #workingQuery}.
	 * 
	 * @param selectedAttributeId
	 *            ID of {@link Attribute} or {@link CompoundAttribute} to add.
	 */
	private void addQueryCondition(String selectedAttributeId)
	{
		AbstractAttribute attribute = null;
		if (selectedAttributeId == null || selectedAttributeId.equals(ATTRIBUTES_LIST_SEPARATOR_VALUE))
		{
			return;
		}
		else if (selectedAttributeId.startsWith(SIMPLE_ATTRIBUTE_PREFIX))
		{
			Long attrId = new Long(selectedAttributeId.substring(SIMPLE_ATTRIBUTE_PREFIX.length()));
			attribute = Attribute.loadById(attrId);
		}
		else if (selectedAttributeId.startsWith(COMPOUND_ATTRIBUTE_PREFIX))
		{
			Long attrId = new Long(selectedAttributeId.substring(COMPOUND_ATTRIBUTE_PREFIX.length()));
			attribute = CompoundAttribute.loadById(attrId);
		}
		workingQuery.addConditionOn(attribute);
	}
	
	/**
	 * Bind a button in UI. Activated when the user adds a new condition to a
	 * query in the beginner mode.
	 * 
	 * @return Always <code>null</code>, i.e. we stay on the same page.
	 */
	public String addQueryConditionBeginner()
	{
		System.out.println(selectedBeginnerAtttibuteId);
		addQueryCondition(selectedBeginnerAtttibuteId);
		return null;
	}
	
	/**
	 * Bind a button in UI. Activated when the user adds a new condition to a
	 * query in the general mode.
	 * 
	 * @return Always <code>null</code>, i.e. we stay on the same page.
	 */
	public String addQueryConditionGeneral()
	{
		System.out.println(selectedGeneralAtttibuteId);
		addQueryCondition(selectedGeneralAtttibuteId);
		return null;
	}

	/**
	 * Bind a button in UI. Activated when the user clicks on the search button
	 * under the list of conditions. Calles {@link #searchInternal(boolean)}
	 * with <code>true</code> meaning that the current query should be stored
	 * in the history list.
	 * 
	 * @return Always <code>null</code>, i.e. we stay on the same page.
	 */
	public String search()
	{
		messageBar.setRendered(false);
		searchInternal(true);
		return null;
	}

	/**
	 * Do actuall seach. Not called directly from UI
	 * Construct a database query ({@link Conditions})
	 * from {@link #workingQuery} by calling
	 * {@link QueryCondition#addToConditions(Conditions)} on each condition.
	 * Then, is the query is different from the last query in the history list
	 * and if <code>storeToHistory</code> is <code>true</code> the stores
	 * {@link #workingQuery} to the history list {@link #history}.
	 * 
	 * @param storeToHistory
	 */
	private void searchInternal(boolean storeToHistory)
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

		if (storeToHistory && !workingQuery.equals(history.getLatestQuery()))
			history.addQuery((Query) workingQuery.clone());

	}
	
	/**
	 * Handler of an event from the history list. Deletes the given history item
	 * from {@link #history}. The deletion is not handled directly by the
	 * {@link HistoryListComponent} because the component does not hold the data
	 * itself (it only gets them from this bean when rendering).
	 * 
	 * @param event
	 *            Contains the history ID to delete.
	 */
	public void historyItemDelete(HistoryItemDeleteEvent event)
	{
		history.deleteItem(event.getHistoryId());
	}
	
	/**
	 * Handler of an event from the history list. Restores the given history
	 * item from {@link #history} and replaces by it the current query
	 * {@link #workingQuery} and invokes search.
	 * 
	 * @param event
	 *            Contains the history ID to restore.
	 */
	public void historyItemRestore(HistoryItemRestoreEvent event)
	{
		HistoryItem historyItem = history.getHistoryItem(event.getHistoryId());
		workingQuery = (Query) historyItem.getQuery().clone();
		searchInternal(false);
	}
	
	/**
	 * Handler of an event from the history list. Creates a permlink to a given
	 * history item. It saves it to the database and displays a message to the
	 * user with the genearated URL.
	 * 
	 * @param event
	 *            Contains the history ID to create a permlink to.
	 */
	public void historyItemPermlink(HistoryItemPermlinkEvent event)
	{
		HistoryItem historyItem = history.getHistoryItem(event.getHistoryId());
		
		//UidGenerator generator = new UidGenerator();
		//String uid = generator.generate();
		
		Configuration conf = new Configuration();
		conf.addEntry("permlink", historyItem.getQuery());
		conf.save();

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		messageBar.setMessage(request.getRequestURL() + "?permlink=" + conf.getId());
		messageBar.setRendered(true);
		
	}
	
	private void restorePermlinkIfAny()
	{
		
		FacesContext context = FacesContext.getCurrentInstance();
		Map params = context.getExternalContext().getRequestParameterMap();
		String permlink = (String)params.get("permlink");
		
		if (permlink == null || permlink.length() == 0)
			return;

		Configuration conf = Configuration.loadConfiguration(new Long(permlink));
		
		workingQuery = (Query) conf.getEntry("permlink");
		history.clear();
		searchInternal(true);

	}
	
//	public void moduleTabChanged(TabChangeEvent event)
//	{
//		tableVisible = "table".equals(event.getTabId());
//		timeLineVisible = "timeline".equals(event.getTabId());
//		statisticsVisible = "statistics".equals(event.getTabId());
//	}
	
	private List getVoyageAttributeGroups(int category)
	{
		Group[] groups = Voyage.getGroups();
		Group.sortByUserLabelOrName(groups);
		List options = new ArrayList();
		for (int i = 0; i < groups.length; i++)
		{
			Group g = groups[i];
			if (g.noOfAllAttributesInCategory(category) > 0)
			{
				SelectItem option = new SelectItem();
				option.setLabel(g.getUserLabelOrName());
				option.setValue(g.getId().toString());
				options.add(option);
			}
		}
		return options;
	}
	
	public List getVoyageAttributeBeginnerGroups()
	{
		return getVoyageAttributeGroups(AbstractAttribute.CATEGORY_BEGINNER);
	}
	
	public List getVoyageAttributeGeneralGroups()
	{
		return getVoyageAttributeGroups(AbstractAttribute.CATEGORY_GENERAL);
	}
	
	private Group getFirstVisibleGroup(int category)
	{
		
		Group[] groups = Voyage.getGroups();
		if (groups == null || groups.length == 0)
			return null;
		
		Group.sortByUserLabelOrName(groups);
		
		for (int i = 0; i < groups.length; i++)
		{
			Group g = groups[i];
			if (g.noOfAllAttributesInCategory(category) > 0)
			{
				return g;
			}
		}
		
		return null;
		
	}

	private String ensureSelectedGroup(String selectedGroupId, int category)
	{
		if (StringUtils.isNullOrEmpty(selectedGeneralGroupId))
		{
			Group g = getFirstVisibleGroup(category);
			if (g != null) return g.getId().toString();
			return null;
		}
		else
		{
			return selectedGroupId;
		}
	}

	private List getVoyageAttributes(String selectedGroupId, int category)
	{
		
		if (StringUtils.isNullOrEmpty(selectedGroupId))
			return new ArrayList();
		
		Group group = Group.loadById(new Long(selectedGroupId));
		if (group == null)
			return new ArrayList();

		List options = new ArrayList();
		
		CompoundAttribute[] compoundAttributes = (CompoundAttribute[]) group.getCompoundAttributes().toArray(new CompoundAttribute[0]);
		Attribute[] attributes = (Attribute[]) group.getAttributes().toArray(new Attribute[0]);
		
		AbstractAttribute.sortByUserLabelOrName(compoundAttributes);
		AbstractAttribute.sortByUserLabelOrName(attributes);
		
		for (int i = 0; i < compoundAttributes.length; i++)
		{
			CompoundAttribute a = compoundAttributes[i];
			if (a.isVisibleByCategory(category))
			{
				SelectItem option = new SelectItem();
				option.setLabel(a.getUserLabelOrName());
				option.setValue(COMPOUND_ATTRIBUTE_PREFIX + a.getId().toString());
				options.add(option);
			}
		}
		
		if (compoundAttributes.length > 0)
		{
			SelectItem sep = new SelectItem();
			sep.setLabel(ATTRIBUTES_LIST_SEPARATOR_TEXT);
			sep.setValue(ATTRIBUTES_LIST_SEPARATOR_VALUE);
			options.add(sep);
		}

		for (int i = 0; i < attributes.length; i++)
		{
			Attribute a = attributes[i];
			if (a.isVisibleByCategory(category))
			{
				SelectItem option = new SelectItem();
				option.setLabel(a.getUserLabelOrName());
				option.setValue(SIMPLE_ATTRIBUTE_PREFIX + a.getId().toString());
				options.add(option);
			}
		}

		return options;

	}
	
	public List getVoyageBeginnerAttributes()
	{
		selectedBeginnerGroupId = ensureSelectedGroup(
				selectedBeginnerGroupId,
				AbstractAttribute.CATEGORY_BEGINNER);
		
		return getVoyageAttributes(
				selectedBeginnerGroupId,
				AbstractAttribute.CATEGORY_BEGINNER);
	}

	public List getVoyageGeneralAttributes()
	{
		selectedGeneralGroupId = ensureSelectedGroup(
				selectedGeneralGroupId,
				AbstractAttribute.CATEGORY_GENERAL);

		return getVoyageAttributes(
				selectedGeneralGroupId,
				AbstractAttribute.CATEGORY_GENERAL);
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

	public String getSelectedBeginnerGroupId()
	{
		return selectedBeginnerGroupId;
	}

	public void setSelectedBeginnerGroupId(String selectedGroupId)
	{
		if (selectedGroupId == null) return;
		this.selectedBeginnerGroupId = selectedGroupId;
	}
	
	public String getSelectedBeginnerAtttibuteId()
	{
		return selectedBeginnerAtttibuteId;
	}

	public void setSelectedBeginnerAtttibuteId(String selectedAtttibuteId)
	{
		if (selectedAtttibuteId == null) return;
		this.selectedBeginnerAtttibuteId = selectedAtttibuteId;
	}

	public String getSelectedGeneralGroupId()
	{
		return selectedGeneralGroupId;
	}

	public void setSelectedGeneralGroupId(String selectedGeneralGroupId)
	{
		if (selectedGeneralGroupId == null) return;
		this.selectedGeneralGroupId = selectedGeneralGroupId;
	}

	public String getSelectedGeneralAtttibuteId()
	{
		return selectedGeneralAtttibuteId;
	}

	public void setSelectedGeneralAtttibuteId(String selectedGeneralAtttibuteId)
	{
		if (selectedGeneralAtttibuteId == null) return;
		this.selectedGeneralAtttibuteId = selectedGeneralAtttibuteId;
	}
	
	public boolean isStatisticsVisible()
	{
		return statisticsVisible;
	}

	public void setStatisticsVisible(boolean statisticsVisible)
	{
		this.statisticsVisible = statisticsVisible;
	}

	public Query getWorkingQuery()
	{
		restorePermlinkIfAny();
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
		restorePermlinkIfAny();
		this.searchParameters.setCategory(getSelectedCategoryTyped());
		return searchParameters;
	}

	public void setSearchParameters(SearchParameters searchParameters)
	{
		this.searchParameters = searchParameters;
	}

	public MessageBarComponent getMessageBar()
	{
		return messageBar;
	}

	public void setMessageBar(MessageBarComponent messageBar)
	{
		this.messageBar = messageBar;
	}

	public String getSelectedCategory()
	{
		return selectedCategory;
	}

	public int getSelectedCategoryTyped()
	{
		return "beginner".equals(selectedCategory) ?
				AbstractAttribute.CATEGORY_BEGINNER :
					AbstractAttribute.CATEGORY_GENERAL;
	}

	public void setSelectedCategory(String selectedCategory)
	{
		this.selectedCategory = selectedCategory;
	}

}