package edu.emory.library.tast.database.listing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.common.listing.ShowDetailsEvent;
import edu.emory.library.tast.common.listing.SortChangeEvent;
import edu.emory.library.tast.common.listing.TableData;
import edu.emory.library.tast.common.listing.links.TableLinkManager;
import edu.emory.library.tast.common.voyage.VoyageDetailBean;
import edu.emory.library.tast.database.SourceInformationLookup;
import edu.emory.library.tast.database.listing.formatters.BooleanAttributeFormatter;
import edu.emory.library.tast.database.listing.formatters.SimpleDateAttributeFormatter;
import edu.emory.library.tast.database.query.SearchBean;
import edu.emory.library.tast.database.query.SearchParameters;
import edu.emory.library.tast.database.tabscommon.MemorizedAction;
import edu.emory.library.tast.database.tabscommon.VisibleAttribute;
import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.Source;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.Group;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.CSVUtils;
import edu.emory.library.tast.util.HibernateUtil;

/**
 * Backing bean for table results presented in web interface.
 * The bean is used in database/search-tab-table.jsp.
 * The main functionality of this bean is to manage visible attributes in table,
 * currently visible data, quering the database, sort column and all the
 * remaining functionality available in table (including switching to details view
 * for particular voyage). 
 * This bean has a reference to search bean which provides current conditions
 * that should be satisfied by results visible in the table.
 */
public class ListingBean {

	private static final String ATTRIBUTE = "Attribute_";

	private static final int MAX_STEP = 50000;

	//Manager for pager (pager is a component that switches between chunks of data)
	private TableLinkManager linkManager = new TableLinkManager(10);
	
	//reference to current instance of voyage bean
	private VoyageDetailBean voyageBean;
	
	/**
	 * Indication of component visibility.
	 */
	private Boolean componentVisible = new Boolean(false);

	/**
	 * Indication of query need.
	 */
	private boolean needQuery = false;

	/**
	 * Currently selected group set.
	 */
	private String selectedGroupSet = null;

	/**
	 * Currently selected list of attributes in added attrs list.
	 */
	private List selectedAttributeAdded = new ArrayList();

	/**
	 * List of visible columns - from configuration.
	 */
	private List visibleColumns = new ArrayList();

	/**
	 * List of selected attributes to be added.
	 */
	private List selectedAttributeToAdd = new ArrayList();


	/**
	 * Data for result table.
	 */
	private TableData data = new TableData();

	/**
	 * Indication of detail mode.
	 */
	private Boolean detailMode = new Boolean(false);

	/**
	 * Indication of configuration mode.
	 */
	private Boolean configurationMode = new Boolean(false);

	/**
	 * Indication of results mode.
	 */
	private Boolean resultsMode = new Boolean(true);

	/**
	 * Indication if parameters from search should be attached.
	 */
	private Boolean attachSearchedParams = new Boolean(true);

	/**
	 * Current search bean reference.
	 */
	private SearchBean searchBean;

	/**
	 * Conditions used in query last time.
	 */
	private TastDbConditions conditions = null;
	
	/**
	 * A list of actions that will be performed after apply configuration was pressed.
	 */
	private List actionsToPerform = new ArrayList();
	
	/**
	 * Constructor.
	 * It fills in default visible attributes in table and sets default sort column.
	 */
	public ListingBean()
	{
		resetToDefault();
	}
	
	public void resetToDefault()
	{
		
		configurationMode = new Boolean(false);
		resultsMode = new Boolean(true);
		attachSearchedParams = new Boolean(true);

		// Setup default columns
		VisibleAttributeInterface[] attrs = new VisibleAttributeInterface[6];
		attrs[0] = VisibleAttribute.getAttribute("voyageid");
		attrs[1] = VisibleAttribute.getAttribute("shipname");
		attrs[2] = VisibleAttribute.getAttribute("anycaptain");
		attrs[3] = VisibleAttribute.getAttribute("yearam");
		attrs[4] = VisibleAttribute.getAttribute("majbyimp");
		attrs[5] = VisibleAttribute.getAttribute("mjselimp");

		if (attrs[2] == null)
		{
			attrs[2] = VisibleAttribute.getAttribute("captaina");
		}

		data.setVisibleColumns(attrs);
		this.visibleColumns = Arrays.asList(attrs);
		
	}

