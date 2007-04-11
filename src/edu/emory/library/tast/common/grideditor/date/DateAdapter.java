package edu.emory.library.tast.common.grideditor.date;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.common.grideditor.Adapter;
import edu.emory.library.tast.common.grideditor.GridEditorComponent;
import edu.emory.library.tast.common.grideditor.Value;

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

	public void encode(FacesContext context, GridEditorComponent gridEditor, UIForm form, String inputPrefix, Value value, boolean readOnly) throws IOException
	{
		
		DateValue dateValue = (DateValue) value;
		
		// get writer
		ResponseWriter writer = context.getResponseWriter();
		
		// day
		writer.startElement("input", gridEditor);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getYearFieldName(inputPrefix), null);
		writer.writeAttribute("value", dateValue.getDayOrEmpty(), null);
		writer.writeAttribute("class", "record-gridEditor-date-day", null);
		writer.endElement("input");
		
		// month
		writer.startElement("input", gridEditor);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getMonthFieldName(inputPrefix), null);
		writer.writeAttribute("value", dateValue.getMonthOrEmpty(), null);
		writer.writeAttribute("class", "record-gridEditor-date-month", null);
		writer.endElement("input");

		// year
		writer.startElement("input", gridEditor);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getDayFieldName(inputPrefix), null);
		writer.writeAttribute("value", dateValue.getYearOrEmpty(), null);
		writer.writeAttribute("class", "record-gridEditor-date-year", null);
		writer.endElement("input");
		
	}

}
