package edu.emory.library.tast.database.query;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.ajaxanywhere.AAUtils;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.MenuItem;
import edu.emory.library.tast.common.MenuItemSection;
import edu.emory.library.tast.common.MenuItemSelectedEvent;
import edu.emory.library.tast.common.PopupComponent;
import edu.emory.library.tast.database.graphs.GraphsBean;
import edu.emory.library.tast.database.listing.ListingBean;
import edu.emory.library.tast.database.map.MapBean;
import edu.emory.library.tast.database.query.searchables.SearchableAttribute;
import edu.emory.library.tast.database.query.searchables.UserCategory;
import edu.emory.library.tast.database.table.TableBean;
import edu.emory.library.tast.database.timeline.TimelineBean;
import edu.emory.library.tast.dm.Revision;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.Group;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * This bean is used to manage the list of groups, attributes, the
 * currently built query and the history list. It passes search parameters (the
 * current query and the current list of attributes) to other beans and
 * components in {@link SearchParameters}. When a user clicks the search
 * button, an internal representation of the query, represented by
 * {@link QueryBuilderQuery}, is converted to a database query represented by
 * {@link edu.emory.library.tast.util.query.Conditions} and stored in
 * {@link #searchParameters}.
 */
public class SearchBean
{
	
	public static final String TAB_ID_LISTING = "listing";
	public static final String TAB_ID_BASIC_GRAPH = "basic-graph";
	public static final String TAB_ID_TABLE = "tableview";
	public static final String TAB_ID_CUSTOM_GRAPHS = "custom-graphs";
	public static final String TAB_ID_BASIC_STATS = "basic-statistics";
	public static final String TAB_ID_MAP = "map-ports";
	
	private String minYear;
	private String maxYear;
	
	private UserCategory selectedCategory = UserCategory.General;
	private String mainSectionId = TAB_ID_LISTING;

	private History history;
	private Query workingQuery;
	private SearchParameters searchParameters;
	
	private String selectedRevision = AppConfig.getConfiguration().getString(AppConfig.DEFAULT_REVISION);
	
	private SelectItem[] revisions = null;
	
	private String expandedGroup = "basic";
	private boolean yearsLocked = false;
	
	private PopupComponent permlinkPopup = null;
	private String lastPermLink = null;
	private int numberOfResults;
	
	public SearchBean()
	{
		history = new History();
		initNewQuery();
		searchInternal(false);
	}
	
	/**
	 * Reinitialises the working query to its default state, i.e. it creates a
	 * new one. Also determines min and max year. Used in the constructor and
	 * when the user presses the "Reset to defaults" button.
	 */
	private void initNewQuery()
	{
		workingQuery = new Query();		
		determineTimeFrameExtent();
	}
	
	/**
	 * Finds the min a max year among all voyages in the database. The "yearam"
	 * field is used.
	 */
	private void determineTimeFrameExtent()
	{

		QueryValue query = new QueryValue("Voyage");
		
		query.addPopulatedAttribute(
				new FunctionAttribute("min",
						new Attribute[] {Voyage.getAttribute("yearam")}));
		
		query.addPopulatedAttribute(
				new FunctionAttribute("max",
						new Attribute[] {Voyage.getAttribute("yearam")}));
		
		List ret = query.executeQueryList();
		if (ret != null && ret.size() == 1)
		{
			Object[] row = (Object[]) ret.get(0);
			minYear = row[0] != null ? row[0].toString() : null;
			maxYear = row[1] != null ? row[1].toString() : null;
			workingQuery.setYearFrom(minYear);
			workingQuery.setYearTo(maxYear);
		}

	}

	public String restoreDefaultTimeFrameExtent()
	{
		
		workingQuery.setYearFrom(minYear);
		workingQuery.setYearTo(maxYear);
		
		return null;

	}
	
	/**
	 * Bound to UI. Event handler for adding a new condition to the current
	 * working query.
	 * 
	 * @param event
	 */
	public void addConditionFromMenu(MenuItemSelectedEvent event)
	{
		workingQuery.addConditionOn(event.getMenuId());
	}
	
	/**
	 * Bound to UI to the "Reset to defaults" button. Uses {@link #initNewQuery()}
	 * and the it calls {@link #searchInternal(boolean)}. 
	 * @return
	 */
	public String startAgain()
	{
		
		ListingBean listingBean = (ListingBean) JsfUtils.getSessionBean("ListingBean");
		MapBean mapBean  = (MapBean) JsfUtils.getSessionBean("MapBean");
		TimelineBean timelineBean = (TimelineBean) JsfUtils.getSessionBean("TimelineBean");
		TableBean tableBean = (TableBean) JsfUtils.getSessionBean("TableBean");
		GraphsBean graphsBean = (GraphsBean) JsfUtils.getSessionBean("GraphsBean");

		listingBean.resetToDefault();
		mapBean.resetToDefault();
		timelineBean.resetToDefault();
		tableBean.resetToDefault();
		graphsBean.resetToDefault();

		mainSectionId = TAB_ID_LISTING;
		
		initNewQuery();
		searchInternal(false);
		
		return null;
		
	}
	
	/**
	 * Bound to UI to the "Search" button. 
	 * @return
	 */
	public String search()
	{
		searchInternal(true);
		return null;
	}
	
	/**
	 * This invokes the search. It constructs the database query first. When there
	 * are any errors it stops. Then it stores the constructed query in
	 * {@link #searchParameters}. Finally, it compares {@link #workingQuery}
	 * with the latest query in the history, and if they differ it adds
	 * {@link #workingQuery} to the history.
	 * 
	 * @param storeToHistory
	 */
	private void searchInternal(boolean storeToHistory)
	{
		
		try
		{
		
			Conditions dbConds = new Conditions();
			boolean errors = workingQuery.addToDbConditions(false, dbConds);
			if (errors) return;
			
			//dbConds.addCondition(Voyage.getAttribute("revision"), new Integer(Voyage.getCurrentRevision()), Conditions.OP_EQUALS);
			dbConds.addCondition(Voyage.getAttribute("revision"), new Integer(selectedRevision), Conditions.OP_EQUALS);
			
			searchParameters = new SearchParameters();
			searchParameters.setConditions(dbConds);
			searchParameters.setColumns(null);
			
			updateNumberOfResults();
	
			if (storeToHistory && !workingQuery.equals(history.getLatestQuery()))
			{
				HistoryItem historyItem = new HistoryItem();
				historyItem.setQuery((Query) workingQuery.clone());
				history.addItem(historyItem);
			}
		
		}
		catch (CloneNotSupportedException cns)
		{
			throw new RuntimeException(cns);
		}

	}
	
	/**
	 * Bound to UI to the query builder compoment. It is invoked whenever the
	 * users changes anything in the HTML form representing the query. The
	 * request is then sent to server on background using AJAX.
	 * 
	 * @param event
	 */
	public void updateTotal(QueryUpdateTotalEvent event)
	{
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
		if (AAUtils.isAjaxRequest(request))
		{
			updateNumberOfResults();
			AAUtils.addZonesToRefresh(request, "total");
		}
	}
	
	private void updateNumberOfResults()
	{
		
		Conditions dbConds = new Conditions();
		dbConds.addCondition(Voyage.getAttribute("revision"), new Integer(this.selectedRevision), Conditions.OP_EQUALS);
		workingQuery.addToDbConditions(false, dbConds);
		
		QueryValue query = new QueryValue("Voyage", dbConds);
		System.out.println(dbConds.toString());
		query.addPopulatedAttribute(new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("iid")}));		
		Object[] ret = query.executeQuery();
		numberOfResults = ((Number)ret[0]).intValue();

	}
	
	public String getNumberOfResultsText()
	{
		MessageFormat fmt = new MessageFormat(TastResource.getText("slaves_search_expected"));
		return fmt.format(new Object[] {new Integer(numberOfResults)});
	}
	
	public int getNumberOfResults()
	{
		return numberOfResults;
	}

	public boolean isNoResult()
	{
		return numberOfResults == 0;
	}

	/**
	 * Bound to UI. Deletes a chosen history item. Called by the history list
	 * component.
	 * 
	 * @param event
	 */
	public void historyItemDelete(HistoryItemDeleteEvent event)
	{
		history.deleteItem(event.getHistoryId());
	}
	
	/**
	 * Bound to UI. Replaces {@link #workingQuery} by a chosen item from the
	 * history list, and executes search. Called by the history list component.
	 * 
	 * @param event
	 */
	public void historyItemRestore(HistoryItemRestoreEvent event)
	{
		try
		{
			HistoryItem historyItem = history.getHistoryItem(event.getHistoryId());
			workingQuery = (Query) historyItem.getQuery().clone();
			searchInternal(false);
		}
		catch (CloneNotSupportedException cns)
		{
			throw new RuntimeException(cns);
		}
	}
	
	public void createPermlink()
	{
		
		lastPermLink =
			AppConfig.getConfiguration().getString(AppConfig.SITE_URL) + 
			"/database/search.faces?" + workingQuery.createUrl();
		
		this.permlinkPopup.display();

	}
	
	/**
	 * Check the current URL if there is permlink=XXX. If there is, the
	 * corresponding query is retrieved from the database, {@link #workingQuery}
	 * is set to it, and search is invoked.
	 */
	public boolean restoreQueryFromUrl(Map params)
	{
		
		Query newQuery = Query.restoreFromUrl(params);
		if (newQuery == null || newQuery.isEmpty())
			return false;
		
		workingQuery = newQuery;
		searchInternal(true);
		return true;

	}
	
	/**
	 * Bound to UI. Provides a list of attributes for the menu components in the
	 * left column on the page.
	 * 
	 * @param category
	 * @return
	 */
	private MenuItemSection[] getMenuAttributes(UserCategory category)
	{
		
		Group[] groups = Group.getGroups();
		QueryBuilderQuery builderQuery = workingQuery.getBuilderQuery();
		
		MenuItemSection[] mainItems = new MenuItemSection[groups.length];
		for (int i = 0; i < groups.length; i++)
		{
			Group group = groups[i];
			SearchableAttribute[] attributes = group.getSearchableAttributesInUserCategory(category);
			if (attributes != null && attributes.length > 0)
			{
				MenuItemSection mainItem = new MenuItemSection();
				MenuItem[] subItems = new MenuItem[attributes.length];
				
				String mainItemText = "<b>" + group.getUserLabel() + "</b>";
				if (attributes.length > 1)
				{
					mainItemText += " (" + attributes.length + " variables)";					
				}
				else
				{
					mainItemText += " (1 variable)";					
				}
				
				mainItems[i] = mainItem;
				mainItem.setId(group.getId().toString());
				mainItem.setText(mainItemText);
				mainItem.setSubmenu(subItems);
				
				int k = 0;
				for (int j = 0; j < attributes.length; j++)
				{
					SearchableAttribute attr = attributes[j];
					MenuItem subItem = new MenuItem();
					subItems[k++] = subItem;
					subItem.setId(attr.getId());
					if (workingQuery != null && builderQuery.containsConditionOn(attr.getId()))
					{
						subItem.setText("<span class=\"attribute-selected\">" + attr.getUserLabel() + "</span>");
					}
					else
					{
						subItem.setText(attr.getUserLabel());
					}
				}
				
			}
		}
		
		return mainItems;
	}
	
	/**
	 * Gets a list of attributes for beginners. 
	 * @return
	 */
	public MenuItemSection[] getMenuAttributesBeginners()
	{
		return getMenuAttributes(UserCategory.Beginners);
	}

	/**
	 * Gets a list of attributes for general audience. 
	 * @return
	 */
	public MenuItemSection[] getMenuAttributesGeneral()
	{
		return getMenuAttributes(UserCategory.General);
	}

	/**
	 * Wrapper for {@link #workingQuery}. 
	 * @return
	 */
	public QueryBuilderQuery getWorkingQuery()
	{
		return workingQuery.getBuilderQuery();
	}

	/**
	 * Wrapper for {@link #workingQuery}. 
	 * @return
	 */
	public void setWorkingQuery(QueryBuilderQuery newWorkingQuery)
	{
		this.workingQuery.setBuilderQuery(newWorkingQuery);
	}

	/**
	 * Wrapper for {@link #history}. 
	 * @return
	 */
	public History getHistory()
	{
		return history;
	}

	/**
	 * Wrapper for {@link #history}. 
	 * @return
	 */
	public void setHistory(History history)
	{
		this.history = history;
	}

	/**
	 * Wrapper for {@link #searchParameters}. 
	 * @return
	 */
	public SearchParameters getSearchParameters()
	{
		return (SearchParameters) searchParameters.clone();
	}

	/**
	 * Wrapper for {@link #searchParameters}. 
	 * @return
	 */

	public String getSelectedCategory()
	{
		return selectedCategory.toString();
	}

	public void setSearchParameters(SearchParameters searchParameters)
	{
		this.searchParameters = searchParameters;
	}

	public void setSelectedCategory(String selectedCategory)
	{
		this.selectedCategory = UserCategory.parse(selectedCategory);
		this.searchParameters.setCategory(this.selectedCategory);
	}

	/**
	 * Wrapper for {@link #mainSectionId} - the current tab. 
	 * @return
	 */
	public String getMainSectionId()
	{
		return mainSectionId;
	}

	/**
	 * Wrapper for {@link #mainSectionId} - the current tab. 
	 * @return
	 */
	public void setMainSectionId(String mainSectionId)
	{
		this.mainSectionId = mainSectionId;
	}

	/**
	 * Wrapper for the starting year of {@link #workingQuery}. 
	 * @return
	 */
	public String getYearFrom()
	{
		return workingQuery.getYearFrom();
	}

	/**
	 * Wrapper for the starting year of {@link #workingQuery}. 
	 * @return
	 */
	public void setYearFrom(String  yearFrom)
	{
		if (yearsLocked) return;
		workingQuery.setYearFrom(yearFrom);
	}

	/**
	 * Wrapper for the ending year of {@link #workingQuery}. 
	 * @return
	 */
	public String  getYearTo()
	{
		return workingQuery.getYearTo();
	}

	/**
	 * Wrapper for the ending year of {@link #workingQuery}. 
	 * @return
	 */
	public void setYearTo(String  yearTo)
	{
		if (yearsLocked) return;
		workingQuery.setYearTo(yearTo);
	}
	
	public String getSelectedRevision() {
		return selectedRevision;
	}
	
	public void setSelectedRevision(String selectedRevision) {
		this.selectedRevision = selectedRevision;
	}
	
	public SelectItem[] getRevisions() {
		if (this.revisions == null) {
			QueryValue query = new QueryValue("Revision");
			Object[] rev = query.executeQuery();
			this.revisions = new SelectItem[rev.length];
			for (int i = 0; i < rev.length; i++) {
				this.revisions[i] = new SelectItem(((Revision)rev[i]).getId().toString(), ((Revision)rev[i]).getName());
			}
		}
		return this.revisions;
	}

	public String getExpandedGroup() {
		return expandedGroup;
	}

	public void setExpandedGroup(String expandedGroup) {
		this.expandedGroup = expandedGroup;
	}

	public void lockYears(boolean b) {
		this.yearsLocked = b;
	}

	public PopupComponent getPermlinkPopup()
	{
		return permlinkPopup;
	}

	public void setPermlinkPopup(PopupComponent permlinkPopup)
	{
		this.permlinkPopup = permlinkPopup;
	}

	public boolean isShowPermLinkTool()
	{
		return !workingQuery.isEmpty();
	}

	public String getPermLink()
	{
		return lastPermLink;
	}
	
	public String getTimeFrameExtentHint()
	{
		return
			"Note: " +
			"The full extent of time from the first to the last voyage is " +
			minYear + " &ndash; " + maxYear;
	}

}