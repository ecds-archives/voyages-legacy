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
			switch (attribute.getType().intValue())
			{
				case AbstractAttribute.TYPE_STRING:
					queryCondition = decodeSimpleCondition(attribute, context, externalContex); 
					break;

				case AbstractAttribute.TYPE_INTEGER:
				case AbstractAttribute.TYPE_LONG:
				case AbstractAttribute.TYPE_FLOAT:
					queryCondition = decodeNumericCondition(attribute, context, externalContex);
					break;
			
				case AbstractAttribute.TYPE_DATE:
					queryCondition = decodeDateCondition(attribute, context, externalContex);
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
			
			switch (attribute.getType().intValue())
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
					if (queryCondition instanceof QueryConditionNumeric)
					{
						encodeStartQueryConditionBox(queryCondition, context, form, writer);
						encodeNumericCondition((QueryConditionNumeric) queryCondition, context, form, writer);
						encodeEndQueryConditionBox(queryCondition, context, form, writer);
					}
					break;
			
				case AbstractAttribute.TYPE_DATE:
					if (queryCondition instanceof QueryConditionDate)
					{
						encodeStartQueryConditionBox(queryCondition, context, form, writer);
						encodeDateCondition((QueryConditionDate) queryCondition, context, form, writer);
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
		writer.write(queryCondition.getAttribute().getUserLabelOrName());
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
	
	private String getHtmlNameForNumericType(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_type";
	}

	private String getHtmlNameForNumericFrom(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_from";
	}

	private String getHtmlNameForNumericTo(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_to";
	}

	private String getHtmlNameForNumericLe(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_le";
	}

	private String getHtmlNameForNumericGe(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_ge";
	}
	
	private String getHtmlNameForNumericEq(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_eq";
	}

	private void encodeNumericCondition(QueryConditionNumeric queryCondition, FacesContext context, UIForm form, ResponseWriter writer) throws IOException
	{

		AbstractAttribute attribute = queryCondition.getAttribute();
		
		String tdFromId = getClientId(context) + "_" + attribute.getId() + "_td_from";
		String tdDashId = getClientId(context) + "_" + attribute.getId() + "_td_dash";
		String tdToId = getClientId(context) + "_" + attribute.getId() + "_td_to";
		String tdLeId = getClientId(context) + "_" + attribute.getId() + "_td_le";
		String tdGeId = getClientId(context) + "_" + attribute.getId() + "_td_ge";
		String tdEqId = getClientId(context) + "_" + attribute.getId() + "_td_eq";
		
		String htmlNameForRangeType = getHtmlNameForNumericType(attribute, context);
		String inputFromName = getHtmlNameForNumericFrom(attribute, context);
		String inputToName = getHtmlNameForNumericTo(attribute, context);
		String inputLeName = getHtmlNameForNumericLe(attribute, context);
		String inputGeName = getHtmlNameForNumericGe(attribute, context);
		String inputEqName = getHtmlNameForNumericEq(attribute, context);
		
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
		if (type == QueryConditionNumeric.TYPE_BETWEEN) writer.writeAttribute("selected", "selected", null);
		writer.write("Between");
		writer.endElement("option");
		
		writer.startElement("option", this);
		writer.writeAttribute("value", "le", null);
		if (type == QueryConditionNumeric.TYPE_LE) writer.writeAttribute("selected", "selected", null);
		writer.write("at most");
		writer.endElement("option");

		writer.startElement("option", this);
		writer.writeAttribute("value", "ge", null);
		if (type == QueryConditionNumeric.TYPE_GE) writer.writeAttribute("selected", "selected", null);
		writer.write("at least");
		writer.endElement("option");
		
		writer.startElement("option", this);
		writer.writeAttribute("value", "eq", null);
		if (type == QueryConditionNumeric.TYPE_EQ) writer.writeAttribute("selected", "selected", null);
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
	
	private QueryCondition decodeNumericCondition(AbstractAttribute attribute, FacesContext context, ExternalContext externalContext)
	{
		
		Map params = externalContext.getRequestParameterMap();
		String typeStr = (String) params.get(getHtmlNameForNumericType(attribute, context));
		String from = (String) params.get(getHtmlNameForNumericFrom(attribute, context));
		String to = (String) params.get(getHtmlNameForNumericTo(attribute, context));
		String le = (String) params.get(getHtmlNameForNumericLe(attribute, context));
		String ge = (String) params.get(getHtmlNameForNumericGe(attribute, context));
		String eq = (String) params.get(getHtmlNameForNumericEq(attribute, context));

		int type;
		if ("between".equals(typeStr))
		{
			type = QueryConditionNumeric.TYPE_BETWEEN;
		}
		else if ("le".equals(typeStr))
		{
			type = QueryConditionNumeric.TYPE_LE; 
		}
		else if ("ge".equals(typeStr))
		{
			type = QueryConditionNumeric.TYPE_GE;
		}
		else if ("eq".equals(typeStr))
		{
			type = QueryConditionNumeric.TYPE_EQ;
		}
		else
		{
			return null;
		}
		
		QueryConditionNumeric queryCondition = new QueryConditionNumeric(attribute, type);
		queryCondition.setFrom(from);
		queryCondition.setTo(to);
		queryCondition.setLe(le);
		queryCondition.setGe(ge);
		queryCondition.setEq(eq);
		
		return queryCondition;
		
	}

	private String getHtmlNameForDateType(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_type";
	}

	private String getHtmlNameForDateFromMonth(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_from_month";
	}

	private String getHtmlNameForDateFromYear(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_from_year";
	}

	private String getHtmlNameForDateToMonth(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_to_month";
	}

	private String getHtmlNameForDateToYear(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_to_year";
	}

	private String getHtmlNameForDateLeMonth(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_le_month";
	}

	private String getHtmlNameForDateLeYear(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_le_year";
	}

	private String getHtmlNameForDateGeMonth(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_ge_month";
	}
	
	private String getHtmlNameForDateGeYear(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_ge_year";
	}

	private String getHtmlNameForDateEqMonth(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_eq_month";
	}

	private String getHtmlNameForDateEqYear(AbstractAttribute attribute, FacesContext context)
	{
		return getClientId(context) + "_" + attribute.getId() + "_eq_year";
	}

	private String getHtmlNameForRangeMonth(AbstractAttribute attribute, FacesContext context, int month)
	{
		return getClientId(context) + "_" + attribute.getId() + "_month_" + month;
	}
	
//	private void encodeDateField(ResponseWriter writer, boolean display)
//	{
//		
//		writer.startElement("td", this);
//		if (!display) writer.writeAttribute("style", "display: none;", null);
//		writer.writeAttribute("id", tdFromMonthId, null);
//		writer.writeAttribute("class", "query-builder-range-value", null);
//		writer.startElement("input", this);
//		writer.writeAttribute("type", "text", null);
//		writer.writeAttribute("name", inputFromMonthName, null);
//		writer.writeAttribute("value", queryCondition.getFromMonth(), null);
//		writer.endElement("input");
//		writer.endElement("td");
//		
//		writer.startElement("td", this);
//		if (!display) writer.writeAttribute("style", "display: none;", null);
//		writer.writeAttribute("id", tdSlashBetweenStartId, null);
//		writer.writeAttribute("class", "query-builder-range-dash", null);
//		writer.write("/");
//		writer.endElement("td");
//
//		writer.startElement("td", this);
//		if (!display) writer.writeAttribute("style", "display: none;", null);
//		writer.writeAttribute("id", tdFromYearId, null);
//		writer.writeAttribute("class", "query-builder-range-value", null);
//		writer.startElement("input", this);
//		writer.writeAttribute("type", "text", null);
//		writer.writeAttribute("name", inputFromYearName, null);
//		writer.writeAttribute("value", queryCondition.getFromYear(), null);
//		writer.endElement("input");
//		writer.endElement("td");
//		
//	}

	private void encodeDateCondition(QueryConditionDate queryCondition, FacesContext context, UIForm form, ResponseWriter writer) throws IOException
	{

		AbstractAttribute attribute = queryCondition.getAttribute();
		
		String tdFromMonthId = getClientId(context) + "_" + attribute.getId() + "_td_from_month";
		String tdSlashBetweenStartId = getClientId(context) + "_" + attribute.getId() + "_td_slash_between_start";
		String tdFromYearId = getClientId(context) + "_" + attribute.getId() + "_td_from_year";
		String tdDashId = getClientId(context) + "_" + attribute.getId() + "_td_dash";
		String tdToMonthId = getClientId(context) + "_" + attribute.getId() + "_td_to_month";
		String tdSlashBetweenEndId = getClientId(context) + "_" + attribute.getId() + "_td_slash_between_end";
		String tdToYearId = getClientId(context) + "_" + attribute.getId() + "_td_to_year";
		String tdLeMonthId = getClientId(context) + "_" + attribute.getId() + "_td_le_month";
		String tdSlashLeId = getClientId(context) + "_" + attribute.getId() + "_td_le_slash";
		String tdLeYearId = getClientId(context) + "_" + attribute.getId() + "_td_le_year";
		String tdGeMonthId = getClientId(context) + "_" + attribute.getId() + "_td_ge_month";
		String tdSlashGeId = getClientId(context) + "_" + attribute.getId() + "_td_ge_slash";
		String tdGeYearId = getClientId(context) + "_" + attribute.getId() + "_td_ge_year";
		String tdEqMonthId = getClientId(context) + "_" + attribute.getId() + "_td_eq_month";
		String tdSlashEqId = getClientId(context) + "_" + attribute.getId() + "_td_eq_slash";
		String tdEqYearId = getClientId(context) + "_" + attribute.getId() + "_td_eq_year";
		
		String htmlNameForRangeType = getHtmlNameForDateType(attribute, context);
		String inputFromMonthName = getHtmlNameForDateFromMonth(attribute, context);
		String inputFromYearName = getHtmlNameForDateFromYear(attribute, context);
		String inputToMonthName = getHtmlNameForDateToMonth(attribute, context);
		String inputToYearName = getHtmlNameForDateToYear(attribute, context);
		String inputLeMonthName = getHtmlNameForDateLeMonth(attribute, context);
		String inputLeYearName = getHtmlNameForDateLeYear(attribute, context);
		String inputGeMonthName = getHtmlNameForDateGeMonth(attribute, context);
		String inputGeYearName = getHtmlNameForDateGeMonth(attribute, context);
		String inputEqMonthName = getHtmlNameForDateEqMonth(attribute, context);
		String inputEqYearName = getHtmlNameForDateEqYear(attribute, context);
		
		StringBuffer js = new StringBuffer();

		js.append("var type = ");
		UtilsJSF.appendFormElementRefJS(js, context, form, htmlNameForRangeType);
		js.append(".selectedIndex;");
		
		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdFromMonthId);
		js.append(".style.display = (type == 0) ? '' : 'none';");
		
		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdSlashBetweenStartId);
		js.append(".style.display = (type == 0) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdFromYearId);
		js.append(".style.display = (type == 0) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdDashId);
		js.append(".style.display = (type == 0) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdToMonthId);
		js.append(".style.display = (type == 0) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdSlashBetweenEndId);
		js.append(".style.display = (type == 0) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdToYearId);
		js.append(".style.display = (type == 0) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdLeMonthId);
		js.append(".style.display = (type == 1) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdSlashLeId);
		js.append(".style.display = (type == 3) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdLeYearId);
		js.append(".style.display = (type == 1) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdGeMonthId);
		js.append(".style.display = (type == 2) ? '' : 'none';");
		
		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdSlashGeId);
		js.append(".style.display = (type == 3) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdGeYearId);
		js.append(".style.display = (type == 2) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdEqMonthId);
		js.append(".style.display = (type == 3) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdSlashEqId);
		js.append(".style.display = (type == 3) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, context, form, tdEqYearId);
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
		if (type == QueryConditionNumeric.TYPE_BETWEEN) writer.writeAttribute("selected", "selected", null);
		writer.write("Between");
		writer.endElement("option");
		
		writer.startElement("option", this);
		writer.writeAttribute("value", "le", null);
		if (type == QueryConditionNumeric.TYPE_LE) writer.writeAttribute("selected", "selected", null);
		writer.write("at most");
		writer.endElement("option");

		writer.startElement("option", this);
		writer.writeAttribute("value", "ge", null);
		if (type == QueryConditionNumeric.TYPE_GE) writer.writeAttribute("selected", "selected", null);
		writer.write("at least");
		writer.endElement("option");
		
		writer.startElement("option", this);
		writer.writeAttribute("value", "eq", null);
		if (type == QueryConditionNumeric.TYPE_EQ) writer.writeAttribute("selected", "selected", null);
		writer.write("is equal");
		writer.endElement("option");
		
		writer.endElement("select");
		writer.endElement("td");

		writer.startElement("td", this);
		if (type != 0) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdFromMonthId, null);
		writer.writeAttribute("class", "query-builder-range-value", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputFromMonthName, null);
		writer.writeAttribute("value", queryCondition.getFromMonth(), null);
		writer.endElement("input");
		writer.endElement("td");
		
		writer.startElement("td", this);
		if (type != 0) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdSlashBetweenStartId, null);
		writer.writeAttribute("class", "query-builder-range-dash", null);
		writer.write("/");
		writer.endElement("td");

		writer.startElement("td", this);
		if (type != 0) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdFromYearId, null);
		writer.writeAttribute("class", "query-builder-range-value", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputFromYearName, null);
		writer.writeAttribute("value", queryCondition.getFromYear(), null);
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
		writer.writeAttribute("id", tdToMonthId, null);
		writer.writeAttribute("class", "query-builder-range-value", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputToMonthName, null);
		writer.writeAttribute("value", queryCondition.getToMonth(), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.startElement("td", this);
		if (type != 1) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdLeMonthId, null);
		writer.writeAttribute("class", "query-builder-range-value", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputLeMonthName, null);
		writer.writeAttribute("value", queryCondition.getLeMonth(), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.startElement("td", this);
		if (type != 2) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdGeMonthId, null);
		writer.writeAttribute("class", "query-builder-range-value", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputGeMonthName, null);
		writer.writeAttribute("value", queryCondition.getGeMonth(), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.startElement("td", this);
		if (type != 3) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdEqMonthId, null);
		writer.writeAttribute("class", "query-builder-range-value", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputEqMonthName, null);
		writer.writeAttribute("value", queryCondition.getEqMonth(), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.endElement("tr");
		writer.endElement("table");
		
			
		for (int i = 0; i < 12; i++)
			UtilsJSF.encodeHiddenInput(this, writer,
					getHtmlNameForRangeMonth(attribute, context, i),
					queryCondition.isMonthSelected(i) ? "1" : "0");

		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("class", "query-builder-range-months", null);
		writer.startElement("tr", this);
		
		for (int i = 0; i < 12; i++)
		{
			
			String tdMonthId = getClientId(context) + "_" + attribute.getId() + "_td_month_" + i;
			
			js.setLength(0);

			js.append("var monthInput = ");
			UtilsJSF.appendFormElementRefJS(js, context, form, getHtmlNameForRangeMonth(attribute, context, i));
			js.append("; ");

			js.append("var monthTd = ");
			UtilsJSF.appendElementRefJS(js, context, form, tdMonthId);
			js.append("; ");

			js.append("if (monthInput.value == '1') {");
			js.append("monthInput.value = '0'; ");
			js.append("monthTd.className = 'query-builder-range-month-delected';");
			js.append("} else {");
			js.append("monthInput.value = '1'; ");
			js.append("monthTd.className = 'query-builder-range-month-selected';");
			js.append("}");
			
			String styleClass = queryCondition.isMonthSelected(i) ? 
					"query-builder-range-month-selected" : 
					"query-builder-range-month-delected"; 
			
			writer.startElement("td", this);
			writer.writeAttribute("id", tdMonthId, null);
			writer.writeAttribute("class", styleClass, null);
			writer.writeAttribute("onclick", js.toString(), null);
			writer.write(QueryConditionDate.MONTH_NAMES[i]);
			writer.endElement("td");

		}
		
		writer.endElement("tr");
		writer.endElement("table");

	}
	
	private QueryCondition decodeDateCondition(AbstractAttribute attribute, FacesContext context, ExternalContext externalContext)
	{
		
		Map params = externalContext.getRequestParameterMap();
		String typeStr = (String) params.get(getHtmlNameForNumericType(attribute, context));
		String from = (String) params.get(getHtmlNameForNumericFrom(attribute, context));
		String to = (String) params.get(getHtmlNameForNumericTo(attribute, context));
		String le = (String) params.get(getHtmlNameForNumericLe(attribute, context));
		String ge = (String) params.get(getHtmlNameForNumericGe(attribute, context));
		String eq = (String) params.get(getHtmlNameForNumericEq(attribute, context));

		int type;
		if ("between".equals(typeStr))
		{
			type = QueryConditionNumeric.TYPE_BETWEEN;
		}
		else if ("le".equals(typeStr))
		{
			type = QueryConditionNumeric.TYPE_LE; 
		}
		else if ("ge".equals(typeStr))
		{
			type = QueryConditionNumeric.TYPE_GE;
		}
		else if ("eq".equals(typeStr))
		{
			type = QueryConditionNumeric.TYPE_EQ;
		}
		else
		{
			return null;
		}
		
		QueryConditionDate queryCondition = new QueryConditionDate(attribute, type);
		queryCondition.setFromMonth(from);
		queryCondition.setToMonth(to);
		queryCondition.setLeMonth(le);
		queryCondition.setGeMonth(ge);
		queryCondition.setEqMonth(eq);
		
		if (attribute.getType().intValue() == AbstractAttribute.TYPE_DATE)
		{
			for (int i = 0; i < 12; i++)
			{
				String monthStatus = (String) params.get(getHtmlNameForRangeMonth(attribute, context, i));
				queryCondition.setMonthStatus(i, "1".equals(monthStatus));
			}
		}

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