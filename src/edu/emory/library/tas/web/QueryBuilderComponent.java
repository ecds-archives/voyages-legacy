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

import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Voyage;

public class QueryBuilderComponent extends UIComponentBase
{

	private Query submittedQuery;
	private Query setQuery;
	private boolean querySet = false;
	private List attributeNames;

	public String getFamily()
	{
		return null;
	}

	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[2];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, attributeNames);
		return values;
	}

	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		attributeNames = (List) restoreAttachedState(context, values[1]);
	}
	
	private String getToDeleteHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_to_delete";
	}

	public void decode(FacesContext context)
	{
		
		ExternalContext externalContex = context.getExternalContext();
		
		String toDelete =
			(String) externalContex.getRequestParameterMap().get(
					getToDeleteHiddenFieldName(context));
		
		submittedQuery = new Query();
		for (Iterator iterAtrributeName = attributeNames.iterator(); iterAtrributeName.hasNext();)
		{
			
			String attributeName = (String) iterAtrributeName.next();
			SchemaColumn col = Voyage.getSchemaColumn(attributeName);
			
			if (col == null)
				continue;
			
			if (col.getName().equals(toDelete))
				continue;
			
			QueryCondition queryCondition = null;
			switch (col.getType())
			{
				case SchemaColumn.TYPE_STRING:
					queryCondition = decodeSimpleCondition(col, context, externalContex); 
					break;

				case SchemaColumn.TYPE_INTEGER:
				case SchemaColumn.TYPE_LONG:
				case SchemaColumn.TYPE_FLOAT:
				case SchemaColumn.TYPE_DATE:
					queryCondition = decodeRangeCondition(col, context, externalContex);
					break;
			
				case SchemaColumn.TYPE_DICT:
					queryCondition = decodeListCondition(col, context, externalContex);
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
		attributeNames = new ArrayList();
		
		writer.startElement("input", this);
		writer.writeAttribute("type", "hidden", null);
		writer.writeAttribute("name", getToDeleteHiddenFieldName(context), null);
		writer.endElement("input");
		
		for (Iterator iterFieldName = query.getConditions().iterator(); iterFieldName.hasNext();)
		{
			
			QueryCondition queryCondition = (QueryCondition) iterFieldName.next();
			attributeNames.add(queryCondition.getAttributeName());
			
			SchemaColumn col = Voyage.getSchemaColumn(queryCondition.getAttributeName());
			if (col == null) continue;
			
			switch (col.getType())
			{
				case SchemaColumn.TYPE_STRING:
					if (queryCondition instanceof QueryConditionText)
						encodeSimpleCondition((QueryConditionText)queryCondition, context, form, writer);
					break;

				case SchemaColumn.TYPE_INTEGER:
				case SchemaColumn.TYPE_LONG:
				case SchemaColumn.TYPE_FLOAT:
				case SchemaColumn.TYPE_DATE:
					if (queryCondition instanceof QueryConditionRange)
						encodeRangeCondition((QueryConditionRange) queryCondition, context, form, writer);
					break;
			
				case SchemaColumn.TYPE_DICT:
					if (queryCondition instanceof QueryConditionList)
						encodeListCondition((QueryConditionList)queryCondition, context, form, writer);
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
		
		StringBuffer js = new StringBuffer();
		
		js.append("document.");
		js.append("forms['").append(form.getClientId(context)).append("'].");
		js.append("elements['").append(getToDeleteHiddenFieldName(context)).append("'].value = ");
		js.append("'").append(queryCondition.getAttributeName()).append("';");

		js.append(" ");
		js.append("document.");
		js.append("forms['").append(form.getClientId(context)).append("'].");
		js.append("submit();");
		
		js.append(" ");
		js.append("return false;");
		
		writer.startElement("a", this);
		writer.writeAttribute("href", "#", null);
		writer.writeAttribute("onclick", js.toString(), null);
		writer.write("del");
		writer.endElement("a");
		
	}
	
	private String getHtmlNameForSimpleValue(String attributeName, FacesContext context)
	{
		return getClientId(context) + "_" + attributeName;
	}

	private void encodeSimpleCondition(QueryConditionText queryCondition, FacesContext context, UIForm form, ResponseWriter writer) throws IOException
	{
		
		String attributeName = queryCondition.getAttributeName();

		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		encodeDeleteButton(queryCondition, context, form, writer);
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.write(attributeName);
		writer.endElement("td");

		writer.startElement("td", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getHtmlNameForSimpleValue(attributeName, context), null);
		writer.writeAttribute("value", queryCondition.getValue(), null);
		writer.endElement("input");
		writer.endElement("td");
		
		writer.endElement("tr");
		writer.endElement("table");

	}
	
	private QueryCondition decodeSimpleCondition(SchemaColumn col, FacesContext context, ExternalContext externalContext)
	{

		String value = (String) externalContext.getRequestParameterMap().get(getHtmlNameForSimpleValue(col.getName(), context));

		QueryConditionText queryCondition = new QueryConditionText(col.getName());
		queryCondition.setValue(value);
		
		return queryCondition;
		
	}
	
	private String getHtmlNameForRangeType(String attributeName, FacesContext context)
	{
		return getClientId(context) + "_" + attributeName + "_type";
	}

	private String getHtmlNameForRangeFrom(String attributeName, FacesContext context)
	{
		return getClientId(context) + "_" + attributeName + "_from";
	}

	private String getHtmlNameForRangeTo(String attributeName, FacesContext context)
	{
		return getClientId(context) + "_" + attributeName + "_to";
	}

	private String getHtmlNameForRangeLe(String attributeName, FacesContext context)
	{
		return getClientId(context) + "_" + attributeName + "_le";
	}

	private String getHtmlNameForRangeGe(String attributeName, FacesContext context)
	{
		return getClientId(context) + "_" + attributeName + "_ge";
	}
	
	private String getHtmlNameForRangeEq(String attributeName, FacesContext context)
	{
		return getClientId(context) + "_" + attributeName + "_eq";
	}

	private void encodeRangeCondition(QueryConditionRange queryCondition, FacesContext context, UIForm form, ResponseWriter writer) throws IOException
	{

		String attributeName = queryCondition.getAttributeName();
		
		String tdFromId = getClientId(context) + attributeName + "_td_from";
		String tdDashId = getClientId(context) + attributeName + "_td_dash";
		String tdToId = getClientId(context) + attributeName + "_td_to";
		String tdLeId = getClientId(context) + attributeName + "_td_le";
		String tdGeId = getClientId(context) + attributeName + "_td_ge";
		String tdEqId = getClientId(context) + attributeName + "_td_eq";
		
		String htmlNameForRangeType = getHtmlNameForRangeType(attributeName, context);
		String inputFromName = getHtmlNameForRangeFrom(attributeName, context);
		String inputToName = getHtmlNameForRangeTo(attributeName, context);
		String inputLeName = getHtmlNameForRangeLe(attributeName, context);
		String inputGeName = getHtmlNameForRangeGe(attributeName, context);
		String inputEqName = getHtmlNameForRangeEq(attributeName, context);
		
		StringBuffer js = new StringBuffer();

		js.append("var type = ");
		UtilsJSF.appendFormElementRefJS(js, context, form, htmlNameForRangeType);
		js.append(".selectedIndex; ");
		
		UtilsJSF.appendElementRefJS(js, context, form, tdFromId);
		js.append(".style.display = (type == 0) ? '' : 'none'; ");
		
//		UtilsJSF.appendFormElementRefJS(js, context, form, inputFromName);
//		js.append(".type = (type == 0) ? 'hidden' : 'text'; ");
		
		UtilsJSF.appendElementRefJS(js, context, form, tdDashId);
		js.append(".style.display = (type == 0) ? '' : 'none'; ");

		UtilsJSF.appendElementRefJS(js, context, form, tdToId);
		js.append(".style.display = (type == 0) ? '' : 'none'; ");

//		UtilsJSF.appendFormElementRefJS(js, context, form, inputToName);
//		js.append(".type = (type == 0) ? 'hidden' : 'text'; ");

		UtilsJSF.appendElementRefJS(js, context, form, tdLeId);
		js.append(".style.display = (type == 1) ? '' : 'none'; ");

//		UtilsJSF.appendFormElementRefJS(js, context, form, inputLeName);
//		js.append(".type = (type == 1) ? 'hidden' : 'text'; ");

		UtilsJSF.appendElementRefJS(js, context, form, tdGeId);
		js.append(".style.display = (type == 2) ? '' : 'none'; ");
		
//		UtilsJSF.appendFormElementRefJS(js, context, form, inputGeName);
//		js.append(".type = (type == 2) ? 'hidden' : 'text'; ");

		UtilsJSF.appendElementRefJS(js, context, form, tdEqId);
		js.append(".style.display = (type == 3) ? '' : 'none'; ");

//		UtilsJSF.appendFormElementRefJS(js, context, form, inputEqName);
//		js.append(".type = (type == 3) ? 'hidden' : 'text'; ");

		int type = queryCondition.getType();

		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		encodeDeleteButton(queryCondition, context, form, writer);
		writer.endElement("td");

		writer.startElement("td", this);
		writer.write(attributeName);
		writer.endElement("td");

		writer.startElement("td", this);
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
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputFromName, null);
		writer.writeAttribute("value", queryCondition.getFrom(), null);
		writer.endElement("input");
		writer.endElement("td");
		
		writer.startElement("td", this);
		if (type != 0) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdDashId, null);
		writer.write("-");
		writer.endElement("td");

		writer.startElement("td", this);
		if (type != 0) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdToId, null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputToName, null);
		writer.writeAttribute("value", queryCondition.getTo(), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.startElement("td", this);
		if (type != 1) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdLeId, null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputLeName, null);
		writer.writeAttribute("value", queryCondition.getLe(), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.startElement("td", this);
		if (type != 2) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdGeId, null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputGeName, null);
		writer.writeAttribute("value", queryCondition.getGe(), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.startElement("td", this);
		if (type != 3) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdEqId, null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputEqName, null);
		writer.writeAttribute("value", queryCondition.getEq(), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.endElement("tr");
		writer.endElement("table");
	
	}
	
	private QueryCondition decodeRangeCondition(SchemaColumn col, FacesContext context, ExternalContext externalContext)
	{
		
		Map params = externalContext.getRequestParameterMap();
		String typeStr = (String) params.get(getHtmlNameForRangeType(col.getName(), context));
		String from = (String) params.get(getHtmlNameForRangeFrom(col.getName(), context));
		String to = (String) params.get(getHtmlNameForRangeTo(col.getName(), context));
		String le = (String) params.get(getHtmlNameForRangeLe(col.getName(), context));
		String ge = (String) params.get(getHtmlNameForRangeGe(col.getName(), context));
		String eq = (String) params.get(getHtmlNameForRangeEq(col.getName(), context));

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
		
		QueryConditionRange queryCondition = new QueryConditionRange(col.getName(), type);
		queryCondition.setFrom(from);
		queryCondition.setTo(to);
		queryCondition.setLe(le);
		queryCondition.setGe(ge);
		queryCondition.setEq(eq);

		return queryCondition;
		
	}

	private String getHtmlNameForList(String attibuteName, FacesContext context)
	{
		return getClientId(context) + "_" + attibuteName + "_list";
	}

	private void encodeListCondition(QueryConditionList queryCondition, FacesContext context, UIForm form, ResponseWriter writer) throws IOException
	{
		
		String attributeName = queryCondition.getAttributeName();
		String userListHtmlName = getClientId(context) + attributeName + "_user_list";

		writer.startElement("input", this);
		writer.writeAttribute("type", "hidden", null);
		writer.writeAttribute("name", getHtmlNameForList(attributeName, context), null);
		writer.endElement("input");
		
		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		encodeDeleteButton(queryCondition, context, form, writer);
		writer.endElement("td");

		writer.startElement("td", this);
		writer.write(attributeName);
		writer.endElement("td");

		writer.startElement("td", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", userListHtmlName, null);
		writer.endElement("input");
		writer.endElement("td");
		
		writer.endElement("tr");
		writer.endElement("table");
		
	}

	private QueryCondition decodeListCondition(SchemaColumn col, FacesContext context, ExternalContext externalContext)
	{
		
		String attributeName = col.getName();
		
		String[] values =
			(String[]) externalContext.getRequestParameterValuesMap().get(
					getHtmlNameForList(attributeName, context));
		
		List list = new ArrayList();
		for (int i = 0; i < values.length; i++) list.add(list);

		QueryConditionList queryCondition = new QueryConditionList(attributeName);
		queryCondition.setValues(list);
		
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