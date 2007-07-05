package edu.emory.library.tast.common.grideditor.date;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.common.grideditor.Adapter;
import edu.emory.library.tast.common.grideditor.Column;
import edu.emory.library.tast.common.grideditor.FieldType;
import edu.emory.library.tast.common.grideditor.GridEditorComponent;
import edu.emory.library.tast.common.grideditor.Row;
import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.util.JsfUtils;

public class DateAdapter extends Adapter
{
	
	public static final String TYPE = "date";
	
	private String getYearFieldName(String prefix)
	{
		return prefix + "_year";
	}

	private String getMonthFieldName(String prefix)
	{
		return prefix + "_month";
	}

	private String getDayFieldName(String prefix)
	{
		return prefix + "_day";
	}

	public Value decode(FacesContext context, String inputPrefix, GridEditorComponent gridEditor)
	{

		Map params = context.getExternalContext().getRequestParameterMap();

		String dayFieldName = getDayFieldName(inputPrefix);
		String monthFieldName = getMonthFieldName(inputPrefix);
		String yearFieldName = getYearFieldName(inputPrefix);
		
		String day = (String) params.get(dayFieldName);
		String month = (String) params.get(monthFieldName);
		String year = (String) params.get(yearFieldName);
		
		return new DateValue(day, month, year);
		
	}
	
	public void createValueJavaScript(FacesContext context, StringBuffer regJS, GridEditorComponent gridEditor, String inputPrefix, Row row, Column column, String cellId, Value value, boolean readOnly) throws IOException
	{
		regJS.append("new GridEditorDate(");
		regJS.append("'").append(cellId).append("'");
		regJS.append(", ");
		regJS.append("'").append(getYearFieldName(inputPrefix)).append("'");
		regJS.append(", ");
		regJS.append("'").append(getMonthFieldName(inputPrefix)).append("'");
		regJS.append(", ");
		regJS.append("'").append(getDayFieldName(inputPrefix)).append("'");
		regJS.append(")");
	} 
	
	private void encodeEditMode(GridEditorComponent gridEditor, String clientGridId, String inputPrefix, DateValue dateValue, ResponseWriter writer, Row row, Column column, boolean invokeCompare) throws IOException
	{
		
		String compareJS = null;
		if (invokeCompare)
			compareJS = 
				"GridEditorGlobals.compare('" +
				"" + clientGridId + "', " +
				"'" + row.getName() + "', " +
				"'" + column.getName() + "')";
		
		// year
		writer.startElement("input", gridEditor);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getYearFieldName(inputPrefix), null);
		writer.writeAttribute("value", dateValue.getYearOrEmpty(), null);
		writer.writeAttribute("class", "grid-editor-date-year", null);
		writer.writeAttribute("onfocus", "if (this.value == 'YYYY') this.value=''", null);
		writer.writeAttribute("onblur", "if (this.value == '') this.value='YYYY'", null);
		if (invokeCompare)
		{
			writer.writeAttribute("onkeyup", compareJS, null);
			writer.writeAttribute("onchange", compareJS, null);
		}
		writer.endElement("input");
		
		// month
		writer.startElement("input", gridEditor);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getMonthFieldName(inputPrefix), null);
		writer.writeAttribute("value", dateValue.getMonthOrEmpty(), null);
		writer.writeAttribute("class", "grid-editor-date-month", null);
		writer.writeAttribute("onfocus", "if (this.value == 'MM') this.value=''", null);
		writer.writeAttribute("onblur", "if (this.value == '') this.value='MM'", null);
		if (invokeCompare)
		{
			writer.writeAttribute("onkeyup", compareJS, null);
			writer.writeAttribute("onchange", compareJS, null);
		}
		writer.endElement("input");

		// day
		writer.startElement("input", gridEditor);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getDayFieldName(inputPrefix), null);
		writer.writeAttribute("value", dateValue.getDayOrEmpty(), null);
		writer.writeAttribute("class", "grid-editor-date-day", null);
		writer.writeAttribute("onfocus", "if (this.value == 'DD') this.value=''", null);
		writer.writeAttribute("onblur", "if (this.value == '') this.value='DD'", null);
		if (invokeCompare)
		{
			writer.writeAttribute("onkeyup", compareJS, null);
			writer.writeAttribute("onchange", compareJS, null);
		}
		writer.endElement("input");

	}

	private void encodeReadOnlyMode(GridEditorComponent gridEditor, String inputPrefix, DateValue dateValue, ResponseWriter writer) throws IOException
	{
		
		JsfUtils.encodeHiddenInput(gridEditor, writer,
				getYearFieldName(inputPrefix),
				dateValue.getYearOrEmpty());

		JsfUtils.encodeHiddenInput(gridEditor, writer,
				getMonthFieldName(inputPrefix),
				dateValue.getMonthOrEmpty());

		JsfUtils.encodeHiddenInput(gridEditor, writer,
				getDayFieldName(inputPrefix),
				dateValue.getDayOrEmpty());

		if (dateValue.isValid())
		{
			writer.write(dateValue.getYearOrEmpty());
			writer.write("/");
			writer.write(dateValue.getMonthOrEmpty());
			writer.write("/");
			writer.write(dateValue.getDayOrEmpty());
		}

	}

	public void encode(FacesContext context, GridEditorComponent gridEditor, String clientGridId, UIForm form, Row row, Column column, FieldType fieldType, String inputPrefix, Value value, boolean readOnly, boolean invokeCompare) throws IOException
	{

		DateValue dateValue = (DateValue) value;
		ResponseWriter writer = context.getResponseWriter();

		if (!readOnly)
			encodeEditMode(gridEditor, clientGridId, inputPrefix, dateValue, writer, row, column, invokeCompare);
		else
			encodeReadOnlyMode(gridEditor, inputPrefix, dateValue, writer);

	}

}