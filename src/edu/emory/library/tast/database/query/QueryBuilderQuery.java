package edu.emory.library.tast.database.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.emory.library.tast.dm.XMLExportable;

/**
 * Holds the list of conditions, i.e. objects of types
 * {@link edu.emory.library.tast.database.query.QueryCondition}. Represents the currently
 * built query. Used by {@link edu.emory.library.tast.database.query.QueryBuilderComponent}.
 * Also, it is important that this class implements {@link #clone()} and
 * {@link #equals(Object)} since is the two methods are used when storing and
 * restoring the query in the history list.
 * 
 * @author Jan Zich
 * 
 */
public class QueryBuilderQuery implements Cloneable, XMLExportable
{

	private static final long serialVersionUID = 5986829888479480030L;

	private List conditions = new ArrayList();

	private transient Map conditionsByAttributes = null;
	
	private void ensureMap()
	{
		if (conditionsByAttributes == null)
		{
			Map newMap = new HashMap();
			for (Iterator iter = conditions.iterator(); iter.hasNext();)
			{
				QueryCondition queryCondition = (QueryCondition) iter.next();
				newMap.put(queryCondition.getSearchableAttributeId(), queryCondition);
			}
			conditionsByAttributes = newMap;
		}
	}
	
	public void addCondition(QueryCondition queryCondition)
	{
		
		if (queryCondition == null) return;
		
		conditions.add(queryCondition);
		
		ensureMap();
		conditionsByAttributes.put(queryCondition.getSearchableAttributeId(), queryCondition);
	
	}

	public List getConditions()
	{
		return conditions;
	}

	public Map getConditionsByAttributes()
	{
		ensureMap();
		return conditionsByAttributes;
	}
	
	public int getConditionCount()
	{
		return conditions.size();
	}
	
	public boolean containsConditionOn(String searchableAttributeId)
	{
		ensureMap();
		return conditionsByAttributes.containsKey(searchableAttributeId);
	}

	public QueryCondition getCondition(String searchableAttributeId)
	{
		ensureMap();
		return (QueryCondition) conditionsByAttributes.get(searchableAttributeId);
	}

	protected Object clone()
	{
		QueryBuilderQuery newQuery = new QueryBuilderQuery();
		for (Iterator iterQueryCondition = conditions.iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			newQuery.addCondition((QueryCondition) queryCondition.clone());
		}
		return newQuery;
	}
	
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof QueryBuilderQuery))
			return false;
		
		QueryBuilderQuery that = (QueryBuilderQuery) obj;
		
		if (this.getConditionCount() != that.getConditionCount())
			return false;
		
		for (Iterator iterAttr = getConditionsByAttributes().keySet().iterator(); iterAttr.hasNext();)
		{
			String attrId = (String) iterAttr.next();
			QueryCondition thisQueryCondition = this.getCondition(attrId);
			QueryCondition thatQueryCondition = that.getCondition(attrId);
			
			if (thatQueryCondition == null)
				return false;
			
			if (!thatQueryCondition.equals(thisQueryCondition))
				return false;
		}
		
		return true;
	}

	public String toXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<queryBuilderQuery>\n");
		Iterator iter = this.conditions.iterator();
		while (iter.hasNext()) {
			QueryCondition qC = (QueryCondition) iter.next();
			buffer.append(qC.toXML());
		}
		buffer.append("</queryBuilderQuery>\n");
		return buffer.toString();
	}

	public void restoreFromXML(Node xmlNode) {
		NodeList childNodes = xmlNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("condition")) {
				String type = node.getAttributes().getNamedItem("type").getNodeValue();								
				if (type.equals(QueryConditionBoolean.TYPE)) {					
					this.addCondition(QueryConditionBoolean.fromXML(node));
				} else if (type.equals(QueryConditionDate.TYPE)) {
					this.addCondition(QueryConditionDate.fromXML(node));
				} else if (type.equals(QueryConditionList.TYPE)) {
					this.addCondition(QueryConditionList.fromXML(node));
				} else if (type.equals(QueryConditionNumeric.TYPE)) {
					this.addCondition(QueryConditionNumeric.fromXML(node));
				} else if (type.equals(QueryConditionText.TYPE)) {
					this.addCondition(QueryConditionText.fromXML(node));
				}
			}
		}
	}

}