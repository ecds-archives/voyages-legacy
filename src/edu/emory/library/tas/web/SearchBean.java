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

/**
 * This bean is used in UI to manage the list of groups, atributes, the
 * currently built query and the history list. It passes search parameters (the
 * current query and the current list of attributes) to other beans and
 * components in {@link SearchParameters}. When a user clicks the search
 * button, an internal representation of the query, represented by {@link Query},
 * is converted to a database query represented by
 * {@link edu.emory.library.tas.util.query.Conditions} and stored in
 * {@link #searchParameters}.
 * 
 * @author Jan Zich
 * 
 */
public class SearchBean
{
	
	//private static final String ATTRIBUTES_LIST_SEPARATOR_TEXT = "-----------------------";
	//private static final String ATTRIBUTES_LIST_SEPARATOR_VALUE = "-";
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

//	private boolean tableVisible = true;
//	private boolean timeLineVisible = false;
//	private boolean statisticsVisible = false;
	
	private MessageBarComponent messageBar;
	
	/**
	 * Makes a nice ID for UI of a attribute. Compound attributes are prefixed
	 * by "compound_" and simple attributes are prefixed by "simple_". This
	 * makes it easier to reconstruct them when the page is submitted since we
	 * know the type of the attribute.
	 * 
	 * @param attr
	 *            The attribute to make the id for.
	 * @return
	 */
	private String makeAttributeMenuId(AbstractAttribute attr)
	{
		if (attr instanceof CompoundAttribute)
			return COMPOUND_ATTRIBUTE_PREFIX + attr.getId().toString();
		else if (attr instanceof Attribute)
			return SIMPLE_ATTRIBUTE_PREFIX + attr.getId().toString();
		else
			throw new RuntimeException("unimplemented attribute type");
	}