	/**
	 * Queries DB for voyages if needed and sets new data in table. 
	 * Otherwise, it does nothing.
	 * 
	 */
	private void getResultsDB()
	{
		needQuery = this.linkManager.wasModified();
		SearchParameters searchParams = this.searchBean.getSearchParameters(); 
		if (!searchParams.getConditions().equals(this.conditions)) {
			this.linkManager.reset();
			this.conditions = (TastDbConditions) searchParams.getConditions().clone();
			needQuery = true;
		}
		if (searchParams.getConditions() != null && needQuery) {
			this.queryAndFillInData(null, this.data, this.linkManager.getCurrentFirstRecord(), this.linkManager.getStep(), false);
			this.setNumberOfResults();
			needQuery = false;
		}
	}

	/**
	 * Builds and executes query
	 * 
	 * @param subCondition
	 *            Conditions for query (for VoyageIndex object)
	 * @param dataTable
	 *            place to store retrieved data
	 * @param start
	 *            first result
	 * @param length
	 *            number of columns
	 */
	private Object[][] queryAndFillInData(TastDbConditions subCondition, TableData dataTable, int start, int length, boolean returnBasicInfo)
	{
		
//		long timeSt = System.currentTimeMillis();

		TastDbQuery qValue = getQuery(subCondition, dataTable, start, length, returnBasicInfo);

		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		SourceInformationLookup sourceInfoUtils = SourceInformationLookup.createSourceInformationUtils(session);
		
		// Execute query
		Object[] ret = qValue.executeQuery();
		dataTable.setData(ret);

		//get additional info for sources
		Attribute[] populatedAttributes = dataTable.getAttributesForQuery();
		for (int i = 0; i < populatedAttributes.length; i++) {
			if (populatedAttributes[i].getName().startsWith("source")) {
				for (int j = 0; j < ret.length; j++) {						
					if (((Object[])ret[j])[i] != null) {
						Source info = sourceInfoUtils.match((String)((Object[])ret[j])[i]);
						if (info != null) {
							data.setRollover(((Object[])ret[j])[i], info.getName());
						}
					}
				}
			}
		}

//		long timeEn = System.currentTimeMillis();
//		System.out.println("list = " + (timeEn - timeSt));		
		
		if (returnBasicInfo && ret.length > 0) {
			int len = ((Object[]) ret[0]).length;
			t.commit();
			session.close();
			return new Object[][] {
					{ VisibleAttribute.getAttribute("voyageid") },
					{ ((Object[]) ret[0])[len - 1]} };
		} else {
			t.commit();
			session.close();
			return new Object[][] {};
		}
	
	}

	private TastDbQuery getQuery(TastDbConditions subCondition, TableData dataTable, int start, int length, boolean returnBasicInfo) {
		// Build condition
		TastDbConditions localCond = (TastDbConditions) this.searchBean.getSearchParameters().getConditions().clone();
		if (subCondition != null) {
			localCond.addCondition(subCondition);
		}
		// Build query
		TastDbQuery qValue = new TastDbQuery("Voyage", localCond);
		if (length != -1) {
			qValue.setLimit(length);
		}
		if (start != -1) {
			qValue.setFirstResult(start);
		}

		// Dictionaries - list of columns with dictionaries.
		if (dataTable != null) {
			dataTable.setKeyAttribute(Voyage.getAttribute("iid"));
		}
		Attribute[] populatedAttributes = dataTable.getAttributesForQuery();
		if (populatedAttributes != null) {
			for (int i = 0; i < populatedAttributes.length; i++) {
				if (populatedAttributes[i] != null) {
					qValue.addPopulatedAttribute(populatedAttributes[i]);
				}
			}
		}

		// Add populated attributes
		Attribute[] populatedAdditionalAttributes = dataTable.getAdditionalAttributesForQuery();
		if (populatedAdditionalAttributes != null) {
			for (int i = 0; i < populatedAdditionalAttributes.length; i++) {
				qValue.addPopulatedAttribute(populatedAdditionalAttributes[i]);
			}
		}

		if (returnBasicInfo) {
			qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("voyageid").getAttributes()[0]);
		}

