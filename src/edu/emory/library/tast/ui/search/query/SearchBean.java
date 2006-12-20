package edu.emory.library.tast.ui.search.query;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.ajaxanywhere.AAUtils;
import org.dom4j.tree.AbstractAttribute;

import edu.emory.library.tast.dm.Configuration;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.Group;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.ui.MenuComponent;
import edu.emory.library.tast.ui.MenuItem;
import edu.emory.library.tast.ui.MenuItemMain;
import edu.emory.library.tast.ui.MenuItemSelectedEvent;
import edu.emory.library.tast.ui.MessageBarComponent;
import edu.emory.library.tast.ui.search.query.searchables.SearchableAttribute;
import edu.emory.library.tast.ui.search.query.searchables.UserCategory;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * This bean is used in UI to manage the list of groups, atributes, the
 * currently built query and the history list. It passes search parameters (the
 * current query and the current list of attributes) to other beans and
 * components in {@link SearchParameters}. When a user clicks the search
 * button, an internal representation of the query, represented by {@link Query},
 * is converted to a database query represented by
 * {@link edu.emory.library.tast.util.query.Conditions} and stored in
 * {@link #searchParameters}.
 * 
 * @author Jan Zich
 * 
 */
public class SearchBean
{
	
	private UserCategory selectedCategory = UserCategory.Beginners;
	private String mainSectionId = "listing";

	private History history = new History();
	private Query workingQuery = new Query();
	private SearchParameters searchParameters = new SearchParameters(new Conditions());
	
	private int yearFrom = 1501;
	private int yearTo = 1865;

	private MessageBarComponent messageBar;
	
	public SearchBean()
	{
		determineTimeFrameExtent();
	}

	private void determineTimeFrameExtent()
	{

		QueryValue query = new QueryValue("Voyage");
		
		query.addPopulatedAttribute(new FunctionAttribute("min", new Attribute[] {Voyage.getAttribute("yearam")}));
		query.addPopulatedAttribute(new FunctionAttribute("max", new Attribute[] {Voyage.getAttribute("yearam")}));
		
		List ret = query.executeQueryList();
		if (ret != null && ret.size() == 1)
		{
			Object[] row = (Object[]) ret.get(0); 
			yearFrom = ((Integer) row[0]).intValue();
			yearTo = ((Integer) row[1]).intValue();
		}

	}
	
	private void addQueryCondition(String selectedAttributeId)
	{
		workingQuery.addConditionOn(selectedAttributeId);
	}
	
	public void addConditionFromMenu(MenuItemSelectedEvent event)
	{
		addQueryCondition(event.getMenuId());
	}

	public String search()
	{
		messageBar.setRendered(false);
		searchInternal(true);
		return null;
	}

	private void searchInternal(boolean storeToHistory)
	{
		
		VisibleAttributeInterface[] columns = new VisibleAttributeInterface[workingQuery.getConditionCount()];
		Conditions conditions = new Conditions();
		
		conditions.addCondition(
				Voyage.getAttribute("yearam"),
				new Integer(yearFrom),
				Conditions.OP_GREATER_OR_EQUAL);

		conditions.addCondition(
				Voyage.getAttribute("yearam"),
				new Integer(yearTo),
				Conditions.OP_SMALLER_OR_EQUAL);

		//int i = 0;
		boolean errors = false;
		for (Iterator iterQueryCondition = workingQuery.getConditions().iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			if (!queryCondition.addToConditions(conditions, false)) errors = true;
			//columns[i++] = queryCondition.getSearchableAttributeId();
		}
		if (errors) return;
		
		searchParameters = new SearchParameters();
		searchParameters.setConditions(conditions);
		searchParameters.setColumns(columns);

		if (storeToHistory && !workingQuery.equals(history.getLatestQuery()))
			history.addQuery((Query) workingQuery.clone());

	}
	
	public void updateTotal(QueryUpdateTotalEvent event)
	{
		HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
		if (AAUtils.isAjaxRequest(request))
			AAUtils.addZonesToRefresh(request,"total"); 
	}
	
	public String getNumberOfResultsText()
	{
		
		Conditions conditions = new Conditions();
		for (Iterator iterQueryCondition = workingQuery.getConditions().iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			queryCondition.addToConditions(conditions, false);
		}
		
		conditions.addCondition(
				Voyage.getAttribute("yearam"),
				new Integer(yearFrom),
				Conditions.OP_GREATER_OR_EQUAL);

		conditions.addCondition(
				Voyage.getAttribute("yearam"),
				new Integer(yearTo),
				Conditions.OP_SMALLER_OR_EQUAL);

		QueryValue query = new QueryValue("Voyage", conditions);
		query.addPopulatedAttribute(new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("iid")}));		
		Object[] ret = query.executeQuery();
		int numberOfResults = ((Number)ret[0]).intValue();
		
		return "Expected number of voyages: " + numberOfResults;
	}

	public void historyItemDelete(HistoryItemDeleteEvent event)
	{
		history.deleteItem(event.getHistoryId());
	}
	
	public void historyItemRestore(HistoryItemRestoreEvent event)
	{
		HistoryItem historyItem = history.getHistoryItem(event.getHistoryId());
		workingQuery = (Query) historyItem.getQuery().clone();
		searchInternal(false);
	}
	
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

	private MenuItemMain[] getMenuAttributes(UserCategory category)
	{
		
		Group[] groups = Group.getGroups();
		
		MenuItemMain[] mainItems = new MenuItemMain[groups.length];
		for (int i = 0; i < groups.length; i++)
		{
			Group group = groups[i];
			SearchableAttribute[] attributes = group.getSearchableAttributesInUserCategory(category);
			if (attributes != null && attributes.length > 0)
			{
				MenuItemMain mainItem = new MenuItemMain();
				MenuItem[] subItems = new MenuItem[attributes.length];
				
				String mainItemText =
					"<b>" + group.getUserLabel() + "</b> " +
					"(" + attributes.length + " attributes)";
				
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
					if (workingQuery != null && workingQuery.containsConditionOn(attr.getId()))
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

	public MenuItemMain[] getMenuAttributesBeginners()
	{
		return getMenuAttributes(UserCategory.Beginners);
	}

	public MenuItemMain[] getMenuAttributesGeneral()
	{
		return getMenuAttributes(UserCategory.General);
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
		return searchParameters;
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

	public String getMainSectionId()
	{
		return mainSectionId;
	}

	public void setMainSectionId(String mainSectionId)
	{
		this.mainSectionId = mainSectionId;
	}

	public int getYearFrom()
	{
		return yearFrom;
	}

	public void setYearFrom(int yearFrom)
	{
		this.yearFrom = yearFrom;
	}

	public int getYearTo()
	{
		return yearTo;
	}

	public void setYearTo(int yearTo)
	{
		this.yearTo = yearTo;
	}

}