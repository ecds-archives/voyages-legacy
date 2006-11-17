package edu.emory.library.tast.ui.search.table;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.Group;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.ui.maps.LegendItemsGroup;
import edu.emory.library.tast.ui.maps.MapLayer;
import edu.emory.library.tast.ui.maps.component.PointOfInterest;
import edu.emory.library.tast.ui.search.query.SearchBean;
import edu.emory.library.tast.ui.search.stat.ComparableSelectItem;
import edu.emory.library.tast.ui.search.table.formatters.SimpleDateAttributeFormatter;
import edu.emory.library.tast.ui.search.tabscommon.MemorizedAction;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttribute;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.ui.search.tabscommon.links.TableLinkManager;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.DirectValue;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * Backing bean for Table results presented in TAST web interface.
 * 
 * @author Pawel Jurczyk
 * 
 */
public class TableResultTabBean {

	private static final String ATTRIBUTE = "Attribute_";

	private static final String MAP_SESSION_KEY = "detail_map__";

	private static final int MAX_STEP = 50000;

	private TableLinkManager linkManager = new TableLinkManager(10);
	
//	/**
//	 * First currently visible record.
//	 */
//	private int current = 0;
//
//	/**
//	 * Number of visible records.
//	 */
//	private int step = 10;

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

//	/**
//	 * Current number of results.
//	 */
//	private Integer numberOfResults;

	/**
	 * Data for result table.
	 */
	private TableData data = new TableData();

	/**
	 * Data for detail voyage view.
	 */
	private TableData detailData = new TableData();

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
	 * voyage ID when claiming detail view.
	 */
	private Long detailVoyageId;

	/**
	 * Indication if query for detail voyage info is needed.
	 */
	private boolean needDetailQuery;

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
	private Conditions conditions = null;

	/**
	 * Map creator for detail voyage map.
	 */
	private DetailVoyageMap detailVoyageMap = new DetailVoyageMap();

	private DetailVoyageInfo[] detailVoyageInfo = new DetailVoyageInfo[] {};
	
	/**
	 * A list of actions that will be performed after apply configuration was pressed.
	 */
	private List actionsToPerform = new ArrayList();
	
	/**
	 * Constructor.
	 * 
	 */
	public TableResultTabBean() {

		// Setup default columns
		VisibleAttributeInterface[] attrs = new VisibleAttributeInterface[6];
		attrs[0] = VisibleAttribute.getAttributeForTable("voyageId");
		attrs[1] = VisibleAttribute.getAttributeForTable("shipname");
		attrs[2] = VisibleAttribute.getAttributeForTable("anycaptain");
		attrs[3] = VisibleAttribute.getAttributeForTable("yearam");
		attrs[4] = VisibleAttribute.getAttributeForTable("majbyimp");
		attrs[5] = VisibleAttribute.getAttributeForTable("majselpt");

		if (attrs[2] == null) {
			attrs[2] = VisibleAttribute.getAttributeForTable("captaina");
		}

		data.setVisibleColumns(attrs);
		this.visibleColumns = Arrays.asList(attrs);

		VisibleAttributeInterface[] additionalAttrs = new VisibleAttributeInterface[] { VoyageIndex.getVisibleAttribute("revisionId"),
				VoyageIndex.getVisibleAttribute("revisionDate") };
		detailData.setVisibleAdditionalColumns(additionalAttrs);
		detailData.setOrderByColumn(VisibleAttribute.getAttributeForTable("revisionId"));
		detailData.setOrder(QueryValue.ORDER_DESC);
		detailData.setFormatter(additionalAttrs[1],
				new SimpleDateAttributeFormatter(new SimpleDateFormat("yyyy-MM-dd")));
	}

