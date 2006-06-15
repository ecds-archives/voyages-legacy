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
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Voyage;

public class QueryBuilderComponent extends UIComponentBase
{

	private List query;
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

	public void decode(FacesContext context)
	{
		
		ExternalContext externalContex = context.getExternalContext();
		
		query = new ArrayList();
		for (Iterator iterAtrributeName = attributeNames.iterator(); iterAtrributeName.hasNext();)
		{
			
			String attributeName = (String) iterAtrributeName.next();
			SchemaColumn col = Voyage.getSchemaColumn(attributeName);
			
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
				query.add(queryCondition);
			
		}
		
	}
	
	public void broadcast(FacesEvent event) throws AbortProcessingException
	{
		super.broadcast(event);
	}

	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = UtilsJSF.getForm(this, context);
		
		List query = getQuery();
		attributeNames = new ArrayList();
		
		for (Iterator iterFieldName = query.iterator(); iterFieldName.hasNext();)
		{
			
			QueryCondition queryCondition = (QueryCondition) iterFieldName.next();
			attributeNames.add(queryCondition.getAttributeName());
			
			SchemaColumn col = Voyage.getSchemaColumn(queryCondition.getAttributeName());
			
			switch (col.getType())
			{
				case SchemaColumn.TYPE_STRING:
					encodeSimpleCondition(queryCondition, context, writer);
					break;

				case SchemaColumn.TYPE_INTEGER:
				case SchemaColumn.TYPE_LONG:
				case SchemaColumn.TYPE_FLOAT:
				case SchemaColumn.TYPE_DATE:
					encodeRangeCondition(queryCondition, context, writer);
					break;
			
				case SchemaColumn.TYPE_DICT:
					encodeListCondition(queryCondition, context, form, writer);
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
	
	private String getHtmlNameForSimpleValue(String attributeName, FacesContext context)
	{
		return getClientId(context) + "_" + attributeName;
	}

	private void encodeSimpleCondition(QueryCondition queryCondition, FacesContext context, ResponseWriter writer) throws IOException
	{
		
		String attributeName = queryCondition.getAttributeName();

		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.write(attributeName);
		writer.endElement("td");

		writer.startElement("td", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getHtmlNameForSimpleValue(attributeName, context), null);
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

	private void encodeRangeCondition(QueryCondition queryCondition, FacesContext context, ResponseWriter writer) throws IOException
	{

		String attributeName = queryCondition.getAttributeName();
		
		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.write(attributeName);
		writer.endElement("td");

		writer.startElement("td", this);
		writer.startElement("select", this);
		writer.writeAttribute("name", getHtmlNameForRangeType(attributeName, context), null);
		writer.startElement("option", this);
		writer.writeAttribute("value", "between", null);
		writer.writeAttribute("name", "between", null);
		writer.endElement("option");
		writer.startElement("option", this);
		writer.writeAttribute("value", "at most", null);
		writer.writeAttribute("name", "le", null);
		writer.endElement("option");
		writer.startElement("option", this);
		writer.writeAttribute("value", "at least", null);
		writer.writeAttribute("name", "ge", null);
		writer.endElement("option");
		writer.startElement("option", this);
		writer.writeAttribute("value", "equal", null);
		writer.writeAttribute("name", "eq", null);
		writer.endElement("option");
		writer.endElement("select");
		writer.endElement("td");

		writer.startElement("td", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getHtmlNameForRangeFrom(attributeName, context), null);
		writer.endElement("input");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.write("-");
		writer.endElement("td");

		writer.startElement("td", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getHtmlNameForRangeTo(attributeName, context), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.startElement("td", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getHtmlNameForRangeLe(attributeName, context), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.startElement("td", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getHtmlNameForRangeGe(attributeName, context), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.startElement("td", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getHtmlNameForRangeEq(attributeName, context), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.endElement("tr");
		writer.endElement("table");
	
	}
	
	private QueryCondition decodeRangeCondition(SchemaColumn col, FacesContext context, ExternalContext externalContext)
	{
		
		Map params = externalContext.getRequestParameterMap();
		String type = (String) params.get(getHtmlNameForRangeType(col.getName(), context));
		String from = (String) params.get(getHtmlNameForRangeFrom(col.getName(), context));
		String to = (String) params.get(getHtmlNameForRangeTo(col.getName(), context));
		String le = (String) params.get(getHtmlNameForRangeLe(col.getName(), context));
		String ge = (String) params.get(getHtmlNameForRangeGe(col.getName(), context));
		String eq = (String) params.get(getHtmlNameForRangeEq(col.getName(), context));
		
		QueryCondition queryCondition = null;
		if ("between".equals(type))
		{
			QueryConditionRange queryConditionRange = new QueryConditionRange(col.getName());; 
			queryConditionRange.setFrom(from);
			queryConditionRange.setTo(to);
			queryCondition = queryConditionRange;
		}
		else if ("le".equals(type))
		{
			QueryConditionLe queryConditionLe = new QueryConditionLe(col.getName()); 
			queryConditionLe.setValue(le);
			queryCondition = queryConditionLe;
		}
		else if ("ge".equals(type))
		{
			QueryConditionGe queryConditionGe = new QueryConditionGe(col.getName());
			queryConditionGe.setValue(ge);
			queryCondition = queryConditionGe;
		}
		else if ("eq".equals(type))
		{
			QueryConditionEq queryConditionEq = new QueryConditionEq(col.getName());
			queryConditionEq.setValue(eq);
			queryCondition = queryConditionEq; 
		}
		
		return queryCondition;
		
	}

	private String getHtmlNameForList(String attibuteName, FacesContext context)
	{
		return getClientId(context) + "_" + attibuteName + "_list";
	}

	private void encodeListCondition(QueryCondition queryCondition, FacesContext context, UIForm form, ResponseWriter writer) throws IOException
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
	
	public void removeCondition(QueryCondition columnName)
	{
		query.remove(columnName);
	}

	public List getQuery()
	{
		if (query != null) return query;
		ValueBinding vb = getValueBinding("query");
		if (vb == null) return null;
		return (List) vb.getValue(getFacesContext());
	}

	public void setQuery(List query)
	{
		this.query = query;
	}

}