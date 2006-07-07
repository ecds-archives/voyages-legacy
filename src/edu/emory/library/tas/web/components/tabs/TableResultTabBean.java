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

	private int current = 0;

	private int step = 10;

	private Conditions condition;

	private Boolean componentVisible = new Boolean(false);

	private boolean needQuery = false;

	private String selectedGroupSet = null;

	private List selectedAttributeAdded = new ArrayList();
	
	private List visibleColumns = new ArrayList();

	private List queryColumns = new ArrayList();
	
	private List selectedAttributeToAdd = new ArrayList();

	private Integer numberOfResults;

	private TableData data = new TableData();

	private TableData detailData = new TableData();

	private Boolean detailMode = new Boolean(false);

	private Boolean configurationMode = new Boolean(false);

	private Boolean resultsMode = new Boolean(true);

	private Long detailVoyageId;

	private boolean needDetailQuery;
	
	private Boolean attachSearchedParams = new Boolean(true);

	public TableResultTabBean() {

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

	private void getResultsDB() {
		if (this.condition != null && this.componentVisible.booleanValue() && needQuery) {
			this.queryAndFillInData(VoyageIndex.getRecent(), this.data, this.getCurrent().intValue(), this.step);
			needQuery = false;
		}
	}

	private void queryAndFillInData(Conditions subCondition, TableData dataTable, int start, int length) {

		subCondition = subCondition.addAttributesPrefix("v.");
		Conditions localCond = (Conditions) this.condition.addAttributesPrefix("v.voyage.");
		localCond.addCondition(subCondition);

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
			qValue.setOrderBy("v.voyageId");
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
				StringBuffer order = new StringBuffer();
				for (int i = 0; i < attr.length; i++) {
					if (!attr[i].isDictinaory()) {
						order.append(orderByPrefix + attr[i].getName());
					} else {
						order.append(orderByPrefix + attr[i].getName() + ".name");
					}
					if (i < attr.length - 1) {
						order.append(", ");
					}
				}
				qValue.setOrderBy(order.toString());
				qValue.setOrder(dataTable.getOrder());
			}
		}

		dataTable.setData(qValue.executeQuery());

	}

	private void getResultsDetailDB() {
		if (this.needDetailQuery && this.detailVoyageId != null && this.condition != null) {
			Conditions c = new Conditions();
			c.addCondition(VoyageIndex.getApproved());
			c.addCondition("voyageId", this.detailVoyageId, Conditions.OP_EQUALS);
			this.queryAndFillInData(c, this.detailData, -1, -1);
			this.needDetailQuery = false;
		}
	}

	private void setNumberOfResults() {

		Conditions localCond = (Conditions) this.condition.addAttributesPrefix("v.voyage.");
		localCond.addCondition(VoyageIndex.getRecent());

		QueryValue qValue = new QueryValue("VoyageIndex as v", localCond);
		qValue.addPopulatedAttribute("count(v.voyageId)", false);
		Object[] ret = qValue.executeQuery();
		this.numberOfResults = (Integer) ret[0];
	}

	/** ACTIONS called from web interface * */

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

	public String remSelectedAttributeFromList() {
		if (this.selectedAttributeAdded == null) {
			return null;
		}

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

	public String addSelectedAttributeToList() {
		if (this.selectedAttributeToAdd == null) {
			return null;
		}

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

	public String moveAttrUp() {
		if (this.selectedAttributeAdded == null) {
			return null;
		}

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

	public String moveAttrDown() {
		if (this.selectedAttributeAdded == null) {
			return null;
		}

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

	public void sortChanged(SortChangeEvent event) {
		String attrToSort = event.getAttributeSort();

		VisibleColumn attr = this.getVisibleAttribute(attrToSort);

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
		this.current = 0;
		this.needQuery = true;
	}

	public void showDetails(ShowDetailsEvent event) {
		this.configurationMode = new Boolean(false);
		this.detailMode = new Boolean(true);
		this.resultsMode = new Boolean(false);
		this.detailVoyageId = event.getVoyageId();
		this.needDetailQuery = true;
	}

	/** GETTERS / SETTERS * */

	private VisibleColumn getVisibleAttribute(String sAttr) {
		VisibleColumn ret = null;
		if (sAttr.startsWith(ATTRIBUTE)) {
			String attrId = sAttr.substring(ATTRIBUTE.length(), sAttr.length());
			Conditions c = new Conditions();
			c.addCondition("id", new Long(attrId), Conditions.OP_EQUALS);
			QueryValue qValue = new QueryValue("Attribute", c);
			Object[] attrs = qValue.executeQuery();
			if (attrs.length > 0) {
				ret = (VisibleColumn) attrs[0];
			}
		} else if (sAttr.startsWith(COMPOUND_ATTRIBUTE)) {
			String attrId = sAttr.substring(COMPOUND_ATTRIBUTE.length(), sAttr.length());
			Conditions c = new Conditions();
			c.addCondition("id", new Long(attrId), Conditions.OP_EQUALS);
			QueryValue qValue = new QueryValue("CompoundAttribute", c);
			Object[] attrs = qValue.executeQuery();
			if (attrs.length > 0) {
				ret = (VisibleColumn) attrs[0];
			}
		} else if (sAttr.startsWith(GROUP)) {
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

	public Integer getFirstDisplayed() {
		if (numberOfResults == null || numberOfResults.intValue() == 0)
			return new Integer(0);
		else
			return new Integer(current + 1);
	}

	public Integer getLastDisplayed() {
		if (numberOfResults == null || numberOfResults.intValue() == 0)
			return new Integer(0);
		else
			return new Integer(current + 1 + (this.data.getData() != null ? this.data.getData().length - 1 : 0));
	}

	public Integer getTotalRows() {
		if (numberOfResults == null)
			return new Integer(0);
		else
			return numberOfResults;
	}

	public Integer getCurrent() {
		return new Integer(current);
	}

	public void setCurrent(Integer current) {
		this.current = current.intValue();
	}

	public String getStep() {
		if (this.step == MAX_STEP) {
			return "all";
		} else {
			return step + "";
		}
	}

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

	public TableData getData() {
		this.getResultsDB();
		return this.data;
	}

	public void setData(Object data) {
	}

	public void setResultSize(Integer size) {
	}

	public Integer getResultSize() {
		return new Integer(this.data.getData() != null ? this.data.getData().length : 0);
	}

	public void setConditions(SearchParameters params) {
		if (params == null) {
			return;
		}
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

	public Boolean getComponentVisible() {
		return componentVisible;
	}

	public void setComponentVisible(Boolean componentVisible) {
		this.componentVisible = componentVisible;
	}

	public Boolean getConfigurationMode() {
		return this.configurationMode;
	}

	public Boolean getResultsMode() {
		return this.resultsMode;
	}

	public void configurationMode() {
		this.configurationMode = new Boolean(true);
		this.resultsMode = new Boolean(false);
		this.detailMode = new Boolean(false);
	}

	public void resultsMode() {
		this.configurationMode = new Boolean(false);
		this.resultsMode = new Boolean(true);
		this.detailMode = new Boolean(false);
	}

	public List getAvailableGroupSets() {
		ArrayList res = new ArrayList();
		Group[] groupSets = Voyage.getGroups();
		for (int i = 0; i < groupSets.length; i++) {
			Group set = (Group) groupSets[i];
			res.add(new ComparableSelectItem("" + set.getId().longValue(), set.toString()));
		}
		if (this.selectedGroupSet == null && groupSets.length > 0) {
			this.selectedGroupSet = ((Group) groupSets[0]).getId().toString();
		}
		Collections.sort(res);
		return res;
	}

	public List getAvailableAttributes() {
		ArrayList res = new ArrayList();
		Conditions c = new Conditions();
		if (this.selectedGroupSet != null) {
			c.addCondition("id", new Long(this.selectedGroupSet), Conditions.OP_EQUALS);
		}
		QueryValue qValue = new QueryValue("Group", c);
		// qValue.setCacheable(true);

		Object[] groupSets = qValue.executeQuery();
		if (groupSets.length > 0) {
			Group set = (Group) groupSets[0];
			Set attrs = set.getAttributes();

			Set groups = set.getCompoundAttributes();
			for (Iterator groupsIter = groups.iterator(); groupsIter.hasNext();) {
				CompoundAttribute element = (CompoundAttribute) groupsIter.next();
				res.add(new ComparableSelectItem(element.encodeToString(), element.toString()));
			}
			for (Iterator iter = attrs.iterator(); iter.hasNext();) {
				Attribute attr = (Attribute) iter.next();
				res.add(new ComparableSelectItem(attr.encodeToString(), attr.toString()));
			}
		}
		Collections.sort(res);
		return res;
	}

	public List getVisibleAttributes() {
		List list = new ArrayList();
//		VisibleColumn[] cols = this.data.getVisibleAttributes();
		for (Iterator iter = this.visibleColumns.iterator(); iter.hasNext();) {
			VisibleColumn element = (VisibleColumn) iter.next();			
			list.add(new ComparableSelectItem(element.encodeToString(), element.toString()));
		}
		return list;
	}

	public String getSelectedGroupSet() {
		return selectedGroupSet;
	}

	public void setSelectedGroupSet(String selectedGroupSet) {
		this.selectedGroupSet = selectedGroupSet;
	}

	public List getSelectedAttributeAdded() {
		return selectedAttributeAdded;
	}

	public void setSelectedAttributeAdded(List selectedAttributeAdded) {
		this.selectedAttributeAdded = selectedAttributeAdded;
	}

	public List getSelectedAttributeToAdd() {
		return selectedAttributeToAdd;
	}

	public void setSelectedAttributeToAdd(List selectedAttributeToAdd) {
		this.selectedAttributeToAdd = selectedAttributeToAdd;
	}

	public Integer getNumberOfResults() {
		return numberOfResults;
	}

	public TableData getDetailData() {
		this.getResultsDetailDB();
		return detailData;
	}

	public void setDetailData(TableData detailData) {
		this.detailData = detailData;
	}

	public Boolean getDetailMode() {
		return detailMode;
	}

	public void setDetailMode(Boolean detailMode) {
		this.detailMode = detailMode;
	}

	public Boolean getAttachSearchedParams() {
		return attachSearchedParams;
	}

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