	/**
	 * Queries DB for voyages if needed.
	 * 
	 */
	private void getResultsDB() {
		if (!this.searchBean.getSearchParameters().getConditions().equals(this.conditions)) {
			this.conditions = (Conditions) this.searchBean.getSearchParameters().getConditions().clone();
			needQuery = true;
		}
		if (this.searchBean.getSearchParameters().getConditions() != null && needQuery) {
			this.queryAndFillInData(VoyageIndex.getRecent(), this.data, this.linkManager.getCurrentFirstRecord(), this.linkManager.getStep(), false);
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
	private Object[][] queryAndFillInData(Conditions subCondition, TableData dataTable, int start, int length,
			boolean returnBasicInfo) {

		// Build condition
		Conditions localCond = (Conditions) this.searchBean.getSearchParameters().getConditions().clone();
		localCond.addCondition(subCondition);
		localCond.addCondition(VoyageIndex.getAttribute("remoteVoyageId"), new DirectValue(Voyage.getAttribute("iid")), Conditions.OP_EQUALS);
		
		// Build query
		QueryValue qValue = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCond);
		if (length != -1) {
			qValue.setLimit(length);
		}
		if (start != -1) {
			qValue.setFirstResult(start);
		}

		// Dictionaries - list of columns with dictionaries.
		dataTable.setKeyAttribute(Voyage.getAttribute("voyageId"));
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
			qValue.addPopulatedAttribute(Voyage.getAttribute("voyageId"));
			qValue.addPopulatedAttribute(Voyage.getAttribute("portdep"));
			qValue.addPopulatedAttribute(Voyage.getAttribute("portret"));
		}

		VisibleAttributeInterface vattr = dataTable.getOrderByColumn();
//		String orderByPrefix = null;
//		if (vattr != null && Arrays.asList(dataTable.getVisibleAdditionalAttributes()).contains(vattr)) {
//			orderByPrefix = "v.";
//		} else {
//			orderByPrefix = "v.voyage.";
//		}
		if (dataTable.getOrderByColumn() == null) {
			qValue.setOrderBy(new Attribute[] { Voyage.getAttribute("voyageId") });
		} else {

			Attribute[] attr = vattr.getAttributes();
//			if (vattr instanceof Attribute) {
//				attr = new Attribute[] { (Attribute) vattr };
//			} else if (vattr instanceof CompoundAttribute) {
//				CompoundAttribute cAttr = (CompoundAttribute) vattr;
//				List attrs = dataTable.getAttrForCAttribute(cAttr);
//				attr = (Attribute[]) attrs.toArray(new Attribute[] {});
//			}

			if (attr != null) {
				Attribute[] order = new Attribute[attr.length];
				for (int i = 0; i < attr.length; i++) {
					if (!(attr[i] instanceof DictionaryAttribute)) {
						order[i] = attr[i];
					} else {
						order[i] = new SequenceAttribute(new Attribute[] {attr[i], Dictionary.getAttribute("name")});
					}
				}
				qValue.setOrderBy(order);
				qValue.setOrder(dataTable.getOrder());
			}
		}

		// Execute query
		Object[] ret = qValue.executeQuery();
		dataTable.setData(ret);

		if (returnBasicInfo && ret.length > 0) {
			int len = ((Object[]) ret[0]).length;
			return new Object[][] {
					{ Voyage.getAttribute("voyageId"), Voyage.getAttribute("portdep"), Voyage.getAttribute("portret") },
					{ ((Object[]) ret[0])[len - 3], ((Object[]) ret[0])[len - 2], ((Object[]) ret[0])[len - 1] } };
		} else {
			return new Object[][] {};
		}
	}

	/**
	 * Queries DB for detail voyage info.
	 * 
	 */
	private void getResultsDetailDB() {
		if (this.needDetailQuery && this.detailVoyageId != null
				&& this.searchBean.getSearchParameters().getConditions() != null) {
			Conditions c = new Conditions();
			c.addCondition(VoyageIndex.getApproved());
			c.addCondition(Voyage.getAttribute("voyageId"), this.detailVoyageId, Conditions.OP_EQUALS);

			List validAttrs = new ArrayList();
			VisibleAttributeInterface[] attrs = VisibleAttribute.getAllAttributes();
			for (int i = 0; i < attrs.length; i++) {
				VisibleAttributeInterface column = attrs[i];
				validAttrs.add(column);
			}
			detailData.setVisibleColumns(validAttrs);

			Object[][] info = this.queryAndFillInData(c, this.detailData, -1, -1, true);

			this.detailVoyageInfo = new DetailVoyageInfo[info[0].length];
			for (int i = 0; i < info[0].length; i++) {
				this.detailVoyageInfo[i] = new DetailVoyageInfo((Attribute) info[0][i], info[1][i]);
			}

			this.needDetailQuery = false;

		}
	}

//	/**
//	 * Next result set action.
//	 * 
//	 * @return
//	 */
//	public String next() {
//		if (this.numberOfResults != null) {
//			if (current + step < this.numberOfResults.intValue()
//					&& this.searchBean.getSearchParameters().getConditions() != null) {
//				current += step;
//				this.needQuery = true;
//			}
//		}
//
//		this.getResultsDB();
//		return null;
//	}

//	/**
//	 * Previous result set action
//	 * 
//	 * @return
//	 */
//	public String prev() {
//		if (this.numberOfResults != null) {
//			if (current > 0 && this.searchBean.getSearchParameters().getConditions() != null) {
//				current -= step;
//				if (current < 0) {
//					current = 0;
//				}
//				this.needQuery = true;
//			}
//		}
//		this.getResultsDB();
//		return null;
//	}