	/**
	 * The complementary method to
	 * {@link #makeAttributeMenuId(AbstractAttribute)}. It retrives the
	 * attribute by its UI id.
	 * 
	 * @param attrMenuId
	 *            The UI id of the attribute.
	 * 
	 * @return
	 */
	private AbstractAttribute getAttributeByMenuId(String attrMenuId)
	{
		if (attrMenuId.startsWith(SIMPLE_ATTRIBUTE_PREFIX))
		{
			Long attrId = new Long(attrMenuId.substring(SIMPLE_ATTRIBUTE_PREFIX.length()));
			return Attribute.loadById(attrId);
		}
		else if (attrMenuId.startsWith(COMPOUND_ATTRIBUTE_PREFIX))
		{
			Long attrId = new Long(attrMenuId.substring(COMPOUND_ATTRIBUTE_PREFIX.length()));
			return CompoundAttribute.loadById(attrId);
		}
		else
		{
			throw new RuntimeException("invalid menu id");
		}
	}

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
		workingQuery.addConditionOn(getAttributeByMenuId(selectedAttributeId));
	}
	
	/**
	 * Bind a button in UI. Activated when the user adds a new condition to a
	 * query in the beginner mode.
	 * 
	 * @return Always <code>null</code>, i.e. we stay on the same page.
	 */
	public String addQueryConditionBeginner()
	{
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
		addQueryCondition(selectedGeneralAtttibuteId);
		return null;
	}
	
	/**
	 * Bind to both menu components (beginner and general) in UI. Each menu item
	 * has an id which is the id of the corresponding attribute or compounded
	 * attribute.
	 * 
	 * @param event
	 */
	public void addConditionFromMenu(MenuItemSelectedEvent event)
	{
		addQueryCondition(event.getMenuId());
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
	 * Handler of the onPermlink event from the history list. Creates a permlink
	 * to a given history item. It saves it to the database and displays a
	 * message to the user with the genearated URL.
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

	/**
	 * When a page is opened with ?permlink parameter in URL, we retrieved the
	 * stored permlink from db and restore the correspodind query. This method
	 * is responsible for it. It is called from any getter which works with
	 * current query and history list, namely {@link #getWorkingQuery},
	 * {@link #getHistory()}, {@link #getSearchParameters()}, so that the
	 * correspoding components have the correct values. It has to be done this
	 * way, since JSF does not provide any other mean to detect that the page is
	 * loaded for the first time.
	 */
	private void restorePermlinkIfAny()
	{
		
		FacesContext context = FacesContext.getCurrentInstance();
		Map params = context.getExternalContext().getRequestParameterMap();
		if (!params.containsKey("permlink"))
			return;
		
		String permlink = (String)params.get("permlink");
		if (StringUtils.isNullOrEmpty(permlink))
			return;

		params.remove("permlink");
		Configuration conf = Configuration.loadConfiguration(permlink);
		if (conf == null)
			return;
		
		workingQuery = (Query) conf.getEntry("permlink");
		history.clear();
		searchInternal(true);

	}

	/**
	 * Called by {@link #getVoyageAttributeBeginnerGroups()} and
	 * {@link #getMenuAttributesGeneral()}. Gets the list of attribute groups
	 * for voyages which have at least one visible attribute for the given
	 * category. As of this moment this method does not cache the data in any
	 * way and leaves this to Hibernate caching meachnisms.
	 * 
	 * @param category
	 *            The category of users, indicated by
	 *            {@link AbstractAttribute#CATEGORY_BEGINNER} or
	 *            {@link AbstractAttribute#CATEGORY_GENERAL}, for which the
	 *            list of group is filtered.
	 * @return
	 */
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
	
	/**
	 * Bind in UI to a dropdown with the list of groups for beginner users.
	 * Internally calles {@link #getVoyageAttributeGroups(int)}.
	 * 
	 * @return The list of groups.
	 */
	public List getVoyageAttributeBeginnerGroups()
	{
		return getVoyageAttributeGroups(AbstractAttribute.CATEGORY_BEGINNER);
	}
	
	/**
	 * Bind in UI to a dropdown with the list of groups for general users.
	 * Internally calles {@link #getVoyageAttributeGroups(int)}.
	 * 
	 * @return The list of groups.
	 */
	public List getVoyageAttributeGeneralGroups()
	{
		return getVoyageAttributeGroups(AbstractAttribute.CATEGORY_GENERAL);
	}
	
	/**
	 * Finds the first visible attribute group of voyages for the given user
	 * catagory.
	 * 
	 * @param category
	 *            One of {@link AbstractAttribute#CATEGORY_BEGINNER} or
	 *            {@link AbstractAttribute#CATEGORY_GENERAL}.
	 * @return The group.
	 */
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

	/**
	 * If <code>selectedGroupId</code> is <code>null</code> or empty, calles
	 * {@link #getFirstVisibleGroup(int)} with the given <code>category</code>.
	 * Otherwise, just returns the given value. Used by
	 * {@link #getVoyageAttributes(String, int)} which is in turn used by the
	 * dropdowns in the search UI.
	 * 
	 * @param selectedGroupId
	 *            Group.
	 * @param category
	 *            One of {@link AbstractAttribute#CATEGORY_BEGINNER} or
	 *            {@link AbstractAttribute#CATEGORY_GENERAL}. Used for
	 *            {@link #getFirstVisibleGroup(int)}.
	 * @return
	 */
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

	/**
	 * Called by {@link #getVoyageBeginnerAttributes} and
	 * {@link #getVoyageGeneralAttributes}. Loads the given group from db and
	 * from its list of attributes and compound atributes creates a list of
	 * {@link SelectItem}.
	 * 
	 * @param selectedGroupId
	 *            The group whose attributes should be returned.
	 * @param category
	 *            User category. One of
	 *            {@link AbstractAttribute#CATEGORY_BEGINNER} or
	 *            {@link AbstractAttribute#CATEGORY_GENERAL}. Used for
	 *            {@link #getFirstVisibleGroup(int)}.
	 * @return
	 */
	private List getVoyageAttributes(String selectedGroupId, int category)
	{
		
		if (StringUtils.isNullOrEmpty(selectedGroupId))
			return new ArrayList();
		
		Group group = Group.loadById(new Long(selectedGroupId));
		if (group == null)
			return new ArrayList();

		List options = new ArrayList();
		
//		CompoundAttribute[] compoundAttributes = (CompoundAttribute[]) group.getCompoundAttributes().toArray(new CompoundAttribute[0]);
//		Attribute[] attributes = (Attribute[]) group.getAttributes().toArray(new Attribute[0]);
//		
//		AbstractAttribute.sortByUserLabelOrName(compoundAttributes);
//		AbstractAttribute.sortByUserLabelOrName(attributes);
//		
//		for (int i = 0; i < compoundAttributes.length; i++)
//		{
//			CompoundAttribute a = compoundAttributes[i];
//			if (a.isVisibleByCategory(category))
//			{
//				SelectItem option = new SelectItem();
//				option.setLabel(a.getUserLabelOrName());
//				option.setValue(COMPOUND_ATTRIBUTE_PREFIX + a.getId().toString());
//				options.add(option);
//			}
//		}
//		
//		if (compoundAttributes.length > 0)
//		{
//			SelectItem sep = new SelectItem();
//			sep.setLabel(ATTRIBUTES_LIST_SEPARATOR_TEXT);
//			sep.setValue(ATTRIBUTES_LIST_SEPARATOR_VALUE);
//			options.add(sep);
//		}
//
//		for (int i = 0; i < attributes.length; i++)
//		{
//			Attribute a = attributes[i];
//			if (a.isVisibleByCategory(category))
//			{
//				SelectItem option = new SelectItem();
//				option.setLabel(a.getUserLabelOrName());
//				option.setValue(SIMPLE_ATTRIBUTE_PREFIX + a.getId().toString());
//				options.add(option);
//			}
//		}

		AbstractAttribute[] allAttrs = group.getAllAttributesSortedByUserLabelOrName();
		for (int i = 0; i < allAttrs.length; i++)
		{
			AbstractAttribute attr = allAttrs[i];
			if (attr.isVisibleByCategory(category))
			{
				SelectItem option = new SelectItem();
				option.setLabel(attr.getUserLabelOrName());
				option.setValue(makeAttributeMenuId(attr));
				options.add(option);
			}
		}

		return options;

	}
	
	/**
	 * Bind to a dropdown component in UI. Returns the list of attributes in
	 * form of {@link SelectItem} visible for beginners contaned in the group
	 * given by {@link #selectedBeginnerGroupId}. Calls internally
	 * {@link #getVoyageAttributes(String, int)}.
	 * 
	 * @return The list of attributes.
	 */
	public List getVoyageBeginnerAttributes()
	{
		selectedBeginnerGroupId = ensureSelectedGroup(
				selectedBeginnerGroupId,
				AbstractAttribute.CATEGORY_BEGINNER);
		
		return getVoyageAttributes(
				selectedBeginnerGroupId,
				AbstractAttribute.CATEGORY_BEGINNER);
	}

	/**
	 * Bind to a dropdown component in UI. Returns the list of attributes in
	 * form of {@link SelectItem} visible for general users contaned in the
	 * group given by {@link #selectedGeneralGroupId}. Calls internally
	 * {@link #getVoyageAttributes(String, int)}.
	 * 
	 * @return The list of attributes.
	 */
	public List getVoyageGeneralAttributes()
	{
		selectedGeneralGroupId = ensureSelectedGroup(
				selectedGeneralGroupId,
				AbstractAttribute.CATEGORY_GENERAL);

		return getVoyageAttributes(
				selectedGeneralGroupId,
				AbstractAttribute.CATEGORY_GENERAL);
	}
	
	/**
	 * Returns a two-level menu data structure with groups in the first level
	 * and its attributes in the second level as required by the menu component
	 * {@link MenuComponent}.
	 * 
	 * @param category
	 *            User category. One of
	 *            {@link AbstractAttribute#CATEGORY_BEGINNER} or
	 *            {@link AbstractAttribute#CATEGORY_GENERAL}.
	 * @return
	 */
	private MenuItemMain[] getMenuAttributes(int category)
	{
		
		Group[] groups = Voyage.getGroups();
		Group.sortByUserLabelOrName(groups);
		
		MenuItemMain[] mainItems = new MenuItemMain[groups.length];
		for (int i = 0; i < groups.length; i++)
		{
			Group group = groups[i];
			int noOfAllAttrs = group.noOfAllAttributesInCategory(category); 
			if (noOfAllAttrs > 0)
			{
				MenuItemMain mainItem = new MenuItemMain();
				MenuItem[] subItems = new MenuItem[noOfAllAttrs];
				
				String mainItemText =
					"<b>" + group.getUserLabelOrName() + "</b> " +
					"(" + noOfAllAttrs + " attributes)";
				
				mainItems[i] = mainItem;
				mainItem.setId(group.getId().toString());
				mainItem.setText(mainItemText);
				mainItem.setSubmenu(subItems);
				
				int k = 0;
				AbstractAttribute[] allAttrs = group.getAllAttributesSortedByUserLabelOrName();
				for (int j = 0; j < allAttrs.length; j++)
				{
					AbstractAttribute attr = allAttrs[j];
					if (attr.isVisibleByCategory(category))
					{
						MenuItem subItem = new MenuItem();
						subItems[k++] = subItem;
						subItem.setId(makeAttributeMenuId(attr));
						System.out.print(attr.getUserLabelOrName());
						if (workingQuery != null && workingQuery.containsConditionOn(attr))
						{
							subItem.setText("<span class=\"attribute-selected\">" + attr.getUserLabelOrName() + "</span>");
						}
						else
						{
							subItem.setText(attr.getUserLabelOrName());
						}
					}
				}
				
			}
		}
		
		return mainItems;
	}

	/**
	 * Bind in UI to a menu component {@link MenuComponent}. Calls
	 * {@link #getMenuAttributes} with
	 * {@link AbstractAttribute#CATEGORY_BEGINNER}. Returns a two-level menu
	 * data structure with groups in the first level and its attributes in the
	 * second level.
	 * 
	 * @return An array of {@link MenuItemMain}.
	 */
	public MenuItemMain[] getMenuAttributesBeginner()
	{
		return getMenuAttributes(AbstractAttribute.CATEGORY_BEGINNER);
	}

	/**
	 * Bind in UI to a menu component {@link MenuComponent}. Calls
	 * {@link #getMenuAttributes} with
	 * {@link AbstractAttribute#CATEGORY_GENERAL}. Returns a two-level menu
	 * data structure with groups in the first level and its attributes in the
	 * second level.
	 * 
	 * @return An array of {@link MenuItemMain}.
	 */
	public MenuItemMain[] getMenuAttributesGeneral()
	{
		return getMenuAttributes(AbstractAttribute.CATEGORY_GENERAL);
	}

//	public boolean isTableVisible()
//	{
//		return tableVisible;
//	}
//
//	public void setTableVisible(boolean tableVisible)
//	{
//		this.tableVisible = tableVisible;
//	}
//
//	public boolean isTimeLineVisible()
//	{
//		return timeLineVisible;
//	}
//
//	public void setTimeLineVisible(boolean timeLineVisible)
//	{
//		this.timeLineVisible = timeLineVisible;
//	}
//
//	public String getSelectedBeginnerGroupId()
//	{
//		return selectedBeginnerGroupId;
//	}
//
//	public boolean isStatisticsVisible()
//	{
//		return statisticsVisible;
//	}
//
//	public void setStatisticsVisible(boolean statisticsVisible)
//	{
//		this.statisticsVisible = statisticsVisible;
//	}

	public void setSelectedBeginnerGroupId(String selectedGroupId)
	{
		if (selectedGroupId == null) return;
		this.selectedBeginnerGroupId = selectedGroupId;
	}
	
	public String getSelectedBeginnerGroupId()
	{
		return selectedGeneralGroupId;
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
		restorePermlinkIfAny();
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