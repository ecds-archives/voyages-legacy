package edu.emory.library.tast.database.query;

import java.io.Serializable;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import edu.emory.library.tast.database.query.searchables.SearchableAttribute;
import edu.emory.library.tast.database.query.searchables.Searchables;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;

/**
 * This class represents one condition in the list of conditions, represented by
 * {@link edu.emory.library.tast.database.query.QueryBuilderQuery}, in the currently built query in the
 * search UI. It is abstract. It provides only the necessary methods and
 * properties for specialized conditions. It is important that this class and,
 * in particular, its descendants implement {@link #clone()} and
 * {@link #equals(Object)} since is the two methods are used when storing and
 * restoring the query in the history list.
 * 
 * @author Jan Zich
 * 
 */
public abstract class QueryCondition implements Serializable
{
	
	private String searchableAttributeId;
	private transient boolean errorFlag;

	protected abstract Object clone();

	public boolean addToConditions(Conditions conditions, boolean markErrors)
	{
		return getSearchableAttribute().addToConditions(markErrors, conditions, this);
	}

	public QueryCondition(String searchableAttributeId)
	{
		this.searchableAttributeId = searchableAttributeId;
		this.errorFlag = false;
	}

	public boolean isErrorFlag()
	{
		return errorFlag;
	}

	public void setErrorFlag(boolean errorFlag)
	{
		this.errorFlag = errorFlag;
	}
	
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof QueryCondition)) return false;
		QueryCondition that = (QueryCondition) obj;
		return StringUtils.compareStrings(this.searchableAttributeId, that.searchableAttributeId);
	}
	
	public String getSearchableAttributeId()
	{
		return searchableAttributeId;
	}
	
	public SearchableAttribute getSearchableAttribute()
	{
		return Searchables.getCurrent().getSearchableAttributeById(searchableAttributeId);
	}

	public abstract String toXML();
	
	protected static StringBuffer appendAttribute(StringBuffer buffer, String attribute, Object value) {
		if (value == null) {
			buffer.append(attribute).append("=\"\" ");
		} else {
			buffer.append(attribute).append("=\"").append(value).append("\" ");
		}
		return buffer;
	}
	
	protected static String getXMLProperty(Node node, String propertyName) {
		Node attr = node.getAttributes().getNamedItem(propertyName);
		System.out.println("prop: " + propertyName + " -> " + attr.getNodeValue());
		if (attr != null && !"".equals(attr.getNodeValue())) {
			return attr.getNodeValue();
		} else {
			return null;
		}
	}
}