		VisibleAttributeInterface vattr = dataTable.getOrderByColumn();
		if (dataTable.getOrderByColumn() == null) {
			qValue.setOrderBy(new Attribute[] { Voyage.getAttribute("voyageid") });
		} else {

			Attribute[] attr = vattr.getAttributes();

			if (attr != null) {
				Attribute[] order = new Attribute[attr.length];
				for (int i = 0; i < attr.length; i++) {
					if (!(attr[i] instanceof DictionaryAttribute)) {
						order[i] = attr[i];
					} else {
						order[i] = new SequenceAttribute(new Attribute[] {attr[i], ((DictionaryAttribute)attr[i]).getAttribute("name")});
					}
				}
				qValue.setOrderBy(order);
				qValue.setOrder(dataTable.getOrder());
			}
		}
		return qValue;
	}


	/**
	 * Removing visible column from table (Remove button)
	 * This function is associated with configuration view of table tab.
	 * @return
	 */
	public String remSelectedAttributeFromList() {
		if (this.selectedAttributeAdded == null) {
			return null;
		}

		for (Iterator iter = selectedAttributeAdded.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			VisibleAttributeInterface attr = getVisibleAttribute(element);
			List list = new ArrayList(visibleColumns);
			int index = list.indexOf(attr);
			if (list.contains(attr)) {
				list.remove(attr);
				visibleColumns = list;
				setVisibleColumns();
				needQuery = true;
				//needDetailQuery = true;
				
				this.actionsToPerform.add(new MemorizedAction (new Object[] {new Integer(index), attr}) {
					public void performAction() {
						List list = new ArrayList(visibleColumns);
						list.add(((Integer)this.params[0]).intValue(), this.params[1]);
						visibleColumns = list;
						setVisibleColumns();
					}
				});
				
			}
		}
		
		return null;
	}

	/**
	 * Adding visible columns to table (Add button).
	 * This function is associated with configuration view of table tab.
	 * 
	 * @return
	 */
	public String addSelectedAttributeToList() {
		if (this.selectedAttributeToAdd == null) {
			return null;
		}

		// For each of attributes - check if not already in columns set and if
		// not - add it
		for (Iterator iter = this.selectedAttributeToAdd.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			VisibleAttributeInterface attr = this.getVisibleAttribute(element);
			VisibleAttributeInterface[] attrs = (VisibleAttributeInterface[]) this.visibleColumns.toArray(new VisibleAttributeInterface[] {});
			boolean is = false;
			for (int i = 0; i < attrs.length; i++) {
				if (attrs[i].getName().equals(attr.getName())) {
					is = true;
				}
			}
			if (!is) {
				List list = new ArrayList(this.visibleColumns);
				list.add(attr);
				this.visibleColumns = list;
				this.setVisibleColumns();
				this.needQuery = true;
				//this.needDetailQuery = true;
				
				int index = list.indexOf(attr);
				this.actionsToPerform.add(new MemorizedAction (new Object[] {new Integer(index)}) {
					public void performAction() {
						List list = new ArrayList(visibleColumns);
						list.remove(((Integer)this.params[0]).intValue());
						visibleColumns = list;
						setVisibleColumns();
					}
				});
				
			}
		}
		return null;
	}

	/**
	 * Moves column to left direction (Up button).
	 * This function is associated with configuration view of table tab.
	 * @return
	 */
	public String moveAttrUp() {
		if (this.selectedAttributeAdded == null) {
			return null;
		}

		// Move attributes one position up
		for (Iterator iter = this.selectedAttributeAdded.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			VisibleAttributeInterface attr = this.getVisibleAttribute(element);
			VisibleAttributeInterface[] attrs = (VisibleAttributeInterface[]) this.visibleColumns.toArray(new VisibleAttributeInterface[] {});
			for (int i = 1; i < attrs.length; i++) {
				if (attrs[i].getName().equals(attr.getName())) {
					VisibleAttributeInterface tmp = attrs[i];
					attrs[i] = attrs[i - 1];
					attrs[i - 1] = tmp;
					this.needQuery = true;
					//this.needDetailQuery = true;
					
					this.actionsToPerform.add(new MemorizedAction (new Object[] {new Integer(i)}) {
						public void performAction() {
							VisibleAttributeInterface[] attrs = (VisibleAttributeInterface[]) visibleColumns.toArray(new VisibleAttributeInterface[] {});
							
							int i = ((Integer)this.params[0]).intValue();
							VisibleAttributeInterface tmp = attrs[i - 1];
							attrs[i - 1] = attrs[i];
							attrs[i] = tmp;
							
							visibleColumns = Arrays.asList(attrs);
							setVisibleColumns();
						}
					});
					
					
					break;
				}
			}
			this.visibleColumns = Arrays.asList(attrs);
			this.setVisibleColumns();
		}
		return null;
	}

	/**
	 * Moves column one step left (Down button).
	 * This function is associated with configuration view of table tab.
	 * 
	 * @return
	 */
	public String moveAttrDown() {
		if (this.selectedAttributeAdded == null) {
			return null;
		}

		// Move each attribute one position down
		for (Iterator iter = this.selectedAttributeAdded.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			VisibleAttributeInterface attr = this.getVisibleAttribute(element);
			VisibleAttributeInterface[] attrs = (VisibleAttributeInterface[]) this.visibleColumns.toArray(new VisibleAttributeInterface[] {});
			for (int i = 0; i < attrs.length - 1; i++) {
				if (attrs[i].getName().equals(attr.getName())) {
					VisibleAttributeInterface tmp = attrs[i];
					attrs[i] = attrs[i + 1];
					attrs[i + 1] = tmp;
					this.needQuery = true;
					//this.needDetailQuery = true;
					
					this.actionsToPerform.add(new MemorizedAction (new Object[] {new Integer(i)}) {
						public void performAction() {
							VisibleAttributeInterface[] attrs = (VisibleAttributeInterface[]) visibleColumns.toArray(new VisibleAttributeInterface[] {});
							
							int i = ((Integer)this.params[0]).intValue();
							VisibleAttributeInterface tmp = attrs[i + 1];
							attrs[i + 1] = attrs[i];
							attrs[i] = tmp;
							
							visibleColumns = Arrays.asList(attrs);
							setVisibleColumns();
						}
					});
					
					break;
				}
			}
			this.visibleColumns = Arrays.asList(attrs);
			this.setVisibleColumns();
		} 
		return null;
	}

	/**
	 * Action invoked when sort has been changed.
	 * The action changes sort order as follows: nosort -> asc -> desc -> nosort...
	 * 
	 * @param event
	 */
	public void sortChanged(SortChangeEvent event) {
		String attrToSort = event.getAttributeSort();

		// Get column that will be sorted
		VisibleAttributeInterface attr = this.getVisibleAttribute(attrToSort);

		// Set appropriate order
		if (this.data.getOrderByColumn().getName().equals(attr.getName())) {
			switch (this.data.getOrder()) {
			case TastDbQuery.ORDER_ASC:
				this.data.setOrder(TastDbQuery.ORDER_DESC);
				break;
			case TastDbQuery.ORDER_DESC:
				this.data.setOrder(TastDbQuery.ORDER_DEFAULT);
				break;
			case TastDbQuery.ORDER_DEFAULT:
				this.data.setOrder(TastDbQuery.ORDER_ASC);
				break;
			}
		} else {
			this.data.setOrderByColumn(attr);
			this.data.setOrder(TastDbQuery.ORDER_ASC);
		}

		// Indicate need of query
		this.linkManager.reset();
		this.needQuery = true;
	}

	/**
	 * Action invoked when show detail about voyage is requested
	 * 
	 * @param event
	 */
	public void showDetails(ShowDetailsEvent event)
	{
		voyageBean.openVoyageByIid(event.getVoyageId().intValue());
		searchBean.setShowVoygeDetail(true);
		//FacesContext context = FacesContext.getCurrentInstance();
		// context.getApplication().getNavigationHandler().handleNavigation(context, null, "voyage-detail");
	}
	
	public String cancelConfiguration() {
		for (int i = this.actionsToPerform.size() - 1; i >= 0; i--) {
			MemorizedAction element = (MemorizedAction) this.actionsToPerform.get(i);
			element.performAction();
		}
		this.actionsToPerform.clear();
		this.configurationMode = new Boolean(false);
		this.resultsMode = new Boolean(true);
		this.detailMode = new Boolean(false);
		return null;
	}

	/**
	 * Gets VisibleColumn object from given string representation
	 * 
	 * @param sAttr
	 * @return
	 */
	private VisibleAttributeInterface getVisibleAttribute(String sAttr) {
		VisibleAttributeInterface ret = null;
		if (sAttr.startsWith(ATTRIBUTE)) {
			String attrId = sAttr.substring(ATTRIBUTE.length(), sAttr.length());
			
			ret = VisibleAttribute.getAttribute(attrId);
		} 
		return ret;
	}

	/**
	 * Gets number of first record displayed in current table view.
	 * 
	 * @return
	 */
	public Integer getFirstDisplayed() {
		if (this.linkManager.getResultsNumber() == 0)
			return new Integer(0);
		else
			return new Integer(this.linkManager.getCurrentFirstRecord() + 1);
	}

	/**
	 * Gets last record displayed in current table view.
	 * 
	 * @return
	 */
	public Integer getLastDisplayed() {
		if (this.linkManager.getResultsNumber() == 0)
			return new Integer(0);
		else
			return new Integer(this.linkManager.getCurrentFirstRecord() + 1 + 
					(this.data.getData() != null ? this.data.getData().length - 1 : 0));
	}

	/**
	 * Gets total number of rows.
	 * 
	 * @return
	 */
	public Integer getTotalRows() {
		return new Integer(this.linkManager.getResultsNumber());
	}

	/**
	 * Sets current step
	 * 
	 * @param step
	 */
	public void setStep(String step) {
		if (step == null)
			return;
		if (step.equals("all")) {
			if (this.linkManager.getStep() != MAX_STEP) {
				this.needQuery = true;
			}
			this.linkManager.setStep(MAX_STEP);
			return;
		}
		if (this.linkManager.getStep() != new Integer(step).intValue()) {
			this.needQuery = true;
		}
		this.linkManager.setStep(Integer.parseInt(step));
	}

	/**
	 * Gets current TableData
	 * 
	 * @return
	 */
	public TableData getData() {
		this.getResultsDB();
		return this.data;
	}

	public void setData(Object data) {
	}

	/**
	 * Sets size of results
	 * 
	 * @param size
	 */
	public void setResultSize(Integer size) {
	}

	/**
	 * Gets size of results.
	 * 
	 * @return
	 */
	public Integer getResultSize() {
		return new Integer(
				this.data.getData() != null ?
						this.data.getData().length :0);
	}

	public boolean isNoResult() {
		return getResultSize().intValue() == 0;
	}

	/**
	 * Sets current visible columns list.
	 * 
	 * @param list
	 */
	private void setVisibleAttributesList(List list) {
		String formatDate = AppConfig.getConfiguration().getString(AppConfig.FORMAT_DATE);
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			VisibleAttributeInterface attr = (VisibleAttributeInterface) iter.next();
			if (attr.getType().equals(VisibleAttributeInterface.DATE_ATTRIBUTE)) {
				this.data.setFormatter(attr, new SimpleDateAttributeFormatter(new SimpleDateFormat(formatDate)));
			}
			else if (attr.getType().equals(VisibleAttributeInterface.BOOLEAN_ATTRIBUTE)) {
				this.data.setFormatter(attr, new BooleanAttributeFormatter());
			}
		}

		this.data.setVisibleColumns(list);
	}

	/**
	 * Checks whether component is currently visible.
	 * 
	 * @return
	 */
	public Boolean getComponentVisible() {
		return componentVisible;
	}

	/**
	 * Sets visibility of current component.
	 * 
	 * @param componentVisible
	 */
	public void setComponentVisible(Boolean componentVisible) {
		this.componentVisible = componentVisible;
	}

	/**
	 * Checks if configuration mode is enabled.
	 * 
	 * @return
	 */
	public Boolean getConfigurationMode() {
		return this.configurationMode;
	}

	/**
	 * Checks if results mode is enabled.
	 * 
	 * @return
	 */
	public Boolean getResultsMode() {		
		return this.resultsMode;
	}

	/**
	 * Sets configuration mode.
	 * 
	 */
	public void configurationMode() {
		this.configurationMode = new Boolean(true);
		this.resultsMode = new Boolean(false);
		this.detailMode = new Boolean(false);
	}

	/**
	 * Sets results mode.
	 * 
	 */
	public void resultsMode() {
		this.actionsToPerform.clear();
		this.actionsToPerform.clear();
		this.configurationMode = new Boolean(false);
		this.resultsMode = new Boolean(true);
		this.detailMode = new Boolean(false);
	}

	/**
	 * Gets attribute groups that are available.
	 * 
	 * @return
	 */
	public List getAvailableGroupSets() {
		ArrayList res = new ArrayList();
		Group[] groupSets = Group.getGroups();
		for (int i = 0; i < groupSets.length; i++) {
			Group set = groupSets[i];
			//if (set.noOfAttributesInCategory(this.searchBean.getSearchParameters().getCategory()) > 0) {
				res.add(new SelectItem(set.getId(), set.toString()));
			//}
		}
		if (this.selectedGroupSet == null && groupSets.length > 0) {
			this.selectedGroupSet = (groupSets[0]).getId().toString();
		}
		//Collections.sort(res);
		return res;
	}

	/**
	 * Gets available attributes for chosen group.
	 * 
	 * @return
	 */
	public List getAvailableAttributes() {
		ArrayList res = new ArrayList();
		Group group = Group.getGroupById(this.selectedGroupSet);
		if (group != null) {
			VisibleAttributeInterface[] attrs = group.getVisibleAttributesInUserCategory(this.searchBean.getSearchParameters().getCategory());
			for (int i = 0; i < attrs.length; i++) {
				VisibleAttributeInterface attr = attrs[i];
				//if (attr.getCategory() == this.searchBean.getSearchParameters().getCategory()) {
					res.add(new SelectItem(attr.encodeToString(), attr.toString()));
				//}
			}
		}

	//	Collections.sort(res);
		return res;
	}

	/**
	 * Gets currently visible columns (according to configuration).
	 * 
	 * @return
	 */
	public List getVisibleAttributes() {
		List list = new ArrayList();
		// VisibleColumn[] cols = this.data.getVisibleAttributes();
		for (Iterator iter = this.visibleColumns.iterator(); iter.hasNext();) {
			VisibleAttributeInterface element = (VisibleAttributeInterface) iter.next();
			list.add(new SelectItem(element.encodeToString(), element.toString()));
		}
		return list;
	}

	/**
	 * Gets currently selected group.
	 * 
	 * @return
	 */
	public String getSelectedGroupSet() {
		return selectedGroupSet;
	}

	/**
	 * Sets currently selected group.
	 * 
	 * @param selectedGroupSet
	 */
	public void setSelectedGroupSet(String selectedGroupSet) {
		if (selectedGroupSet == null)
			return;
		this.selectedGroupSet = selectedGroupSet;
	}

	/**
	 * Gets currently selected columns (in configuration).
	 * 
	 * @return
	 */
	public List getSelectedAttributeAdded() {
		return selectedAttributeAdded;
	}

	/**
	 * Sets currently selected columns (in configuration).
	 * 
	 * @param selectedAttributeAdded
	 */
	public void setSelectedAttributeAdded(List selectedAttributeAdded) {
		this.selectedAttributeAdded = selectedAttributeAdded;
	}

	/**
	 * Gets selected attributes in list "to add".
	 * 
	 * @return
	 */
	public List getSelectedAttributeToAdd() {
		return selectedAttributeToAdd;
	}

	/**
	 * Sets selected attributes from list "to add".
	 * 
	 * @param selectedAttributeToAdd
	 */
	public void setSelectedAttributeToAdd(List selectedAttributeToAdd) {
		if (selectedAttributeToAdd == null)
			return;
		this.selectedAttributeToAdd = selectedAttributeToAdd;
	}

	/**
	 * Gets number of results.
	 * 
	 * @return
	 */
	public Integer getNumberOfResults() {
		return new Integer(this.linkManager.getResultsNumber());
	}

	/**
	 * Checks if detail mode is enabled.
	 * 
	 * @return
	 */
	public Boolean getDetailMode() {
		return detailMode;
	}

	/**
	 * Sets detail mode.
	 * 
	 * @param detailMode
	 */
	public void setDetailMode(Boolean detailMode) {
		this.detailMode = detailMode;
	}

	/**
	 * Checks if attributes from query should be attached to results.
	 * 
	 * @return
	 */
	public Boolean getAttachSearchedParams() {
		return attachSearchedParams;
	}

	/**
	 * Sets if attributes from query should be attached to results.
	 * 
	 * @param attachSearchedParams
	 */
	public void setAttachSearchedParams(Boolean attachSearchedParams) {
		if (!this.attachSearchedParams.equals(attachSearchedParams)) {
			this.attachSearchedParams = attachSearchedParams;			
			this.setVisibleColumns();
			
			this.actionsToPerform.add(new MemorizedAction (new Object[] {new Boolean(!attachSearchedParams.booleanValue())}) {
				public void performAction() {
					ListingBean.this.attachSearchedParams = (Boolean)this.params[0];
					setVisibleColumns();
				}
			});
			
			this.needQuery = true;
		}
	}

	private void setVisibleColumns() {
		List cols = new ArrayList(this.visibleColumns);
		setVisibleAttributesList(cols);
	}

	public SearchBean getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}

	/**
	 * Checks current number of results.
	 * 
	 */
	private void setNumberOfResults()
	{
		
		// It seems that PostgreSQL is not very good in running COUNT(*) queries.
		// To check the performance avoiding COUNT(*) queries, replace the content
		// of this function with the following:
		//
		// this.linkManager.setResultsNumber(100);

		TastDbConditions localCond = (TastDbConditions) this.searchBean.getSearchParameters().getConditions().clone();
		TastDbQuery qValue = new TastDbQuery("Voyage", localCond);
		qValue.addPopulatedAttribute(new  FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("iid")}));
		Object[] ret = qValue.executeQuery();
		this.linkManager.setResultsNumber(((Number) ret[0]).intValue());

	}

	public TableLinkManager getTableManager() {
		return linkManager;
	}

	public VoyageDetailBean getVoyageBean()
	{
		return voyageBean;
	}

	public void setVoyageBean(VoyageDetailBean voyageBean)
	{
		this.voyageBean = voyageBean;
	}
	
	public String getFileCurrentData()
	{

		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		TastDbQuery q = this.getQuery(this.conditions, this.data, this.linkManager.getCurrentFirstRecord(), this.linkManager.getStep(), false);
		CSVUtils.writeResponse(session, q, this.conditions.toString());
		
		t.commit();
		session.close();
		
		return null;
		
	}
	
	public String getFileAllData() {	
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		TastDbQuery q = this.getQuery(this.conditions, this.data, 0, -1, false);
		CSVUtils.writeResponse(session, q,this.conditions.toString());	
		
		t.commit();
		session.close();
		return null;
	}
}
