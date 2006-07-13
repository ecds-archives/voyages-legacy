package edu.emory.library.tas.web.components.tabs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.VoyageIndex;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.attrGroups.CompoundAttribute;
import edu.emory.library.tas.attrGroups.Group;
import edu.emory.library.tas.attrGroups.VisibleColumn;
import edu.emory.library.tas.attrGroups.formatters.SimpleDateAttributeFormatter;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;
import edu.emory.library.tas.web.SearchParameters;

/**
 * Backing bean for Table results presented in TAST web interface.
 * 
 * @author Pawel Jurczyk
 * 
 */
public class TableResultTabBean {

	private static final String ATTRIBUTE = "Attribute_";

	private static final String COMPOUND_ATTRIBUTE = "CompoundAttribute_";

	private static final String GROUP = "Group";

	private static final int MAX_STEP = 50000;

	/**
	 * First currently visible record.
	 */
	private int current = 0;

	/**
	 * Number of visible records.
	 */
	private int step = 10;

	/**
	 * Currently used conditions.
	 */
	private Conditions condition;

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
	 * List of columns added from query.
	 */
	private List queryColumns = new ArrayList();
	
	/**
	 * List of selected attributes to be added.
	 */
	private List selectedAttributeToAdd = new ArrayList();

	/**
	 * Current number of results.
	 */
	private Integer numberOfResults;

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
	 * Current category of attributes (Basic or General).
	 */
	private int category;

	/**
	 * Constructor.
	 *
	 */
	public TableResultTabBean() {

		//Setup default columns
		VisibleColumn[] attrs = new VisibleColumn[6];
		attrs[0] = Voyage.getAttribute("voyageId");
		attrs[1] = Voyage.getAttribute("shipname");
		attrs[2] = Voyage.getCoumpoundAttribute("anycaptain");
		attrs[3] = Voyage.getAttribute("yearam");
		attrs[4] = Voyage.getAttribute("majbyimp");
		attrs[5] = Voyage.getAttribute("majselpt");

		if (attrs[2] == null) {
			attrs[2] = Voyage.getAttribute("captaina");
		}

		data.setVisibleColumns(attrs);
		this.visibleColumns = Arrays.asList(attrs);
		
		
		detailData.setVisibleColumns(Voyage.getAttributes());

		VisibleColumn[] additionalAttrs = new VisibleColumn[] { VoyageIndex.getAttribute("revisionId"),
				VoyageIndex.getAttribute("revisionDate") };
		detailData.setVisibleAdditionalColumns(additionalAttrs);
		detailData.setOrderByColumn(VoyageIndex.getAttribute("revisionId"));
		detailData.setOrder(QueryValue.ORDER_DESC);
		detailData.setFormatter(additionalAttrs[1],
				new SimpleDateAttributeFormatter(new SimpleDateFormat("yyyy-MM-dd")));
	}

	/**
	 * Queries DB for voyages if needed.
	 *
	 */
	private void getResultsDB() {
		if (this.condition != null && this.componentVisible.booleanValue() && needQuery) {
			this.queryAndFillInData(VoyageIndex.getRecent(), this.data, this.getCurrent().intValue(), this.step);
			needQuery = false;
		}
	}

