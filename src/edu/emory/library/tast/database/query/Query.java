package edu.emory.library.tast.database.query;

import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.emory.library.tast.database.query.searchables.SearchableAttribute;
import edu.emory.library.tast.database.query.searchables.SearchableAttributeSimpleDate;
import edu.emory.library.tast.database.query.searchables.SearchableAttributeSimpleNumeric;
import edu.emory.library.tast.database.query.searchables.Searchables;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.XMLExportable;
import edu.emory.library.tast.util.query.Conditions;

public class Query implements Cloneable, XMLExportable {

	private static final long serialVersionUID = 6693392749775025817L;

	private QueryBuilderQuery builderQuery = new QueryBuilderQuery();

	private int yearFrom;

	private int yearTo;

	public QueryBuilderQuery getBuilderQuery() {
		return builderQuery;
	}

	public void addConditionOn(String searchableAttributeId) {

		if (builderQuery.containsConditionOn(searchableAttributeId))
			return;

		SearchableAttribute searchableAttribute = Searchables.getCurrent().getSearchableAttributeById(searchableAttributeId);
		QueryCondition queryCondition = searchableAttribute.createQueryCondition();

		if (searchableAttribute instanceof SearchableAttributeSimpleDate) {
			QueryConditionDate queryConditionDate = (QueryConditionDate) queryCondition;
			queryConditionDate.setFromMonth("01");
			queryConditionDate.setFromYear(String.valueOf(yearFrom));
			queryConditionDate.setToMonth("12");
			queryConditionDate.setToYear(String.valueOf(yearTo));
		}

		if (searchableAttribute instanceof SearchableAttributeSimpleNumeric) {
			SearchableAttributeSimpleNumeric searchableAttributeNumeric = (SearchableAttributeSimpleNumeric) searchableAttribute;
			if (searchableAttributeNumeric.getType() == SearchableAttributeSimpleNumeric.TYPE_YEAR) {
				QueryConditionNumeric queryConditionNumeric = (QueryConditionNumeric) queryCondition;
				queryConditionNumeric.setFrom(String.valueOf(yearFrom));
				queryConditionNumeric.setTo(String.valueOf(yearTo));
			}
		}

		builderQuery.addCondition(queryCondition);

	}

	public boolean addToDbConditions(boolean markErrors, Conditions dbConds)
	{
		
		dbConds.addCondition(
				Voyage.getAttribute("yearam"),
				new Integer(yearFrom),
				Conditions.OP_GREATER_OR_EQUAL);

		dbConds.addCondition(
				Voyage.getAttribute("yearam"),
				new Integer(yearTo),
				Conditions.OP_SMALLER_OR_EQUAL);

		boolean errors = false;
		for (Iterator iterQueryCondition = builderQuery.getConditions().iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			if (!queryCondition.addToConditions(dbConds, markErrors))
				errors = true;
		}

		return errors;

	}
	
	public boolean isEmpty()
	{
		return builderQuery.getConditionCount() == 0;
	}

	public void setBuilderQuery(QueryBuilderQuery builderQuery) {
		this.builderQuery = builderQuery;
	}

	public int getYearFrom() {
		return yearFrom;
	}

	public void setYearFrom(int yearFrom) {
		this.yearFrom = yearFrom;
	}

	public int getYearTo() {
		return yearTo;
	}

	public void setYearTo(int yearTo) {
		this.yearTo = yearTo;
	}

	protected Object clone() throws CloneNotSupportedException {
		Query newQuery = new Query();
		newQuery.setYearFrom(yearFrom);
		newQuery.setYearTo(yearTo);
		newQuery.setBuilderQuery((QueryBuilderQuery) builderQuery.clone());
		return newQuery;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Query))
			return false;

		Query that = (Query) obj;

		if (that.yearFrom != yearFrom)
			return false;

		if (that.yearTo != yearTo)
			return false;

		if ((that.builderQuery == null && this.builderQuery == null) || !that.builderQuery.equals(this.builderQuery))
			return false;

		return true;

	}

	public void restoreFromXML(Node xmlNode) {
		NodeList children = xmlNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("query")) {
				this.yearFrom = Integer.parseInt(node.getAttributes().getNamedItem("yearFrom").getNodeValue());
				this.yearTo = Integer.parseInt(node.getAttributes().getNamedItem("yearTo").getNodeValue());
				this.builderQuery = new QueryBuilderQuery();
				NodeList candidatesForQBQ = node.getChildNodes();
				for (int j = 0; j < candidatesForQBQ.getLength(); j++) {
					Node candidate = candidatesForQBQ.item(j);
					if (candidate.getNodeType() == Node.ELEMENT_NODE && candidate.getNodeName().equals("queryBuilderQuery")) {
						this.builderQuery.restoreFromXML(candidate);
					}
				}				
			}
		}
	}
	
	public String toXML() {
		StringBuffer xml = new StringBuffer();
		xml.append("<query yearFrom=\"").append(this.yearFrom).append("\" yearTo=\"");
		xml.append(this.yearTo).append("\">\n");
		xml.append(this.builderQuery.toXML());
		xml.append("</query>\n");
		return xml.toString();
	}

}
