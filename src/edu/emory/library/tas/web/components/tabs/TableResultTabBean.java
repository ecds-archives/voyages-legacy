package edu.emory.library.tas.web.components.tabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.VoyageIndex;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.attrGroups.CompoundAttribute;
import edu.emory.library.tas.attrGroups.Group;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;

public class TableResultTabBean {

	private int current = 0;

	private int step = 10;

	private Object[] results;

	private String[] populatedAttributes;

	private Conditions condition;

	private Boolean componentVisible = new Boolean(false);

	private boolean needQuery = false;

	private boolean configuration = false;

	private String selectedGroupSet = null;

	private String selectedAttributeAdded;

	private String selectedAttributeToAdd;

	public TableResultTabBean() {

		populatedAttributes = new String[] { "voyageId", "captaina",
				"captainb", "captainc", "ownere", "arrport" };
	}

	public String next() {
		if (current % step == 0 && this.condition != null) {
			current += step;
			this.needQuery = true;
		}

		this.getResultsDB();
		return null;
	}

	public String prev() {
		if (current > 0 && this.condition != null) {
			current -= step;
			this.needQuery = true;
		}
		this.getResultsDB();
		return null;
	}

	private void getResultsDB() {
//		if (this.condition != null) {
//			System.out.println("2: --------------------------------------");
//			System.out
//					.println(this.condition.getConditionHQL().conditionString);
//		}
		if (this.condition != null && this.componentVisible.booleanValue() && needQuery) {
			Conditions localCond = (Conditions) this.condition
					.addAttributesPrefix("v.voyage.");
			localCond.addCondition(VoyageIndex.getRecent());

			QueryValue qValue = new QueryValue("VoyageIndex as v", localCond);
			qValue.setLimit(this.getStep().intValue());
			qValue.setFirstResult(this.getCurrent().intValue());
			qValue.setOrderBy("v.voyageId");
			if (this.populatedAttributes != null) {
				for (int i = 0; i < this.populatedAttributes.length; i++) {
					qValue.addPopulatedAttribute("v.voyage."
							+ this.populatedAttributes[i], Voyage
							.getSchemaColumn(this.populatedAttributes[i])
							.isDictinaory());
				}
			}

			this.results = qValue.executeQuery();
			needQuery = false;
		}

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
		this.step = step.intValue();
	}

	public Conditions getCondition() {
		return condition;
	}

	public void setCondition(Conditions condition) {
		this.condition = condition;
	}

	public String[] getPopulatedAttributes() {
		return populatedAttributes;
	}

	public void setPopulatedAttributes(String[] populatedAttributes) {
		this.populatedAttributes = populatedAttributes;
	}

	public Object[] getResults() {
		this.getResultsDB();
		return this.results;
	}

	public void setResults(Object[] results) {
		this.results = results;
	}

	public void setResultSize(Integer size) {
	}

	public Integer getResultSize() {
		return new Integer(this.results != null ? this.results.length : 0);
	}

	public void setConditions(Conditions c) {
		if (c != null) {
			System.out.println("1: --------------------------------------");
			System.out.println(c.getConditionHQL().conditionString);
		}
		if (c == null) {
			//needQuery = false;
		} else if (c.equals(condition)) {
			//needQuery = false;
		} else {
			condition = c;
			needQuery = true;
		}
	}

	public Boolean getComponentVisible() {
		return componentVisible;
	}

	public void setComponentVisible(Boolean componentVisible) {
		this.componentVisible = componentVisible;
	}

	public Boolean getConfigurationMode() {
		return new Boolean(this.configuration);
	}

	public Boolean getResultsMode() {
		return new Boolean(!this.configuration);
	}

	public void configurationMode() {
		this.configuration = true;
	}

	public void resultsMode() {
		this.configuration = false;
	}

	public List getAvailableGroupSets() {
		ArrayList res = new ArrayList();
		Conditions c = new Conditions();
		QueryValue qValue = new QueryValue("Group", c);
		Object[] groupSets = qValue.executeQuery();
		for (int i = 0; i < groupSets.length; i++) {
			Group set = (Group) groupSets[i];
			res.add(new SelectItem("" + set.getId().longValue(), set
					.getName()));
		}
		if (this.selectedGroupSet == null && groupSets.length > 0) {
			this.selectedGroupSet = ((Group)groupSets[0]).getId().toString();
		}
		return res;
	}