	/**
	 * Removing of columns from table (Remove button)
	 * 
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
				needDetailQuery = true;
				
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
	 * Adding of attributes to table (Add button).
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
				this.needDetailQuery = true;
				
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
	 * Moves attributes up.
	 * 
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
					this.needDetailQuery = true;
					
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
	 * Moves attributes down
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
					this.needDetailQuery = true;
					
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
			case QueryValue.ORDER_ASC:
				this.data.setOrder(QueryValue.ORDER_DESC);
				break;
			case QueryValue.ORDER_DESC:
				this.data.setOrder(QueryValue.ORDER_DEFAULT);
				break;
			case QueryValue.ORDER_DEFAULT:
				this.data.setOrder(QueryValue.ORDER_ASC);
				break;
			}
		} else {
			this.data.setOrderByColumn(attr);
			this.data.setOrder(QueryValue.ORDER_ASC);
		}

		// Indicate need of query
		this.linkManager.setCurrentTab(0);
		this.needQuery = true;
	}

	/**
	 * Action invoked when show detail about voyage is requested
	 * 
	 * @param event
	 */
	public void showDetails(ShowDetailsEvent event) {
		this.configurationMode = new Boolean(false);
		this.detailMode = new Boolean(true);
		this.resultsMode = new Boolean(false);
		this.detailVoyageId = event.getVoyageId();
		this.needDetailQuery = true;
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
	
	public String refresh() {
		this.detailVoyageMap.refresh();
		return null;
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

//	/**
//	 * TODO Needed?
//	 * 
//	 * @return
//	 */
//	public Integer getCurrent() {
//		return new Integer(current);
//	}

//	public void setCurrent(Integer current) {
//		this.current = current.intValue();
//	}

//	/**
//	 * Gets current step
//	 * 
//	 * @return
//	 */
//	public String getStep() {
//		if (this.step == MAX_STEP) {
//			return "all";
//		} else {
//			return step + "";
//		}
//	}

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
		return new Integer(this.data.getData() != null ? this.data.getData().length : 0);
	}

	/**
	 * Sets current visible columns list.
	 * 
	 * @param list
	 */
	private void setVisibleAttributesList(List list) {
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			VisibleAttributeInterface element = (VisibleAttributeInterface) iter.next();
			if (element.getType().equals("DateAttribute")) {
				this.data.setFormatter(element, new SimpleDateAttributeFormatter(new SimpleDateFormat("yyyy-MM-dd")));
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
				res.add(new ComparableSelectItem("" + set.getId(), set.toString()));
			//}
		}
		if (this.selectedGroupSet == null && groupSets.length > 0) {
			this.selectedGroupSet = (groupSets[0]).getId().toString();
		}
		Collections.sort(res);
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
					res.add(new ComparableSelectItem(attr.encodeToString(), attr.toString()));
				//}
			}
		}

		Collections.sort(res);
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
			list.add(new ComparableSelectItem(element.encodeToString(), element.toString()));
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
	 * Gets detail voyage table data.
	 * 
	 * @return
	 */
	public TableData getDetailData() {
		this.getResultsDetailDB();
		return detailData;
	}

	/**
	 * Sets detail voyage data.
	 * 
	 * @param detailData
	 */
	public void setDetailData(TableData detailData) {
		this.detailData = detailData;
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
					TableResultTabBean.this.attachSearchedParams = (Boolean)this.params[0];
					setVisibleColumns();
				}
			});
			
			this.needQuery = true;
		}
	}

	private void setVisibleColumns() {
		List cols = new ArrayList(this.visibleColumns);
		if (attachSearchedParams.booleanValue()) {
			cols.addAll(Arrays.asList(this.searchBean.getSearchParameters().getColumns()));
		}
		setVisibleAttributesList(cols);
	}

	public String getMapPath() {

		this.detailVoyageMap.setVoyageId(this.detailVoyageId);

		if (this.detailVoyageMap.prepareMapFile()) {

			long time = System.currentTimeMillis();
			
			ExternalContext servletContext = FacesContext.getCurrentInstance().getExternalContext();
			((HttpSession) servletContext.getSession(true)).setAttribute(MAP_SESSION_KEY + time, this.detailVoyageMap
					.getCurrentMapFilePath());
			return MAP_SESSION_KEY + time;
		}
		return "";
	}

	public void setMapPath(String path) {
	}

	public PointOfInterest[] getPointsOfInterest() {
		return this.detailVoyageMap.getPointsOfInterest();
	}

	public DetailVoyageInfo[] getDetailVoyageInfo() {
		getResultsDetailDB();
		return this.detailVoyageInfo;
	}

	public SearchBean getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}
	
	public LegendItemsGroup[] getLegend() {
		return this.detailVoyageMap.getLegend();
	}
	
	public MapLayer[] getLayers() {
		return this.detailVoyageMap.getLayers();
	}

	/**
	 * Checks current number of results.
	 * 
	 */
	private void setNumberOfResults() {

		Conditions localCond = (Conditions) this.searchBean.getSearchParameters().getConditions().clone();
		localCond.addCondition(VoyageIndex.getRecent());
		localCond.addCondition(VoyageIndex.getAttribute("remoteVoyageId"), new DirectValue(Voyage.getAttribute("iid")), Conditions.OP_EQUALS);
		
		QueryValue qValue = new QueryValue(new String[] {"VoyageIndex", "Voyage"}, new String[] {"vi", "v"}, localCond);
		qValue.addPopulatedAttribute(new  FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("voyageId")}));
		Object[] ret = qValue.executeQuery();
		this.linkManager.setResultsNumber(((Integer) ret[0]).intValue());
	}

	public TableLinkManager getLinkManager() {
		return linkManager;
	}
}