	/**
	 * Builds and executes query
	 * @param subCondition Conditions for query (for VoyageIndex object)
	 * @param dataTable place to store retrieved data
	 * @param start	first result
	 * @param length number of columns
	 */
	private void queryAndFillInData(Conditions subCondition, TableData dataTable, int start, int length) {

		//Build condition
		subCondition = subCondition.addAttributesPrefix("v.");
		Conditions localCond = (Conditions) this.condition.addAttributesPrefix("v.voyage.");
		localCond.addCondition(subCondition);

		//Build query
		QueryValue qValue = new QueryValue("VoyageIndex as v", localCond);
		if (length != -1) {
			qValue.setLimit(length);
		}
		if (start != -1) {
			qValue.setFirstResult(start);
		}
		Attribute[] populatedAttributes = dataTable.getAttributesForQuery();
		if (populatedAttributes != null) {
			for (int i = 0; i < populatedAttributes.length; i++) {
				qValue.addPopulatedAttribute("v.voyage." + populatedAttributes[i].getName(), populatedAttributes[i]
						.isDictinaory());
			}
		}

		//Add populated attributes
		Attribute[] populatedAdditionalAttributes = dataTable.getAdditionalAttributesForQuery();
		if (populatedAdditionalAttributes != null) {
			for (int i = 0; i < populatedAdditionalAttributes.length; i++) {
				qValue.addPopulatedAttribute("v." + populatedAdditionalAttributes[i].getName(),
						populatedAdditionalAttributes[i].isDictinaory());
			}
		}

		String orderByPrefix = null;
		if (populatedAdditionalAttributes != null && populatedAdditionalAttributes.length > 0) {
			orderByPrefix = "v.";
		} else {
			orderByPrefix = "v.voyage.";
		}
		if (dataTable.getOrderByColumn() == null) {
			qValue.setOrderBy(new String[] {"v.voyageId"});
		} else {
			VisibleColumn vattr = dataTable.getOrderByColumn();
			Attribute[] attr = null;
			if (vattr instanceof Attribute) {
				attr = new Attribute[] { (Attribute) vattr };
			} else if (vattr instanceof CompoundAttribute) {
				CompoundAttribute cAttr = (CompoundAttribute) vattr;
				List attrs = dataTable.getAttrForCAttribute(cAttr);
				attr = (Attribute[]) attrs.toArray(new Attribute[] {});
			}

			if (attr != null) {
				String[] order = new String[attr.length];
				for (int i = 0; i < attr.length; i++) {
					if (!attr[i].isDictinaory()) {
						order[i] = orderByPrefix + attr[i].getName();
					} else {
						order[i] = orderByPrefix + attr[i].getName() + ".name";
					}
				}
				qValue.setOrderBy(order);
				qValue.setOrder(dataTable.getOrder());
			}
		}

		//Execute query
		dataTable.setData(qValue.executeQuery());

	}

	/**
	 * Queries DB for detail voyage info.
	 *
	 */
	private void getResultsDetailDB() {
		if (this.needDetailQuery && this.detailVoyageId != null && this.condition != null) {
			Conditions c = new Conditions();
			c.addCondition(VoyageIndex.getApproved());
			c.addCondition("voyageId", this.detailVoyageId, Conditions.OP_EQUALS);
			this.queryAndFillInData(c, this.detailData, -1, -1);
			this.needDetailQuery = false;
		}
	}

	/**
	 * Checks current number of results.
	 *
	 */
	private void setNumberOfResults() {

		Conditions localCond = (Conditions) this.condition.addAttributesPrefix("v.voyage.");
		localCond.addCondition(VoyageIndex.getRecent());

		QueryValue qValue = new QueryValue("VoyageIndex as v", localCond);
		qValue.addPopulatedAttribute("count(v.voyageId)", false);
		Object[] ret = qValue.executeQuery();
		this.numberOfResults = (Integer) ret[0];
	}

	
	/**
	 * Next result set action.
	 * @return
	 */
	public String next() {
		if (this.numberOfResults != null) {
			if (current + step < this.numberOfResults.intValue() && this.condition != null) {
				current += step;
				this.needQuery = true;
			}
		}

		this.getResultsDB();
		return null;
	}

	/**
	 * Previous result set action
	 * @return
	 */
	public String prev() {
		if (this.numberOfResults != null) {
			if (current > 0 && this.condition != null) {
				current -= step;
				if (current < 0) {
					current = 0;
				}
				this.needQuery = true;
			}
		}
		this.getResultsDB();
		return null;
	}

	/**
	 * Removing of columns from table (Remove button)
	 * @return
	 */
	public String remSelectedAttributeFromList() {
		if (this.selectedAttributeAdded == null) {
			return null;
		}

		//Find and remove attributes
		for (Iterator iter = this.selectedAttributeAdded.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			VisibleColumn attr = this.getVisibleAttribute(element);
			List list = new ArrayList(this.visibleColumns);
			if (list.contains(attr)) {
				list.remove(attr);
				this.visibleColumns = list;
				this.setVisibleColumns();
				this.needQuery = true;
				this.needDetailQuery = true;
			}
		}
		return null;
	}