	public List getAvailableAttributes() {
		ArrayList res = new ArrayList();
		Conditions c = new Conditions();
		if (this.selectedGroupSet != null) {
			c.addCondition("id", new Long(this.selectedGroupSet), Conditions.OP_EQUALS);
		}		
		QueryValue qValue = new QueryValue("Group", c);
		Object[] groupSets = qValue.executeQuery();
		if (groupSets.length > 0) {
			Group set = (Group) groupSets[0];
			Set attrs = set.getAttributes();
			
			Set groups = set.getCompoundAttributes();
			for (Iterator groupsIter = groups.iterator(); groupsIter.hasNext();) {
				CompoundAttribute element = (CompoundAttribute) groupsIter.next();
				res.add(new SelectItem("Group_" + element.getId(), element.getName()));
			}
			for (Iterator iter = attrs.iterator(); iter.hasNext();) {
				Attribute attr = (Attribute) iter.next();
				res.add(new SelectItem("Attribute_" + attr.getName(), (""
								.equals(attr.getUserLabel()) || attr
								.getUserLabel() == null) ? (attr.getName())
								: (attr.getUserLabel())));
			}
		}
		return res;
	}

	public List getVisibleAttributes() {
		ArrayList res = new ArrayList();
		for (int i = 0; i < this.populatedAttributes.length; i++) {
			res.add(new SelectItem(this.populatedAttributes[i]));
		}
		return res;
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

	public String moveAttrUp() {
		if (this.selectedAttributeAdded == null) {
			return null;
		}
		for (int i = 1; i < this.populatedAttributes.length; i++) {
			if (this.populatedAttributes[i].equals(this.selectedAttributeAdded)) {
				String tmp = this.populatedAttributes[i];
				this.populatedAttributes[i] = this.populatedAttributes[i - 1];
				this.populatedAttributes[i - 1] = tmp;
				this.needQuery = true;
				break;
			}
		}
		return null;
	}

	public String moveAttrDown() {
		if (this.selectedAttributeAdded == null) {
			return null;
		}
		for (int i = 0; i < this.populatedAttributes.length - 1; i++) {
			if (this.populatedAttributes[i].equals(this.selectedAttributeAdded)) {
				String tmp = this.populatedAttributes[i];
				this.populatedAttributes[i] = this.populatedAttributes[i + 1];
				this.populatedAttributes[i + 1] = tmp;
				this.needQuery = true;
				break;
			}
		}
		return null;
	}

	public String addSelectedAttributeToList() {
		if (this.selectedAttributeToAdd == null) {
			return null;
		}
		boolean is = false;
		String attrsToAdd[] = null;
		if (this.selectedAttributeToAdd.startsWith("Attribute_")) {
			attrsToAdd = new String[1];
			attrsToAdd[0] = this.selectedAttributeToAdd.substring("Attribute_"
					.length(), this.selectedAttributeToAdd.length());
		} else {
			String groupName = this.selectedAttributeToAdd.substring("Group_"
					.length(), this.selectedAttributeToAdd.length());
			Conditions c = new Conditions();
			c.addCondition("id", new Long(groupName), Conditions.OP_EQUALS);
			QueryValue qValue = new QueryValue("CompoundAttribute", c);
			Object[] groups = qValue.executeQuery();
			if (groups.length > 0) {
				CompoundAttribute group = (CompoundAttribute) groups[0];
				Set attrs = group.getAttributes();
				attrsToAdd = new String[attrs.size()];
				int i = 0;
				for (Iterator iter = attrs.iterator(); iter.hasNext();) {
					Attribute attr = (Attribute) iter.next();
					attrsToAdd[i] = ("".equals(attr.getUserLabel()) || attr
							.getUserLabel() == null) ? (attr.getName()) : (attr
							.getUserLabel());
					i++;
				}
			}
		}
		for (int j = 0; j < attrsToAdd.length; j++) {
			for (int i = 0; i < this.populatedAttributes.length; i++) {
				if (this.populatedAttributes[i].equals(attrsToAdd[j])) {
					is = true;
				}
			}
			if (!is) {
				List list = Arrays.asList(this.populatedAttributes);
				list = new ArrayList(list);
				list.add(attrsToAdd[j]);
				this.populatedAttributes = (String[]) list
						.toArray(new String[] {});
				this.needQuery = true;
			}
		}

		return null;
	}

	public String remSelectedAttributeFromList() {
		if (this.selectedAttributeToAdd == null) {
			return null;
		}
		List list = Arrays.asList(this.populatedAttributes);
		if (list.contains(this.selectedAttributeAdded)) {
			list = new ArrayList(list);
			list.remove(this.selectedAttributeAdded);
			this.populatedAttributes = (String[]) list.toArray(new String[] {});
			this.needQuery = true;
		}
		return null;
	}
}
