package edu.emory.library.tas.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.attrGroups.AbstractAttribute;

public class QueryBuilderComponent extends UIComponentBase
{

	private Query submittedQuery;
	private Query setQuery;
	private boolean querySet = false;
	private List attributes;

	public String getFamily()
	{
		return null;
	}

	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[2];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, attributes);
		return values;
	}

	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		attributes = (List) restoreAttachedState(context, values[1]);
	}
	
	private String getToDeleteHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_to_delete";
	}

	public void decode(FacesContext context)
	{
		
		ExternalContext externalContex = context.getExternalContext();
		
		String toDelete = (String) externalContex.getRequestParameterMap().get(
				getToDeleteHiddenFieldName(context));
		
		submittedQuery = new Query();
		for (Iterator iterAtrribute = attributes.iterator(); iterAtrribute.hasNext();)
		{
			
			AbstractAttribute attribute = (AbstractAttribute) iterAtrribute.next();
			
			if (attribute.getId().toString().equals(toDelete))
				continue;
			
			QueryCondition queryCondition = null;
			switch (attribute.getType())
			{
				case AbstractAttribute.TYPE_STRING:
					queryCondition = decodeSimpleCondition(attribute, context, externalContex); 
					break;

				case AbstractAttribute.TYPE_INTEGER:
				case AbstractAttribute.TYPE_LONG:
				case AbstractAttribute.TYPE_FLOAT:
				case AbstractAttribute.TYPE_DATE:
					queryCondition = decodeRangeCondition(attribute, context, externalContex);
					break;
			
				case AbstractAttribute.TYPE_DICT:
					queryCondition = decodeDictionaryCondition(attribute, context, externalContex);
					break;
			}
			
			if (queryCondition != null)
				submittedQuery.addCondition(queryCondition);
			
		}
		
	}
	
	public void processUpdates(FacesContext context)
	{
		ValueBinding vb = getValueBinding("query");
		if (vb != null) vb.setValue(context, submittedQuery);
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = UtilsJSF.getForm(this, context);
		
		Query query = getQuery();
		attributes = new ArrayList();
		
		UtilsJSF.encodeHiddenInput(this, writer, getToDeleteHiddenFieldName(context));
		
		for (Iterator iterFieldName = query.getConditions().iterator(); iterFieldName.hasNext();)
		{
			
			QueryCondition queryCondition = (QueryCondition) iterFieldName.next();
			AbstractAttribute attribute = queryCondition.getAttribute(); 
			attributes.add(attribute);
			
			switch (attribute.getType())
			{
				case AbstractAttribute.TYPE_STRING:
					if (queryCondition instanceof QueryConditionText)
					{
						encodeStartQueryConditionBox(queryCondition, context, form, writer);
						encodeSimpleCondition((QueryConditionText)queryCondition, context, form, writer);
						encodeEndQueryConditionBox(queryCondition, context, form, writer);
					}
					break;

				case AbstractAttribute.TYPE_INTEGER:
				case AbstractAttribute.TYPE_LONG:
				case AbstractAttribute.TYPE_FLOAT:
				case AbstractAttribute.TYPE_DATE:
					if (queryCondition instanceof QueryConditionRange)
					{
						encodeStartQueryConditionBox(queryCondition, context, form, writer);
						encodeRangeCondition((QueryConditionRange) queryCondition, context, form, writer);
						encodeEndQueryConditionBox(queryCondition, context, form, writer);
					}
					break;
			
				case AbstractAttribute.TYPE_DICT:
					if (queryCondition instanceof QueryConditionDictionary)
					{
						encodeStartQueryConditionBox(queryCondition, context, form, writer);
						encodeDictionaryCondition((QueryConditionDictionary)queryCondition, context, form, writer);
						encodeEndQueryConditionBox(queryCondition, context, form, writer);
					}
					break;

			}

			
		}

	}

	public void encodeChildren(FacesContext context) throws IOException
	{
	}

	public void encodeEnd(FacesContext context) throws IOException
	{
	}
	
	private void encodeDeleteButton(QueryCondition queryCondition, FacesContext context, UIForm form, ResponseWriter writer) throws IOException
	{
		
		String jsToDelete = UtilsJSF.generateSubmitJS(context, form,
				getToDeleteHiddenFieldName(context), 
				queryCondition.getAttribute().getId().toString());

		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "query-builder-remove", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "side-box-button", null);
		writer.writeAttribute("onclick", jsToDelete, null);
		writer.write("&times;");
		writer.endElement("td");
		
		writer.endElement("tr");
		writer.endElement("table");
		
	}
	
	private void encodeStartQueryConditionBox(QueryCondition queryCondition, FacesContext context, UIForm form, ResponseWriter writer) throws IOException
	{
		
		//String errorClassName = queryCondition.isErrorFlag() ? " query-builder-error" : "";
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "side-box", null);
		
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "query-builder-label", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "query-builder-label", null);
		if (queryCondition.isErrorFlag())
		{
			writer.startElement("span", this);
			writer.writeAttribute("class", "query-builder-error", null);
			writer.write("&nbsp;!&nbsp;");
			writer.endElement("span");
			writer.write(" ");
		}
		writer.write(queryCondition.getAttribute().getUserLabel());
		writer.endElement("td");
		
		writer.startElement("td", this);
		encodeDeleteButton(queryCondition, context, form, writer);
		writer.endElement("td");
		
		writer.endElement("tr");
		writer.endElement("table");

	}
	
	private void encodeEndQueryConditionBox(QueryCondition queryCondition, FacesContext context, UIForm form, ResponseWriter writer) throws IOException
	{
		writer.endElement("div");
	}
	
	private String getHtmlNameForSimpleValue(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId();
	}

	private void encodeSimpleCondition(QueryConditionText queryCondition, FacesContext context, UIForm form, ResponseWriter writer) throws IOException
	{
		
		AbstractAttribute attribute = queryCondition.getAttribute();

		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("class", "query-builder-text", null);
		writer.writeAttribute("name", getHtmlNameForSimpleValue(attribute, context), null);
		writer.writeAttribute("value", queryCondition.getValue(), null);
		writer.endElement("input");
		writer.endElement("td");
		
		writer.endElement("tr");
		writer.endElement("table");

	}
	
	private QueryCondition decodeSimpleCondition(AbstractAttribute attribute, FacesContext context, ExternalContext externalContext)
	{

		String value = (String) externalContext.getRequestParameterMap().get(
				getHtmlNameForSimpleValue(attribute, context));

		QueryConditionText queryCondition = new QueryConditionText(attribute);
		queryCondition.setValue(value);
		
		return queryCondition;
		
	}
	
	private String getHtmlNameForRangeType(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_type";
	}

	private String getHtmlNameForRangeFrom(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_from";
	}

	private String getHtmlNameForRangeTo(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_to";
	}

	private String getHtmlNameForRangeLe(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_le";
	}

	private String getHtmlNameForRangeGe(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_ge";
	}
	
	private String getHtmlNameForRangeEq(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_eq";
	}

	private void encodeRangeCondition(QueryConditionRange queryCondition, FacesContext context, UIForm form, ResponseWriter writer) throws IOException
	{

		AbstractAttribute attribute = queryCondition.getAttribute();
		
		String tdFromId = getClientId(context) + attribute.getId() + "_td_from";
		String tdDashId = getClientId(context) + attribute.getId() + "_td_dash";
		String tdToId = getClientId(context) + attribute.getId() + "_td_to";
		String tdLeId = getClientId(context) + attribute.getId() + "_td_le";
		String tdGeId = getClientId(context) + attribute.getId() + "_td_ge";
		String tdEqId = getClientId(context) + attribute.getId() + "_td_eq";
		
		String htmlNameForRangeType = getHtmlNameForRangeType(attribute, context);
		String inputFromName = getHtmlNameForRangeFrom(attribute, context);
		String inputToName = getHtmlNameForRangeTo(attribute, context);
		String inputLeName = getHtmlNameForRangeLe(attribute, context);
		String inputGeName = getHtmlNameForRangeGe(attribute, context);
		String inputEqName = getHtmlNameForRangeEq(attribute, context);
		
		StringBuffer js = new StringBuffer();

		js.append("var type = ");
		UtilsJSF.appendFormElementRefJS(js, context, form, htmlNameForRangeType);
		js.append(".selectedIndex;");
		
		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdFromId);
		js.append(".style.display = (type == 0) ? '' : 'none';");
		
		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdDashId);
		js.append(".style.display = (type == 0) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdToId);
		js.append(".style.display = (type == 0) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdLeId);
		js.append(".style.display = (type == 1) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdGeId);
		js.append(".style.display = (type == 2) ? '' : 'none';");
		
		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdEqId);
		js.append(".style.display = (type == 3) ? '' : 'none';");

		int type = queryCondition.getType();

		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "query-builder-range-type", null);
		writer.startElement("select", this);
		writer.writeAttribute("name", htmlNameForRangeType, null);
		writer.writeAttribute("onchange", js.toString(), null);
		
		writer.startElement("option", this);
		writer.writeAttribute("value", "between", null);
		if (type == QueryConditionRange.TYPE_BETWEEN) writer.writeAttribute("selected", "selected", null);
		writer.write("Between");
		writer.endElement("option");
		
		writer.startElement("option", this);
		writer.writeAttribute("value", "le", null);
		if (type == QueryConditionRange.TYPE_LE) writer.writeAttribute("selected", "selected", null);
		writer.write("at most");
		writer.endElement("option");

		writer.startElement("option", this);
		writer.writeAttribute("value", "ge", null);
		if (type == QueryConditionRange.TYPE_GE) writer.writeAttribute("selected", "selected", null);
		writer.write("at least");
		writer.endElement("option");
		
		writer.startElement("option", this);
		writer.writeAttribute("value", "eq", null);
		if (type == QueryConditionRange.TYPE_EQ) writer.writeAttribute("selected", "selected", null);
		writer.write("is equal");
		writer.endElement("option");
		
		writer.endElement("select");
		writer.endElement("td");

		writer.startElement("td", this);
		if (type != 0) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdFromId, null);
		writer.writeAttribute("class", "query-builder-range-value", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputFromName, null);
		writer.writeAttribute("value", queryCondition.getFrom(), null);
		writer.endElement("input");
		writer.endElement("td");
		
		writer.startElement("td", this);
		if (type != 0) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdDashId, null);
		writer.writeAttribute("class", "query-builder-range-dash", null);
		writer.write("-");
		writer.endElement("td");

		writer.startElement("td", this);
		if (type != 0) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdToId, null);
		writer.writeAttribute("class", "query-builder-range-value", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputToName, null);
		writer.writeAttribute("value", queryCondition.getTo(), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.startElement("td", this);
		if (type != 1) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdLeId, null);
		writer.writeAttribute("class", "query-builder-range-value", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputLeName, null);
		writer.writeAttribute("value", queryCondition.getLe(), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.startElement("td", this);
		if (type != 2) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdGeId, null);
		writer.writeAttribute("class", "query-builder-range-value", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputGeName, null);
		writer.writeAttribute("value", queryCondition.getGe(), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.startElement("td", this);
		if (type != 3) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdEqId, null);
		writer.writeAttribute("class", "query-builder-range-value", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputEqName, null);
		writer.writeAttribute("value", queryCondition.getEq(), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.endElement("tr");
		writer.endElement("table");
	
	}
	
	private QueryCondition decodeRangeCondition(AbstractAttribute attribute, FacesContext context, ExternalContext externalContext)
	{
		
		Map params = externalContext.getRequestParameterMap();
		String typeStr = (String) params.get(getHtmlNameForRangeType(attribute, context));
		String from = (String) params.get(getHtmlNameForRangeFrom(attribute, context));
		String to = (String) params.get(getHtmlNameForRangeTo(attribute, context));
		String le = (String) params.get(getHtmlNameForRangeLe(attribute, context));
		String ge = (String) params.get(getHtmlNameForRangeGe(attribute, context));
		String eq = (String) params.get(getHtmlNameForRangeEq(attribute, context));

		int type;
		if ("between".equals(typeStr))
		{
			type = QueryConditionRange.TYPE_BETWEEN;
		}
		else if ("le".equals(typeStr))
		{
			type = QueryConditionRange.TYPE_LE; 
		}
		else if ("ge".equals(typeStr))
		{
			type = QueryConditionRange.TYPE_GE;
		}
		else if ("eq".equals(typeStr))
		{
			type = QueryConditionRange.TYPE_EQ;
		}
		else
		{
			return null;
		}
		
		QueryConditionRange queryCondition = new QueryConditionRange(attribute, type);
		queryCondition.setFrom(from);
		queryCondition.setTo(to);
		queryCondition.setLe(le);
		queryCondition.setGe(ge);
		queryCondition.setEq(eq);

		return queryCondition;
		
	}

	private String getHtmlNameForList(AbstractAttribute attibute, FacesContext context)
	{
		return getClientId(context) + "_" + attibute.getId() + "_list";
	}

	private void encodeDictionaryCondition(QueryConditionDictionary queryCondition, FacesContext context, UIForm form, ResponseWriter writer) throws IOException
	{
		
		AbstractAttribute attribute = queryCondition.getAttribute();
		String displayListHtmlName = getClientId(context) + "_" + attribute.getId() + "_user_list";
		
		StringBuffer valuesList = new StringBuffer();
		StringBuffer displayList = new StringBuffer();
		for (Iterator iterItem = queryCondition.getDictionaries().iterator(); iterItem.hasNext();)
		{
			Dictionary dict = (Dictionary) iterItem.next();
			valuesList.append(dict.getRemoteId());
			displayList.append(dict.getName());
			if (iterItem.hasNext())
			{
				valuesList.append(",");
				displayList.append(", ");
			}
		}
		
		UtilsJSF.encodeHiddenInput(this, writer,
				getHtmlNameForList(attribute, context),
				valuesList.toString());
		
		StringBuffer jsPopup = new StringBuffer();
		jsPopup.append("window.open(");
		jsPopup.append("'dictionary-list.jsp");
		jsPopup.append("?attributeId=").append(attribute.getId());
		jsPopup.append("&formName=").append(form.getClientId(context));
		jsPopup.append("&hiddenFieldName=").append(getHtmlNameForList(attribute, context));
		jsPopup.append("&displayFieldName=").append(displayListHtmlName);
		jsPopup.append("', ");
		jsPopup.append("'search-list', ");
		jsPopup.append("'width=300,height=500,resizable=yes,scrollbars=yes,status=no');");
		
		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "query-builder-list", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", displayListHtmlName, null);
		writer.writeAttribute("value", displayList.toString(), null);
		writer.endElement("input");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "query-builder-list-select", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("value", "Select", null);
		writer.writeAttribute("onclick", jsPopup.toString(), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.endElement("tr");
		writer.endElement("table");
		
	}

	private QueryCondition decodeDictionaryCondition(AbstractAttribute attribute, FacesContext context, ExternalContext externalContext)
	{
		
		String valuesStr = (String) externalContext.getRequestParameterMap().get(
				getHtmlNameForList(attribute, context));
		
		String[] values = null;
		if (valuesStr != null && valuesStr.length() > 0)
			values = valuesStr.split(",");
		
		QueryConditionDictionary queryCondition =
			new QueryConditionDictionary(attribute);
		
		if (values != null)
			for (int i = 0; i < values.length; i++)
				queryCondition.addDictionary(Integer.parseInt(values[i]));
		
		return queryCondition;

	}
	
	public Query getQuery()
	{
		if (querySet) return setQuery;
		ValueBinding vb = getValueBinding("query");
		if (vb == null) return submittedQuery;
		return (Query) vb.getValue(getFacesContext());
	}

	public void setQuery(Query query)
	{
		querySet = true;
		this.setQuery = query;
	}

}