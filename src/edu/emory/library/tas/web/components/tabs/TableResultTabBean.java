package edu.emory.library.tas.web.components.tabs;

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
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;

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

	private int current = 0;

	private int step = 10;

	private Conditions condition;

	private Boolean componentVisible = new Boolean(false);

	private boolean needQuery = false;

	private String selectedGroupSet = null;

	private String selectedAttributeAdded;

	private String selectedAttributeToAdd;

	private Integer numberOfResults;

	private TableData data = new TableData();

	private TableData detailData = new TableData();

	private Boolean detailMode = new Boolean(false);

	private Boolean configurationMode = new Boolean(false);

	private Boolean resultsMode = new Boolean(true);

	private Long detailVoyageId;

	private boolean needDetailQuery;

	public TableResultTabBean() {

		Attribute[] attrs = new Attribute[6];
		attrs[0] = Voyage.getAttribute("voyageId");
		attrs[1] = Voyage.getAttribute("shipname");
		attrs[2] = Voyage.getAttribute("captaina");
		attrs[3] = Voyage.getAttribute("captainb");
		attrs[4] = Voyage.getAttribute("captainc");
		attrs[5] = Voyage.getAttribute("portdep");
		data.setVisibleColumns(attrs);
		
		detailData.setVisibleColumns(Voyage.getAttributes());

	}

	private void getResultsDB() {
		if (this.condition != null && this.componentVisible.booleanValue()
				&& needQuery) {
			this.queryAndFillInData(VoyageIndex.getRecent(), this.data);
			needQuery = false;
		}
	}

	private void queryAndFillInData(Conditions subCondition, TableData dataTable) {

		subCondition = subCondition.addAttributesPrefix("v.");
		Conditions localCond = (Conditions) this.condition
				.addAttributesPrefix("v.voyage.");
		localCond.addCondition(subCondition);

		QueryValue qValue = new QueryValue("VoyageIndex as v", localCond);
		qValue.setLimit(this.getStep().intValue());
		qValue.setFirstResult(this.getCurrent().intValue());
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
						order.append("v.voyage." + attr[i].getName());
					} else {
						order.append("v.voyage." + attr[i].getName() + ".name");
					}
					if (i < attr.length - 1) {
						order.append(", ");
					}
					qValue.setOrderBy(order.toString());
					qValue.setOrder(dataTable.getOrder());
				}
			}
		}

		Attribute[] populatedAttributes = dataTable.getAttributesForQuery();
		if (populatedAttributes != null) {
			for (int i = 0; i < populatedAttributes.length; i++) {
				qValue.addPopulatedAttribute("v.voyage."
						+ populatedAttributes[i].getName(),
						populatedAttributes[i].isDictinaory());
			}
		}

		dataTable.setData(qValue.executeQuery());

	}

	private void getResultsDetailDB() {
		if (this.needDetailQuery && this.detailVoyageId != null) {
			Conditions c = new Conditions();
			c.addCondition(VoyageIndex.getApproved());
			c.addCondition("voyageId", this.detailVoyageId, Conditions.OP_EQUALS);
			this.queryAndFillInData(c, this.detailData);
			this.needDetailQuery = false;
		}
	}

	private void setNumberOfResults() {

		Conditions localCond = (Conditions) this.condition
				.addAttributesPrefix("v.voyage.");
		localCond.addCondition(VoyageIndex.getRecent());

		QueryValue qValue = new QueryValue("VoyageIndex as v", localCond);
		qValue.addPopulatedAttribute("count(v.voyageId)", false);
		Object[] ret = qValue.executeQuery();
		this.numberOfResults = (Integer) ret[0];
	}

	/** ACTIONS called from web interface * */

	public String next() {
		if (this.numberOfResults != null) {
			if (current + step < this.numberOfResults.intValue()
					&& this.condition != null) {
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
		if (this.selectedAttributeToAdd == null) {
			return null;
		}

		VisibleColumn attr = this
				.getVisibleAttribute(this.selectedAttributeToAdd);

		List list = Arrays.asList(this.data.getVisibleAttributes());
		if (list.contains(attr)) {
			list = new ArrayList(list);
			list.remove(attr);
			this.data.setVisibleColumns(list);
			this.needQuery = true;
			this.needDetailQuery = true;
		}
		return null;
	}

	public String addSelectedAttributeToList() {
		if (this.selectedAttributeToAdd == null) {
			return null;
		}

		VisibleColumn attr = this
				.getVisibleAttribute(this.selectedAttributeToAdd);
		VisibleColumn[] attrs = this.data.getVisibleAttributes();

		boolean is = false;

		for (int i = 0; i < attrs.length; i++) {
			if (attrs[i].getId().equals(attr.getId())) {
				is = true;
			}
		}
		if (!is) {
			List list = Arrays.asList(attrs);
			list = new ArrayList(list);
			list.add(attr);
			this.data.setVisibleColumns(list);
			this.needQuery = true;
			this.needDetailQuery = true;
		}
		return null;
	}

	public String moveAttrUp() {
		if (this.selectedAttributeAdded == null) {
			return null;
		}
		VisibleColumn attr = this
				.getVisibleAttribute(this.selectedAttributeAdded);
		VisibleColumn[] attrs = data.getVisibleAttributes();
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
		return null;
	}

	public String moveAttrDown() {
		if (this.selectedAttributeAdded == null) {
			return null;
		}
		VisibleColumn attr = this
				.getVisibleAttribute(this.selectedAttributeAdded);
		VisibleColumn[] attrs = data.getVisibleAttributes();
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
			String attrId = sAttr.substring(COMPOUND_ATTRIBUTE.length(), sAttr
					.length());
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
			return new Integer(
					current
							+ 1
							+ (this.data.getData() != null ? this.data
									.getData().length - 1 : 0));
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

	public Integer getStep() {
		return new Integer(step);
	}

	public void setStep(Integer step) {
		if (this.step != step.intValue()) {
			this.needQuery = true;
		}
		this.step = step.intValue();
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
		return new Integer(
				this.data.getData() != null ? this.data.getData().length : 0);
	}

	public void setConditions(Conditions c) {
		if (c != null) {
			System.out.println("1: --------------------------------------");
			System.out.println(c.getConditionHQL().conditionString);
		}
		if (c == null) {
			// needQuery = false;
		} else if (c.equals(condition)) {
			// needQuery = false;
		} else {
			condition = c;
			needQuery = true;
			this.needDetailQuery = true;
			this.setNumberOfResults();
			this.current = 0;
		}
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
			res.add(new ComparableSelectItem("" + set.getId().longValue(), set
					.toString()));
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
			c.addCondition("id", new Long(this.selectedGroupSet),
					Conditions.OP_EQUALS);
		}
		QueryValue qValue = new QueryValue("Group", c);
		// qValue.setCacheable(true);

		Object[] groupSets = qValue.executeQuery();
		if (groupSets.length > 0) {
			Group set = (Group) groupSets[0];
			Set attrs = set.getAttributes();

			Set groups = set.getCompoundAttributes();
			for (Iterator groupsIter = groups.iterator(); groupsIter.hasNext();) {
				CompoundAttribute element = (CompoundAttribute) groupsIter
						.next();
				res.add(new ComparableSelectItem(element.encodeToString(),
						element.toString()));
			}
			for (Iterator iter = attrs.iterator(); iter.hasNext();) {
				Attribute attr = (Attribute) iter.next();
				res.add(new ComparableSelectItem(attr.encodeToString(), attr
						.toString()));
			}
		}
		Collections.sort(res);
		return res;
	}

	public List getVisibleAttributes() {
		List list = new ArrayList();
		VisibleColumn[] cols = this.data.getVisibleAttributes();
		for (int i = 0; i < cols.length; i++) {
			list.add(new ComparableSelectItem(cols[i].encodeToString(), cols[i]
					.toString()));
		}
		return list;
	}

	public String getSelectedGroupSet() {
		return selectedGroupSet;
	}

	public void setSelectedGroupSet(String selectedGroupSet) {
		this.selectedGroupSet = selectedGroupSet;
	}

	public String getSelectedAttributeAdded() {
		return selectedAttributeAdded;
	}

	public void setSelectedAttributeAdded(String selectedAttributeAdded) {
		this.selectedAttributeAdded = selectedAttributeAdded;
	}

	public String getSelectedAttributeToAdd() {
		return selectedAttributeToAdd;
	}

	public void setSelectedAttributeToAdd(String selectedAttributeToAdd) {
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
}