	/**
	 * Adding of attributes to table (Add button).  
	 * @return
	 */
	public String addSelectedAttributeToList() {
		if (this.selectedAttributeToAdd == null) {
			return null;
		}

		//For each of attributes - check if not already in columns set and if not - add it
		for (Iterator iter = this.selectedAttributeToAdd.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			VisibleColumn attr = this.getVisibleAttribute(element);
			VisibleColumn[] attrs = (VisibleColumn[])this.visibleColumns.toArray(new VisibleColumn[] {});
			boolean is = false;
			for (int i = 0; i < attrs.length; i++) {
				if (attrs[i].getId().equals(attr.getId())) {
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
			}
		}
		return null;
	}

	/**
	 * Moves attributes up.
	 * @return
	 */
	public String moveAttrUp() {
		if (this.selectedAttributeAdded == null) {
			return null;
		}

		//Move attributes one position up
		for (Iterator iter = this.selectedAttributeAdded.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			VisibleColumn attr = this.getVisibleAttribute(element);
			VisibleColumn[] attrs = (VisibleColumn[])this.visibleColumns.toArray(new VisibleColumn[] {});
			for (int i = 1; i < attrs.length; i++) {
				if (attrs[i].getId().equals(attr.getId())) {
					VisibleColumn tmp = attrs[i];
					attrs[i] = attrs[i - 1];
					attrs[i - 1] = tmp;
					this.needQuery = true;
					this.needDetailQuery = true;
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
	 * @return
	 */
	public String moveAttrDown() {
		if (this.selectedAttributeAdded == null) {
			return null;
		}

		//Move each attribute one position down
		for (Iterator iter = this.selectedAttributeAdded.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			VisibleColumn attr = this.getVisibleAttribute(element);
			VisibleColumn[] attrs = (VisibleColumn[])this.visibleColumns.toArray(new VisibleColumn[] {});
			for (int i = 0; i < attrs.length - 1; i++) {
				if (attrs[i].getId().equals(attr.getId())) {
					VisibleColumn tmp = attrs[i];
					attrs[i] = attrs[i + 1];
					attrs[i + 1] = tmp;
					this.needQuery = true;
					this.needDetailQuery = true;
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
	 * @param event
	 */
	public void sortChanged(SortChangeEvent event) {
		String attrToSort = event.getAttributeSort();

		//Get column that will be sorted
		VisibleColumn attr = this.getVisibleAttribute(attrToSort);

		//Set appropriate order
		if (this.data.getOrderByColumn().getId().equals(attr.getId())) {
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
		
		//Indicate need of query
		this.current = 0;
		this.needQuery = true;
	}

	/**
	 * Action invoked when show detail about voyage is requested
	 * @param event
	 */
	public void showDetails(ShowDetailsEvent event) {
		this.configurationMode = new Boolean(false);
		this.detailMode = new Boolean(true);
		this.resultsMode = new Boolean(false);
		this.detailVoyageId = event.getVoyageId();
		this.needDetailQuery = true;
	}

	
	/**
	 * Gets VisibleColumn object from given string representation
	 * @param sAttr
	 * @return
	 */
	private VisibleColumn getVisibleAttribute(String sAttr) {
		VisibleColumn ret = null;
		if (sAttr.startsWith(ATTRIBUTE)) {
			//Attribute_#####
			String attrId = sAttr.substring(ATTRIBUTE.length(), sAttr.length());
			Conditions c = new Conditions();
			c.addCondition("id", new Long(attrId), Conditions.OP_EQUALS);
			QueryValue qValue = new QueryValue("Attribute", c);
			Object[] attrs = qValue.executeQuery();
			if (attrs.length > 0) {
				ret = (VisibleColumn) attrs[0];
			}
		} else if (sAttr.startsWith(COMPOUND_ATTRIBUTE)) {
			//CompoundAttribute_#####
			String attrId = sAttr.substring(COMPOUND_ATTRIBUTE.length(), sAttr.length());
			Conditions c = new Conditions();
			c.addCondition("id", new Long(attrId), Conditions.OP_EQUALS);
			QueryValue qValue = new QueryValue("CompoundAttribute", c);
			Object[] attrs = qValue.executeQuery();
			if (attrs.length > 0) {
				ret = (VisibleColumn) attrs[0];
			}
		} else if (sAttr.startsWith(GROUP)) {
			//Group_#####
			String attrId = sAttr.substring(GROUP.length(), sAttr.length());
			Conditions c = new Conditions();
			c.addCondition("id", new Long(attrId), Conditions.OP_EQUALS);
			QueryValue qValue = new QueryValue("Group", c);
			Object[] attrs = qValue.executeQuery();
			if (attrs.length > 0) {
				ret = (VisibleColumn) attrs[0];
			}
		}
		return ret;
	}

	/**
	 * Gets number of first record displayed in current table view.
	 * @return
	 */
	public Integer getFirstDisplayed() {
		if (numberOfResults == null || numberOfResults.intValue() == 0)
			return new Integer(0);
		else
			return new Integer(current + 1);
	}

	/**
	 * Gets last record displayed in current table view.
	 * @return
	 */
	public Integer getLastDisplayed() {
		if (numberOfResults == null || numberOfResults.intValue() == 0)
			return new Integer(0);
		else
			return new Integer(current + 1 + (this.data.getData() != null ? this.data.getData().length - 1 : 0));
	}

	/**
	 * Gets total number of rows.
	 * @return
	 */
	public Integer getTotalRows() {
		if (numberOfResults == null)
			return new Integer(0);
		else
			return numberOfResults;
	}

	/**
	 * TODO Needed? 
	 * @return
	 */
	public Integer getCurrent() {
		return new Integer(current);
	}
	public void setCurrent(Integer current) {
		this.current = current.intValue();
	}

	/**
	 * Gets current step
	 * @return
	 */
	public String getStep() {
		if (this.step == MAX_STEP) {
			return "all";
		} else {
			return step + "";
		}
	}

	/**
	 * Sets current step
	 * @param step
	 */
	public void setStep(String step) {
		if (step.equals("all")) {
			if (this.step != MAX_STEP) {
				this.needQuery = true;
			}
			this.step = MAX_STEP;
			return;
		}
		if (this.step != new Integer(step).intValue()) {
			this.needQuery = true;
		}
		this.step = new Integer(step).intValue();
	}

	/**
	 * Gets current TableData
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
	 * @param size
	 */
	public void setResultSize(Integer size) {
	}

	/**
	 * Gets size of results.
	 * @return
	 */
	public Integer getResultSize() {
		return new Integer(this.data.getData() != null ? this.data.getData().length : 0);
	}

	/**
	 * Sets current query parameters
	 * @param params
	 */
	public void setConditions(SearchParameters params) {
		if (params == null) {
			return;
		}
		this.category = params.getCategory();
		
		Conditions c = params.getConditions();
		this.queryColumns = Arrays.asList(params.getColumns());
		if (c != null) {
			System.out.println("1: --------------------------------------");
			System.out.println(c.getConditionHQL().conditionString);
		}
		if (c == null) {
			// needQuery = false;
		} else if (c.equals(condition)) {
			// needQuery = false;
		} else {
			List list = new ArrayList(this.visibleColumns);
			if (this.attachSearchedParams.booleanValue()) {
				list.addAll(this.queryColumns);
			}
			setVisibleAttributesList(list);
			condition = c;
			needQuery = true;
			this.needDetailQuery = true;
			this.setNumberOfResults();
			this.current = 0;
		}
	}

	/**
	 * Sets current visible columns list.
	 * @param list
	 */
	private void setVisibleAttributesList(List list) {
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			VisibleColumn element = (VisibleColumn) iter.next();
			if (element.getType().intValue() == Attribute.TYPE_DATE) {
				this.data.setFormatter(element, new SimpleDateAttributeFormatter(new SimpleDateFormat("yyyy-MM-dd")));
			}
		} {
			
		}
		this.data.setVisibleColumns(list);
	}

	/**
	 * Checks whether component is currently visible.
	 * @return
	 */
	public Boolean getComponentVisible() {
		return componentVisible;
	}

	/**
	 * Sets visibility of current component.
	 * @param componentVisible
	 */
	public void setComponentVisible(Boolean componentVisible) {
		this.componentVisible = componentVisible;
	}

	/**
	 * Checks if configuration mode is enabled.
	 * @return
	 */
	public Boolean getConfigurationMode() {
		return this.configurationMode;
	}

	/**
	 * Checks if results mode is enabled.
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
		this.configurationMode = new Boolean(false);
		this.resultsMode = new Boolean(true);
		this.detailMode = new Boolean(false);
	}

	/**
	 * Gets attribute groups that are available.
	 * @return
	 */
	public List getAvailableGroupSets() {
		ArrayList res = new ArrayList();
		Group[] groupSets = Voyage.getGroups();
		for (int i = 0; i < groupSets.length; i++) {
			Group set = (Group) groupSets[i];
			if (set.noOfAttributesInCategory(this.category) > 0 ||
					set.noOfCompoundAttributesInCategory(this.category) > 0) {
				res.add(new ComparableSelectItem("" + set.getId().longValue(), set.toString()));
			}
		}
		if (this.selectedGroupSet == null && groupSets.length > 0) {
			this.selectedGroupSet = ((Group) groupSets[0]).getId().toString();
		}
		Collections.sort(res);
		return res;
	}

	/**
	 * Gets available attributes for chosen group.
	 * @return
	 */
	public List getAvailableAttributes() {
		ArrayList res = new ArrayList();
		Conditions c = new Conditions();
		if (this.selectedGroupSet != null) {
			c.addCondition("id", new Long(this.selectedGroupSet), Conditions.OP_EQUALS);
		}
		QueryValue qValue = new QueryValue("Group", c);
		// qValue.setCacheable(true);

		//Query for attributes of group
		Object[] groupSets = qValue.executeQuery();
		if (groupSets.length > 0) {
			Group set = (Group) groupSets[0];
			Set attrs = set.getAttributes();

			Set groups = set.getCompoundAttributes();
			for (Iterator groupsIter = groups.iterator(); groupsIter.hasNext();) {
				CompoundAttribute element = (CompoundAttribute) groupsIter.next();
				if (element.isVisibleByCategory(this.category)) {
					res.add(new ComparableSelectItem(element.encodeToString(), element.toString()));
				}
			}
			for (Iterator iter = attrs.iterator(); iter.hasNext();) {
				Attribute attr = (Attribute) iter.next();
				if (attr.isVisibleByCategory(this.category)) {
					res.add(new ComparableSelectItem(attr.encodeToString(), attr.toString()));
				}
			}
		}
		
		Collections.sort(res);
		return res;
	}

	/**
	 * Gets currently visible columns (according to configuration).
	 * @return
	 */
	public List getVisibleAttributes() {
		List list = new ArrayList();
//		VisibleColumn[] cols = this.data.getVisibleAttributes();
		for (Iterator iter = this.visibleColumns.iterator(); iter.hasNext();) {
			VisibleColumn element = (VisibleColumn) iter.next();			
			list.add(new ComparableSelectItem(element.encodeToString(), element.toString()));
		}
		return list;
	}

	/**
	 * Gets currently selected group.
	 * @return
	 */
	public String getSelectedGroupSet() {
		return selectedGroupSet;
	}

	/**
	 * Sets currently selected group.
	 * @param selectedGroupSet
	 */
	public void setSelectedGroupSet(String selectedGroupSet) {
		this.selectedGroupSet = selectedGroupSet;
	}

	/**
	 * Gets currently selected columns (in configuration).
	 * @return
	 */
	public List getSelectedAttributeAdded() {
		return selectedAttributeAdded;
	}

	/**
	 * Sets currently selected columns (in configuration).
	 * @param selectedAttributeAdded
	 */
	public void setSelectedAttributeAdded(List selectedAttributeAdded) {
		this.selectedAttributeAdded = selectedAttributeAdded;
	}

	/**
	 * Gets selected attributes in list "to add".
	 * @return
	 */
	public List getSelectedAttributeToAdd() {
		return selectedAttributeToAdd;
	}

	/**
	 * Sets selected attributes from list "to add".
	 * @param selectedAttributeToAdd
	 */
	public void setSelectedAttributeToAdd(List selectedAttributeToAdd) {
		this.selectedAttributeToAdd = selectedAttributeToAdd;
	}

	/**
	 * Gets number of results.
	 * @return
	 */
	public Integer getNumberOfResults() {
		return numberOfResults;
	}

	/**
	 * Gets detail voyage table data.
	 * @return
	 */
	public TableData getDetailData() {
		this.getResultsDetailDB();
		return detailData;
	}

	/**
	 * Sets detail voyage data.
	 * @param detailData
	 */
	public void setDetailData(TableData detailData) {
		this.detailData = detailData;
	}

	/**
	 * Checks if detail mode is enabled.
	 * @return
	 */
	public Boolean getDetailMode() {
		return detailMode;
	}

	/**
	 * Sets detail mode.
	 * @param detailMode
	 */
	public void setDetailMode(Boolean detailMode) {
		this.detailMode = detailMode;
	}

	/**
	 * Checks if attributes from query should be attached to results.
	 * @return
	 */
	public Boolean getAttachSearchedParams() {
		return attachSearchedParams;
	}

	/**
	 * Sets if attributes from query should be attached to results.
	 * @param attachSearchedParams
	 */
	public void setAttachSearchedParams(Boolean attachSearchedParams) {
		if (!this.attachSearchedParams.equals(attachSearchedParams)) {
			this.attachSearchedParams = attachSearchedParams;
			this.setVisibleColumns();
			this.needQuery = true;
		}
	}
	
	private void setVisibleColumns() {
		List cols = new ArrayList(this.visibleColumns);
		if (attachSearchedParams.booleanValue()) {				
			cols.addAll(this.queryColumns);
		}
		setVisibleAttributesList(cols);
	}
}
