package edu.emory.library.tast.database.query;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.w3c.dom.Node;

import edu.emory.library.tast.util.XMLUtils;

public class QueryConditionList extends QueryCondition
{
	
	private static final long serialVersionUID = 6147345036427086382L;

	public static final String TYPE = "list";
	
	private Set selectedIds = new HashSet();
	private Set expandedIds = new HashSet();
	private boolean edit = false;
	private boolean autoSelection = false; 
	
	public QueryConditionList(String searchableAttributeId)
	{
		super(searchableAttributeId);
	}
	
	public boolean isEdit()
	{
		return edit;
	}

	public void setEdit(boolean edit)
	{
		this.edit = edit;
	}

	public boolean isAutoSelection() 
	{
		return autoSelection;
	}

	public void setAutoSelection(boolean autoSelection)
	{
		this.autoSelection = autoSelection;
	}

	public int getSelectedIdsCount()
	{
		if (selectedIds == null) return 0;
		return selectedIds.size();
	}

	public Set getSelectedIds()
	{
		return selectedIds;
	}

	public void setSelectedIds(Set values)
	{
		this.selectedIds = values;
	}
	
	public boolean containsId(String id)
	{
		if (selectedIds == null) return false;
		return selectedIds.contains(id);
	}

	public void addId(String id)
	{
		if (id == null) return;
		if (selectedIds == null) selectedIds = new HashSet();
		selectedIds.add(id);
	}
	
	public void setExpanded(String id, boolean state)
	{
		if (id == null) return;
		if (expandedIds == null) expandedIds = new HashSet();
		if (state)
		{
			expandedIds.add(id);
		}
		else
		{
			expandedIds.remove(id);
		}
	}
	
	public boolean isExpanded(String id)
	{
		if (expandedIds == null)
		{
			return false;
		}
		else
		{
			return expandedIds.contains(id);
		}
	}
	
	public void setExpandedIds(String[] ids)
	{
		if (expandedIds == null) expandedIds = new HashSet();
		expandedIds.clear();
		for (int i = 0; i < ids.length; i++) expandedIds.add(ids[i]);
	}

	public void setSelectedIds(String[] ids)
	{
		if (selectedIds == null) selectedIds = new HashSet();
		selectedIds.clear();
		for (int i = 0; i < ids.length; i++) selectedIds.add(ids[i]);
	}

	public boolean equals(Object obj)
	{
		if (!super.equals(obj))
			return false;

		if (!(obj instanceof QueryConditionList))
			return false;
		
		QueryConditionList that = (QueryConditionList) obj;

		if (this.getSelectedIdsCount() != that.getSelectedIdsCount())
			return false;
		
		for (Iterator iter = selectedIds.iterator(); iter.hasNext();)
			if (!that.containsId((String) iter.next()))
				return false;
		
		return true;
	}
	
	protected Object clone()
	{
		QueryConditionList newQueryCondition =
			new QueryConditionList(getSearchableAttributeId());
		
		for (Iterator iterDict = selectedIds.iterator(); iterDict.hasNext();)
			newQueryCondition.addId((String) iterDict.next());
		
		return newQueryCondition;
	}

	public String toXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<condition ");
		XMLUtils.appendAttribute(buffer, "type", TYPE);
		XMLUtils.appendAttribute(buffer, "attribute", this.getSearchableAttributeId());
		XMLUtils.appendAttribute(buffer, "edit", new Boolean(this.edit));
		XMLUtils.appendAttribute(buffer, "auto", new Boolean(this.autoSelection));
		XMLUtils.appendAttribute(buffer, "expanded", getIDs(this.expandedIds));
		XMLUtils.appendAttribute(buffer, "selected", getIDs(this.selectedIds));
		buffer.append("/>\n");
		return buffer.toString();
	}

	private static Object getIDs(Set ids) {
		StringBuffer buffer = new StringBuffer();
		Iterator iter = ids.iterator();
		boolean first = true;
		while (iter.hasNext()) {
			if (!first) {
				buffer.append(",");
			}
			first = false;
			buffer.append(iter.next());
		}
		return buffer.toString();
	}
	
	private static void restoreIDs(Set dst, String ids) {
		if (ids == null) {
			return;
		}
		String[] idsTable = ids.split(",");
		for (int i = 0; i < idsTable.length; i++) {
			dst.add(idsTable[i]);
		}
	}
	
	public static QueryCondition fromXML(Node node) {
		QueryConditionList qc = new QueryConditionList(XMLUtils.getXMLProperty(node, "attribute"));
		qc.edit = Boolean.parseBoolean(XMLUtils.getXMLProperty(node, "edit"));
		qc.autoSelection = Boolean.parseBoolean(XMLUtils.getXMLProperty(node, "auto"));
		restoreIDs(qc.expandedIds, XMLUtils.getXMLProperty(node, "expanded"));
		restoreIDs(qc.selectedIds, XMLUtils.getXMLProperty(node, "selected"));
		return qc;
	}

}