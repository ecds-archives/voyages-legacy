package edu.emory.library.tas.web;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.util.StringUtils;

/**
 * 
 * @author Jan Zich
 *
 */
public class QueryBuilderComponent extends UIComponentBase
{

	private Query submittedQuery;
	private Query setQuery;
	private boolean querySet = false;

	public String getFamily()
	{
		return null;
	}

	private String getAttributesListHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_attributes";
	}

	public void decode(FacesContext context)
	{
		
		Map params = context.getExternalContext().getRequestParameterMap();
		
		String attrFieldName = getAttributesListHiddenFieldName(context); 
		if (!params.containsKey(attrFieldName))
		{
			submittedQuery = null;
			return;
		}
		
		String submittedAttributesStr = (String) params.get(attrFieldName);
		submittedQuery = new Query();

		if (!StringUtils.isNullOrEmpty(submittedAttributesStr))
		{
			
			String[] submittedAttributes = submittedAttributesStr.split(",");
			for (int i = 0; i < submittedAttributes.length; i++)
			{
				
				AbstractAttribute attribute = (AbstractAttribute)
					AbstractAttribute.loadById(new Long(submittedAttributes[i]));
				
				QueryCondition queryCondition = null;
				switch (attribute.getType().intValue())
				{
					case AbstractAttribute.TYPE_STRING:
						queryCondition = decodeSimpleCondition(attribute, context); 
						break;
	
					case AbstractAttribute.TYPE_INTEGER:
					case AbstractAttribute.TYPE_LONG:
					case AbstractAttribute.TYPE_FLOAT:
						queryCondition = decodeNumericCondition(attribute, context);
						break;
				
					case AbstractAttribute.TYPE_DATE:
						queryCondition = decodeDateCondition(attribute, context);
						break;
	
					case AbstractAttribute.TYPE_DICT:
						queryCondition = decodeDictionaryCondition(attribute, context);
						break;
				}
				
				if (queryCondition != null)
					submittedQuery.addCondition(queryCondition);
				
			}
			
		}
		
	}
	
	public void processUpdates(FacesContext context)
	{
		ValueBinding vb = getValueBinding("query");
		if (vb != null && submittedQuery != null) vb.setValue(context, submittedQuery);
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = UtilsJSF.getForm(this, context);
		
		Query query = getQuery();
		//attributes = new ArrayList();
		
		StringBuffer attributeIds = new StringBuffer();
		for (Iterator iterFieldName = query.getConditions().iterator(); iterFieldName.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterFieldName.next();
			AbstractAttribute attribute = queryCondition.getAttribute();
			if (attributeIds.length() > 0) attributeIds.append(",");
			attributeIds.append(attribute.getId().toString());
		}
		
		UtilsJSF.encodeHiddenInput(this, writer,
				getAttributesListHiddenFieldName(context),
				attributeIds.toString());
		
		int i = 0;
		for (Iterator iterFieldName = query.getConditions().iterator(); iterFieldName.hasNext();  i++)
		{
			
			QueryCondition queryCondition = (QueryCondition) iterFieldName.next();
			AbstractAttribute attribute = queryCondition.getAttribute();
			
			//attributes.add(attribute);
			
			switch (attribute.getType().intValue())
			{
				case AbstractAttribute.TYPE_STRING:
					if (queryCondition instanceof QueryConditionText)
					{
						encodeStartQueryConditionBox(queryCondition, context, form, writer, i);
						encodeSimpleCondition((QueryConditionText)queryCondition, context, form, writer);
						encodeEndQueryConditionBox(queryCondition, context, form, writer);
					}
					break;

				case AbstractAttribute.TYPE_INTEGER:
				case AbstractAttribute.TYPE_LONG:
				case AbstractAttribute.TYPE_FLOAT:
					if (queryCondition instanceof QueryConditionNumeric)
					{
						encodeStartQueryConditionBox(queryCondition, context, form, writer, i);
						encodeNumericCondition((QueryConditionNumeric) queryCondition, context, form, writer);
						encodeEndQueryConditionBox(queryCondition, context, form, writer);
					}
					break;
			
				case AbstractAttribute.TYPE_DATE:
					if (queryCondition instanceof QueryConditionDate)
					{
						encodeStartQueryConditionBox(queryCondition, context, form, writer, i);
						encodeDateCondition((QueryConditionDate) queryCondition, context, form, writer);
						encodeEndQueryConditionBox(queryCondition, context, form, writer);
					}
					break;

				case AbstractAttribute.TYPE_DICT:
					if (queryCondition instanceof QueryConditionDictionary)
					{
						encodeStartQueryConditionBox(queryCondition, context, form, writer, i);
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
	
	private String getConditionDivId(FacesContext context, int conditionIndex)
	{
		return getClientId(context) + "_" + conditionIndex;
	}
	
	private void encodeConditionButtonsStart(ResponseWriter writer) throws IOException
	{
		writer.startElement("table", this);
		writer.writeAttribute("align", "right", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.startElement("tr", this);
	}

	private void encodeConditionButton(ResponseWriter writer, String imgSrc, String jsOnClick) throws IOException
	{
		writer.startElement("td", this);
		writer.writeAttribute("class", "query-builder-button", null);

		writer.startElement("img", this);
		writer.writeAttribute("src", imgSrc, null);
		writer.writeAttribute("onclick", jsOnClick, null);
		writer.writeAttribute("width", "12", null);
		writer.writeAttribute("height", "12", null);
		writer.writeAttribute("border", "0", null);
		writer.endElement("img");

		writer.endElement("td");
	}

	private void encodeConditionButtonSeparator(ResponseWriter writer) throws IOException
	{
		writer.startElement("td", this);
		writer.writeAttribute("class", "query-builder-button-separator", null);
		writer.endElement("td");
	}

	private void encodeConditionButtonsEnd(ResponseWriter writer) throws IOException
	{
		writer.endElement("tr");
		writer.endElement("table");
	}

	private void encodeConditionButtons(QueryCondition queryCondition, FacesContext context, UIForm form, ResponseWriter writer, int conditionIndex) throws IOException
	{
		
		String conditionDivId = getConditionDivId(context, conditionIndex);
		String attrListFieldName = getAttributesListHiddenFieldName(context);
		String conditionId = queryCondition.getAttribute().getId().toString();

		StringBuffer jsDelete = new StringBuffer();
		jsDelete.append("var cond = ");
		UtilsJSF.appendElementRefJS(jsDelete, conditionDivId).append("; ");
		jsDelete.append("var attrListField = ");
		UtilsJSF.appendFormElementRefJS(jsDelete, context, form, attrListFieldName).append("; ");
		jsDelete.append("var attrs = attrListField.value.split(','); ");
		jsDelete.append("for (var i=0; i<attrs.length; i++) {");
		{
			jsDelete.append("if (attrs[i] == '").append(conditionId).append("') {");
			{
				jsDelete.append("attrs.splice(i, 1); ");
				jsDelete.append("attrListField.value = attrs.join(','); ");
				jsDelete.append("if (Scriptaculous) {");
				{
					jsDelete.append("new Effect.Opacity(");
					jsDelete.append("cond, ");
					jsDelete.append("{");
					{
						jsDelete.append("from: 1.0, ");
						jsDelete.append("to: 0.0, ");
						jsDelete.append("duration: 0.5, ");
						jsDelete.append("afterFinishInternal: function(effect) ");
						jsDelete.append("{effect.element.parentNode.removeChild(effect.element);}");
					}
					jsDelete.append("}");
					jsDelete.append(");");
				}
				jsDelete.append("} else {");
				{
					jsDelete.append("cond.parentNode.removeChild(cond);");
				}
				jsDelete.append("}");
				jsDelete.append("return;");
			}
			jsDelete.append("}");
		}
		jsDelete.append("}");


		StringBuffer jsMoveUp = new StringBuffer();
		jsMoveUp.append("var cond = ");
		UtilsJSF.appendElementRefJS(jsMoveUp, conditionDivId).append("; ");
		jsMoveUp.append("var attrListField = ");
		UtilsJSF.appendFormElementRefJS(jsMoveUp, context, form, attrListFieldName).append("; ");
		jsMoveUp.append("var attrs = attrListField.value.split(','); ");
		jsMoveUp.append("for (var i=0; i<attrs.length; i++) {");
		{
			jsMoveUp.append("if (attrs[i] == '").append(conditionId).append("') {");
			{
				jsMoveUp.append("if (i != 0) {");
				{
					jsMoveUp.append("var prevCond = cond.previousSibling; ");
					jsMoveUp.append("var parent = cond.parentNode; ");
					jsMoveUp.append("parent.removeChild(cond); ");
					jsMoveUp.append("parent.insertBefore(cond, prevCond); ");
					jsMoveUp.append("attrs[i] = attrs[i-1]; ");
					jsMoveUp.append("attrs[i-1] = '").append(conditionId).append("'; ");
					jsMoveUp.append("attrListField.value = attrs.join(',');");
					jsMoveUp.append("if (Scriptaculous) {");
					{
						jsMoveUp.append("Element.setOpacity(cond, 0); ");
						jsMoveUp.append("Effect.Appear(cond, {duration: 0.5});");
					}
					jsMoveUp.append("} ");
				}
				jsMoveUp.append("} ");
				jsMoveUp.append("return;");
			}
			jsMoveUp.append("}");
		}
		jsMoveUp.append("}");
		
		StringBuffer jsMoveDown = new StringBuffer();
		jsMoveDown.append("var cond = ");
		UtilsJSF.appendElementRefJS(jsMoveDown, conditionDivId).append("; ");
		jsMoveDown.append("var attrListField = ");
		UtilsJSF.appendFormElementRefJS(jsMoveDown, context, form, attrListFieldName).append("; ");
		jsMoveDown.append("var attrs = attrListField.value.split(','); ");
		jsMoveDown.append("for (var i=0; i<attrs.length; i++) {");
		{
			jsMoveDown.append("if (attrs[i] == '").append(conditionId).append("') {");
			{
				jsMoveDown.append("if (i != attrs.length-1) {");
				{
					jsMoveDown.append("var nextNextCond = cond.nextSibling.nextSibling; ");
					jsMoveDown.append("var parent = cond.parentNode; ");
					jsMoveDown.append("parent.removeChild(cond); ");
					jsMoveDown.append("parent.insertBefore(cond, nextNextCond); ");
					jsMoveDown.append("attrs[i] = attrs[i+1]; ");
					jsMoveDown.append("attrs[i+1] = '").append(conditionId).append("'; ");
					jsMoveDown.append("attrListField.value = attrs.join(',');");
					jsMoveDown.append("if (Scriptaculous) {");
					{
						jsMoveDown.append("Element.setOpacity(cond, 0); ");
						jsMoveDown.append("Effect.Appear(cond, {duration: 0.5});");
					}
					jsMoveDown.append("}");
				}
				jsMoveDown.append("} ");
				jsMoveDown.append("return;");
			}
			jsMoveDown.append("}");
		}
		jsMoveDown.append("}");

		encodeConditionButtonsStart(writer);
		encodeConditionButton(writer, "icon-move-up.png", jsMoveUp.toString());
		encodeConditionButtonSeparator(writer);
		encodeConditionButton(writer, "icon-move-down.png", jsMoveDown.toString());
		encodeConditionButtonSeparator(writer);
		encodeConditionButton(writer, "icon-remove.png", jsDelete.toString());
		encodeConditionButtonsEnd(writer);
		
	}
	
	private void encodeStartQueryConditionBox(QueryCondition queryCondition, FacesContext context, UIForm form, ResponseWriter writer, int conditionIndex) throws IOException
	{
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "side-box", null);
		writer.writeAttribute("id", getConditionDivId(context, conditionIndex), null);
		
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
		encodeConditionButtons(queryCondition, context, form, writer, conditionIndex);
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
	
	private QueryCondition decodeSimpleCondition(AbstractAttribute attribute, FacesContext context)
	{
		
		String fieldName = getHtmlNameForSimpleValue(attribute, context); 
		
		Map params = context.getExternalContext().getRequestParameterMap();
		if (!params.containsKey(fieldName))
			return null;
		
		String value = (String) params.get(fieldName);
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

	private void encodeRangeSelect(ResponseWriter writer, String htmlNameForRangeType, String jsOnChange, int type) throws IOException
	{
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "query-builder-range-type", null);
		writer.startElement("select", this);
		writer.writeAttribute("name", htmlNameForRangeType, null);
		writer.writeAttribute("onchange", jsOnChange.toString(), null);
		
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

	}
	
	private void encodeRangeDash(ResponseWriter writer, String tdDashId, boolean visible) throws IOException
	{
		
		writer.startElement("td", this);
		if (!visible) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdDashId, null);
		writer.writeAttribute("class", "query-builder-range-dash", null);
		writer.write("-");
		writer.endElement("td");
		
	}
	
	private void encodeNumericField(ResponseWriter writer, String value, String tdId, String inputName, boolean visible) throws IOException
	{
		
		writer.startElement("td", this);
		if (!visible) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdId, null);
		writer.writeAttribute("class", "query-builder-range-value", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputName, null);
		writer.writeAttribute("value", value, null);
		writer.endElement("input");
		writer.endElement("td");
		
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
		UtilsJSF.appendElementRefJS(js, tdFromId);
		js.append(".style.display = (type == 0) ? '' : 'none';");
		
		js.append(" ");
		UtilsJSF.appendElementRefJS(js, tdDashId);
		js.append(".style.display = (type == 0) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, tdToId);
		js.append(".style.display = (type == 0) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, tdLeId);
		js.append(".style.display = (type == 1) ? '' : 'none';");

		js.append(" ");
		UtilsJSF.appendElementRefJS(js, tdGeId);
		js.append(".style.display = (type == 2) ? '' : 'none';");
		
		js.append(" ");
		UtilsJSF.appendElementRefJS(js, tdEqId);
		js.append(".style.display = (type == 3) ? '' : 'none';");

		int type = queryCondition.getType();

		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.startElement("tr", this);
		
		encodeRangeSelect(writer,
				htmlNameForRangeType, js.toString(),
				type);

		encodeNumericField(writer,
				queryCondition.getFrom(),
				tdFromId, inputFromName,
				type == 0);
		
		encodeRangeDash(writer,
				tdDashId,
				type == 0);

		encodeNumericField(writer,
				queryCondition.getTo(),
				tdToId, inputToName,
				type == 0);

		encodeNumericField(writer,
				queryCondition.getLe(),
				tdLeId, inputLeName,
				type == 1);

		encodeNumericField(writer,
				queryCondition.getGe(),
				tdGeId, inputGeName,
				type == 2);

		encodeNumericField(writer,
				queryCondition.getEq(),
				tdEqId, inputEqName,
				type == 3);
		
		writer.endElement("tr");
		writer.endElement("table");
	
	}

	private QueryCondition decodeNumericCondition(AbstractAttribute attribute, FacesContext context)
	{
		
		Map params = context.getExternalContext().getRequestParameterMap();
		if (!params.containsKey(getHtmlNameForNumericType(attribute, context)))
			return null;
		
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
	
	private void encodeDateField(FacesContext context, UIForm form, ResponseWriter writer, String month, String year, String tdMonthId, String tdSlashId, String tdYearId, String inputMonthName, String inputYearName, boolean visible) throws IOException
	{
		
		String jsMonthOnFocus =
			"if (this.value == '" + QueryConditionDate.EMPTY_MONTH + "') " +
			"this.value = '';";

		String jsYearOnFocus =
			"if (this.value == '" + QueryConditionDate.EMPTY_YEAR + "') " +
			"this.value = '';";

		writer.startElement("td", this);
		if (!visible) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdMonthId, null);
		writer.writeAttribute("class", "query-builder-range-month", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputMonthName, null);
		writer.writeAttribute("onfocus", jsMonthOnFocus, null);
		writer.writeAttribute("value", month, null);
		writer.endElement("input");
		writer.endElement("td");
		
		writer.startElement("td", this);
		if (!visible) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdSlashId, null);
		writer.writeAttribute("class", "query-builder-range-slash", null);
		writer.write("/");
		writer.endElement("td");

		writer.startElement("td", this);
		if (!visible) writer.writeAttribute("style", "display: none;", null);
		writer.writeAttribute("id", tdYearId, null);
		writer.writeAttribute("class", "query-builder-range-year", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputYearName, null);
		writer.writeAttribute("onfocus", jsYearOnFocus, null);
		writer.writeAttribute("value", year, null);
		writer.endElement("input");
		writer.endElement("td");

	}
	
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
		String inputGeYearName = getHtmlNameForDateGeYear(attribute, context);
		String inputEqMonthName = getHtmlNameForDateEqMonth(attribute, context);
		String inputEqYearName = getHtmlNameForDateEqYear(attribute, context);
		
		StringBuffer jsChnageType = new StringBuffer();

		jsChnageType.append("var type = ");
		UtilsJSF.appendFormElementRefJS(jsChnageType, context, form, htmlNameForRangeType);
		jsChnageType.append(".selectedIndex;");
		
		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdFromMonthId);
		jsChnageType.append(".style.display = (type == 0) ? '' : 'none';");
		
		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdSlashBetweenStartId);
		jsChnageType.append(".style.display = (type == 0) ? '' : 'none';");

		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdFromYearId);
		jsChnageType.append(".style.display = (type == 0) ? '' : 'none';");

		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdDashId);
		jsChnageType.append(".style.display = (type == 0) ? '' : 'none';");

		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdToMonthId);
		jsChnageType.append(".style.display = (type == 0) ? '' : 'none';");

		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdSlashBetweenEndId);
		jsChnageType.append(".style.display = (type == 0) ? '' : 'none';");

		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdToYearId);
		jsChnageType.append(".style.display = (type == 0) ? '' : 'none';");

		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdLeMonthId);
		jsChnageType.append(".style.display = (type == 1) ? '' : 'none';");

		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdSlashLeId);
		jsChnageType.append(".style.display = (type == 1) ? '' : 'none';");

		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdLeYearId);
		jsChnageType.append(".style.display = (type == 1) ? '' : 'none';");

		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdGeMonthId);
		jsChnageType.append(".style.display = (type == 2) ? '' : 'none';");
		
		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdSlashGeId);
		jsChnageType.append(".style.display = (type == 2) ? '' : 'none';");

		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdGeYearId);
		jsChnageType.append(".style.display = (type == 2) ? '' : 'none';");

		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdEqMonthId);
		jsChnageType.append(".style.display = (type == 3) ? '' : 'none';");

		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdSlashEqId);
		jsChnageType.append(".style.display = (type == 3) ? '' : 'none';");

		jsChnageType.append(" ");
		UtilsJSF.appendElementRefJS(jsChnageType, tdEqYearId);
		jsChnageType.append(".style.display = (type == 3) ? '' : 'none';");
		
		int type = queryCondition.getType();

		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.startElement("tr", this);
		
		encodeRangeSelect(writer,
				htmlNameForRangeType, jsChnageType.toString(),
				type);

		encodeDateField(context, form, writer, 
				queryCondition.getFromMonth(), queryCondition.getFromYear(),
				tdFromMonthId, tdSlashBetweenStartId, tdFromYearId,
				inputFromMonthName, inputFromYearName,
				type == 0);
		
		encodeRangeDash(writer,
				tdDashId,
				type == 0);

		encodeDateField(context, form, writer,
				queryCondition.getToMonth(), queryCondition.getToYear(),
				tdToMonthId, tdSlashBetweenEndId, tdToYearId,
				inputToMonthName, inputToYearName,
				type == 0);

		encodeDateField(context, form, writer,
				queryCondition.getLeMonth(), queryCondition.getLeYear(),
				tdLeMonthId, tdSlashLeId, tdLeYearId,
				inputLeMonthName, inputLeYearName,
				type == 1);

		encodeDateField(context, form, writer,
				queryCondition.getGeMonth(), queryCondition.getGeYear(),
				tdGeMonthId, tdSlashGeId, tdGeYearId,
				inputGeMonthName, inputGeYearName,
				type == 2);

		encodeDateField(context, form, writer,
				queryCondition.getEqMonth(), queryCondition.getEqYear(),
				tdEqMonthId, tdSlashEqId, tdEqYearId,
				inputEqMonthName, inputEqYearName,
				type == 3);

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
		
		StringBuffer jsSelectMonth = new StringBuffer();
		
		for (int i = 0; i < 12; i++)
		{
			
			String tdMonthId = getClientId(context) + "_" + attribute.getId() + "_td_month_" + i;
			
			jsSelectMonth.setLength(0);
			
			jsSelectMonth.append("var monthInput = ");
			UtilsJSF.appendFormElementRefJS(jsSelectMonth, context, form, getHtmlNameForRangeMonth(attribute, context, i));
			jsSelectMonth.append("; ");

			jsSelectMonth.append("var monthTd = ");
			UtilsJSF.appendElementRefJS(jsSelectMonth, tdMonthId);
			jsSelectMonth.append("; ");

			jsSelectMonth.append("if (monthInput.value == '1') {");
			jsSelectMonth.append("monthInput.value = '0'; ");
			jsSelectMonth.append("monthTd.className = 'query-builder-range-month-delected';");
			jsSelectMonth.append("} else {");
			jsSelectMonth.append("monthInput.value = '1'; ");
			jsSelectMonth.append("monthTd.className = 'query-builder-range-month-selected';");
			jsSelectMonth.append("}");
			
			String styleClass = queryCondition.isMonthSelected(i) ? 
					"query-builder-range-month-selected" : 
					"query-builder-range-month-delected"; 
			
			writer.startElement("td", this);
			writer.writeAttribute("id", tdMonthId, null);
			writer.writeAttribute("class", styleClass, null);
			writer.writeAttribute("onclick", jsSelectMonth.toString(), null);
			writer.write(QueryConditionDate.MONTH_NAMES[i]);
			writer.endElement("td");

		}
		
		writer.endElement("tr");
		writer.endElement("table");

	}

	private QueryCondition decodeDateCondition(AbstractAttribute attribute, FacesContext context)
	{
		
		Map params = context.getExternalContext().getRequestParameterMap();
		if (!params.containsKey(getHtmlNameForDateType(attribute, context)))
			return null;
		
		String typeStr = (String) params.get(getHtmlNameForDateType(attribute, context));
		String fromMonth = (String) params.get(getHtmlNameForDateFromMonth(attribute, context));
		String fromYear = (String) params.get(getHtmlNameForDateFromYear(attribute, context));
		String toMonth = (String) params.get(getHtmlNameForDateToMonth(attribute, context));
		String toYear = (String) params.get(getHtmlNameForDateToYear(attribute, context));
		String leMonth = (String) params.get(getHtmlNameForDateLeMonth(attribute, context));
		String leYear = (String) params.get(getHtmlNameForDateLeYear(attribute, context));
		String geMonth = (String) params.get(getHtmlNameForDateGeMonth(attribute, context));
		String geYear = (String) params.get(getHtmlNameForDateGeYear(attribute, context));
		String eqMonth = (String) params.get(getHtmlNameForDateEqMonth(attribute, context));
		String eqYear = (String) params.get(getHtmlNameForDateEqYear(attribute, context));

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
		queryCondition.setFromMonth(fromMonth);
		queryCondition.setFromYear(fromYear);
		queryCondition.setToMonth(toMonth);
		queryCondition.setToYear(toYear);
		queryCondition.setLeMonth(leMonth);
		queryCondition.setLeYear(leYear);
		queryCondition.setGeMonth(geMonth);
		queryCondition.setGeYear(geYear);
		queryCondition.setEqMonth(eqMonth);
		queryCondition.setEqYear(eqYear);
		
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

	private QueryCondition decodeDictionaryCondition(AbstractAttribute attribute, FacesContext context)
	{
		
		Map params = context.getExternalContext().getRequestParameterMap();
		String fieldName = getHtmlNameForList(attribute, context);
		if (!params.containsKey(fieldName))
			return null;
		
		String valuesStr = (String) params.get(fieldName);
		
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